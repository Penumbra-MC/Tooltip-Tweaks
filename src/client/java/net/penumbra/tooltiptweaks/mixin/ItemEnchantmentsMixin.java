package net.penumbra.tooltiptweaks.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsMixin {

    @Final
    @Shadow
    Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Inject(method = "addToTooltip", at = @At("HEAD"))
    public void addHeader(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter getter, CallbackInfo info) {
        if (TooltipTweaksConfig.getInstance().updateEnchantmentTooltips && !enchantments.isEmpty()) {
            consumer.accept(Component.translatable("tooltiptweaks.ui.enchantments").withStyle(ChatFormatting.GRAY));
        }
    }

    @ModifyArg(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"), index = 0)
    private Object addSpacingToEntries(Object object) {
        if (!TooltipTweaksConfig.getInstance().updateEnchantmentTooltips) return object;
        return Component.literal(" ").append((Component) object);
    }
}