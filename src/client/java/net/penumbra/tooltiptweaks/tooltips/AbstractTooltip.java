package net.penumbra.tooltiptweaks.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;

public abstract class AbstractTooltip implements ConvertibleTooltipData, ClientTooltipComponent {

    protected static final Minecraft minecraft = Minecraft.getInstance();
    protected static final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    @Override
    public abstract int getHeight(Font textRenderer);

    @Override
    public abstract int getWidth(Font textRenderer);

    @Override
    public ClientTooltipComponent getComponent() {
        return this;
    }
}