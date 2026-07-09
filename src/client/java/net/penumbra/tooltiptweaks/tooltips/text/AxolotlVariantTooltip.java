package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AxolotlVariantTooltip implements TextTooltipProvider {

    @Override
    public void register(ItemStack stack, List<Component> lines) {
        if (!config.displayAxolotlVariants || !stack.has(DataComponents.AXOLOTL_VARIANT)) return;

        Axolotl.Variant variant = stack.getOrDefault(DataComponents.AXOLOTL_VARIANT, Axolotl.Variant.DEFAULT);
        MutableComponent value = Component.translatable("tooltiptweaks.ui.axolotl." + variant.getName());
        MutableComponent formatted = Component.translatable("tooltiptweaks.ui.axolotl.value_formatted", value);

        lines.add(formatted.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
    }
}