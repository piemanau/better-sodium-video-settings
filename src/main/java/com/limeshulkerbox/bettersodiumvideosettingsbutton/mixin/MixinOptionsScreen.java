package com.limeshulkerbox.bettersodiumvideosettingsbutton.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OptionsScreen.class, priority = 9999)
public class MixinOptionsScreen extends Screen {

    @Shadow
    @Final
    private GameOptions settings;

    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void initAfter(CallbackInfo callbackInfo) {
        this.buttons.removeIf(abstractButtonWidget -> {
            if (abstractButtonWidget.getMessage().getString().equals((new TranslatableText("options.video")).getString())) {
                this.children.remove(abstractButtonWidget);
                return true;
            }
            return false;
        });
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, new TranslatableText("options.video"), (button) -> {
            if (this.client != null) this.client.openScreen(new VideoOptionsScreen(this, this.settings));
        }));
    }
}
