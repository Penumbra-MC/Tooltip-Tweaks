package net.penumbra.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum DurabilityStyle implements NameableEnum {
    PERCENTAGE,
    FRACTION,
    DISABLED;

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}