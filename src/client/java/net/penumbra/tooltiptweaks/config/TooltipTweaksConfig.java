package net.penumbra.tooltiptweaks.config;


import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.penumbra.tooltiptweaks.config.options.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.penumbra.tooltiptweaks.TooltipTweaks.id;

public class TooltipTweaksConfig {

    public static final ConfigClassHandler<TooltipTweaksConfig> HANDLER = ConfigClassHandler.createBuilder(TooltipTweaksConfig.class)
            .id(id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("tooltiptweaks.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public static TooltipTweaksConfig getInstance() {
        return HANDLER.instance();
    }

    @SerialEntry public boolean displayAxolotlVariants = true;
    @SerialEntry public boolean displayDayNumber = false;
    @SerialEntry public boolean displayMaps = true;
    @SerialEntry public boolean displayMoonPhase = false;
    @SerialEntry public boolean displayPaintings = true;
    @SerialEntry public boolean displayUsesLeft = false;
    @SerialEntry public boolean updateEnchantmentTooltips = true;
    @SerialEntry public boolean updatePotionTooltips = true;
    @SerialEntry public boolean updateTippedArrowTooltips = true;

    @SerialEntry public boolean displayCompassCoordinates = false;
    @SerialEntry public boolean displayCompassDistance = true;
    @SerialEntry public boolean displayCompassDimension = false;
    @SerialEntry public boolean displayCompassInfoOnShift = false;

    @SerialEntry public int containerEntries = 6;
    @SerialEntry public int percentageDigits = 0;

    @SerialEntry public ClockTimeDisplay clockTimeDisplay = ClockTimeDisplay.TWELVE_HOUR;
    @SerialEntry public ContainerStyle containerStyle = ContainerStyle.LIST_PER_ITEM;
    @SerialEntry public CrossbowDisplay updateCrossbowTooltips = CrossbowDisplay.WHITE_ITEM_TEXT;
    @SerialEntry public DurabilityStyle durabilityStyle = DurabilityStyle.PERCENTAGE;
    @SerialEntry public EffectDisplay foodEffectDisplay = EffectDisplay.POSITIVE_EFFECTS_ONLY;
    @SerialEntry public EffectDisplay stewEffectDisplay = EffectDisplay.CREATIVE_ONLY;
    @SerialEntry public IconLocation nourishmentIconLocation = IconLocation.BELOW;
    @SerialEntry public InstrumentDisplay instrumentDisplay = InstrumentDisplay.WHILE_CARRYING_NOTE_BLOCKS;
    @SerialEntry public NourishmentDisplay nourishmentDisplay = NourishmentDisplay.NUTRITION_ONLY;
    @SerialEntry public NourishmentStyle nourishmentStyle = NourishmentStyle.TEXT;
    @SerialEntry public OtherEffectDisplay modifierDisplay = OtherEffectDisplay.ENABLED;
    @SerialEntry public OtherEffectDisplay otherEffectDisplay = OtherEffectDisplay.ENABLED;
    @SerialEntry public RepairCostDisplay repairCostDisplay = RepairCostDisplay.ON_RELEVANT_MENUS;

    private static ConfigCategory toolsCategory(TooltipTweaksConfig config) {

        Option<?> clockDisplayTime = Option.<ClockTimeDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.clock_time_display"))
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.clock_time_display.desc")).webpImage(id("textures/gui/previews/time_display.webp")).build())
                .binding(ClockTimeDisplay.TWELVE_HOUR, () -> config.clockTimeDisplay, value -> config.clockTimeDisplay = value)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(ClockTimeDisplay.class))
                .build();

        Option<?> clockDisplayDayNumber = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.day_number"))
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.day_number.desc")).webpImage(id("textures/gui/previews/day_number.webp")).build())
                .binding(false, () -> config.displayDayNumber, value -> config.displayDayNumber = value)
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> clockDisplayMoonPhase = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.moon_phase"))
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.moon_phase.desc")).webpImage(id("textures/gui/previews/moon_phase.webp")).build())
                .binding(false, () -> config.displayMoonPhase, value -> config.displayMoonPhase = value)
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<?> containerDisplayStyle = Option.<ContainerStyle>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.container_display_style"))
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.container_display_style.desc")).webpImage(id("textures/gui/previews/container_display_style.webp")).build())
                .binding(ContainerStyle.LIST_PER_ITEM, () -> config.containerStyle, value -> config.containerStyle = value)
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(ContainerStyle.class))
                .build();

