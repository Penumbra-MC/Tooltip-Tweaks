package net.penumbra.tooltiptweaks.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.enchantment.Enchantment;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @ModifyArg(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/ComponentUtils;mergeStyles(Lnet/minecraft/network/chat/MutableComponent;Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;"), index = 1)
    private static Style changeNameColor(Style style) {
        if (!TooltipTweaksConfig.getInstance().updateEnchantmentTooltips) return style;
        return Style.EMPTY.withColor(ChatFormatting.BLUE);
    }
}