package net.bunten.tooltiptweaks.tooltips.text;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.ContainerStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.LinkedHashMap;
import java.util.List;

public class ContainerTooltips {

    private static final Component UNKNOWN_CONTENTS_TEXT = Component.translatable("container.shulkerBox.unknownContents");

    private final NonNullList<ItemStack> INVENTORY = NonNullList.withSize(27, ItemStack.EMPTY);
    private final LinkedHashMap<Item, Integer> ITEM_COUNT_MAP = new LinkedHashMap<>();

    private final Minecraft client = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private void addPerItemTooltips(ItemStack stack, List<Component> lines) {

        // Go through the inventory and add items to a HashMap
        for (ItemStack itemStack : stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()) {
            if (!itemStack.isEmpty()) {
                Item item = itemStack.getItem();
                int count = itemStack.getCount();

                if (!ITEM_COUNT_MAP.containsKey(item)) ITEM_COUNT_MAP.put(item, count); else ITEM_COUNT_MAP.put(item, ITEM_COUNT_MAP.get(item) + count);
            }
        }

        int maxrenderedLines = config.containerEntries;
        var renderedLines = 0;
        var moreItems = 0;

        // Go through the HashMap and render lines based on the item and count data
        for (var set : ITEM_COUNT_MAP.entrySet()) {
            var name = set.getKey().getName().plainCopy();
            var count = set.getValue();

            if (renderedLines < maxrenderedLines) {
                lines.add(name.withStyle(ChatFormatting.GRAY).append(Component.translatable("tooltiptweaks.ui.container.entry", count).withStyle(ChatFormatting.WHITE)));
                renderedLines++;
            } else {
                moreItems++;
            }
        }

        if (renderedLines >= maxrenderedLines && moreItems > 0) {
            lines.add(Component.translatable("tooltiptweaks.ui.container.more", moreItems).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }

    private void addPerStackTooltips(ItemStack stack, List<Component> lines) {

        int maxrenderedLines = config.containerEntries;
        var renderedLines = 0;
        var moreItems = 0;

        for (ItemStack itemStack : stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()) {
            var name = itemStack.getHoverName().plainCopy();
            var count = itemStack.getCount();

            if (renderedLines < maxrenderedLines) {
                lines.add(name.withStyle(ChatFormatting.GRAY).append(Component.translatable("tooltiptweaks.ui.container.entry", count).withStyle(ChatFormatting.WHITE)));
                renderedLines++;
            } else {
                moreItems++;
            }
        }

        if (renderedLines >= maxrenderedLines && moreItems > 0) {
            lines.add(Component.translatable("tooltiptweaks.ui.container.more", moreItems).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }

    public void register(ItemStack stack, List<Component> lines) {
        if (stack.has(DataComponents.CONTAINER_LOOT)) {
            lines.add(UNKNOWN_CONTENTS_TEXT);
        }

        var display = config.containerStyle;
        if (stack.has(DataComponents.CONTAINER)) {
            if (display == ContainerStyle.LIST_PER_ITEM) addPerItemTooltips(stack, lines);
            if (display == ContainerStyle.LIST_PER_STACK) addPerStackTooltips(stack, lines);
        }
    }
}