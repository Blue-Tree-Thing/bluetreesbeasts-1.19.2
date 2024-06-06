package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;

import net.fabricmc.bluetreebeasts.entities.custom.ai.CustomLandMobWanderGoal;
import net.fabricmc.bluetreebeasts.entities.custom.ai.GigantelopeForageGoal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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

public class WoollyGigantelopeEntity extends AnimalEntity implements IAnimatable {

    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected static final AnimationBuilder walking_animation = new AnimationBuilder().addAnimation("animation.woolly_gigantelope.walk", LOOP);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.woolly_gigantelope.idle", LOOP);
    private static final AnimationBuilder baby_idle_animation = new AnimationBuilder().addAnimation("animation.baby_gigantelope.idle", LOOP);
    private static final AnimationBuilder baby_walking_animation = new AnimationBuilder().addAnimation("animation.baby_gigantelope.walk", LOOP);
    private int eatGrassTimer;






    public WoollyGigantelopeEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .5f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAtEntityGoal(this, LivingEntity.class, 20.0f));
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(3, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(4, new GigantelopeForageGoal(this));
        this.goalSelector.add(5, new CustomLandMobWanderGoal(this, .5, 80, 3));
        this.targetSelector.add(7, new AnimalMateGoal(this,1));

    }



    @Override
    protected void mobTick() {
        this.eatGrassTimer = GigantelopeForageGoal.getTimer();
        super.mobTick();
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            this.eatGrassTimer = Math.max(0, this.eatGrassTimer - 1);
        }
        super.tickMovement();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.WHEAT;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
            return super.interactMob(player, hand);

    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.WOOLLYGIGANTELOPE.create(world);
    }

    private void setAnimationWithTransition(AnimationController<WoollyGigantelopeEntity> controller, AnimationBuilder animation, int transitionTicks) {
        controller.transitionLengthTicks = transitionTicks;
        controller.setAnimation(animation);
    }

    private PlayState predicate(AnimationEvent<WoollyGigantelopeEntity> event) {
        AnimationController<WoollyGigantelopeEntity> controller = event.getController();
        boolean isMoving = event.isMoving();


        String targetAnimation;
        int transitionTicks;

        if (isMoving) {
            targetAnimation = "animation.woolly_gigantelope.walk";
            transitionTicks = 7; // Custom transition length for walking animation
        } else {
            targetAnimation = "animation.woolly_gigantelope.idle";
            transitionTicks = 2; // Custom transition length for idle animation
        }

        String currentAnimationName = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animationName : "";
        if (!currentAnimationName.equals(targetAnimation)) {
            // Use the helper method to set the animation with a custom transition period.
            switch (targetAnimation) {

                case "animation.woolly_gigantelope.walk" ->
                        setAnimationWithTransition(controller, walking_animation, transitionTicks);
                case "animation.woolly_gigantelope.idle" ->
                        setAnimationWithTransition(controller, idle_animation, transitionTicks);
            }
        }

        return PlayState.CONTINUE;
    }

    private PlayState babyPredicate(AnimationEvent<WoollyGigantelopeEntity> event) {
        AnimationController<WoollyGigantelopeEntity> controller = event.getController();
        boolean isMoving = event.isMoving();

        String targetAnimation;

        if (this.isBaby()) {
            if (isMoving) {
                targetAnimation = "animation.baby_gigantelope.walk";
            } else {
                targetAnimation = "animation.baby_gigantelope.idle";
            }

            String currentAnimationName = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animationName : "";
            if (!currentAnimationName.equals(targetAnimation)) {
                // Assuming a hypothetical way to specify transition ticks.
                // This is conceptual and may not match GeckoLib's actual API.
                controller.transitionLengthTicks = 2; // This is a made-up property for illustration purposes.
                switch (targetAnimation) {
                    case "animation.baby_gigantelope.walk" -> controller.setAnimation(baby_walking_animation);
                    case "animation.baby_gigantelope.idle" -> controller.setAnimation(baby_idle_animation);
                }
            }
        }
        return PlayState.CONTINUE;
    }





    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController(this,"baby_controller", 0, this::babyPredicate));
    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return factory;
    }
}
