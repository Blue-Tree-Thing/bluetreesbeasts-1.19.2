package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.entities.custom.ai.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
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
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class GannetWhaleEntity extends AnimalEntity implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    // Honk animation control
    private static final AnimationBuilder honk_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_honk", PLAY_ONCE);
    private int honkTimer = 0; // Timer to manage when to start honking
    private boolean isHonking = false; // Flag to indicate if the entity is currently honking

    // Movement and idle animations
    private static final AnimationBuilder move_land_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_move_land", LOOP);
    private static final AnimationBuilder idle_land_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_idle_land", LOOP);
    private static final AnimationBuilder move_water_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_move_water", LOOP);
    private static final AnimationBuilder idle_water_animation = new AnimationBuilder().addAnimation("animation.gannet_whale_idle_water", LOOP);

    // Navigation
    private final EntityNavigation landNavigation;
    private final EntityNavigation waterNavigation;

    public GannetWhaleEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.landNavigation = new MobNavigation(this, world);
        this.waterNavigation = new SwimNavigation(this, world);
        setCustomNavigation(this.isInsideWaterOrBubbleColumn());
    }

    private void setCustomNavigation(boolean isInWater) {
        this.navigation = isInWater ? waterNavigation : landNavigation;
        if (isInWater) {
            this.moveControl = new AquaticMoveControl(this, 80, 10, 2f, 0.2f, false);
            this.lookControl = new YawAdjustingLookControl(this, 10);
        } else {
            this.moveControl = new MoveControl(this);
        }
    }

    @Override
    public void tick() {
        super.tick();
        boolean currentlyInWater = this.isInsideWaterOrBubbleColumn();
        if ((currentlyInWater && this.navigation != waterNavigation) ||
                (!currentlyInWater && this.navigation != landNavigation)) {
            setCustomNavigation(currentlyInWater);
        }

        // Honking logic
        if (isHonking) {
            if (honkTimer > 0) {
                honkTimer--;
            } else {
                isHonking = false; // Stop honking when the timer runs out
            }
        } else if (this.getRandom().nextInt(1000) == 0) { // Randomly start honking
            isHonking = true;
            honkTimer = 20; // Play honk animation for 20 ticks (~1 second)
        }
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .4f)
                .add(EntityAttributes.GENERIC_ARMOR, 2f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new EscapeDangerGoal(this, .5));
        this.goalSelector.add(3, new GannetWhaleTransitionGoal(this, .7));
        this.goalSelector.add(1, new UnifiedWanderGoal(this, 0.4, 3.0, 100, 10, 500));
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 8, this::predicate));
    }

    private PlayState predicate(AnimationEvent<GannetWhaleEntity> event) {
        AnimationController<GannetWhaleEntity> controller = event.getController();
        if (isHonking) {
            controller.setAnimation(honk_animation);
            return PlayState.CONTINUE; // Continue playing the honk animation while the timer is active
        }

        // Regular movement and idle animations
        boolean isMoving = event.isMoving();
        boolean isSwimming = this.isTouchingWater();
        if (isMoving) {
            controller.setAnimation(isSwimming ? move_water_animation : move_land_animation);
        } else {
            controller.setAnimation(isSwimming ? idle_water_animation : idle_land_animation);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}


