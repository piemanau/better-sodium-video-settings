package com.limeshulkerbox.bettersodiumvideosettingsbutton.mixin;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.Option;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen extends Screen {
    @Shadow
    @Final
    @Mutable
    private static Option[] OPTIONS;

    protected MixinVideoOptionsScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void mixinInit(CallbackInfo callbackInfo) {
        // We can't do this because sodium hasn't released for 1.17 yet
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 27, 150, 20, new LiteralText("Sodium Video Settings"), (button) -> {
            assert this.client != null;
            this.client.openScreen(new SodiumOptionsGUI(this));
        }));
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addSingleOptionEntry(Lnet/minecraft/client/option/Option;)I", ordinal = 0))
    private int removeGraphicsButton(ButtonListWidget buttonListWidget, Option option) {
        return 0;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void removeOptions(CallbackInfo ci) {
        OPTIONS = ArrayUtils.removeElement(OPTIONS, Option.FULLSCREEN);
        OPTIONS = ArrayUtils.removeElement(OPTIONS, Option.CLOUDS);
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 100, ordinal = 0))
    private int modifyDonePos(int input) {
        return 155;
    }

    @ModifyConstant(method = "init", constant = @Constant(intValue = 200, ordinal = 0))
    private int modifyDoneWidth(int input) {
        return 150;
    }
}