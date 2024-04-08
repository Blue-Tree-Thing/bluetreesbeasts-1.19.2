package net.fabricmc.bluetreebeasts.entities.custom;


import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class ForestFlishEntity extends AnimalEntity implements IAnimatable {

    public static final int field_28637 = MathHelper.ceil(2.4166098f);

    private static final TrackedData<Byte> BAT_FLAGS = DataTracker.registerData(ForestFlishEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(4.0);
    @Nullable
    private BlockPos hangingPosition;
    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final AnimationBuilder flight_animation = new AnimationBuilder().addAnimation("animation.forest_flish.flight", LOOP);
    private static final AnimationBuilder resting_animation = new AnimationBuilder().addAnimation("animation.forest_flish.resting",LOOP);

    public ForestFlishEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setRoosting(true);
        this.experiencePoints = 5;
    }

    @Override
    public boolean hasWings() {
        return !this.isRoosting() && this.age % field_28637 == 0;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5);
    }

    @Override
    protected void initGoals() {
        super.initGoals();

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    protected void tickCramming() {
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BAT_FLAGS, (byte)0);
    }

    public boolean isRoosting() {
        return (this.dataTracker.get(BAT_FLAGS) & 1) != 0;
    }

    public void setRoosting(boolean roosting) {
        byte b = this.dataTracker.get(BAT_FLAGS);
        if (roosting) {
            this.dataTracker.set(BAT_FLAGS, (byte)(b | 1));
        } else {
            this.dataTracker.set(BAT_FLAGS, (byte)(b & 0xFFFFFFFE));
        }
    }




    @Override
    public void tick() {
        super.tick();
            if (this.isRoosting()) {
                this.setVelocity(Vec3d.ZERO);
                this.setPos(this.getX(), (double) MathHelper.floor(this.getY()) + 1.0 - (double) this.getHeight(), this.getZ());
            } else {
                this.setVelocity(this.getVelocity().multiply(1.0, 0.6, 1.0));
            }

    }
    @Override
    protected void mobTick() {
        super.mobTick();
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.up();
        if (this.isRoosting()) {
            boolean bl = this.isSilent();
            if (this.world.getBlockState(blockPos2).isFullCube(this.world, blockPos)) {
                if (this.random.nextInt(200) == 0) {
                    this.headYaw = this.random.nextInt(360);
                }
                if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                    this.setRoosting(false);
                    if (!bl) {
                        this.world.syncWorldEvent(null, WorldEvents.BAT_TAKES_OFF, blockPos, 0);
                    }
                }
            } else {
                this.setRoosting(false);
                if (!bl) {
                    this.world.syncWorldEvent(null, WorldEvents.BAT_TAKES_OFF, blockPos, 0);
                }
            }
        } else {
            if (!(this.hangingPosition == null || this.world.isAir(this.hangingPosition) && this.hangingPosition.getY() > this.world.getBottomY())) {
                this.hangingPosition = null;
            }
            if (this.hangingPosition == null || this.random.nextInt(30) == 0 || this.hangingPosition.isWithinDistance(this.getPos(), 2.0)) {
                this.hangingPosition = new BlockPos(this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - 2.0, this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
            }
            double d = (double)this.hangingPosition.getX() + 0.5 - this.getX();
            assert this.hangingPosition != null;
            double e = (double)this.hangingPosition.getY() + 0.1 - this.getY();
            assert this.hangingPosition != null;
            double f = (double)this.hangingPosition.getZ() + 0.5 - this.getZ();
            Vec3d vec3d = this.getVelocity();
            Vec3d vec3d2 = vec3d.add((Math.signum(d) * 0.5 - vec3d.x) * (double)0.1f, (Math.signum(e) * (double)0.7f - vec3d.y) * (double)0.1f, (Math.signum(f) * 0.5 - vec3d.z) * (double)0.1f);
            this.setVelocity(vec3d2);
            float g = (float)(MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875) - 90.0f;
            float h = MathHelper.wrapDegrees(g - this.getYaw());
            this.forwardSpeed = 0.6f;
            this.setYaw(this.getYaw() + h);
            if (this.random.nextInt(100) == 0 && this.world.getBlockState(blockPos2).isFullCube(this.world, blockPos2)) {
                this.setRoosting(true);
            }
        }
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public boolean canAvoidTraps() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.world.isClient && this.isRoosting()) {
            this.setRoosting(false);
        }
        return super.damage(source, amount);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(BAT_FLAGS, nbt.getByte("BatFlags"));
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("BatFlags", this.dataTracker.get(BAT_FLAGS));
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height / 2.0f;
    }


    private PlayState predicate (AnimationEvent event) {
        if (this.isRoosting()) {
            event.getController().setAnimation(resting_animation);
            return PlayState.CONTINUE;
        } else if(!this.isRoosting()){
            event.getController().setAnimation(flight_animation);}
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
