package net.penumbra.tooltiptweaks.tooltips.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.penumbra.tooltiptweaks.tooltips.AbstractTooltip;

import static net.minecraft.core.component.DataComponents.PAINTING_VARIANT;

public class PaintingTooltipGUI extends AbstractTooltip {

    private PaintingVariant variant;

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        if (!config.displayPaintings || !stack.is(Items.PAINTING) || minecraft.level == null || !stack.has(PAINTING_VARIANT)) return false;

        Holder<PaintingVariant> holder = stack.get(PAINTING_VARIANT);

        if (holder != null && holder.isBound()) {
            this.variant = holder.value();
            return true;
        }

        return true;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return (variant.width() * 16) + 6;
    }

    @Override
    public int getHeight(Font textRenderer) {
        return (variant.height() * 16) + 6;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        Identifier assetId = Identifier.fromNamespaceAndPath(variant.assetId().getNamespace(), "textures/painting/" + variant.assetId().getPath() + ".png");
        graphics.blit(RenderPipelines.GUI_TEXTURED, assetId, x, y, 0, 0, variant.width() * 16, variant.height() * 16, variant.width() * 16, variant.height() * 16);
    }
}