package com.limeshulkerbox.bsvsb.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OptionsScreen.class, priority = -5000)
public abstract class MixinOptionsScreen extends Screen {
    @Shadow
    @Final
    private GameOptions settings;

    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Inject(method = "method_19828", at = @At("HEAD"), cancellable = true)
    private void disableSodiumSettings(CallbackInfoReturnable<Screen> cir) {
        assert this.client != null;
        cir.setReturnValue(new VideoOptionsScreen(this, this.settings));
    }
}