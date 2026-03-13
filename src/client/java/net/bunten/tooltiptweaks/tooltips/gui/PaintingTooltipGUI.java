package net.bunten.tooltiptweaks.tooltips.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bunten.tooltiptweaks.TooltipTweaksMod;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.tooltips.AbstractTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

public class PaintingTooltipGUI extends AbstractTooltip {

    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();
    private PaintingVariant variant;

    Minecraft client = Minecraft.getInstance();

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        if (!config.displayPaintings || !stack.is(Items.PAINTING) || client.level == null) return false;
        if (!stack.has(DataComponents.ENTITY_DATA)) return false;

        CustomData nbtComponent = stack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
        if (!nbtComponent.isEmpty()) {
            ResourceLocation identifier = ResourceLocation.parse(nbtComponent.getUnsafe().getString("variant"));
            PaintingVariant variant = client.level.registryAccess().lookup(Registries.PAINTING_VARIANT).get().getValue(identifier);
            if (variant == null) return false;
            this.variant = variant;
            return true;
        }

        return true;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return (variant.width() * 16) + 6;
    }

    @Override
    public int getHeight(Font textRenderer) {
        return (variant.height() * 16) + 6;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics context) {
        RenderSystem.enableBlend();

        ResourceLocation assetId = ResourceLocation.fromNamespaceAndPath(variant.assetId().getNamespace(), "textures/painting/" + variant.assetId().getPath() + ".png");

        context.blit(RenderType::guiTextured, assetId, x, y, 0, 0, variant.width() * 16, variant.height() * 16, variant.width() * 16, variant.height() * 16);

        RenderSystem.disableBlend();
    }
}