package net.fabricmc.bluetreebeasts.block.entity;

import net.fabricmc.bluetreebeasts.block.ModBlockEntities;


import net.fabricmc.bluetreebeasts.block.custom.SnifflerColonyFeedBlock;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;


import java.util.*;


public class SnifflerColonyEnterBlockEntity extends BlockEntity {
    private final Map<UUID, NbtCompound> storedSnifflers = new HashMap<>();
    private BlockPos feedBlockPos; // Directly store the position of the feed block

    public SnifflerColonyEnterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SNIFFLER_COLONY_ENTER_BLOCK_ENTITY, pos, state);
    }

    public void discoverFeedBlock() {
        BlockPos belowPos = pos.down(); // Position directly below the enter block
        if (isValidFeedBlock(belowPos)) {
            this.feedBlockPos = belowPos;
            System.out.println("Feed block discovered: " + feedBlockPos);
        } else {
            this.feedBlockPos = null;
            System.out.println("No feed block directly underneath.");
        }
    }

    private boolean isValidFeedBlock(BlockPos pos) {
        assert world != null;
        return world.getBlockState(pos).getBlock() instanceof SnifflerColonyFeedBlock;
    }

    public void handleSnifflerEntry(CitySnifflerEntity sniffler) {
        addSniffler(sniffler);
        if (sniffler.hasSeeds()) {
            depositSeed(sniffler);
        }
    }

    public void depositSeed(CitySnifflerEntity sniffler) {
        if (feedBlockPos != null && sniffler.hasSeeds()) {
            SnifflerColonyFeedBlockEntity feedBlock = getFeedBlockEntity(feedBlockPos);
            if (feedBlock != null && !feedBlock.isFull()) {
                feedBlock.addSeed();
                sniffler.decreaseSeedCount();  // Subtract one seed
                System.out.println("Seed deposited at " + feedBlockPos);
            } else {
                System.out.println("Feed block is full or not found.");
            }
        }
    }

    private SnifflerColonyFeedBlockEntity getFeedBlockEntity(BlockPos pos) {
        assert this.world != null;
        BlockEntity entity = this.world.getBlockEntity(pos);
        if (entity instanceof SnifflerColonyFeedBlockEntity) {
            return (SnifflerColonyFeedBlockEntity) entity;
        }
        return null;
    }



    public void addSniffler(CitySnifflerEntity sniffler) {
        assert world != null;
        if (!world.isClient()) {
            NbtCompound snifflerData = new NbtCompound();
            sniffler.writeNbt(snifflerData);
            storedSnifflers.put(sniffler.getUuid(), snifflerData);
            discoverFeedBlock();  // Check for feed block whenever a sniffler is added
            System.out.println("Sniffler added: UUID=" + sniffler.getUuid() + " at " + pos);
            markDirty();
        }
    }

    public void releaseSnifflers() {
        assert world != null;
        if (!world.isClient() && canRelease()) {
            System.out.println("Releasing snifflers at " + pos + " during daytime.");
            storedSnifflers.forEach((uuid, data) -> {
                CitySnifflerEntity sniffler = new CitySnifflerEntity(ModEntities.CITY_SNIFFLER, world);
                sniffler.readNbt(data);
                sniffler.refreshPositionAndAngles(pos.up(), 0.0F, 0.0F);
                if (world.spawnEntity(sniffler)) {
                    System.out.println("Sniffler released: UUID=" + uuid);
                } else {
                    System.out.println("Failed to spawn sniffler: UUID=" + uuid);
                }
            });
            storedSnifflers.clear();
            markDirty();
        }
    }

    private boolean canRelease() {
        assert world != null;
        long timeOfDay = world.getTimeOfDay() % 24000;
        return timeOfDay >= 1000 && timeOfDay < 12000;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtList dataList = new NbtList();
        storedSnifflers.forEach((uuid, data) -> {
            NbtCompound snifflerData = new NbtCompound();
            snifflerData.putUuid("UUID", uuid);
            snifflerData.put("Data", data);
            dataList.add(snifflerData);
        });
        nbt.put("StoredSnifflers", dataList);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        NbtList dataList = nbt.getList("StoredSnifflers", 10);
        storedSnifflers.clear();
        for (int i = 0; i < dataList.size(); i++) {
            NbtCompound data = dataList.getCompound(i);
            UUID uuid = data.getUuid("UUID");
            storedSnifflers.put(uuid, data.getCompound("Data"));
        }
    }
}