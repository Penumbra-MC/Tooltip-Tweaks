package net.penumbra.tooltiptweaks.tooltips;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.penumbra.tooltiptweaks.tooltips.gui.ContainerGuiTooltip;
import net.penumbra.tooltiptweaks.tooltips.gui.FoodGuiTooltip;
import net.penumbra.tooltiptweaks.tooltips.gui.MapGuiTooltip;
import net.penumbra.tooltiptweaks.tooltips.gui.PaintingGuiTooltip;

import static net.penumbra.tooltiptweaks.TooltipTweaks.id;

public class ConvertibleTooltips {

    public static final MappedRegistry<ConvertibleTooltipData> CONVERTIBLE_TOOLTIP_DATA_REGISTRY = FabricRegistryBuilder.<ConvertibleTooltipData>createSimple(ResourceKey.createRegistryKey(id("convertible_tooltip"))).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final ConvertibleTooltipData CONTAINER = register("container", new ContainerGuiTooltip());
    public static final ConvertibleTooltipData FOOD = register("food", new FoodGuiTooltip());
    public static final ConvertibleTooltipData MAP = register("map", new MapGuiTooltip());
    public static final ConvertibleTooltipData PAINTING = register("painting", new PaintingGuiTooltip());

    private static ConvertibleTooltipData register(String name, ConvertibleTooltipData element) {
        return Registry.register(CONVERTIBLE_TOOLTIP_DATA_REGISTRY, id(name), element);
    }
}