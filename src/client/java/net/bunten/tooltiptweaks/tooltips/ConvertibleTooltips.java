package net.bunten.tooltiptweaks.tooltips;

import net.bunten.tooltiptweaks.tooltips.gui.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;

import static net.bunten.tooltiptweaks.TooltipTweaksMod.id;

public class ConvertibleTooltips {

    public static final MappedRegistry<ConvertibleTooltipData> CONVERTIBLE_TOOLTIP_DATA_REGISTRY = FabricRegistryBuilder.createSimple(ConvertibleTooltipData.class, id("convertible_tooltip")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final ConvertibleTooltipData CONTAINER = register("container", new ContainerTooltipGUI());
    public static final ConvertibleTooltipData FOOD = register("food", new FoodTooltipGUI());
    public static final ConvertibleTooltipData MAP = register("map", new MapTooltipGUI());
    public static final ConvertibleTooltipData PAINTING = register("painting", new PaintingTooltipGUI());

    private static ConvertibleTooltipData register(String name, ConvertibleTooltipData element) {
        return Registry.register(CONVERTIBLE_TOOLTIP_DATA_REGISTRY, id(name), element);
    }
}