package net.fabricmc.bluetreebeasts.entities.custom.ai;

import net.fabricmc.bluetreebeasts.entities.custom.WoollyGigantelopeEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldEvents;

import java.util.EnumSet;
import java.util.function.Predicate;

public class GigantelopeForageGoal extends Goal {

    public static boolean isDigging;
    private static int timer;
    private static final Predicate<BlockState> SNOW_LAYER_PREDICATE = BlockStatePredicate.forBlock(Blocks.SNOW);
    private final WoollyGigantelopeEntity woollyGigantelope;

    public GigantelopeForageGoal(WoollyGigantelopeEntity mob) {
        this.woollyGigantelope = mob;
        woollyGigantelope.world = mob.world;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
    }
    @Override
    public boolean canStart() {
        if (this.woollyGigantelope.getRandom().nextInt(this.woollyGigantelope.isBaby() ? 25 : 500) != 0) {
            return false;
        }
        BlockPos blockPos = this.woollyGigantelope.getBlockPos();
        if (SNOW_LAYER_PREDICATE.test(this.woollyGigantelope.world.getBlockState(blockPos))) {
            return true;
        }
        return this.woollyGigantelope.world.getBlockState(blockPos.down()).isOf(Blocks.SNOW);
    }

    @Override
    public void start() {
        timer = this.getTickCount(40);

        this.woollyGigantelope.world.sendEntityStatus(this.woollyGigantelope, EntityStatuses.SET_SHEEP_EAT_GRASS_TIMER_OR_PRIME_TNT_MINECART);
        this.woollyGigantelope.getNavigation().stop();
    }

    @Override
    public void stop() {
        timer = 0;

    }

    @Override
    public boolean shouldContinue() {
        return timer > 0;
    }

    public static int getTimer() {
        return timer;
    }

    @Override
    public void tick() {
        timer = Math.max(0, timer - 1);
        if (timer != this.getTickCount(4)) {
            return;
        }
        BlockPos blockPos = this.woollyGigantelope.getBlockPos();
        if (SNOW_LAYER_PREDICATE.test(this.woollyGigantelope.world.getBlockState(blockPos))) {
            if (this.woollyGigantelope.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                this.woollyGigantelope.world.breakBlock(blockPos, false);
                isDigging = true;
            }
            this.woollyGigantelope.onEatingGrass();
        } else {
            BlockPos blockPos2 = blockPos.down();
            if (this.woollyGigantelope.world.getBlockState(blockPos2).isOf(Blocks.SNOW)) {
                if (this.woollyGigantelope.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    isDigging = true;
                    this.woollyGigantelope.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos2, Block.getRawIdFromState(Blocks.SNOW.getDefaultState()));
                    this.woollyGigantelope.world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);

                }
                this.woollyGigantelope.onEatingGrass();
            }
        }
        isDigging = false;
    }
}
