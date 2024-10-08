package net.fabricmc.bluetreebeasts.entities.custom.ai;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.EnumSet;
import java.util.List;


public class CustomLandMobWanderGoal extends Goal {
    private final PathAwareEntity mob;
    private final double speed;
    private final int executionChance;
    private final WorldView worldView;
    private long lastStartTime;
    private final long cooldown;
    private Vec3d currentDestination = null; // Added: Current target destination
    private static final float MAX_ROTATION_CHANGE_PER_TICK = 5.0F; // Max degrees the entity can rotate per tick

    public CustomLandMobWanderGoal(PathAwareEntity mob, double speed, int executionChance, long cooldown) {
        this.mob = mob;
        this.speed = speed;
        this.executionChance = executionChance;
        this.worldView = mob.getWorld();
        this.cooldown = cooldown;
        this.lastStartTime = 0;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        long currentTime = this.mob.getWorld().getTime();
        boolean isIdle = this.mob.getNavigation().isIdle();
        boolean isTimeForExecution = this.mob.getRandom().nextInt(executionChance) == 0;
        boolean isCooldownElapsed = (currentTime - this.lastStartTime) > this.cooldown;
        this.mob.isOnGround();



        return isIdle && isTimeForExecution && isCooldownElapsed;
    }

    @Override
    public void start() {
        this.mob.setNoGravity(false);
        Vec3d targetPos = findRandomTarget();
        if (targetPos != null && !targetPos.equals(currentDestination)) {
            this.mob.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, this.speed);
            this.currentDestination = targetPos;
            this.lastStartTime = this.mob.getWorld().getTime();
        }
    }



    @Override
    public void tick() {
        // Ensure there is a current destination and the mob is actually moving
        if (currentDestination != null && isMoving()) {
            if (shouldUpdateRotation(currentDestination)) {
                lookTowards(currentDestination.x, currentDestination.z);
            }
        }
    }

    // Check if the mob has moved since the last tick
    private boolean isMoving() {
        Vec3d currentPos = this.mob.getPos();
        Vec3d lastPos = new Vec3d(this.mob.prevX, this.mob.prevY, this.mob.prevZ);
        return !currentPos.equals(lastPos);
    }

    private boolean shouldUpdateRotation(Vec3d targetPos) {
        double distanceSquared = this.mob.getPos().squaredDistanceTo(targetPos);
        return distanceSquared > 4;
    }

    private void lookTowards(double targetX, double targetZ) {
        double dx = targetX - this.mob.getX();
        double dz = targetZ - this.mob.getZ();
        float targetYaw = (float)(MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;

        // Smooth rotation change
        this.mob.setYaw(smoothRotation(this.mob.getYaw(), targetYaw));
        this.mob.headYaw = this.mob.getYaw();
        this.mob.bodyYaw = this.mob.getYaw();
    }

    // Smoothly adjust the yaw towards the target yaw, limiting the change per tick
    private float smoothRotation(float currentYaw, float targetYaw) {
        float yawChange = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float limitedChange = MathHelper.clamp(yawChange, -CustomLandMobWanderGoal.MAX_ROTATION_CHANGE_PER_TICK, CustomLandMobWanderGoal.MAX_ROTATION_CHANGE_PER_TICK);
        return currentYaw + limitedChange;
    }
    private Vec3d findRandomTarget() {
        net.minecraft.util.math.random.Random random = this.mob.getRandom();
        double minDistanceSquared = 25;  // Minimum distance squared
        Vec3d bestTarget = null;
        double bestDistanceSquared = 0;

        for (int i = 0; i < 10; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = 10 + random.nextDouble() * 10;
            double dx = Math.cos(angle) * distance;
            double dz = Math.sin(angle) * distance;
            int dy = random.nextBoolean() ? random.nextInt(2) - 1 : 0;  // Allow for stepping up or down one block

            BlockPos targetPos = new BlockPos(this.mob.getX() + dx, this.mob.getY() + dy, this.mob.getZ() + dz);

            if (isPositionSafe(targetPos, (World) worldView)) {
                Vec3d targetVector = Vec3d.ofCenter(targetPos);
                double targetDistanceSquared = targetVector.squaredDistanceTo(this.mob.getPos());

                if (bestTarget == null || targetDistanceSquared > bestDistanceSquared) {
                    bestTarget = targetVector;
                    bestDistanceSquared = targetDistanceSquared;
                }
            }
        }
        return bestTarget != null && bestDistanceSquared > minDistanceSquared ? bestTarget : null;
    }

    private boolean isPositionSafe(BlockPos pos, World world) {
        if (!world.getBlockState(pos.down()).isOpaque()) {
            return false;
        }

        EntityDimensions entityDimensions = this.mob.getDimensions(this.mob.getPose());
        boolean isAreaNavigable = isAreaNavigable(pos, world, entityDimensions);

        return isAreaNavigable && isLandingSafe(pos, world) && !isEntityBlockingPath(pos, world, entityDimensions);
    }

    private boolean isAreaNavigable(BlockPos pos, World world, EntityDimensions dimensions) {
        // Check for solid ground and sufficient headroom around the entity
        for (int dx = -MathHelper.floor(dimensions.width / 2.0F); dx <= MathHelper.ceil(dimensions.width / 2.0F); dx++) {
            for (int dz = -MathHelper.floor(dimensions.width / 2.0F); dz <= MathHelper.ceil(dimensions.width / 2.0F); dz++) {
                BlockPos groundPos = pos.add(dx, -1, dz);
                BlockPos headroomPos = pos.add(dx, MathHelper.ceil(dimensions.height) - 1, dz);
                if (!world.getBlockState(groundPos).isOpaque() || !world.isAir(headroomPos)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isEntityBlockingPath(BlockPos pos, World world, EntityDimensions dimensions) {
        Box searchBox = new Box(pos.getX() - dimensions.width / 2.0, pos.getY(), pos.getZ() - dimensions.width / 2.0,
                pos.getX() + dimensions.width / 2.0, pos.getY() + dimensions.height, pos.getZ() + dimensions.width / 2.0);
        List<Entity> otherEntities = world.getOtherEntities(this.mob, searchBox, e -> !e.equals(this.mob));
        return !otherEntities.isEmpty();
    }

    private boolean isLandingSafe(BlockPos pos, World world) {
        BlockPos groundPos = pos.down();
        return world.getBlockState(groundPos).isOpaque() && world.getBlockState(groundPos.up()).isAir();  // Ensure there's air above the solid block
    }
}