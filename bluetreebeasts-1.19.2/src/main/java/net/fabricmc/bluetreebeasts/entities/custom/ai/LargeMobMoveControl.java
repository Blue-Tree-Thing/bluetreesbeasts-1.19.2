package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class LargeMobMoveControl extends MoveControl {
    public LargeMobMoveControl(MobEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        if (this.state == MoveControl.State.MOVE_TO) {
            double dx = this.targetX - this.entity.getX();
            double dy = this.targetY - this.entity.getY();
            double dz = this.targetZ - this.entity.getZ();
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (distance < this.entity.getBoundingBox().getAverageSideLength() * 0.5) {  // Consider half the side length to start slowing down
                this.entity.setVelocity(Vec3d.ZERO);
                return;
            }

            dx /= distance;
            dz /= distance;
            double speed = this.speed * this.entity.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED);
            Vec3d velocity = new Vec3d(dx * speed, this.entity.getVelocity().y, dz * speed);
            this.entity.setVelocity(velocity);

            float desiredYaw = (float)(MathHelper.atan2(dz, dx) * (180D / Math.PI)) - 90F;
            this.entity.setYaw(wrapDegrees(this.entity.getYaw(), desiredYaw, 90F));
            this.entity.forwardSpeed = (float)speed;
        }
    }


    public float wrapDegrees(float currentYaw, float targetYaw, float maxTurnSpeed) {
        float deltaYaw = MathHelper.subtractAngles(targetYaw, currentYaw);
        float adjustedYaw = MathHelper.clamp(deltaYaw, -maxTurnSpeed, maxTurnSpeed);
        return currentYaw + adjustedYaw;
    }
}
