package net.bunten.tooltiptweaks.tooltips.text;

import com.mojang.datafixers.util.Pair;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.EffectDisplay;
import net.bunten.tooltiptweaks.config.options.OtherEffectDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;

import java.util.ArrayList;
import java.util.List;

import static net.bunten.tooltiptweaks.TooltipTweaks.creative;
import static net.bunten.tooltiptweaks.tooltips.CommonText.*;

public class StatusEffectTooltips {

    private final Minecraft client = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private List<MobEffectInstance> statusEffects;
    private EffectDisplay style;

    private boolean configAllows(ItemStack stack) {
        if (stack.getItem() instanceof PotionItem || stack.is(Items.OMINOUS_BOTTLE)) return config.updatePotionTooltips;
        if (stack.is(Items.TIPPED_ARROW) || stack.is(Items.SPECTRAL_ARROW)) return config.updateTippedArrowTooltips;
        return true;
    }

    public void gatherEffects(ItemStack stack) {
        DataComponentType<?> component = DataComponents.CONSUMABLE;
        style = config.foodEffectDisplay;

        if (stack.is(Items.SUSPICIOUS_STEW)) {
            component = DataComponents.SUSPICIOUS_STEW_EFFECTS;
            style = config.stewEffectDisplay;
        }
        if (stack.getItem() instanceof PotionItem || stack.getItem() instanceof TippedArrowItem) {
            component = DataComponents.POTION_CONTENTS;
            style = EffectDisplay.ALL_EFFECTS;
        }
        if (stack.is(Items.OMINOUS_BOTTLE)) {
            component = DataComponents.OMINOUS_BOTTLE_AMPLIFIER;
            style = EffectDisplay.ALL_EFFECTS;
        }

        statusEffects = getAppliedStatusEffects(stack, component);
    }

    private List<MobEffectInstance> getAppliedStatusEffects(ItemStack stack, DataComponentType<?> component) {
        List<MobEffectInstance> effects = new ArrayList<>();

        if (stack.is(Items.SPECTRAL_ARROW)) {
            effects.add(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            return effects;
        }

        if (!stack.has(component)) return effects;

        if (component == DataComponents.CONSUMABLE) {
            stack.get(DataComponents.CONSUMABLE).onConsumeEffects().forEach(entry -> {
                if (entry instanceof ApplyStatusEffectsConsumeEffect effect) effects.addAll(effect.effects());
            });
        }
        if (component == DataComponents.SUSPICIOUS_STEW_EFFECTS) {
            stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS).effects().forEach(effect -> effects.add(effect.createEffectInstance()));
        }

        if (component == DataComponents.POTION_CONTENTS) {
            stack.get(DataComponents.POTION_CONTENTS).getAllEffects().forEach(effects::add);
        }

        if (component == DataComponents.OMINOUS_BOTTLE_AMPLIFIER) {
            OminousBottleAmplifier amplifier = stack.get(DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
            return List.of(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, amplifier.value(), false, false, true));
        }

        return effects;
    }

    private void addAppliedStatusEffects(ItemStack stack, List<Component> lines) {
        boolean isPotion = stack.getItem() instanceof PotionItem;
        boolean isWaterBottle = isPotion && stack.get(DataComponents.POTION_CONTENTS).potion().isPresent() && stack.get(DataComponents.POTION_CONTENTS).potion().get() == Potions.WATER;

        if ((!isPotion && statusEffects.isEmpty()) || isWaterBottle) return;

        if (isPotion && statusEffects.isEmpty()) {
            addStatusEffectHeader(lines);

            lines.add(Component.literal(" ").append(Component.translatable("effect.none").withStyle(ChatFormatting.DARK_GRAY)));
            return;
        }

        List<MobEffectInstance> filtered = statusEffects.stream().filter((instance) -> style != EffectDisplay.POSITIVE_EFFECTS_ONLY || creative() || instance.getEffect().value().getCategory() != MobEffectCategory.HARMFUL).toList();

        if (!filtered.isEmpty()) {
            addStatusEffectHeader(lines);

            filtered.forEach((instance) -> {
                MobEffectCategory category = instance.getEffect().value().getCategory();

                MutableComponent text = Component.translatable(instance.getDescriptionId());

                if (instance.getAmplifier() > 0)
                    text = Component.translatable("potion.withAmplifier", text, Component.translatable("potion.potency." + instance.getAmplifier()));

                if (instance.getDuration() > 20)
                    text = Component.translatable("potion.withDuration", text, MobEffectUtil.formatDuration(instance, stack.is(Items.LINGERING_POTION) ? 0.25F : 1.0F, client.level.tickRateManager().tickrate()));

                ChatFormatting formatting = switch (category) {
                    case BENEFICIAL -> BENEFICIAL_STATUS_EFFECT_COLOR;
                    case NEUTRAL -> NEUTRAL_STATUS_EFFECT_COLOR;
                    default -> HARMFUL_STATUS_EFFECT_COLOR;
                };
                lines.add(Component.literal(" ").append(text.withStyle(formatting)));
            });
        }
    }

    public void addRemoveEffectTooltips(ItemStack stack, List<Component> lines) {
        if (OtherEffectDisplay.canDisplay(config.modifierDisplay) && stack.has(DataComponents.CONSUMABLE)) {
            Consumable component = stack.get(DataComponents.CONSUMABLE);

            component.onConsumeEffects().forEach((effect) -> {
                if (effect instanceof RemoveStatusEffectsConsumeEffect(HolderSet<MobEffect> effects)) {
                    effects.forEach((entry) -> {
                        addStatusEffectHeader(lines);
                        lines.add(Component.literal(" ").append(Component.translatable("tooltiptweaks.ui.removes_prefix", Component.translatable(entry.value().getDescriptionId())).withStyle(NEUTRAL_STATUS_EFFECT_COLOR)));
                    });
                }

                if (effect instanceof ClearAllStatusEffectsConsumeEffect) {
                    addStatusEffectHeader(lines);
                    lines.add(Component.literal(" ").append(Component.translatable("tooltiptweaks.ui.removes_all_effects").withStyle(NEUTRAL_STATUS_EFFECT_COLOR)));
                }
            });
        }
    }

    private void addModifiers(List<Component> lines) {
        if (statusEffects.isEmpty()) return;

        List<Pair<Holder<Attribute>, AttributeModifier>> modifiers = new ArrayList<>();
        statusEffects.forEach((instance) -> instance.getEffect().value().createModifiers(instance.getAmplifier(), (attribute, modifier) -> modifiers.add(new Pair<>(attribute, modifier))));

        if (modifiers.isEmpty()) return;

        addModifiersHeader(lines);

        modifiers.forEach((pair) -> {
            AttributeModifier modifier = pair.getSecond();
            double value = (modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) ? modifier.amount() : modifier.amount() * 100.0;
            String modifierKey = modifier.amount() > 0.0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
            lines.add(Component.translatable(modifierKey + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(value)), Component.translatable(pair.getFirst().value().getDescriptionId())).withStyle(modifier.amount() > 0.0 ? POSITIVE_MODIFIER_COLOR : NEGATIVE_MODIFIER_COLOR));
        });
    }

    public void register(ItemStack stack, List<Component> lines) {
        gatherEffects(stack);

        if (EffectDisplay.canDisplay(style) && configAllows(stack)) addAppliedStatusEffects(stack, lines);
        if (OtherEffectDisplay.canDisplay(config.otherEffectDisplay)) addRemoveEffectTooltips(stack, lines);
        if (OtherEffectDisplay.canDisplay(config.modifierDisplay)) addModifiers(lines);
    }
}