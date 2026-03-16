package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.config.options.CompassDisplay;

import java.util.List;
import java.util.Optional;

public class CompassTooltips {

    private final Minecraft minecraft = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private int roundedHorizontalDistance(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return (int) Mth.sqrt(x * x + z * z);
    }

    public void register(ItemStack stack, List<Component> lines) {
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        if (level == null || player == null) return;

        Optional<GlobalPos> target = Optional.empty();

        if (stack.has(DataComponents.LODESTONE_TRACKER) && !stack.getCreatorNamespace().equals("enderscape")) {
            LodestoneTracker component = stack.get(DataComponents.LODESTONE_TRACKER);
            if (component != null) target = component.target();
        } else {
            if (stack.is(Items.COMPASS)) target = Optional.of(level.getRespawnData().globalPos());
            if (stack.is(Items.RECOVERY_COMPASS)) target = minecraft.player.getLastDeathLocation();
        }

        target.ifPresent(global -> {
            if (config.compassDisplay != CompassDisplay.DISABLED) {
                BlockPos pos = global.pos();

                if (minecraft.hasShiftDown()) {
                    if (config.compassDisplay == CompassDisplay.DISTANCE) {
                        int int_distance = roundedHorizontalDistance(player.blockPosition(), pos);
                        MutableComponent value = Component.translatable("tooltiptweaks.ui.distance.value", int_distance, Component.translatable("tooltiptweaks.ui.distance.append"));
                        MutableComponent unknown = Component.translatable("tooltiptweaks.ui.unknown");

                        lines.add(Component.translatable("tooltiptweaks.ui.distance", global.dimension() == level.dimension() ? value : unknown).withStyle(ChatFormatting.DARK_GREEN));
                    } else if (config.compassDisplay == CompassDisplay.COORDINATES) {
                        MutableComponent position = Component.translatable("tooltiptweaks.ui.position.coordinates", pos.getX(), pos.getY(), pos.getZ());
                        MutableComponent unknown_position = Component.translatable("tooltiptweaks.ui.unknown");

                        lines.add(Component.translatable("tooltiptweaks.ui.position", global.dimension() == level.dimension() ? position : unknown_position).withStyle(ChatFormatting.DARK_GREEN));
                    }
                } else {
                    lines.add(Component.translatable("tooltiptweaks.ui.unshifted").withStyle(ChatFormatting.GRAY));
                }
            }
        });
    }
}
