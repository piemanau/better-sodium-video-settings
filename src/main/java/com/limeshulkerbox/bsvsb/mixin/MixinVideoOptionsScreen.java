package com.limeshulkerbox.bsvsb.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

@Mixin(VideoOptionsScreen.class)
public abstract class MixinVideoOptionsScreen extends GameOptionsScreen {
    @Unique
    Constructor<?> SodiumVideoOptionsScreenClassCtor;
    @Unique
    Constructor<?> SodiumOptionsGUIClassCtor;
    @Unique
    Field SodiumOptionsGUIClassPagesField;
    @Unique
    Class<?> SodiumOptionsGUIClass;

    public MixinVideoOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "getOptions", at = @At("RETURN"), cancellable = true)
    private static void removeChunkBuilderButton(GameOptions gameOptions, CallbackInfoReturnable<SimpleOption<?>[]> cir) {
        var value = cir.getReturnValue();
        value = ArrayUtils.removeElement(value, gameOptions.getChunkBuilderMode());
        cir.setReturnValue(value);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void mixinInit(CallbackInfo callbackInfo) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 27, 150, 20, Text.translatable("text.bettersodiumvideosettings.sodiumvideosettings"), (button) -> {
            if (FabricLoader.getInstance().isModLoaded("reeses-sodium-options")) {
                flashyReesesOptionsScreen();
            } else {
                sodiumVideoOptionsScreen();
            }
        }));
    }

    @Unique
    void flashyReesesOptionsScreen() {
        if (SodiumVideoOptionsScreenClassCtor == null) {
            try {
                SodiumVideoOptionsScreenClassCtor = Class.forName("me.flashyreese.mods.reeses_sodium_options.client.gui.SodiumVideoOptionsScreen").getConstructor(Screen.class, List.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            assert this.client != null;
            ensureSodiumOptionsGUI();
            var tmpScreen = SodiumOptionsGUIClassCtor.newInstance(this);
            var pages = SodiumOptionsGUIClassPagesField.get(tmpScreen);
            this.client.setScreen((Screen) SodiumVideoOptionsScreenClassCtor.newInstance(this, pages));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Unique
    void ensureSodiumOptionsGUI()
    {
        if (SodiumOptionsGUIClass == null) {
            try {
                SodiumOptionsGUIClass = Class.forName("me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI");
                SodiumOptionsGUIClassCtor = SodiumOptionsGUIClass.getConstructor(Screen.class);
                SodiumOptionsGUIClassPagesField = SodiumOptionsGUIClass.getDeclaredField("pages");
                SodiumOptionsGUIClassPagesField.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Unique
    void sodiumVideoOptionsScreen() {
        ensureSodiumOptionsGUI();
        try {
            assert this.client != null;
            this.client.setScreen((Screen) SodiumOptionsGUIClassCtor.newInstance(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
