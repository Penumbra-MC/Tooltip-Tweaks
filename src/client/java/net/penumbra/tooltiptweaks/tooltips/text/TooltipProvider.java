package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;

import java.util.List;

public interface TooltipProvider {

    TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();
    Minecraft minecraft = Minecraft.getInstance();

    void register(ItemStack stack, List<Component> lines);
}