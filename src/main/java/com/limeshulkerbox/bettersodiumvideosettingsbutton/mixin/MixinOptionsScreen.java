package com.limeshulkerbox.bettersodiumvideosettingsbutton.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OptionsScreen.class, priority = 500)
public abstract class MixinOptionsScreen extends Screen {
    @Shadow @Final private GameOptions settings;

    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Dynamic
    @Inject(method = "method_19828(Lnet/minecraft/client/gui/widget/ButtonWidget;)V", at = @At("HEAD"), cancellable = true)
    private void disableSodiumSettings(ButtonWidget widget, CallbackInfo ci) {
        assert this.client != null;
        this.client.openScreen(new VideoOptionsScreen((Screen) (Object) this, settings));

        ci.cancel();
    }
}
