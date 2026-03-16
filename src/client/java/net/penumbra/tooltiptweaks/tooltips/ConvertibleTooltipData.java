package net.penumbra.tooltiptweaks.tooltips;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public interface ConvertibleTooltipData extends TooltipComponent {

    boolean canDisplay(ItemStack stack);

    ConvertibleTooltipData withStack(ItemStack stack);

    @Environment(EnvType.CLIENT)
    ClientTooltipComponent getComponent();
}