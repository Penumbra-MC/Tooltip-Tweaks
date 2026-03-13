package net.bunten.tooltiptweaks.tooltips.text;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.ClockTimeDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClockTooltips {

    private final Minecraft client = Minecraft.getInstance();
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    private static final MutableComponent UNKNOWN_TEXT = Component.translatable("tooltiptweaks.ui.unknown");

    public static MutableComponent getClockText(ClientLevel world) {
        MutableComponent value = UNKNOWN_TEXT;

        if (world.dimensionType().natural()) {
            boolean twelveHour = TooltipTweaksConfig.getInstance().clockTimeDisplay == ClockTimeDisplay.TWELVE_HOUR;
            long time = Minecraft.getInstance().level.getDayTime();

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

    public static MutableComponent getDayText(ClientLevel world) {
        MutableComponent value = !world.dimensionType().natural() ? UNKNOWN_TEXT : Component.literal(String.valueOf(world.getDayTime() / 24000L));
        return Component.translatable("tooltiptweaks.ui.clock.day_number", value);
    }

    public static MutableComponent getMoonPhaseText(ClientLevel world) {
        MutableComponent value = !world.dimensionType().natural() ? UNKNOWN_TEXT : Component.translatable("tooltiptweaks.ui.clock.moon_phase.value_" + world.getMoonPhase());
        return Component.translatable("tooltiptweaks.ui.clock.moon_phase", value);
    }

    public void register(ItemStack stack, List<Component> lines) {
        @Nullable ClientLevel world = client.level;
        if (!stack.is(Items.CLOCK) || world == null) return;

        MutableComponent dayText = getDayText(world);
        MutableComponent timeText = getClockText(world);
        MutableComponent phaseText = getMoonPhaseText(world);

        if (config.displayDayNumber && config.clockTimeDisplay == ClockTimeDisplay.DISABLED) lines.add(dayText.withStyle(ChatFormatting.GRAY));

        if (config.clockTimeDisplay != ClockTimeDisplay.DISABLED) {
            MutableComponent value = config.displayDayNumber  ? Component.translatable("tooltiptweaks.ui.clock.multiple_values", dayText, timeText) : timeText;
            lines.add(value.withStyle(ChatFormatting.GRAY));
        }

        if (config.displayMoonPhase) lines.add(phaseText.withStyle(ChatFormatting.GRAY));
    }
}