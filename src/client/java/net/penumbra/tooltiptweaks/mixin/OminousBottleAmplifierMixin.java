package net.penumbra.tooltiptweaks.mixin;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OminousBottleAmplifier.class)
public abstract class OminousBottleAmplifierMixin {

    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    public void appendTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter, CallbackInfo info) {
        if (TooltipTweaksConfig.getInstance().updatePotionTooltips) info.cancel();
    }
}