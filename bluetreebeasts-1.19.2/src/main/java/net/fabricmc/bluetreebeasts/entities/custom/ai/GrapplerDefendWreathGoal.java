package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.block.custom.WreathBlock;
import net.fabricmc.bluetreebeasts.entities.custom.GreaterGrapplerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GrapplerDefendWreathGoal extends Goal {
    private final GreaterGrapplerEntity grappler;
    private final World world;
    private final double range;
    private BlockPos wreathPosition;

    public GrapplerDefendWreathGoal(GreaterGrapplerEntity grappler, double range) {
        this.grappler = grappler;
        this.world = grappler.getWorld();
        this.range = range;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!grappler.isPacified() || grappler.getTarget() != null) {
            return false;
        }
        findNearestWreathBlock();
        return wreathPosition != null;
    }

    private void findNearestWreathBlock() {
        BlockPos pos = grappler.getBlockPos();
        BlockPos.Mutable searchPos = new BlockPos.Mutable();
        for (int dx = (int) -range; dx <= range; dx++) {
            for (int dy = (int) -range; dy <= range; dy++) {
                for (int dz = (int) -range; dz <= range; dz++) {
                    searchPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                    if (world.getBlockState(searchPos).getBlock() instanceof WreathBlock) {
                        wreathPosition = searchPos.toImmutable();
                        return;
                    }
                }
            }
        }
        wreathPosition = null;
    }

    @Override
    public boolean shouldContinue() {
        return grappler.isPacified() && grappler.getTarget() == null && wreathPosition != null && world.getBlockState(wreathPosition).getBlock() instanceof WreathBlock;
    }

    @Override
    public void start() {
        moveTowardsWreath();
    }

    @Override
    public void tick() {
        double distanceSquared = grappler.squaredDistanceTo(Vec3d.ofCenter(wreathPosition));
        if (distanceSquared > range * range || !grappler.getNavigation().isFollowingPath()) {
            moveTowardsWreath();
        }
    }

    private void moveTowardsWreath() {
        if (wreathPosition != null) {
            this.grappler.getNavigation().startMovingTo(wreathPosition.getX() + 0.5, wreathPosition.getY() + 0.5, wreathPosition.getZ() + 0.5, 0.6);
        }
    }

    @Override
    public void stop() {
        wreathPosition = null;
    }
}
