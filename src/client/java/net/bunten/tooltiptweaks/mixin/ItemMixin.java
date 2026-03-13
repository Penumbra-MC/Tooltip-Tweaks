package net.bunten.tooltiptweaks.mixin;

import net.bunten.tooltiptweaks.tooltips.text.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
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

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type, CallbackInfo ci) {
        new DurabilityTooltips().register(stack, tooltip);
        new RepairCostTooltip().register(stack, tooltip);

        new NutritionTooltips().register(stack, tooltip);
        new StatusEffectTooltips().register(stack, tooltip);

        new AxolotlVariantTooltip().register(stack, tooltip);
        new ClockTooltips().register(stack, tooltip);
        new CompassTooltips().register(stack, tooltip);
        new ContainerTooltips().register(stack, tooltip);

        new InstrumentTooltip().register(stack, tooltip);
    }
}