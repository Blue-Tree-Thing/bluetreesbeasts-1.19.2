package net.fabricmc.bluetreebeasts.entities.custom;

import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
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

public class HopperShellEntity extends PathAwareEntity implements IAnimatable {

    @Nullable
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final AnimationBuilder spin_animation = new AnimationBuilder().addAnimation("animation.hopper_shell_projectile.spin", LOOP);

    public HopperShellEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1);
    }

    @Override
    public void tick() {
        super.tick();
        

        // When on the ground, reduce motion gradually
        if (this.onGround) {
            double friction = 0.6; // Simulate ground friction
            Vec3d velocity = this.getVelocity();
            this.setVelocity(velocity.x * friction, velocity.y, velocity.z * friction);

            // Stop the entity if it's barely moving
            if (velocity.horizontalLengthSquared() < 0.001) {
                this.discard(); // Remove the entity if it's practically stopped
            }
        }

        // Rotate the entity to align with its velocity vector for visual effect
        double horizontalSpeed = Math.sqrt(Math.pow(this.getVelocity().x, 2) + Math.pow(this.getVelocity().z, 2));
        float newYaw = (float) Math.atan2(this.getVelocity().z, this.getVelocity().x);
        this.setYaw((float) Math.toDegrees(newYaw) - 90);
    }

    private PlayState predicate(AnimationEvent event) {
        event.getController().setAnimation(spin_animation);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    @Override
    public @Nullable AnimationFactory getFactory() {
        return this.factory;
    }

}
