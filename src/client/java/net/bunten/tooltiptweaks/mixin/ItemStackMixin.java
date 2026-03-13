package net.bunten.tooltiptweaks.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
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

    @Unique
    private final TooltipTweaksConfig config = TooltipTweaksConfig.getInstance();

    @Unique
    private Consumer<Component> consumer;

    @Shadow public abstract boolean isEnchanted();

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"), ordinal = 0)
    private Consumer<Component> setConsumer(Consumer<Component> consumer) {
        return this.consumer = consumer;
    }

    @WrapWithCondition(
            method = "method_57370",
            at = @At(value = "INVOKE", target = "java/util/function/Consumer.accept (Ljava/lang/Object;)V")
    )
    private boolean shouldDisplay(Consumer<Component> instance, Object object) {
        return object != CommonComponents.EMPTY || !config.updateEnchantmentTooltips || !isEnchanted();
    }

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    ordinal = 2,
                    target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    shift = At.Shift.AFTER
            )
    )
    private void addHeaderIfMissing(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> info) {
        if (config.updateEnchantmentTooltips && isEnchanted()) consumer.accept(CommonComponents.EMPTY);
    }
}
