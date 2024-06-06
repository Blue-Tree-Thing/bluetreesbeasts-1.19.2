package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.custom.ai.GannetWhaleLandMovementGoal;
import net.fabricmc.bluetreebeasts.entities.custom.ai.GannetWhaleWaterMovementGoal;
import net.fabricmc.bluetreebeasts.entities.custom.ai.GannetWhaleWaterToLand;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlSwimNavigation;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.PLAY_ONCE;

public class GannetWhaleEntity extends AnimalEntity implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public boolean hasEgg = false;

    // Animation controls
    private static final AnimationBuilder honk_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_honk", PLAY_ONCE);
    private static final AnimationBuilder move_land_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_move_land", LOOP);
    private static final AnimationBuilder idle_land_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_idle_land", LOOP);
    private static final AnimationBuilder move_water_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_move_water", LOOP);
    private static final AnimationBuilder idle_water_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_idle_water", LOOP);

    private int honkTimer = 0;
    private boolean isHonking = false;

    // Navigation
    private final EntityNavigation landNavigation;
    private final EntityNavigation waterNavigation;

    public GannetWhaleEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.landNavigation = new MobNavigation(this, world);
        this.waterNavigation = new AxolotlSwimNavigation(this, world);
        initializeNavigation(this.isSubmergedInWater());
    }

    private void initializeNavigation(boolean inWater) {
        if (inWater) {
            switchToWaterNavigation();
        } else {
            switchToLandNavigation();
        }
    }

    private void switchToWaterNavigation() {
        if (this.navigation != waterNavigation) {
            this.navigation = waterNavigation;
            this.moveControl = new AquaticMoveControl(this, 80, 10, 2f, 0.2f, false);
            this.lookControl = new YawAdjustingLookControl(this, 10);
            this.navigation.recalculatePath();
        }
    }

    private void switchToLandNavigation() {
        if (this.navigation != landNavigation) {
            this.navigation = landNavigation;
            this.moveControl = new MoveControl(this);
            this.lookControl = new LookControl(this);
            this.navigation.recalculatePath();
        }
    }

    @Override
    public void tick() {
        super.tick();
        boolean currentlyInWater = this.isSubmergedInWater() || this.isTouchingWater();
        initializeNavigation(currentlyInWater);

        manageHonking();
    }

    private void manageHonking() {
        if (isHonking) {
            if (honkTimer > 0) {
                honkTimer--;
            } else {
                isHonking = false; // Stop honking when the timer runs out
            }
        } else if (this.getRandom().nextInt(1000) == 0) {
            isHonking = true;
            honkTimer = 20; // Play honk animation for 20 ticks (~1 second)
        }
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .5f)
                .add(EntityAttributes.GENERIC_ARMOR, 2f);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new EscapeDangerGoal(this, .5));
        this.goalSelector.add(3, new GannetWhaleWaterToLand(this, 1.5)); // Finding and moving into water

        this.goalSelector.add(3, new GannetWhaleWaterMovementGoal(this, 1.5)); // Active swimming
        this.targetSelector.add(7, new AnimalMateGoal(this,.6));
        this.goalSelector.add(3, new GannetWhaleLandMovementGoal(this, 0.4, 5000, 0.1, 30000));

    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.SALMON;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity mate) {
        return null; // No child creation logic here
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 8, this::predicate));
    }

    private PlayState predicate(AnimationEvent<GannetWhaleEntity> event) {
        AnimationController<GannetWhaleEntity> controller = event.getController();
        if (isHonking) {
            controller.setAnimation(honk_animation);
            return PlayState.CONTINUE; // Continue honking
        }
        // Select animation based on movement and environment
        boolean isMoving = event.isMoving();
        if (isMoving) {
            controller.setAnimation(this.isInsideWaterOrBubbleColumn() ? move_water_animation : move_land_animation);
        } else {
            controller.setAnimation(this.isInsideWaterOrBubbleColumn() ? idle_water_animation : idle_land_animation);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