        Option<?> containerMaximumListLength = Option.<Integer>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.container_entries"))
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.container_entries.desc")).webpImage(id("textures/gui/previews/container_entries.webp")).build())
                .binding(6, () -> config.containerEntries, value -> config.containerEntries = value)
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 27).step(1))
                .build();

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

        Option<?> displayCompassCoordinates = createBool("display_compass_coordinates", false, () -> config.displayCompassCoordinates, value -> config.displayCompassCoordinates = value);
        Option<?> displayCompassDistance = createBool("display_compass_distance", true, () -> config.displayCompassDistance, value -> config.displayCompassDistance = value);
        Option<?> displayCompassDimension = createBool("display_compass_dimension", false, () -> config.displayCompassDimension, value -> config.displayCompassDimension = value);
        Option<?> displayCompassInfoOnShift = createBool("display_compass_info_on_shift", false, () -> config.displayCompassInfoOnShift, value -> config.displayCompassInfoOnShift = value);

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
                        .option(clockDisplayDayNumber)
                        .option(clockDisplayMoonPhase)
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

        Option<?> NOURISHMENT_STYLE = Option.<NourishmentStyle>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.nourishment_style"))
                .binding(NourishmentStyle.TEXT, () -> config.nourishmentStyle, value -> config.nourishmentStyle = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.nourishment_style.desc")).webpImage(id("textures/gui/previews/nourishment_style.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(NourishmentStyle.class))
                .build();

        Option<?> NOURISHMENT_ICON_LOCATION = Option.<IconLocation>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.nourishment_icon_location"))
                .binding(IconLocation.BELOW, () -> config.nourishmentIconLocation, value -> config.nourishmentIconLocation = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.nourishment_icon_location.desc")).webpImage(id("textures/gui/previews/nourishment_icon_location.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(IconLocation.class))
                .build();

        Option<?> NOURISHMENT_DISPLAY = Option.<NourishmentDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.nourishment_display"))
                .binding(NourishmentDisplay.NUTRITION_ONLY, () -> config.nourishmentDisplay, value -> config.nourishmentDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.nourishment_display.desc")).webpImage(id("textures/gui/previews/nourishment_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(NourishmentDisplay.class))
                .build();

        Option<?> FOOD_EFFECT_DISPLAY = Option.<EffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.food_effect_display"))
                .binding(EffectDisplay.POSITIVE_EFFECTS_ONLY, () -> config.foodEffectDisplay, value -> config.foodEffectDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.food_effect_display.desc")).webpImage(id("textures/gui/previews/food_effect_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(EffectDisplay.class))
                .build();

        Option<?> STEW_EFFECT_DISPLAY = Option.<EffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.stew_effect_display"))
                .binding(EffectDisplay.CREATIVE_ONLY, () -> config.stewEffectDisplay, value -> config.stewEffectDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.stew_effect_display.desc")).webpImage(id("textures/gui/previews/stew_effect_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(EffectDisplay.class))
                .build();

        Option<?> MODIFIER_DISPLAY = Option.<OtherEffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.modifier_display"))
                .binding(OtherEffectDisplay.ENABLED, () -> config.modifierDisplay, value -> config.modifierDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.modifier_display.desc")).webpImage(id("textures/gui/previews/modifier_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(OtherEffectDisplay.class))
                .build();

        Option<?> OTHER_EFFECT_DISPLAY = Option.<OtherEffectDisplay>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.other_effect_display"))
                .binding(OtherEffectDisplay.ENABLED, () -> config.otherEffectDisplay, value -> config.otherEffectDisplay = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.other_effect_display.desc")).webpImage(id("textures/gui/previews/other_effect_display.webp")).build())
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(OtherEffectDisplay.class))
                .build();

        Option<?> UPDATE_POTION_TOOLTIPS = Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option.update_potion_tooltips"))
                .binding(true, () -> config.updatePotionTooltips, value -> config.updatePotionTooltips = value)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.update_potion_tooltips.desc")).webpImage(id("textures/gui/previews/update_potion_tooltips.webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.translatable("tooltiptweaks.category.consumables"))
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.food"))

                        .option(NOURISHMENT_STYLE)
                        .option(NOURISHMENT_ICON_LOCATION)
                        .option(NOURISHMENT_DISPLAY)
                        .option(FOOD_EFFECT_DISPLAY)
                        .option(STEW_EFFECT_DISPLAY)
                        .option(MODIFIER_DISPLAY)
                        .option(OTHER_EFFECT_DISPLAY)

                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("tooltiptweaks.group.other_consumables"))

                        .option(UPDATE_POTION_TOOLTIPS)

                        .build())
                .build();
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

    static {
        HANDLER.load();
    }

    private static Option<Boolean> createBool(String name, boolean defaultValue, @NotNull Supplier<Boolean> getter, @NotNull Consumer<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(Component.translatable("tooltiptweaks.option." + name))
                .binding(defaultValue, getter, setter)
                .description(OptionDescription.createBuilder().text(Component.translatable("tooltiptweaks.option.display_axolotl_variants.desc")).webpImage(id("textures/gui/previews/" + name + ".webp")).build())
                .controller(TickBoxControllerBuilder::create)
                .build();
    }
}