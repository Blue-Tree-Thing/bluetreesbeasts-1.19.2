package net.fabricmc.bluetreebeasts.items.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MetalDetectorItem extends Item {
    private static final int CHECK_RADIUS = 30;
    private static final long CACHE_DURATION = 10000;
    private final Map<UUID, CacheEntry> searchCache = new HashMap<>();


    public MetalDetectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()){
            tooltip.add(Text.literal("Detects Iron Ore!").formatted(Formatting.AQUA));
        }else{
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }

    private static class CacheEntry {
        BlockPos lastSearchCenter;
        long lastSearchTime;
        VeinSearchResult lastSearchResult;

        public CacheEntry(BlockPos lastSearchCenter, long lastSearchTime, VeinSearchResult lastSearchResult) {
            this.lastSearchCenter = lastSearchCenter;
            this.lastSearchTime = lastSearchTime;
            this.lastSearchResult = lastSearchResult;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient) {
            ItemStack itemStack = playerEntity.getStackInHand(hand);
            BlockPos playerPos = playerEntity.getBlockPos();
            UUID playerUUID = playerEntity.getUuid();
            long currentTime = System.currentTimeMillis();

            CacheEntry cache = searchCache.get(playerUUID);
            boolean useCache = cache != null && cache.lastSearchCenter.isWithinDistance(playerPos, CHECK_RADIUS)
                    && (currentTime - cache.lastSearchTime) < CACHE_DURATION;

            VeinSearchResult result;
            if (useCache) {
                result = cache.lastSearchResult;
            } else {
                result = checkForIronVeins(world, playerPos);
                searchCache.put(playerUUID, new CacheEntry(playerPos, currentTime, result));
            }

            if (result.ironOreCount > 0 || result.rawIronCount > 0) {
                if (result.isVein) {
                    playerEntity.sendMessage(Text.translatable("message.bluetreebeasts.potential_vein_found", result.ironOreCount, result.rawIronCount).formatted(Formatting.GREEN), true);
                } else {
                    playerEntity.sendMessage(Text.translatable("message.bluetreebeasts.iron_ore_found", result.ironOreCount).formatted(Formatting.YELLOW), true);
                }
            } else {
                playerEntity.sendMessage(Text.translatable("message.bluetreebeasts.no_iron_found").formatted(Formatting.RED), true);
            }
            
            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    private VeinSearchResult checkForIronVeins(World world, BlockPos centerPos) {
        int ironOreCount = 0;
        int rawIronCount = 0;
        boolean foundTuff = false;
        List<BlockPos> orePositions = new ArrayList<>();


        for (int x = -CHECK_RADIUS; x <= CHECK_RADIUS; x++) {
            for (int y = -CHECK_RADIUS; y <= CHECK_RADIUS; y++) {
                for (int z = -CHECK_RADIUS; z <= CHECK_RADIUS; z++) {
                    BlockPos checkPos = centerPos.add(x, y, z);
                    BlockState state = world.getBlockState(checkPos);
                    if (state.isOf(Blocks.IRON_ORE)) {
                        ironOreCount++;
                    } else if (state.isOf(Blocks.RAW_IRON_BLOCK)) {
                        orePositions.add(checkPos);

                    } else if (state.isOf(Blocks.TUFF)) {
                        foundTuff = true;
                    }
                }
            }
        }

        return new VeinSearchResult(ironOreCount, rawIronCount, foundTuff && ironOreCount > 0, orePositions);

    }



    private static class VeinSearchResult {
        int ironOreCount;
        int rawIronCount;
        boolean isVein;
        List<BlockPos> orePositions;

        VeinSearchResult(int ironOreCount, int rawIronCount, boolean isVein, List<BlockPos> orePositions) {
            this.ironOreCount = ironOreCount;
            this.rawIronCount = rawIronCount;
            this.isVein = isVein;
            this.orePositions = orePositions;
        }
    }

}
