package net.bunten.tooltiptweaks.mixin;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static net.bunten.tooltiptweaks.tooltips.ConvertibleTooltips.CONVERTIBLE_TOOLTIP_DATA_REGISTRY;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void getTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> info) {
        CONVERTIBLE_TOOLTIP_DATA_REGISTRY.forEach((data) -> {
            if (data.canDisplay(stack)) info.setReturnValue(Optional.of(data.withStack(stack)));
        });
    }
}