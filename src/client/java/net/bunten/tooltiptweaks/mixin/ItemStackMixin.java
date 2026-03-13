package net.bunten.tooltiptweaks.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.tooltips.text.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

    @Shadow public abstract boolean isEnchanted();

    @Unique private static final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();
    @Unique private List<Component> lines;

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"), ordinal = 0)
    private List<Component> setConsumer(List<Component> lines) {
        return this.lines = lines;
    }

    @WrapWithCondition(
            method = "method_57370",
            at = @At(value = "INVOKE", target = "java/util/function/Consumer.accept (Ljava/lang/Object;)V", ordinal = 0)
    )
    private static boolean shouldDisplay(Consumer<Component> instance, Object object) {
        return object != CommonComponents.EMPTY || !config.updateEnchantmentTooltips;
    }

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void addHeaderIfMissing(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> info) {
        ItemStack stack = (ItemStack) (Object) this;
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
