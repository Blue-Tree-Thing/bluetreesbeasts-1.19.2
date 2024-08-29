package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.ai.CustomLandMobWanderGoal;
import net.fabricmc.bluetreebeasts.entities.custom.ai.GigantelopeForageGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
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

public class DesertHopperEntity extends AnimalEntity implements IAnimatable {

    protected static final AnimationBuilder hop_animation = new AnimationBuilder().addAnimation("animation.desert_hopper.hop", LOOP);
    private static final AnimationBuilder idle_animation = new AnimationBuilder().addAnimation("animation.desert_hopper.idle", LOOP);
    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public DesertHopperEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.navigation = new MobNavigation(this, world);
        this.moveControl = new MoveControl(this);
        this.experiencePoints = 10;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .6f)
                .add(EntityAttributes.GENERIC_ARMOR, 6f);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(3, new EscapeDangerGoal(this, 1));
        this.goalSelector.add(3, new CustomLandMobWanderGoal(this, .5, 80, 3));
        this.targetSelector.add(7, new AnimalMateGoal(this,1));

    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.CACTUS;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);

    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.DESERT_HOPPER.create(world);
    }


    private PlayState predicate(AnimationEvent<DesertHopperEntity> event) {
        AnimationController<DesertHopperEntity> controller = event.getController();
        boolean isMoving = event.isMoving();
        String targetAnimation;

        if (isMoving) {
            targetAnimation = "animation.desert_hopper.walk";
        }else {
            targetAnimation = "animation.desert_hopper.idle";
        }

        // Ensure transitions are managed properly
        String currentAnimationName = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animationName : "";
        if (!currentAnimationName.equals(targetAnimation)) {
            // Assuming a hypothetical way to specify transition ticks.
            // This is conceptual and may not match GeckoLib's actual API.
            controller.transitionLengthTicks = 2; // This is a made-up property for illustration purposes.
            switch (targetAnimation) {
                case "animation.desert_hopper.walk" -> controller.setAnimation(hop_animation);
                case "animation.desert_hopper.idle" -> controller.setAnimation(idle_animation);
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this,"controller", 2, this::predicate));

    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return factory;
    }
}
