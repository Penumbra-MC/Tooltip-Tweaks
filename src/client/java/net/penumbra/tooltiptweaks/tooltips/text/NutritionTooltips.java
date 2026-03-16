package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.penumbra.tooltiptweaks.config.options.NourishmentDisplay;
import net.penumbra.tooltiptweaks.config.options.NourishmentStyle;

import java.text.DecimalFormat;
import java.util.List;

import static net.penumbra.tooltiptweaks.tooltips.CommonText.NUTRITION_COLOR;
import static net.penumbra.tooltiptweaks.tooltips.CommonText.addConsumedHeader;

public class NutritionTooltips implements TooltipProvider {

    @Override
    public void register(ItemStack stack, List<Component> lines) {
        if (stack.getItem() instanceof MobBucketItem || stack.is(Items.OMINOUS_BOTTLE) || config.nourishmentStyle != NourishmentStyle.TEXT) return;

        if (stack.is(Items.CAKE) && config.nourishmentDisplay != NourishmentDisplay.DISABLED) {
            addConsumedHeader(lines, true);
            addNutrition(lines, 14);
            if (config.nourishmentDisplay == NourishmentDisplay.NUTRITION_AND_SATURATION) addSaturation(lines, 2.4F);
        }

        if (stack.has(DataComponents.FOOD)) {
            FoodProperties food = stack.get(DataComponents.FOOD);
            if (food == null) return;

            if (config.nourishmentDisplay != NourishmentDisplay.DISABLED) {
                addConsumedHeader(lines, false);
                addNutrition(lines, food.nutrition());
            }

            if (config.nourishmentDisplay == NourishmentDisplay.NUTRITION_AND_SATURATION) addSaturation(lines, food.saturation());
        }
    }

    private void addNutrition(List<Component> lines, int nutrition) {
        lines.add(Component.literal(" ").append(Component.translatable("tooltiptweaks.ui.nutrition", nutrition).withStyle(NUTRITION_COLOR)));
    }

    private void addSaturation(List<Component> lines, float saturation) {
        String formattedSaturation = new DecimalFormat("#.#").format(saturation);
        lines.add(Component.literal(" ").append(Component.translatable("tooltiptweaks.ui.saturation", formattedSaturation).withStyle(NUTRITION_COLOR)));
    }
}