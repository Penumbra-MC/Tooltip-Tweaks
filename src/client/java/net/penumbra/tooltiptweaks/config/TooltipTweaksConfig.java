package net.penumbra.tooltiptweaks.config;


import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.penumbra.tooltiptweaks.config.options.*;

import static net.penumbra.tooltiptweaks.TooltipTweaks.id;

public class TooltipTweaksConfig {

    public static final ConfigClassHandler<TooltipTweaksConfig> HANDLER = ConfigClassHandler.createBuilder(TooltipTweaksConfig.class)
            .id(id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("tooltiptweaks.json5"))
                    .setJson5(true)
                    .build())
            .build();

    public static TooltipTweaksConfig getInstance() {
        return HANDLER.instance();
    }

    @SerialEntry public boolean displayAxolotlVariants = true;
    @SerialEntry public boolean displayDayNumber = false;
    @SerialEntry public boolean displayYearNumber = false;
    @SerialEntry public boolean displayMaps = true;
    @SerialEntry public boolean displayMoonPhase = false;
    @SerialEntry public boolean displayPaintings = true;
    @SerialEntry public boolean displayUsesLeft = false;
    @SerialEntry public boolean updateEnchantmentTooltips = true;
    @SerialEntry public boolean updatePotionTooltips = true;
    @SerialEntry public boolean updateTippedArrowTooltips = true;

    @SerialEntry public boolean displayCompassCoordinates = false;
    @SerialEntry public boolean displayCompassDistance = true;
    @SerialEntry public boolean displayCompassDimension = false;
    @SerialEntry public boolean displayCompassInfoOnShift = false;

    @SerialEntry public int containerEntries = 6;
    @SerialEntry public int percentageDigits = 0;

    @SerialEntry public ClockTimeDisplay clockTimeDisplay = ClockTimeDisplay.TWELVE_HOUR;
    @SerialEntry public ContainerStyle containerStyle = ContainerStyle.LIST_PER_ITEM;
    @SerialEntry public CrossbowDisplay crossbowDisplay = CrossbowDisplay.WHITE_ITEM_TEXT;
    @SerialEntry public DurabilityStyle durabilityStyle = DurabilityStyle.PERCENTAGE;
    @SerialEntry public DurabilityTextColor durabilityTextColor = DurabilityTextColor.MULTICOLOR;
    @SerialEntry public EffectDisplay foodEffectDisplay = EffectDisplay.POSITIVE_EFFECTS_ONLY;
    @SerialEntry public EffectDisplay stewEffectDisplay = EffectDisplay.CREATIVE_ONLY;
    @SerialEntry public IconLocation nourishmentIconLocation = IconLocation.BELOW;
    @SerialEntry public InstrumentDisplay instrumentDisplay = InstrumentDisplay.WHILE_CARRYING_NOTE_BLOCKS;
    @SerialEntry public NourishmentDisplay nourishmentDisplay = NourishmentDisplay.NUTRITION_ONLY;
    @SerialEntry public NourishmentStyle nourishmentStyle = NourishmentStyle.TEXT;
    @SerialEntry public OtherEffectDisplay modifierDisplay = OtherEffectDisplay.ENABLED;
    @SerialEntry public OtherEffectDisplay otherEffectDisplay = OtherEffectDisplay.ENABLED;
    @SerialEntry public RepairCostDisplay repairCostDisplay = RepairCostDisplay.ON_RELEVANT_MENUS;

    static {
        HANDLER.load();
    }
}