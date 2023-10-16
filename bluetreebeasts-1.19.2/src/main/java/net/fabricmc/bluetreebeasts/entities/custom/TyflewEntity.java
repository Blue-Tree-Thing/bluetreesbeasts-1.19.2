package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class TyflewEntity extends HostileEntity implements IAnimatable {
    private PlayerEntity player;
    private static final AnimationBuilder fly_animation = new AnimationBuilder().addAnimation("animation.tyflew.fly", true);

    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.GREEN, BossBar.Style.PROGRESS).setDarkenSky(false);
    private final AnimationFactory factory = new AnimationFactory(this);
    Vec3d targetPosition = Vec3d.ZERO;
    BlockPos circlingCenter = BlockPos.ORIGIN;
    TyflewEntity.TyflewMovementType movementType = TyflewEntity.TyflewMovementType.CIRCLE;

    public TyflewEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistent();
        this.checkDespawn();
        this.moveControl = new TyflewEntity.TyflewMoveControl(this);
        this.experiencePoints = 2000;
    }


    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 400)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(1, new HalfHealthGoal(this));
        this.goalSelector.add(3, new RevengeGoal(this));
        this.goalSelector.add(2, new ShootBulletGoal(this));
        this.goalSelector.add(3, new TyflewEntity.CircleMovementGoal());
        this.targetSelector.add(1, new ActiveTargetGoal<>( this, PlayerEntity.class, false));

    }



    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("AX")) {
            this.circlingCenter = new BlockPos(nbt.getInt("AX"), nbt.getInt("AY"), nbt.getInt("AZ"));
        }
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("AX", this.circlingCenter.getX());
        nbt.putInt("AY", this.circlingCenter.getY());
        nbt.putInt("AZ", this.circlingCenter.getZ());
    }
    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }


    static class HalfHealthGoal extends Goal {
        private final TyflewEntity tyflew;


        HalfHealthGoal(TyflewEntity tyflew) {
            this.tyflew = tyflew;
        }

        @Override
        public boolean canStart() {
            return true;
        }

        public void tick(){
            if(tyflew.getHealth() <= 200){

                tyflew.bossBar.setColor(BossBar.Color.YELLOW);
                if(tyflew.getHealth() <= 100){

                    tyflew.bossBar.setColor(BossBar.Color.RED);
                }
            }
        }
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
    }

    enum TyflewMovementType {
        CIRCLE,
    }
    class CircleMovementGoal
            extends TyflewEntity.MovementGoal {
        private float angle;
        private float radius;
        private float yOffset;
        private float circlingDirection;

        CircleMovementGoal() {
        }

        @Override
        public boolean canStart() {
            return TyflewEntity.this.getTarget() == null || TyflewEntity.this.movementType == TyflewEntity.TyflewMovementType.CIRCLE;
        }

        @Override
        public void start() {
            this.radius = 15.0f;
            this.yOffset = 0f;
            this.circlingDirection = 1f;
        }
        public void tick(){
            this.adjustDirection();
        }

        private void adjustDirection() {
            if (BlockPos.ORIGIN.equals(TyflewEntity.this.circlingCenter)) {
                TyflewEntity.this.circlingCenter = TyflewEntity.this.getBlockPos();
            }
            this.angle += this.circlingDirection * ((float)Math.PI / 180);
            TyflewEntity.this.targetPosition = Vec3d.of(TyflewEntity.this.circlingCenter).add(this.radius * MathHelper.cos(this.angle),  this.yOffset, this.radius * MathHelper.sin(this.angle));
        }
    }
    class TyflewMoveControl
            extends MoveControl {
        private float targetSpeed;

        public TyflewMoveControl(MobEntity owner) {
            super(owner);
            this.targetSpeed = 0.1f;
        }

        @Override
        public void tick() {
            if (TyflewEntity.this.horizontalCollision) {
                TyflewEntity.this.setYaw(TyflewEntity.this.getYaw() + 180.0f);
                this.targetSpeed = 0.1f;
            }
            double d = TyflewEntity.this.targetPosition.x - TyflewEntity.this.getX();
            double e = TyflewEntity.this.targetPosition.y - TyflewEntity.this.getY();
            double f = TyflewEntity.this.targetPosition.z - TyflewEntity.this.getZ();
            double g = Math.sqrt(d * d + f * f);
            if (Math.abs(g) > (double)1.0E-5f) {
                double h = 1.0 - Math.abs(e * (double)0.7f) / g;
                g = Math.sqrt((d *= h) * d + (f *= h) * f);
                double i = Math.sqrt(d * d + f * f + e * e);
                float j = TyflewEntity.this.getYaw();
                float k = (float)MathHelper.atan2(f, d);
                float l = MathHelper.wrapDegrees(TyflewEntity.this.getYaw() + 90.0f);
                float m = MathHelper.wrapDegrees(k * 57.295776f);
                TyflewEntity.this.setYaw(MathHelper.stepUnwrappedAngleTowards(l, m, 4.0f) - 90.0f);
                TyflewEntity.this.bodyYaw = TyflewEntity.this.getYaw();
                this.targetSpeed = MathHelper.angleBetween(j, TyflewEntity.this.getYaw()) < 3.0f ? MathHelper.stepTowards(this.targetSpeed, 1.8f, 0.005f * (1.8f / this.targetSpeed)) : MathHelper.stepTowards(this.targetSpeed, 0.2f, 0.025f);
                float n = (float)(-(MathHelper.atan2(-e, g) * 57.2957763671875));
                TyflewEntity.this.setPitch(n);
                float o = TyflewEntity.this.getYaw() + 90.0f;
                double p = (double)(this.targetSpeed * MathHelper.cos(o * ((float)Math.PI / 180))) * Math.abs(d / i);
                double q = (double)(this.targetSpeed * MathHelper.sin(o * ((float)Math.PI / 180))) * Math.abs(f / i);
                double r = (double)(this.targetSpeed * MathHelper.sin(n * ((float)Math.PI / 180))) * Math.abs(e / i);
                Vec3d vec3d = TyflewEntity.this.getVelocity();
                TyflewEntity.this.setVelocity(vec3d.add(new Vec3d(p, r, q).subtract(vec3d).multiply(0.2)));
            }
        }
    }



    static class ShootBulletGoal extends Goal {
        private int counter;
        private final TyflewEntity tyflewEntity; // Reference to the parent TyflewEntity

        public ShootBulletGoal(TyflewEntity tyflewEntity) {
            this.tyflewEntity = tyflewEntity;
            this.counter = 20; // Initial cooldown before the first shot
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = tyflewEntity.getTarget();
            return livingEntity != null && livingEntity.isAlive() && tyflewEntity.world.getDifficulty() != Difficulty.PEACEFUL;
        }

        @Override
        public void start() {
            this.counter = 20;
        }

        @Override
        public void stop() {
            this.counter = 0;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (tyflewEntity.world.getDifficulty() == Difficulty.PEACEFUL) {
                return;
            }

            --this.counter;
            LivingEntity target = tyflewEntity.getTarget();
            if (target == null) {
                return;
            }

            double distance = tyflewEntity.squaredDistanceTo(target);
            if (distance < 400.0) { // Check if target is within range
                if (this.counter <= 0) {
                    this.counter = 40 + tyflewEntity.random.nextInt(20); // Adjust cooldown as needed

                    if (!tyflewEntity.world.isClient) {
                        // Create and spawn the HomingFlishEntity
                        HomingFlishEntity homingFlish = new HomingFlishEntity(ModEntities.HOMINGFLISH, tyflewEntity.world); // Ensure you have a reference to your ModEntities.HOMINGFLISH
                        homingFlish.refreshPositionAndAngles(tyflewEntity.getX(), tyflewEntity.getY(), tyflewEntity.getZ(), 0.0F, 0.0F);
                        homingFlish.setTarget(target); // Set the target for the homing missile
                        tyflewEntity.world.spawnEntity(homingFlish);
                    }

                    tyflewEntity.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (tyflewEntity.random.nextFloat() - tyflewEntity.random.nextFloat()) * 0.2F + 1.0F);
                }
            } else {
                tyflewEntity.setTarget(null); // Lose target if out of range
            }
        }
    }


    @Override
    protected void mobTick(){

        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    abstract static class MovementGoal
            extends Goal {
        public MovementGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }
    }

    private PlayState predicate (AnimationEvent event) {
        event.getController().setAnimation(fly_animation);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 0, this::predicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
