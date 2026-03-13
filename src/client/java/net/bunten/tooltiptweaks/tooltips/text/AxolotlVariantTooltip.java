package net.bunten.tooltiptweaks.tooltips.text;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import java.util.List;
import java.util.Optional;

public class AxolotlVariantTooltip {

    private final Minecraft client = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    public record Variant(Axolotl.Variant variant) {
        public static final Codec<Variant> CODEC = Codec.INT.xmap(Variant::new, Variant::getId);

        public Variant(int id) {
            this(Axolotl.Variant.byId(id));
        }

        public int getId() {
            return variant.getId();
        }

        public String getName() {
            return variant().getName();
        }
    }

    private static final MapCodec<Variant> AXOLOTL_VARIANT_MAP_CODEC = Variant.CODEC.fieldOf("Variant");

    public void register(ItemStack stack, List<Component> lines) {
        if (!config.displayAxolotlVariants) return;

        CustomData nbt = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        if (nbt.isEmpty()) return;

        Optional<Variant> optional = nbt.read(AXOLOTL_VARIANT_MAP_CODEC).result();

        optional.ifPresent((variant) -> {
            ChatFormatting[] formattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};

            MutableComponent text = Component.translatable("tooltiptweaks.ui.axolotl." + variant.getName());
            MutableComponent message = text.withStyle(ChatFormatting.GRAY);

            message.withStyle(formattings);

            lines.add(message);
        });
    }
}
