package com.limeshulkerbox.bettersodiumvideosettingsbutton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class DisableSodiumOptionsMixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_SODIUM_OPTIONS_SCREEN = "me.jellysquid.mods.sodium.mixin.features.options.MixinOptionsScreen";
    private final Logger logger = LogManager.getLogger("Better Sodium Video Settings Button");

    @Override
    public void onLoad(String mixinPackage) {
        logger.info("The sodium options screen has been successfully moved!");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        logger.info("Loading: " + mixinClassName);
        return !mixinClassName.equals(MIXIN_SODIUM_OPTIONS_SCREEN);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
