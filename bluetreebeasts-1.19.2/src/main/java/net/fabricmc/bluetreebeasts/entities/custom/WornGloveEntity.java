package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.custom.ai.GreaterGrapplerNavigation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WornGloveEntity extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final AnimationBuilder run_animation = new AnimationBuilder().addAnimation("animation.greater_grappler_run", true);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.greater_grappler_idle", true);
    private static final AnimationBuilder attack_animation = new AnimationBuilder().addAnimation("animation.greater_grappler_attack", true);
    private LivingEntity target;

    public WornGloveEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistent();
        this.checkDespawn();
        this.navigation = this.getNavigation();
        this.moveControl = getMoveControl();
        this.experiencePoints = 50;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .8f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAtEntityGoal(this, LivingEntity.class, 20.0f));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, .5f));
        this.goalSelector.add(2, new MeleeAttackGoal(this, .5, false));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, HostileEntity.class, true));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new GreaterGrapplerNavigation(this, world);
    }

    @Override
    public boolean disablesShield() {
        return true;
    }


    private PlayState predicate (AnimationEvent event) {
        if (!event.isMoving()) {
            event.getController().setAnimation(idle_animation);
            return PlayState.CONTINUE;
        } else if(event.isMoving()){
            event.getController().setAnimation(run_animation);}
        return PlayState.CONTINUE;
    }
    private PlayState attackPredicate (AnimationEvent event) {
        if (this.handSwinging) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(attack_animation);
            this.handSwinging = false;
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }



    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController(this,"attack_controller", 0, this::attackPredicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
