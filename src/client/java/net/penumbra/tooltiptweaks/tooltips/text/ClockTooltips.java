package net.penumbra.tooltiptweaks.tooltips.text;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.phys.Vec3;
import net.penumbra.tooltiptweaks.config.options.ClockTimeDisplay;

import java.util.ArrayList;
import java.util.List;

public class ClockTooltips implements TextTooltipProvider {

    private static final Component UNKNOWN_TEXT = Component.translatable("tooltiptweaks.ui.unknown");

    @Override
    public void register(ItemStack stack, List<Component> lines) {
        ClientLevel level = minecraft.level;
        Entity cameraEntity = minecraft.getCameraEntity();

        if (!stack.is(Items.CLOCK) || level == null || cameraEntity == null) return;

        Component dayText = getDayText(level);
        Component yearText = getYearText(level);
        Component timeText = getClockText(level);
        Component phaseText = getMoonPhaseText(level, cameraEntity.position());

        List<Component> values = new ArrayList<>();

        if (config.displayDayNumber) values.add(dayText);
        if (config.displayYearNumber) values.add(yearText);
        if (!config.clockTimeDisplay.equals(ClockTimeDisplay.DISABLED)) values.add(timeText);

        MutableComponent mutable = Component.empty();

        for (int i = 0; i < values.size(); i++) {
            if (i > 0) mutable.append(", ");
            mutable.append(values.get(i));
        }

        if (!values.isEmpty()) {
            lines.add(mutable.withStyle(ChatFormatting.GRAY));
        }

        if (config.displayMoonPhase) lines.add(phaseText.copy().withStyle(ChatFormatting.GRAY));
    }

    private Component getClockText(ClientLevel level) {
        Component value = UNKNOWN_TEXT;

        if (natural(level)) {
            boolean twelveHour = config.clockTimeDisplay == ClockTimeDisplay.TWELVE_HOUR;
            long time = level.getOverworldClockTime();

            int hour = (int) ((time / 1000L + 6L) % 24L);
            int minute = (int) (60L * (time % 1000L) / 1000L);

            int displayedHour = twelveHour ? (hour % 12 == 0 ? 12 : hour % 12) : hour;

            String hourDisplay = twelveHour ? String.format("%d", displayedHour) : String.format("%02d", displayedHour);
            String minuteDisplay = String.format("%02d", minute);

            Component suffix = twelveHour ? Component.translatable("tooltiptweaks.ui.clock." + (hour >= 12 ? "pm" : "am")) : CommonComponents.EMPTY;
            value = Component.translatable("tooltiptweaks.ui.clock.time.value", hourDisplay, minuteDisplay, suffix);
        }

        return Component.translatable("tooltiptweaks.ui.clock.time", value);
    }

    private long dayCount(ClientLevel level) {
        return level.getOverworldClockTime() / 24000L;
    }

    private Component getDayText(ClientLevel level) {
        long dayCount = dayCount(level);
        long finalDayCount = config.displayYearNumber ? (dayCount % 365) + 1 : dayCount;

        Component value = !natural(level) ? UNKNOWN_TEXT : Component.literal(String.valueOf(finalDayCount));
        return Component.translatable("tooltiptweaks.ui.clock.day_number", value);
    }

    private Component getYearText(ClientLevel level) {
        long dayCount = dayCount(level);
        Component value = !natural(level) ? UNKNOWN_TEXT : Component.literal(String.valueOf((dayCount / 365) + 1));
        return Component.translatable("tooltiptweaks.ui.clock.year_number", value);
    }

    private Component getMoonPhaseText(ClientLevel level, Vec3 position) {
        Component value;

        if (natural(level)) {
            MoonPhase phase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, position);
            value = Component.translatable("tooltiptweaks.ui.clock.moon_phase." + phase.getSerializedName());
        } else {
            value = UNKNOWN_TEXT;
        }

        return Component.translatable("tooltiptweaks.ui.clock.moon_phase", value);
    }

    private boolean natural(ClientLevel level) {
        return level != null && level.dimension().equals(ClientLevel.OVERWORLD);
    }
}