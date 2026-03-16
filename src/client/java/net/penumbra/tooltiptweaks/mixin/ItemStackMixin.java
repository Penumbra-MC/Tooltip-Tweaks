package net.penumbra.tooltiptweaks.mixin;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.tooltips.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique private static final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();
    @Unique private final ItemStack stack = (ItemStack) (Object) this;
    @Unique private List<Component> lines;

    @Shadow public abstract boolean isEnchanted();

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"), ordinal = 0)
    private List<Component> TooltipTweaks$saveLinesLocally(List<Component> lines) {
        return this.lines = lines;
    }

    @ModifyVariable(
            method = "addAttributeTooltips",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Consumer<Component> TooltipTweaks$wrapConsumer(Consumer<Component> original) {
        List<Component> tooltipLines = this.lines;

        return component -> {
            if (component != CommonComponents.EMPTY || config.updateEnchantmentTooltips && !tooltipLines.contains(CommonComponents.EMPTY)) {
                original.accept(component);
            }
        };
    }

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void TooltipTweaks$addLines(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> info) {
        if (config.updateEnchantmentTooltips && isEnchanted()) lines.add(CommonComponents.EMPTY);

        new DurabilityTooltips().register(stack, lines);
        new RepairCostTooltip().register(stack, lines);

        new NutritionTooltips().register(stack, lines);
        new StatusEffectTooltips().register(stack, lines);

        new AxolotlVariantTooltip().register(stack, lines);
        new ClockTooltips().register(stack, lines);
        new CompassTooltips().register(stack, lines);
        new ContainerTooltips().register(stack, lines);

        new InstrumentTooltip().register(stack, lines);
    }
}
