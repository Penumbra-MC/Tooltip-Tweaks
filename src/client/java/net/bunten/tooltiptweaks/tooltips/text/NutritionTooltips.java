package net.bunten.tooltiptweaks.tooltips.text;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.NourishmentDisplay;
import net.bunten.tooltiptweaks.config.options.NourishmentStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.text.DecimalFormat;
import java.util.List;

import static net.bunten.tooltiptweaks.tooltips.CommonText.NUTRITION_COLOR;
import static net.bunten.tooltiptweaks.tooltips.CommonText.addConsumedHeader;

public class NutritionTooltips {

    private final Minecraft client = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private void addNutrition(ItemStack stack, List<Component> lines, int nutrition) {
        lines.add(Component.literal(" ").append(Component.translatable("tooltiptweaks.ui.nutrition", nutrition).withStyle(NUTRITION_COLOR)));
    }

    private void addSaturation(ItemStack stack, List<Component> lines, float saturation) {
        String formattedSaturation = new DecimalFormat("#.#").format(saturation);
        lines.add(Component.literal(" ").append(Component.translatable("tooltiptweaks.ui.saturation", formattedSaturation).withStyle(NUTRITION_COLOR)));
    }

    public void register(ItemStack stack, List<Component> lines) {
        if (stack.is(Items.OMINOUS_BOTTLE) || config.nourishmentStyle != NourishmentStyle.TEXT) return;

        if (stack.is(Items.CAKE) && config.nourishmentDisplay != NourishmentDisplay.DISABLED) {
            addConsumedHeader(lines, true);
            addNutrition(stack, lines, 14);
            if (config.nourishmentDisplay == NourishmentDisplay.NUTRITION_AND_SATURATION) addSaturation(stack, lines, 2.4F);
        }

        if (stack.has(DataComponents.FOOD)) {
            FoodProperties food = stack.get(DataComponents.FOOD);
            if (food == null) return;

            if (config.nourishmentDisplay != NourishmentDisplay.DISABLED) {
                addConsumedHeader(lines, false);
                addNutrition(stack, lines, food.nutrition());
            }

            if (config.nourishmentDisplay == NourishmentDisplay.NUTRITION_AND_SATURATION) addSaturation(stack, lines, food.saturation());
        }
    }
}