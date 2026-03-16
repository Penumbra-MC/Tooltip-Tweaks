package net.penumbra.tooltiptweaks;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.tooltips.ConvertibleTooltips;
import net.penumbra.tooltiptweaks.tooltips.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TooltipTweaks implements ClientModInitializer {
    public static final String MOD_ID = "tooltiptweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static boolean creative() {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) return false;
        return player.getAbilities().instabuild;
    }

    public static void appendBeforeHoverText(ItemStack stack, List<Component> lines) {
        new DurabilityTooltips().register(stack, lines);
        new RepairCostTooltip().register(stack, lines);
    }

    public static void appendAfterHoverText(ItemStack stack, List<Component> lines) {
        new NutritionTooltips().register(stack, lines);
        new StatusEffectTooltips().register(stack, lines);

        new AxolotlVariantTooltip().register(stack, lines);
        new ClockTooltips().register(stack, lines);
        new CompassTooltips().register(stack, lines);
        new ContainerTooltips().register(stack, lines);
    }

    public static void appendAfterDetails(ItemStack stack, List<Component> lines) {
        new InstrumentTooltip().register(stack, lines);
    }

    @Override
    public void onInitializeClient() {
        Reflection.initialize(TooltipTweaksConfig.class, ConvertibleTooltips.class);
    }
}