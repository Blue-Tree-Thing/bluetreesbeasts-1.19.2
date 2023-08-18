package net.fabricmc.bluetreebeasts.items;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup BLUETREEBEASTS = FabricItemGroupBuilder.build(new Identifier(BlueTreeBeasts.MODID, "heartburn_glaive"), () -> new ItemStack(ModItems.HEARTBURN_GLAIVE));

}
