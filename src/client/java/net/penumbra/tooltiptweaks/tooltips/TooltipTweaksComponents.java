package net.penumbra.tooltiptweaks.tooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;
import java.util.List;

public class TooltipTweaksComponents {

    public static final ChatFormatting NUTRITION_COLOR = ChatFormatting.GOLD;
    
    public static final ChatFormatting BENEFICIAL_STATUS_EFFECT_COLOR = ChatFormatting.BLUE;
    public static final ChatFormatting NEUTRAL_STATUS_EFFECT_COLOR = ChatFormatting.BLUE;
    public static final ChatFormatting HARMFUL_STATUS_EFFECT_COLOR = ChatFormatting.DARK_PURPLE;

    public static final ChatFormatting POSITIVE_MODIFIER_COLOR = ChatFormatting.DARK_GREEN;
    public static final ChatFormatting NEGATIVE_MODIFIER_COLOR = ChatFormatting.RED;

    public static final MutableComponent WHEN_CONSUMED_HEADER = Component.translatable("tooltiptweaks.ui.when_consumed").withStyle(ChatFormatting.GRAY);
    public static final MutableComponent WHEN_FULLY_CONSUMED_HEADER = Component.translatable("tooltiptweaks.ui.when_fully_consumed").withStyle(ChatFormatting.GRAY);
    public static final MutableComponent STATUS_EFFECTS_HEADER = Component.translatable("tooltiptweaks.ui.status_effects").withStyle(ChatFormatting.GRAY);
    public static final MutableComponent MODIFIERS_HEADER = Component.translatable("tooltiptweaks.ui.modifiers").withStyle(ChatFormatting.GRAY);

    public static final List<MutableComponent> HEADER_TEXTS = Arrays.asList(WHEN_CONSUMED_HEADER, WHEN_FULLY_CONSUMED_HEADER, STATUS_EFFECTS_HEADER, MODIFIERS_HEADER);

    public static void tryAddSpace(List<Component> lines) {
        for (MutableComponent header : HEADER_TEXTS) if (lines.contains(header)) return;
        lines.add(CommonComponents.EMPTY);
    }

    public static void addConsumedHeader(List<Component> lines, boolean whenFullyConsumed) {
        if (lines.contains(WHEN_FULLY_CONSUMED_HEADER) || lines.contains(WHEN_CONSUMED_HEADER)) return;

        tryAddSpace(lines);
        lines.add(whenFullyConsumed ? WHEN_FULLY_CONSUMED_HEADER : WHEN_CONSUMED_HEADER);
    }

    public static void addStatusEffectHeader(List<Component> lines) {
        if (lines.contains(STATUS_EFFECTS_HEADER)) return;

        tryAddSpace(lines);
        lines.add(STATUS_EFFECTS_HEADER);
    }

    public static void addModifiersHeader(List<Component> lines) {
        if (lines.contains(MODIFIERS_HEADER)) return;

        tryAddSpace(lines);
        lines.add(MODIFIERS_HEADER);
    }
}
