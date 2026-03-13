package net.bunten.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum NourishmentStyle implements NameableEnum {
    TEXT,
    ICONS;

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}