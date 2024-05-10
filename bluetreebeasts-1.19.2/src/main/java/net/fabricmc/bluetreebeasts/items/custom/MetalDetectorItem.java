package net.fabricmc.bluetreebeasts.items.custom;

import net.fabricmc.bluetreebeasts.block.Modblocks;
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
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.literal("Detects Sophont Scrap!").formatted(Formatting.AQUA));
        } else {
            tooltip.add(Text.literal("Press Shift For Enlightenment :)").formatted(Formatting.GOLD));
        }
    }

    private static class CacheEntry {
        BlockPos lastSearchCenter;
        long lastSearchTime;
        ScrapSearchResult lastSearchResult;

        public CacheEntry(BlockPos lastSearchCenter, long lastSearchTime, ScrapSearchResult lastSearchResult) {
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

            ScrapSearchResult result;
            if (useCache) {
                result = cache.lastSearchResult;
            } else {
                result = checkForScrap(world, playerPos);
                searchCache.put(playerUUID, new CacheEntry(playerPos, currentTime, result));
            }

            if (result.scrapCount > 0) {
                playerEntity.sendMessage(Text.translatable("message.bluetreebeasts.scrap_found", result.scrapCount).formatted(Formatting.GREEN), true);
            } else {
                playerEntity.sendMessage(Text.translatable("message.bluetreebeasts.no_scrap_found").formatted(Formatting.RED), true);
            }

            return TypedActionResult.success(itemStack);
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }

    private ScrapSearchResult checkForScrap(World world, BlockPos centerPos) {
        int scrapCount = 0;
        List<BlockPos> scrapPositions = new ArrayList<>();

        for (int x = -CHECK_RADIUS; x <= CHECK_RADIUS; x++) {
            for (int y = -CHECK_RADIUS; y <= CHECK_RADIUS; y++) {
                for (int z = -CHECK_RADIUS; z <= CHECK_RADIUS; z++) {
                    BlockPos checkPos = centerPos.add(x, y, z);
                    BlockState state = world.getBlockState(checkPos);
                    if (state.isOf(Modblocks.SOPHONT_SCRAP)) {
                        scrapCount++;
                        scrapPositions.add(checkPos);                    }
                }
            }
        }

        return new ScrapSearchResult(scrapCount, scrapPositions);
    }

    private static class ScrapSearchResult {
        int scrapCount;
        List<BlockPos> scrapPositions;

        ScrapSearchResult(int scrapCount, List<BlockPos> scrapPositions) {
            this.scrapCount = scrapCount;
            this.scrapPositions = scrapPositions;
        }
    }
}