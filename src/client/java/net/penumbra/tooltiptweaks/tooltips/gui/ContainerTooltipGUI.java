package net.penumbra.tooltiptweaks.tooltips.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.penumbra.tooltiptweaks.TooltipTweaks;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.config.options.ContainerStyle;
import net.penumbra.tooltiptweaks.tooltips.AbstractTooltip;

import java.util.List;

public class ContainerTooltipGUI extends AbstractTooltip {
    private ItemContainerContents component;

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        component = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        return stack.getComponents().has(DataComponents.CONTAINER) && TooltipTweaksConfig.getInstance().containerStyle == ContainerStyle.INVENTORY;
    }

    private boolean isEmpty() {
        return component.stream().filter(stack -> (!stack.isEmpty())).toList().isEmpty();
    }

    @Override
    public int getHeight(Font textRenderer) {
        if (isEmpty()) return 0;
        return 59;
    }

    @Override
    public int getWidth(Font textRenderer) {
        if (isEmpty()) return 0;
        return 164;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        if (isEmpty()) return;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TooltipTweaks.id("textures/gui/container.png"), x, y, 0, 0, 172, 64, 256, 128);

        int xOffset = 2;
        int yOffset = -16;

        List<ItemStack> list = component.stream().toList();

        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            xOffset += 18;

            if (i % 9 == 0) {
                xOffset = 2;
                yOffset += 18;
            }

            graphics.renderItem(stack, x + xOffset, y + yOffset);
            graphics.renderItemDecorations(font, stack, x + xOffset, y + yOffset);
        }
    }
}