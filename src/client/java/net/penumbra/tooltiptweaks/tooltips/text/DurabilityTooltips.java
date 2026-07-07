package net.penumbra.tooltiptweaks.tooltips.text;

import com.ibm.icu.text.DecimalFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DurabilityTooltips implements TextTooltipProvider {

    @Override
    public void register(ItemStack stack, List<Component> lines) {
        if (!stack.isDamageableItem() || !stack.isDamaged()) return;

        float max = stack.getMaxDamage();
        float damage = stack.getDamageValue();

        float durability = max - damage;
        float percent = (durability / max) * 100;

        MutableComponent message;
        switch (config.durabilityStyle) {
            case PERCENTAGE:
                message = Component.translatable("tooltiptweaks.ui.durability", getDurabilityDecimalFormat().format(percent) + "%");
                lines.add(message.setStyle(message.getStyle().withColor(getDurabilityTextColor(max, damage))));
                break;
            case FRACTION:
                message = Component.translatable("tooltiptweaks.ui.durability", (int) durability + " / " + (int) max);
                lines.add(message.setStyle(message.getStyle().withColor(getDurabilityTextColor(max, damage))));
                break;
            default:
                break;
        }

        if (config.displayUsesLeft && percent <= 25) {
            message = Component.translatable("tooltiptweaks.ui.uses_left", new DecimalFormat("#").format(durability));
            lines.add(message.setStyle(message.getStyle().withColor(getDurabilityTextColor(max, damage))));
        }
    }

    private DecimalFormat getDurabilityDecimalFormat() {
        StringBuilder string = new StringBuilder("#");

        for (int i = 0; i < config.percentageDigits; i++) {
            if (i == 0) {
                string.append(".");
            }
            string.append("#");
        }

        return new DecimalFormat(string.toString());
    }

    private int getDurabilityTextColor(float max, float damage) {
        return switch (config.durabilityTextColor) {
            case MULTICOLOR -> {
                float f = Math.max(0, (max - damage) / max);
                yield Mth.hsvToRgb(f / 3, 1, 1);
            }
            case WHITE -> TextColor.WHITE.getValue();
            case GRAY -> TextColor.GRAY.getValue();
        };
    }
}