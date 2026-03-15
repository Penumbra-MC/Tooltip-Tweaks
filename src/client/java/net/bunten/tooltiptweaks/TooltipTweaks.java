package net.bunten.tooltiptweaks;

import com.google.common.reflect.Reflection;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.tooltips.ConvertibleTooltips;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void onInitializeClient() {
        Reflection.initialize(TooltipTweaksConfig.class, ConvertibleTooltips.class);
    }
}