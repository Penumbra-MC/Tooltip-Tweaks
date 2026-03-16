package net.penumbra.tooltiptweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.penumbra.tooltiptweaks.TooltipTweaks;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.config.options.EffectDisplay;
import org.jspecify.annotations.Nullable;
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
        return component -> {
            if (component != CommonComponents.EMPTY || !lines.contains(CommonComponents.EMPTY)) {
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
    private void TooltipTweaks$appendBeforeHoverText(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> info) {
        TooltipTweaks.appendBeforeHoverText(stack, lines);

        if (config.updateEnchantmentTooltips && isEnchanted()) lines.add(CommonComponents.EMPTY);
    }

    @WrapOperation(
            method = "addDetailsToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V"
            )
    )
    private void TooltipTweaks$appendAfterHoverText(Item instance, ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag, Operation<Void> original) {
        if (!TooltipTweaks$shouldHideHoverText(stack)) {
            original.call(instance, stack, context, display, consumer, flag);
        }

        TooltipTweaks.appendAfterHoverText(stack, lines);
    }

    @Unique
    private static boolean TooltipTweaks$shouldHideHoverText(ItemStack stack) {
        if (stack.getCreatorNamespace().equals("farmersdelight") && stack.has(DataComponents.CONSUMABLE)) {
            List<ConsumeEffect> effects = stack.get(DataComponents.CONSUMABLE).onConsumeEffects();

            if (effects.isEmpty()) {
                return false;
            } else {
                for (ConsumeEffect effect : effects) {
                    if (effect instanceof ApplyStatusEffectsConsumeEffect) {
                        return stack.has(DataComponents.FOOD) ? !config.foodEffectDisplay.equals(EffectDisplay.DISABLED) : config.updatePotionTooltips;
                    }
                }
            }
        }

        return false;
    }

    @WrapOperation(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V"
            )
    )
    private void TooltipTweaks$appendAfterDetails(ItemStack instance, Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag flag, Consumer<Component> consumer, Operation<Void> original) {
        original.call(instance, context, display, player, flag, consumer);
        TooltipTweaks.appendAfterDetails(stack, lines);
    }
}