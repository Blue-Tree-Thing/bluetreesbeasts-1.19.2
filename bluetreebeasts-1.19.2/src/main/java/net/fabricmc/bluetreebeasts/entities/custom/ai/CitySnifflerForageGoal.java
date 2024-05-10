package net.fabricmc.bluetreebeasts.entities.custom.ai;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.fabricmc.bluetreebeasts.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.ItemEntity;
import java.util.List;

public class CitySnifflerForageGoal extends Goal {
    private final CitySnifflerEntity sniffler;
    private final World world;
    private int cooldown;
    private int timer;

    public CitySnifflerForageGoal(CitySnifflerEntity sniffler) {
        this.sniffler = sniffler;
        this.world = sniffler.getWorld();
        this.cooldown = 0;
        this.timer = 0;
    }

    @Override
    public boolean canStart() {
        if (cooldown > 0) {
            --cooldown;
            return false;
        }

        BlockState stateBelow = world.getBlockState(sniffler.getBlockPos().down());
        return !sniffler.hasSeeds() && !sniffler.hasProduce() && sniffler.isAlive() && !sniffler.isCarryingBlock() && (stateBelow.isOf(Blocks.TALL_GRASS) || stateBelow.isOf(Blocks.GRASS));
    }

    @Override
    public void start() {
        BlockPos pos = sniffler.getBlockPos().down();
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isOf(Blocks.TALL_GRASS) || blockState.isOf(Blocks.GRASS)) {
            world.breakBlock(pos, true);
            sniffler.setForaging(true);
            cooldown = 100;
            timer = 20; // Set the timer to one second (20 ticks)
        }
    }

    @Override
    public boolean shouldContinue() {
        return sniffler.isForaging;  // This goal triggers once per valid check and does not need to continue
    }

    public void tick() {
        if (timer > 0) {
            --timer;
            return; // Do not continue processing if the timer is active
        }

        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class,
                sniffler.getBoundingBox().expand(1),
                e -> (e.getStack().getItem() == Items.WHEAT_SEEDS || e.getStack().getItem() == ModItems.SNIFFLERPRODUCE) && e.getStack().getCount() > 0
        );
        if (!items.isEmpty()) {
            ItemStack itemStack = items.get(0).getStack();
            sniffler.pickUpItem(itemStack);
            if (itemStack.isEmpty()) {
                items.get(0).discard();
            }
            sniffler.setForaging(false);
        }
    }
}