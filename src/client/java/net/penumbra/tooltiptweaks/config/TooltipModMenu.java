package net.penumbra.tooltiptweaks.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.penumbra.tooltiptweaks.config.options.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.penumbra.tooltiptweaks.TooltipTweaks.id;

public class TooltipModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TooltipModMenu::buildMenu;
    }

    public static Screen buildMenu(Screen parent) {
        return YetAnotherConfigLib.create(TooltipTweaksConfig.HANDLER,
                (defaults, config, builder) -> {
                    return builder
                            .title(Component.translatable("tooltiptweaks.menu"))
                            .category(toolsCategory(config))
                            .category(consumablesCategory(config));
                }).generateScreen(parent);
    }

    private static ConfigCategory toolsCategory(TooltipTweaksConfig config) {

        Option<?> clockDisplayTime = create(
                "clock_time_display",
                ClockTimeDisplay.TWELVE_HOUR,
                () -> config.clockTimeDisplay,
                value -> config.clockTimeDisplay = value,
                option -> EnumControllerBuilder.create(option).enumClass(ClockTimeDisplay.class)
        );

        Option<?> displayDayNumber = create("day_number", false, () -> config.displayDayNumber, value -> config.displayDayNumber = value, TickBoxControllerBuilder::create);
        Option<?> displayYearNumber = create("year_number", false, () -> config.displayYearNumber, value -> config.displayYearNumber = value, TickBoxControllerBuilder::create);
        Option<?> displayMoonPhase = create("moon_phase", false, () -> config.displayMoonPhase, value -> config.displayMoonPhase = value, TickBoxControllerBuilder::create);

        Option<?> containerDisplayStyle = create(
                "container_display_style",
                ContainerStyle.LIST_PER_ITEM,
                () -> config.containerStyle,
                value -> config.containerStyle = value,
                option -> EnumControllerBuilder.create(option).enumClass(ContainerStyle.class)
        );

        Option<?> containerMaximumListLength = create(
                "container_entries",
                6,
                () -> config.containerEntries,
                value -> config.containerEntries = value,
                option -> IntegerSliderControllerBuilder.create(option).range(1, 27).step(1)
        );

        Option<?> displayMaps = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.display_maps"))
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_maps.desc")).webpImage(id("textures/gui/previews/display_maps.webp")).build())
                .binding(true, () -> config.displayMaps, value -> config.displayMaps = value)
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> durabilityDisplayStyle = Option.<DurabilityStyle>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.durability_display_style"))
                .binding(DurabilityStyle.PERCENTAGE, () -> config.durabilityStyle, value -> config.durabilityStyle = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.durability_display_style.desc")).webpImage(id("textures/gui/previews/durability_display_style.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(DurabilityStyle.class))
                .build();

        Option<?> durabilityDigitCount = Option.<Integer>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.percentage_digits"))
                .binding(0, () -> config.percentageDigits, value -> config.percentageDigits = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.percentage_digits.desc")).webpImage(id("textures/gui/previews/percentage_digits.webp")).build())
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 4).step(1))
                .build();

        Option<?> durabilityDisplayUsesLeft = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.display_uses_left"))
                .binding(false, () -> config.displayUsesLeft, value -> config.displayUsesLeft = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_uses_left.desc")).webpImage(id("textures/gui/previews/display_uses_left.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> displayCompassCoordinates = create("display_compass_coordinates", false, () -> config.displayCompassCoordinates, value -> config.displayCompassCoordinates = value, TickBoxControllerBuilder::create);
        Option<?> displayCompassDistance = create("display_compass_distance", true, () -> config.displayCompassDistance, value -> config.displayCompassDistance = value, TickBoxControllerBuilder::create);
        Option<?> displayCompassDimension = create("display_compass_dimension", false, () -> config.displayCompassDimension, value -> config.displayCompassDimension = value, TickBoxControllerBuilder::create);
        Option<?> displayCompassInfoOnShift = create("display_compass_info_on_shift", false, () -> config.displayCompassInfoOnShift, value -> config.displayCompassInfoOnShift = value, TickBoxControllerBuilder::create);

        Option<?> displayAxolotlVariants = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.display_axolotl_variants"))
                .binding(true, () -> config.displayAxolotlVariants, value -> config.displayAxolotlVariants = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_axolotl_variants.desc")).webpImage(id("textures/gui/previews/display_axolotl_variants.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> displayInstrumentType = Option.<InstrumentDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.display_instrument"))
                .binding(InstrumentDisplay.WHILE_CARRYING_NOTE_BLOCKS, () -> config.instrumentDisplay, value -> config.instrumentDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_instrument.desc")).webpImage(id("textures/gui/previews/display_instrument.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(InstrumentDisplay.class))
                .build();

        Option<?> displayPaintings = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.display_paintings"))
                .binding(true, () -> config.displayPaintings, value -> config.displayPaintings = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_paintings.desc")).webpImage(id("textures/gui/previews/display_paintings.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> displayRepairCost = Option.<RepairCostDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.display_repair_cost"))
                .binding(RepairCostDisplay.ON_RELEVANT_MENUS, () -> config.repairCostDisplay, value -> config.repairCostDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_repair_cost.desc")).webpImage(id("textures/gui/previews/display_repair_cost.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(RepairCostDisplay.class))
                .build();

        Option<?> updateCrossbowTooltips = Option.<CrossbowDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.update_crossbow_tooltips"))
                .binding(CrossbowDisplay.WHITE_ITEM_TEXT, () -> config.updateCrossbowTooltips, value -> config.updateCrossbowTooltips = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.update_crossbow_tooltips.desc")).webpImage(id("textures/gui/previews/update_crossbow_tooltips.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(CrossbowDisplay.class))
                .build();

        Option<?> updateEnchantmentTooltips = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.update_enchantment_tooltips"))
                .binding(true, () -> config.updateEnchantmentTooltips, value -> config.updateEnchantmentTooltips = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.update_enchantment_tooltips.desc")).webpImage(id("textures/gui/previews/update_enchantment_tooltips.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> updateTippedArrowTooltips = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.update_tipped_arrow_tooltips"))
                .binding(true, () -> config.updateTippedArrowTooltips, value -> config.updateTippedArrowTooltips = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.update_tipped_arrow_tooltips.desc")).webpImage(id("textures/gui/previews/update_tipped_arrow_tooltips.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.translatable("tooltiptweaks.category.general"))

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.tools"))

                        .option(durabilityDisplayStyle)
                        .option(durabilityDigitCount)
                        .option(durabilityDisplayUsesLeft)
                        .option(displayRepairCost)
                        .option(updateCrossbowTooltips)
                        .option(updateEnchantmentTooltips)
                        .option(updateTippedArrowTooltips)

                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.informational_tools"))

                        .option(clockDisplayTime)
                        .option(displayDayNumber)
                        .option(displayYearNumber)
                        .option(displayMoonPhase)
                        .option(displayCompassCoordinates)
                        .option(displayCompassDistance)
                        .option(displayCompassDimension)
                        .option(displayCompassInfoOnShift)
                        .option(containerDisplayStyle)
                        .option(containerMaximumListLength)
                        .option(displayMaps)

                        .build())

                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.other_items"))

                        .option(displayAxolotlVariants)
                        .option(displayInstrumentType)
                        .option(displayPaintings)

                        .build())
                .build();
    }

    private static ConfigCategory consumablesCategory(TooltipTweaksConfig config) {

        Option<?> nourishmentStyle = Option.<NourishmentStyle>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.nourishment_style"))
                .binding(NourishmentStyle.TEXT, () -> config.nourishmentStyle, value -> config.nourishmentStyle = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.nourishment_style.desc")).webpImage(id("textures/gui/previews/nourishment_style.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(NourishmentStyle.class))
                .build();

        Option<?> nourishmentIconLocation = Option.<IconLocation>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.nourishment_icon_location"))
                .binding(IconLocation.BELOW, () -> config.nourishmentIconLocation, value -> config.nourishmentIconLocation = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.nourishment_icon_location.desc")).webpImage(id("textures/gui/previews/nourishment_icon_location.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(IconLocation.class))
                .build();

        Option<?> nourishmentDisplay = Option.<NourishmentDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.nourishment_display"))
                .binding(NourishmentDisplay.NUTRITION_ONLY, () -> config.nourishmentDisplay, value -> config.nourishmentDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.nourishment_display.desc")).webpImage(id("textures/gui/previews/nourishment_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(NourishmentDisplay.class))
                .build();

        Option<?> foodEffectDisplay = Option.<EffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.food_effect_display"))
                .binding(EffectDisplay.POSITIVE_EFFECTS_ONLY, () -> config.foodEffectDisplay, value -> config.foodEffectDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.food_effect_display.desc")).webpImage(id("textures/gui/previews/food_effect_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(EffectDisplay.class))
                .build();

        Option<?> stewEffectDisplay = Option.<EffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.stew_effect_display"))
                .binding(EffectDisplay.CREATIVE_ONLY, () -> config.stewEffectDisplay, value -> config.stewEffectDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.stew_effect_display.desc")).webpImage(id("textures/gui/previews/stew_effect_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(EffectDisplay.class))
                .build();

        Option<?> modifierDisplay = Option.<OtherEffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.modifier_display"))
                .binding(OtherEffectDisplay.ENABLED, () -> config.modifierDisplay, value -> config.modifierDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.modifier_display.desc")).webpImage(id("textures/gui/previews/modifier_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(OtherEffectDisplay.class))
                .build();

        Option<?> otherEffectDisplay = Option.<OtherEffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.other_effect_display"))
                .binding(OtherEffectDisplay.ENABLED, () -> config.otherEffectDisplay, value -> config.otherEffectDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.other_effect_display.desc")).webpImage(id("textures/gui/previews/other_effect_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(OtherEffectDisplay.class))
                .build();

        Option<?> updatePotionTooltips = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.update_potion_tooltips"))
                .binding(true, () -> config.updatePotionTooltips, value -> config.updatePotionTooltips = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.update_potion_tooltips.desc")).webpImage(id("textures/gui/previews/update_potion_tooltips.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.translatable("tooltiptweaks.category.consumables"))
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.food"))

                        .option(nourishmentStyle)
                        .option(nourishmentIconLocation)
                        .option(nourishmentDisplay)
                        .option(foodEffectDisplay)
                        .option(stewEffectDisplay)
                        .option(modifierDisplay)
                        .option(otherEffectDisplay)

                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.other_consumables"))

                        .option(updatePotionTooltips)

                        .build())
                .build();
    }

    private static <T> Option<T> create(String name, T defaultValue, Supplier<T> getter, Consumer<T> setter, Function<Option<T>, ControllerBuilder<T>> controller) {
        return Option.<T>createBuilder()
                .name(Component.translatable("tooltiptweaks.option." + name))
                .binding(defaultValue, getter, setter)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option." + name + ".desc")).webpImage(id("textures/gui/previews/" + name + ".webp")).build())
                .controller(controller)
                .build();
    }
}