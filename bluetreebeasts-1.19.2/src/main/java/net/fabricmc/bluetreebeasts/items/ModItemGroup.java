package net.fabricmc.bluetreebeasts.items;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup BLUETREEBEASTS = FabricItemGroupBuilder.build(new Identifier(BlueTreeBeasts.MODID, "horrane_hide"), () -> new ItemStack(ModItems.HORRANEHIDE));

}
