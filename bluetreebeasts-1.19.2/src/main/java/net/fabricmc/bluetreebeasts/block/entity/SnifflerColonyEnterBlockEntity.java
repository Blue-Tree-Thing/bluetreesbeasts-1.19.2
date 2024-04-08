package net.fabricmc.bluetreebeasts.block.entity;

import net.fabricmc.bluetreebeasts.block.ModBlockEntities;
import net.fabricmc.bluetreebeasts.entities.ModEntities;
import net.fabricmc.bluetreebeasts.entities.custom.CitySnifflerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.*;


public class SnifflerColonyEnterBlockEntity extends BlockEntity {
    private final Map<UUID, NbtCompound> storedSnifflers = new HashMap<>();

    public SnifflerColonyEnterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SNIFFLER_COLONY_ENTER_BLOCK_ENTITY, pos, state);
    }

    public void addSniffler(CitySnifflerEntity sniffler) {
        if (!world.isClient()) {
            NbtCompound snifflerData = new NbtCompound();
            sniffler.writeNbt(snifflerData);
            storedSnifflers.put(sniffler.getUuid(), snifflerData);
            System.out.println("Sniffler added: UUID=" + sniffler.getUuid() + " at " + pos);
            markDirty();
        }
    }

    public void releaseSnifflers() {
        if (!world.isClient() && canRelease()) {
            System.out.println("Releasing snifflers at " + pos + " during daytime.");
            storedSnifflers.forEach((uuid, data) -> {
                CitySnifflerEntity sniffler = new CitySnifflerEntity(ModEntities.CITY_SNIFFLER, (ServerWorld) world);
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
        long timeOfDay = world.getTimeOfDay() % 24000;
        return timeOfDay >= 1000 && timeOfDay < 12000; // True during Minecraft daytime
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        NbtList dataList = nbt.getList("StoredSnifflers", 10);
        storedSnifflers.clear();
        for (int i = 0; i < dataList.size(); i++) {
            UUID uuid = dataList.getCompound(i).getUuid("UUID");
            storedSnifflers.put(uuid, dataList.getCompound(i));
            System.out.println("Loaded sniffler data from NBT: UUID=" + uuid);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtList dataList = new NbtList();
        storedSnifflers.forEach((uuid, data) -> {
            data.putUuid("UUID", uuid);
            dataList.add(data);
            System.out.println("Saving sniffler data to NBT: UUID=" + uuid);
        });
        nbt.put("StoredSnifflers", dataList);
    }
}