package net.penumbra.tooltiptweaks.mixin;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.penumbra.tooltiptweaks.tooltips.ConvertibleTooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.stream.Stream;

import static net.penumbra.tooltiptweaks.tooltips.ConvertibleTooltips.CONVERTIBLE_TOOLTIP_DATA_REGISTRY;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void getTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> info) {
        Stream<ConvertibleTooltipData> applicable = CONVERTIBLE_TOOLTIP_DATA_REGISTRY.stream().filter(data -> data.canDisplay(stack));
        applicable.findFirst().ifPresent(data -> info.setReturnValue(Optional.of(data.withStack(stack))));
    }
}