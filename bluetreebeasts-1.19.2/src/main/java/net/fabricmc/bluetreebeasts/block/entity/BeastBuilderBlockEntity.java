package net.fabricmc.bluetreebeasts.block.entity;

import net.fabricmc.bluetreebeasts.recipe.BeastBuilderRecipe;
import net.fabricmc.bluetreebeasts.screen.BeastBuilderScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BeastBuilderBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;




    public BeastBuilderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEASTBUILDER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {

                switch (index) {
                    case 0: return BeastBuilderBlockEntity.this.progress;
                    case 1: return BeastBuilderBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0: BeastBuilderBlockEntity.this.progress = value; break;
                    case 1: BeastBuilderBlockEntity.this.maxProgress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }




    @Override
    public Text getDisplayName() {
        return Text.translatable("Beast Builder");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BeastBuilderScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("quack_station.progress", progress);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("quack_station.progress");
        super.readNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }
    private void resetProgress() {
        this.progress = 0;
    }

    public static  void tick(World world, BlockPos blockPos, BlockState state, BeastBuilderBlockEntity entity) {
        if(world.isClient()){
            return;
        }
        if(hasRecipe(entity)){
            entity.progress++;
            markDirty(world, blockPos,state);
        
        if(entity.progress >= entity.maxProgress) {
            craftItem(entity);
        }
        }else{
            entity.resetProgress();
            markDirty(world, blockPos,state);
        }
    }



    private static void craftItem(BeastBuilderBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<BeastBuilderRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(BeastBuilderRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipe(entity)){
            entity.removeStack(1,1);
            entity.removeStack(0,1);

            entity.setStack(2,new ItemStack(recipe.get().getOutput().getItem(),entity.getStack(2).getCount() + 1));

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(BeastBuilderBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<BeastBuilderRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(BeastBuilderRecipe.Type.INSTANCE, inventory, entity.getWorld());
        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }


}
