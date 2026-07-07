package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.penumbra.tooltiptweaks.config.options.RepairCostDisplay;

import java.util.List;

import static net.penumbra.tooltiptweaks.TooltipTweaks.creative;

public class RepairCostTooltip implements TextTooltipProvider {

    @Override
    public void register(ItemStack stack, List<Component> lines) {
        if (canDisplay(stack)) {
            Integer repairCost = stack.get(DataComponents.REPAIR_COST);
            if (repairCost == null || repairCost < 1) return;
            MutableComponent message = Component.translatable("tooltiptweaks.ui.repair_cost", repairCost);

            if (!creative() && repairCost + 1 >= 40) message = Component.translatable("tooltiptweaks.ui.cannot_repair");

            lines.add(message.setStyle(message.getStyle().withColor(getRepairCostTextColor(repairCost))));
        }
    }

    private boolean canDisplay(ItemStack stack) {
        if (!stack.has(DataComponents.REPAIR_COST) || config.repairCostDisplay == RepairCostDisplay.DISABLED) return false;

        if (config.repairCostDisplay == RepairCostDisplay.ON_RELEVANT_MENUS) {
            return minecraft.gui.screen() instanceof AnvilScreen || minecraft.gui.screen() instanceof GrindstoneScreen || minecraft.gui.screen() instanceof SmithingScreen;
        }

        return config.repairCostDisplay == RepairCostDisplay.ENABLED;
    }

    private int getRepairCostTextColor(int repairCost) {
        float max = 36;
        float damage = Math.min(repairCost, max);

        float f = Math.max(0, (max - damage) / max);
        return Mth.hsvToRgb(f / 5, 1, 1);
    }
}