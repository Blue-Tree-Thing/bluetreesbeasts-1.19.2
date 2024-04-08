package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.block.entity.SnifflerColonyEnterBlockEntity;
import net.fabricmc.bluetreebeasts.entities.custom.ai.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

public class CitySnifflerEntity extends AnimalEntity implements IAnimatable, IBlockCarrier {

    public static final TrackedData<Boolean> INSIDE_NEST = DataTracker.registerData(CitySnifflerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected static final AnimationBuilder walking_animation = new AnimationBuilder().addAnimation("animation.city_sniffler.walk", LOOP);
    private static final AnimationBuilder foraging_animation = new AnimationBuilder().addAnimation("animation.city_sniffler.gather", LOOP);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.city_sniffler.idle", LOOP);


    public CitySnifflerEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .5f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(INSIDE_NEST, false);
    }

    @Override
    protected void initGoals() {


        this.goalSelector.add(1, new LookAtEntityGoal(this, LivingEntity.class, 20.0f));
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(8, new EscapeDangerGoal(this, .8));
            this.goalSelector.add(6, new CitySnifflerPathGoal(this));
            this.goalSelector.add(5, new CustomLandMobWanderGoal(this, .5, 60, 3));
            this.goalSelector.add(4, new CitySnifflerConstructNestGoal(this));
            this.goalSelector.add(3, new CitySnifflerEnterNestGoal(this));
        this.targetSelector.add(7, new AnimalMateGoal(this,1));

    }


    @Override
    public void tick() {
        super.tick();
        BlockPos currentPos = this.getBlockPos();
        BlockPos blockEntityPos = currentPos.down(); // Assuming the nest block entity is directly beneath the entity
        World world = this.getWorld();
        BlockEntity blockEntity = world.getBlockEntity(blockEntityPos);
        SnifflerColonyEnterBlockEntity nestBlockEntity = (SnifflerColonyEnterBlockEntity) blockEntity;
        if (blockEntity != null) {

            if (this.getDataTracker().get(INSIDE_NEST) && !this.isRemoved()) {

                nestBlockEntity.addSniffler(this);
                this.remove(RemovalReason.DISCARDED); // Use the appropriate RemovalReason if needed

            }
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }


    @Override
    public boolean isCarryingBlock() {
        return false;
    }

    public boolean isInsideNest() {
        return this.dataTracker.get(INSIDE_NEST);
    }

    public boolean canEnterNest() {
        boolean isNight = this.world.getTimeOfDay() >= 13000 && this.world.getTimeOfDay() <= 23000;

        return isNight && !this.isInsideNest();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.dataTracker.get(INSIDE_NEST)) {
            return false; // Prevent damage if inside the nest.
        }
        return super.damage(source, amount);
    }



    @Nullable
    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        if (this.getDataTracker().get(INSIDE_NEST)) {
            // Return a smaller, non-collidable dimension when inside the nest
            return EntityDimensions.fixed(0.0F, 0.0F);
        }
        return super.getDimensions(pose);
    }


    private PlayState predicate (AnimationEvent event) {
        if (!event.isMoving()) {
            event.getController().setAnimation(idle_animation);
            return PlayState.CONTINUE;
        } else if(event.isMoving()){
            event.getController().setAnimation(walking_animation);}
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 4, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
