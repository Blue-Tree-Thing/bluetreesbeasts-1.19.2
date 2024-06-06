package net.fabricmc.bluetreebeasts.items.custom;

import net.fabricmc.bluetreebeasts.items.BTBArmorMaterials;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.LOOP;

public class FlutterPackItem extends ArmorItem implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public FlutterPackItem(Settings settings) {
        super(BTBArmorMaterials.FLUTTERPACK, EquipmentSlot.CHEST, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ItemStack equippedItem = player.getEquippedStack(EquipmentSlot.CHEST);

            // Check if the currently equipped chest item is a FlutterPackItem
            if (equippedItem.getItem() instanceof FlutterPackItem) {
                NbtCompound nbt = stack.getOrCreateNbt();
                player.fallDistance = 0; // Reset fall distance
                updateJetpackState(nbt, player, world.getTime());

                boolean justStoppedSneaking = nbt.contains("LastSneaking") && !player.isSneaking() && nbt.getBoolean("LastSneaking");
                if (nbt.getBoolean("IsCharged") && justStoppedSneaking && player.isOnGround()) {
                    launchPlayer(player);
                    nbt.putBoolean("IsCharged", false); // Reset the charge after launch
                }


                BlockPos playerPos = player.getBlockPos();
                boolean isOnEdge = !world.getBlockState(playerPos.down()).isSolidBlock(world, playerPos.down()) &&
                        world.getBlockState(playerPos.down(2)).isSolidBlock(world, playerPos.down(2));

                if (!isOnEdge) {
                    player.setNoGravity(player.getVelocity().y < -0.15f && !player.isOnGround());
                }

                nbt.putBoolean("LastSneaking", player.isSneaking());
            } else {
                // Optionally reset any persistent effects when the item is not equipped
                player.setNoGravity(false);
                if (player.fallDistance == 0) {
                    player.fallDistance = -1; // Indicative reset value, adjust based on game logic needs
                }
            }
        }
    }

    private void updateJetpackState(NbtCompound nbt, PlayerEntity player, long currentTick) {
        if (player.isSneaking() && !nbt.getBoolean("IsCharging") && player.isOnGround()) {
            nbt.putBoolean("IsCharging", true);
            nbt.putLong("ChargeStartTick", currentTick);
        } else if (!player.isSneaking() && nbt.getBoolean("IsCharging")) {
            nbt.putBoolean("IsCharging", false);
            if (currentTick - nbt.getLong("ChargeStartTick") >= 20) { // 20 ticks is approximately 1 second
                nbt.putBoolean("IsCharged", true);
            }
        }
    }

    private void launchPlayer(PlayerEntity player) {
        player.addVelocity(0, 1.5, 0);
        player.velocityModified = true;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<FlutterPackItem> controller = new AnimationController<>(this, "controller", 3, this::animationPredicate);
        data.addAnimationController(controller);
    }

    private <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
        PlayerEntity player = event.getExtraDataOfType(PlayerEntity.class).get(0);
        NbtCompound nbt = player.getEquippedStack(EquipmentSlot.CHEST).getOrCreateNbt();

        boolean isCharged = nbt.getBoolean("IsCharged");
        boolean isCharging = nbt.getBoolean("IsCharging");
        boolean isThrusting = player.getVelocity().y > 0 && !player.isOnGround();
        boolean isInAir = player.getVelocity().y < 0 && !player.isOnGround();

        AnimationController<P> controller = event.getController();
        if (isThrusting) {
            // Set the thrusting animation whenever the player moves up
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.flutter_pack_thrust", HOLD_ON_LAST_FRAME));

        } else if (isCharging || isCharged) {
            // Set the charging animation
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.flutter_pack_charge", HOLD_ON_LAST_FRAME));

        } else if (isInAir) {
            // Set the slow descent animation
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.flutter_pack_slow_descent", LOOP));


        } else {
            // Set the idle animation
            controller.setAnimation(new AnimationBuilder().addAnimation("animation.flutter_pack_idle", LOOP));

        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}