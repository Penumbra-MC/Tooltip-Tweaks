package net.bunten.tooltiptweaks.tooltips.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.tooltips.AbstractTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public class SpawnEggTooltipGUI extends AbstractTooltip {
    private ItemStack stack;

    @Nullable
    private Item item;

    @Override
    public AbstractTooltip withStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    @Override
    public boolean canDisplay(ItemStack stack) {
        if (!TooltipTweaksConfig.getInstance().displaySpawnEgg) return false;

        String key = stack.is(Items.TRIAL_SPAWNER) ? "spawn_data" : "SpawnData";
        if (!stack.has(DataComponents.BLOCK_ENTITY_DATA)) return false;

        CompoundTag nbtCompound = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).getUnsafe();
        if (!nbtCompound.contains(key)) return false;

        ResourceLocation identifier = getSpawnedEntityId(nbtCompound, key);
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(identifier);
        item = SpawnEggItem.byId(type);

        return item != null && (item != Items.PIG_SPAWN_EGG || type == EntityType.PIG);
    }

    @Override
    public int getHeight(Font textRenderer) {
        return 20;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return 20;
    }

    @Nullable
    private static ResourceLocation getSpawnedEntityId(CompoundTag nbt, String spawnDataKey) {
        if (nbt.contains(spawnDataKey, Tag.TAG_COMPOUND)) {
            String string = nbt.getCompound(spawnDataKey).getCompound("entity").getString("id");
            return ResourceLocation.tryParse(string);
        } else {
            return null;
        }
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics context) {
        if (item == null) return;

        RenderSystem.enableBlend();

        context.renderItem(item.getDefaultInstance(), x, y);

        RenderSystem.disableBlend();
    }
}