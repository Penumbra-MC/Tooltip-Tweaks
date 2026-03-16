package net.penumbra.tooltiptweaks.tooltips.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.tooltips.AbstractTooltip;
import org.jetbrains.annotations.Nullable;

public class MapTooltipGUI extends AbstractTooltip {

    private final Minecraft minecraft = Minecraft.getInstance();
    private final MapRenderState mapRenderState = new MapRenderState();

    private MapId mapId;

    @Nullable
    private MapItemSavedData getMapItemSavedData() {
        ClientLevel level = minecraft.level;
        return level != null ? MapItem.getSavedData(mapId, level) : null;
    }

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        mapId = stack.get(DataComponents.MAP_ID);
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        return TooltipTweaksConfig.getInstance().displayMaps && stack.has(DataComponents.MAP_ID);
    }

    @Override
    public int getWidth(Font textRenderer) {
        return getMapItemSavedData() != null ? 66 : 0;
    }

    @Override
    public int getHeight(Font textRenderer) {
        return getMapItemSavedData() != null ? 69 : 0;
    }

    @Override
    public void renderImage(Font font, int x, int y, int width, int height, GuiGraphics graphics) {
        if (getMapItemSavedData() == null) return;

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("container/cartography_table/map"), x, y, 66, 66);
        renderMap(graphics, mapId, getMapItemSavedData(), x + 4, y + 4, 0.45F);
    }

    private void renderMap(GuiGraphics guiGraphics, MapId mapId, MapItemSavedData mapItemSavedData, int i, int j, float f) {
        if (mapId != null && mapItemSavedData != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(i, j);
            guiGraphics.pose().scale(f, f);
            this.minecraft.getMapRenderer().extractRenderState(mapId, mapItemSavedData, this.mapRenderState);
            guiGraphics.submitMapRenderState(this.mapRenderState);
            guiGraphics.pose().popMatrix();
        }
    }
}