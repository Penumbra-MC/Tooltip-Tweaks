package net.bunten.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum InstrumentDisplay implements NameableEnum {
    ENABLED,
    WHILE_CARRYING_NOTE_BLOCKS,
    DISABLED;

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}