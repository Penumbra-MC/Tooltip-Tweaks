package net.penumbra.tooltiptweaks.tooltips;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.penumbra.tooltiptweaks.tooltips.gui.ContainerTooltipGUI;
import net.penumbra.tooltiptweaks.tooltips.gui.FoodTooltipGUI;
import net.penumbra.tooltiptweaks.tooltips.gui.MapTooltipGUI;
import net.penumbra.tooltiptweaks.tooltips.gui.PaintingTooltipGUI;

import static net.penumbra.tooltiptweaks.TooltipTweaks.id;

public class ConvertibleTooltips {

    public static final MappedRegistry<ConvertibleTooltipData> CONVERTIBLE_TOOLTIP_DATA_REGISTRY = FabricRegistryBuilder.<ConvertibleTooltipData>createSimple(ResourceKey.createRegistryKey(id("convertible_tooltip"))).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final ConvertibleTooltipData CONTAINER = register("container", new ContainerTooltipGUI());
    public static final ConvertibleTooltipData FOOD = register("food", new FoodTooltipGUI());
    public static final ConvertibleTooltipData MAP = register("map", new MapTooltipGUI());
    public static final ConvertibleTooltipData PAINTING = register("painting", new PaintingTooltipGUI());

    private static ConvertibleTooltipData register(String name, ConvertibleTooltipData element) {
        return Registry.register(CONVERTIBLE_TOOLTIP_DATA_REGISTRY, id(name), element);
    }
}