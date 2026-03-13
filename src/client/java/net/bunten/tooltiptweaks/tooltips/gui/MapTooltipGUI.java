package net.bunten.tooltiptweaks.tooltips.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.tooltips.AbstractTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class MapTooltipGUI extends AbstractTooltip {

    Minecraft client = Minecraft.getInstance();

    private MapId component;
    private final MapRenderState mapRenderState = new MapRenderState();

    @Nullable
    private MapItemSavedData getMapState() {
        ClientLevel world = client.level;
        if (world != null) return world.getMapData(component);
        return null;
    }

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        component = stack.get(DataComponents.MAP_ID);
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        return TooltipTweaksConfig.getInstance().displayMaps && stack.has(DataComponents.MAP_ID);
    }

    @Override
    public int getWidth(Font textRenderer) {
        return getMapState() != null ? 66 : 0;
    }

    @Override
    public int getHeight(Font textRenderer) {
        return getMapState() != null ? 69 : 0;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics context) {
        if (getMapState() == null) return;

        context.blitSprite(RenderType::guiTextured, ResourceLocation.withDefaultNamespace("container/cartography_table/map"), x, y, 66, 66);

        PoseStack matrices = context.pose();

        matrices.pushPose();

        matrices.translate(x + 4, y + 4, 1);
        matrices.scale(0.45F, 0.45F, 1.0F);

        MapRenderer mapRenderer = client.getMapRenderer();
        mapRenderer.extractRenderState(component, getMapState(), mapRenderState);
        context.drawSpecial(vertexConsumers -> mapRenderer.render(mapRenderState, context.pose(), vertexConsumers, true, 15728880));

        matrices.popPose();
    }
}