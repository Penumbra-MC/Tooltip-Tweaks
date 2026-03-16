package net.penumbra.tooltiptweaks.tooltips.text;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;

import java.util.List;
import java.util.Optional;

public class AxolotlVariantTooltip {

    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private static final MapCodec<Axolotl.Variant> AXOLOTL_VARIANT_MAP_CODEC = Axolotl.Variant.CODEC.fieldOf("Variant");

    public void register(ItemStack stack, List<Component> lines) {
        if (!config.displayAxolotlVariants) return;

        CustomData data = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        if (data.isEmpty()) return;

        Optional<Axolotl.Variant> optional = data.copyTag().read(AXOLOTL_VARIANT_MAP_CODEC);

        optional.ifPresent((variant) -> {
            ChatFormatting[] formattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};

            MutableComponent text = Component.translatable("tooltiptweaks.ui.axolotl." + variant.getName());
            MutableComponent message = text.withStyle(ChatFormatting.GRAY);

            message.withStyle(formattings);

            lines.add(message);
        });
    }
}
