package net.penumbra.tooltiptweaks.config.options;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

import static net.penumbra.tooltiptweaks.TooltipTweaks.creative;

public enum OtherEffectDisplay implements NameableEnum {
    ENABLED,
    CREATIVE_ONLY,
    DISABLED;

    public static boolean canDisplay(OtherEffectDisplay style) {
        if (style == DISABLED) return false;
        if (style == CREATIVE_ONLY) return creative();
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("tooltiptweaks.value." + name().toLowerCase());
    }
}