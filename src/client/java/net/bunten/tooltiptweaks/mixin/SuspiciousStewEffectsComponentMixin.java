package net.bunten.tooltiptweaks.mixin;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(SuspiciousStewEffects.class)
public abstract class SuspiciousStewEffectsComponentMixin {

    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    public void appendTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, CallbackInfo info) {
        if (TooltipTweaksConfig.getInstance().updatePotionTooltips) info.cancel();
    }
}