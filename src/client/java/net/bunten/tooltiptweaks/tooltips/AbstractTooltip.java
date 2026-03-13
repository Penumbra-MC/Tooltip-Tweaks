package net.bunten.tooltiptweaks.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public abstract class AbstractTooltip implements ConvertibleTooltipData, ClientTooltipComponent {

    @Override
    public abstract int getHeight(Font textRenderer);

    @Override
    public abstract int getWidth(Font textRenderer);

    @Override
    public ClientTooltipComponent getComponent() {
        return this;
    }
}