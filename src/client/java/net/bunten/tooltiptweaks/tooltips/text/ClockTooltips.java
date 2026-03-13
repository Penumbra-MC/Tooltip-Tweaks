package net.bunten.tooltiptweaks.tooltips.text;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.ClockTimeDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ClockTooltips {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private static final MutableComponent UNKNOWN_TEXT = Component.translatable("tooltiptweaks.ui.unknown");

    public static MutableComponent getClockText(ClientLevel level) {
        MutableComponent value = UNKNOWN_TEXT;

        if (natural(level)) {
            boolean twelveHour = TooltipTweaksConfig.getInstance().clockTimeDisplay == ClockTimeDisplay.TWELVE_HOUR;
            long time = minecraft.level.getDayTime();

            int hour = (int) ((time / 1000L + 6L) % 24L);
            int minute = (int) (60L * (time % 1000L) / 1000L);

            int displayedHour = twelveHour ? (hour % 12 == 0 ? 12 : hour % 12) : hour;

            String hourDisplay = twelveHour ? String.format("%d", displayedHour) : String.format("%02d", displayedHour);
            String minuteDisplay = String.format("%02d", minute);

            MutableComponent suffix = twelveHour ? Component.translatable("tooltiptweaks.ui.clock." + (hour >= 12 ? "pm" : "am")) : Component.literal("");
            value = Component.translatable("tooltiptweaks.ui.clock.time.value", hourDisplay, minuteDisplay, suffix);
        }

        return Component.translatable("tooltiptweaks.ui.clock.time", value);
    }

    public static MutableComponent getDayText(ClientLevel level) {
        MutableComponent value = !natural(level) ? UNKNOWN_TEXT : Component.literal(String.valueOf(level.getDayTime() / 24000L));
        return Component.translatable("tooltiptweaks.ui.clock.day_number", value);
    }

    public static MutableComponent getMoonPhaseText(ClientLevel level, Vec3 position) {
        MutableComponent value;

        if (natural(level)) {
            MoonPhase phase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, position);
            value = Component.translatable("tooltiptweaks.ui.clock.moon_phase." + phase.getSerializedName());
        } else {
            value = UNKNOWN_TEXT;
        }
        return Component.translatable("tooltiptweaks.ui.clock.moon_phase", value);
    }

    public void register(ItemStack stack, List<Component> lines) {
        ClientLevel level = minecraft.level;
        Entity cameraEntity = minecraft.getCameraEntity();

        if (!stack.is(Items.CLOCK) || level == null || cameraEntity == null) return;

        MutableComponent dayText = getDayText(level);
        MutableComponent timeText = getClockText(level);
        MutableComponent phaseText = getMoonPhaseText(level, cameraEntity.position());

        if (config.displayDayNumber && config.clockTimeDisplay == ClockTimeDisplay.DISABLED) lines.add(dayText.withStyle(ChatFormatting.GRAY));

        if (config.clockTimeDisplay != ClockTimeDisplay.DISABLED) {
            MutableComponent value = config.displayDayNumber  ? Component.translatable("tooltiptweaks.ui.clock.multiple_values", dayText, timeText) : timeText;
            lines.add(value.withStyle(ChatFormatting.GRAY));
        }

        if (config.displayMoonPhase) lines.add(phaseText.withStyle(ChatFormatting.GRAY));
    }

    private static boolean natural(ClientLevel level) {
        return level != null && level.dimension().equals(ClientLevel.OVERWORLD);
    }
}