package net.penumbra.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum ContainerStyle implements NameableEnum {
    LIST_PER_ITEM,
    LIST_PER_STACK,
    INVENTORY,
    VANILLA;

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}