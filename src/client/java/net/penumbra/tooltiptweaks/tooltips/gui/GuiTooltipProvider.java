package net.penumbra.tooltiptweaks.tooltips.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.tooltips.ConvertibleTooltipData;

public interface GuiTooltipProvider extends ConvertibleTooltipData, ClientTooltipComponent {

    Minecraft minecraft = Minecraft.getInstance();
    TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    @Override
    int getHeight(Font font);

    @Override
    int getWidth(Font font);

    @Override
    default ClientTooltipComponent getComponent() {
        return this;
    }
}