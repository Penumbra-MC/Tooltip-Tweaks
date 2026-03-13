package net.bunten.tooltiptweaks.mixin;

import net.bunten.tooltiptweaks.tooltips.ConvertibleTooltipData;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTooltipComponent.class)
public interface TooltipComponentMixin {

    @Inject(method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;", at = @At("HEAD"), cancellable = true)
    private static void of(TooltipComponent data, CallbackInfoReturnable<ClientTooltipComponent> info) {
        if (data instanceof ConvertibleTooltipData convertible)
            info.setReturnValue(convertible.getComponent());
    }
}