package net.bunten.tooltiptweaks.mixin;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.CrossbowDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChargedProjectiles.class)
public abstract class ChargedProjectilesMixin {

    @ModifyArg(
            method = "addProjectileTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V",
                    ordinal = 0
            )
    )
    private static Object recolorProjectileText1(Object object) {
        return TooltipTweaksConfig.getInstance().updateCrossbowTooltips != CrossbowDisplay.DISABLED ? ((Component) object).copy().withStyle(ChatFormatting.GRAY) : object;
    }

    @ModifyArg(
            method = "addProjectileTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V",
                    ordinal = 1
            )
    )
    private static Object recolorProjectileText2(Object object) {
        return TooltipTweaksConfig.getInstance().updateCrossbowTooltips != CrossbowDisplay.DISABLED ? ((Component) object).copy().withStyle(ChatFormatting.GRAY) : object;
    }
}