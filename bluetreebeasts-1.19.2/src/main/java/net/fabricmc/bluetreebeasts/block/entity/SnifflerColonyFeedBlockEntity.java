package net.fabricmc.bluetreebeasts.block.entity;

import net.fabricmc.bluetreebeasts.block.ModBlockEntities;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class SnifflerColonyFeedBlockEntity extends BlockEntity {
    private boolean isFull;

    public SnifflerColonyFeedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SNIFFLER_COLONY_FEED_BLOCK_ENTITY, pos, state);
        this.isFull = false;
    }

    public void addSeed() {
        if (!isFull) {
            isFull = true;
            produceSlop(); // Produce slop (bone meal) when a seed is added
            markDirty(); // Mark the block entity dirty for saving
            System.out.println("Feed block at " + pos + " is now full and producing slop.");
        }
    }

    private void produceSlop() {
        // Simulate production of bone meal
        ItemStack produce = new ItemStack(ModItems.SNIFFLERPRODUCE);
        // Eject the bone meal above this block
        assert world != null;
        Block.dropStack(world, pos.up(), produce);
    }

    public boolean isFull() {
        return isFull;
    }
}