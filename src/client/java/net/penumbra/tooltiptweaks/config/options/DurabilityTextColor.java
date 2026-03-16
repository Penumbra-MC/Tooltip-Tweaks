package net.penumbra.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum DurabilityTextColor implements NameableEnum {
    MULTICOLOR,
    WHITE,
    GRAY;

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}