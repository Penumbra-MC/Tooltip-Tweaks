package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.config.options.InstrumentDisplay;

import java.util.List;
import java.util.Set;

import static net.minecraft.world.level.block.SkullBlock.Types.DRAGON;
import static net.minecraft.world.level.block.SkullBlock.Types.PLAYER;

public class InstrumentTooltip {

    private final Minecraft client = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    public void register(ItemStack stack, List<Component> lines) {
        if (config.instrumentDisplay == InstrumentDisplay.DISABLED) return;
        if (config.instrumentDisplay == InstrumentDisplay.WHILE_CARRYING_NOTE_BLOCKS) {
            LocalPlayer player = client.player;
            if (player == null) return;
            if (!player.getInventory().hasAnyOf(Set.of(Items.NOTE_BLOCK))) return;
        }

        if (stack.getItem() instanceof BlockItem blockItem) {
            NoteBlockInstrument instrument = blockItem.getBlock().defaultBlockState().instrument();
            MutableComponent value = Component.translatable("tooltiptweaks.ui.instrument." + instrument.getSerializedName());

            if (blockItem.getBlock() instanceof SkullBlock skull) {
                value = switch (skull.getType()) {
                    case DRAGON -> EntityType.ENDER_DRAGON.getDescription().copy();
                    case PLAYER -> Component.translatable("tooltiptweaks.ui.instrument.custom_head");
                    default -> {
                        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(skull.getType().getSerializedName()));
                        yield type != null ? type.getDescription().copy() : Component.literal("ERROR");
                    }
                };
            }

            lines.add(Component.translatable("tooltiptweaks.ui.instrument", value).withStyle(ChatFormatting.GRAY));
        }
    }
}