package net.penumbra.tooltiptweaks.tooltips.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.penumbra.tooltiptweaks.TooltipTweaks;
import net.penumbra.tooltiptweaks.config.options.IconLocation;
import net.penumbra.tooltiptweaks.config.options.NourishmentDisplay;
import net.penumbra.tooltiptweaks.config.options.NourishmentStyle;
import net.penumbra.tooltiptweaks.tooltips.AbstractTooltip;

public class FoodTooltipGUI extends AbstractTooltip {

    private ItemStack stack;
    private FoodProperties component;

    private static final Identifier FOOD_HALF_TEXTURE = Identifier.withDefaultNamespace("hud/food_half");
    private static final Identifier FOOD_FULL_TEXTURE = Identifier.withDefaultNamespace("hud/food_full");

    private static final Identifier SATURATION_HALF_TEXTURE = TooltipTweaks.id("hud/saturation_half");
    private static final Identifier SATURATION_FULL_TEXTURE = TooltipTweaks.id("hud/saturation_full");

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        this.stack = stack;
        this.component = stack.get(DataComponents.FOOD);
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        if (config.nourishmentStyle != NourishmentStyle.ICONS) return false;
        if (config.nourishmentDisplay == NourishmentDisplay.DISABLED) return false;
        if (stack.is(Items.OMINOUS_BOTTLE)) return false;
        if (stack.getItem() instanceof MobBucketItem) return false;
        return stack.getComponents().has(DataComponents.FOOD) || stack.is(Items.CAKE);
    }

    @Override
    public int getWidth(Font textRenderer) {
        int offset = config.nourishmentIconLocation == IconLocation.BELOW ? 0 : textRenderer.width(stack.getHoverName());
        int width = getNutrition() * 4;

        if (config.nourishmentDisplay == NourishmentDisplay.NUTRITION_AND_SATURATION) width = Math.max(width, getSaturation() * 4);

        return offset + width + 4;
    }

    @Override
    public int getHeight(Font textRenderer) {
        return (config.nourishmentIconLocation == IconLocation.BELOW) ? 12 : 0;
    }

    private int getNutrition() {
        return stack.is(Items.CAKE) ? 14 : component.nutrition();
    }

    private int getSaturation() {
        float value = stack.is(Items.CAKE) ? 2.8F : component.saturation();
        return (int) value;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        int xOffset = (config.nourishmentIconLocation == IconLocation.BESIDE) ? font.width(stack.getHoverName()) + 2 : 0;
        int yOffset = (config.nourishmentIconLocation == IconLocation.BESIDE) ? -12 : 0;

        int rx = x + xOffset;
        int ry = y + yOffset;

        for (int index = 0; index < 10; index++) {

            if (index * 2 + 1 < getNutrition()) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOOD_FULL_TEXTURE, rx + index * 8, ry, 9, 9);
            }

            if (index * 2 + 1 == getNutrition()) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FOOD_HALF_TEXTURE, rx + index * 8, ry, 9, 9);
            }

            if (config.nourishmentDisplay == NourishmentDisplay.NUTRITION_AND_SATURATION) {
                if (index * 2 + 1 < getSaturation()) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SATURATION_FULL_TEXTURE, rx + index * 8, ry, 9, 9);
                }

                if (index * 2 + 1 == getSaturation()) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SATURATION_HALF_TEXTURE, rx + index * 8, ry, 9, 9);
                }
            }
        }
    }
}