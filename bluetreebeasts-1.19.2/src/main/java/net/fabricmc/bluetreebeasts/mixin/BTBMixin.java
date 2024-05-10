package net.fabricmc.bluetreebeasts.mixin;

import net.fabricmc.bluetreebeasts.BlueTreeBeasts;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class BTBMixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		BlueTreeBeasts.LOGGER.info("What's up gamers. It's ya boy.");
	}
}
