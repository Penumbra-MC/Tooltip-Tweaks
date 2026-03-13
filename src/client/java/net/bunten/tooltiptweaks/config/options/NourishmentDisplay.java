package net.bunten.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum NourishmentDisplay implements NameableEnum {
    NUTRITION_ONLY,
    NUTRITION_AND_SATURATION,
    DISABLED;

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}