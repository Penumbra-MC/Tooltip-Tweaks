package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import net.penumbra.tooltiptweaks.TooltipTweaks;

import java.util.List;
import java.util.Optional;

public class CompassTooltips implements TextTooltipProvider {

    @Override
    public void register(ItemStack stack, List<Component> lines) {
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        if (level == null || player == null) return;

        Optional<GlobalPos> target = Optional.empty();
        Component header = CommonComponents.EMPTY;

        if (stack.has(DataComponents.LODESTONE_TRACKER) && !stack.getCreatorNamespace().equals("enderscape")) {
            LodestoneTracker component = stack.get(DataComponents.LODESTONE_TRACKER);
            if (component != null) {
                target = component.target();
                header = tooltip("header.lodestone_location");
            }
        } else {
            if (stack.is(Items.COMPASS)) {
                target = Optional.of(level.getRespawnData().globalPos());
                header = tooltip("header.level_respawn_location");
            }

            if (stack.is(Items.RECOVERY_COMPASS)) {
                target = minecraft.player.getLastDeathLocation();
                header = tooltip("header.last_death_location");
            }
        }

        final Component finalHeader = header;

        target.ifPresent(global -> {
            ChatFormatting headerColor = ChatFormatting.GRAY;
            ChatFormatting infoColor = ChatFormatting.DARK_GRAY;
            ChatFormatting valueColor = ChatFormatting.BLUE;

            if (!config.displayCompassInfoOnShift || minecraft.hasShiftDown()) {
                BlockPos myPosition = player.blockPosition();

                BlockPos targetPos = global.pos();
                ResourceKey<Level> targetDimension = global.dimension();

                if (!lines.contains(CommonComponents.EMPTY)) {
                    lines.add(CommonComponents.EMPTY);
                }
                
                lines.add(finalHeader.copy().withStyle(headerColor));

                if (config.displayCompassCoordinates) {
                    MutableComponent position = tooltip("position.coordinates", targetPos.getX(), targetPos.getY(), targetPos.getZ()).withStyle(valueColor);
                    MutableComponent unknown = tooltip("position.unknown").withStyle(valueColor);
                    MutableComponent component = tooltip("position", isSameDimension(player, targetDimension) ? position : unknown);

                    lines.add(CommonComponents.space().append(component.withStyle(infoColor)));
                }

                if (config.displayCompassDistance) {
                    MutableComponent approximate = tooltip("distance.value", distanceBetweenPoints(myPosition, targetPos)).withStyle(valueColor);
                    MutableComponent unknown = tooltip("distance.unknown").withStyle(valueColor);
                    MutableComponent component = tooltip("distance", isSameDimension(player, targetDimension) ? approximate : unknown);

                    lines.add(CommonComponents.space().append(component.withStyle(infoColor)));
                }

                if (config.displayCompassDimension) {
                    MutableComponent dimension = Component.translatable(Util.makeDescriptionId("dimension", targetDimension.identifier())).withStyle(valueColor);;
                    MutableComponent component = tooltip("dimension", dimension);

                    lines.add(CommonComponents.space().append(component.withStyle(infoColor)));
                }

            } else {
                lines.add(tooltip("unshifted").withStyle(headerColor));
            }
        });
    }

    private boolean isSameDimension(LocalPlayer player, ResourceKey<Level> dimension) {
        return player.level().dimension().equals(dimension);
    }

    private MutableComponent tooltip(String name, Object... objects) {
        return Component.translatable("item." + TooltipTweaks.MOD_ID + ".location." + name, objects);
    }

    private int distanceBetweenPoints(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return (int) Mth.sqrt(x * x + z * z);
    }
}
