package net.penumbra.tooltiptweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsMixin {

    @Final
    @Shadow
    Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Inject(method = "addToTooltip", at = @At("HEAD"))
    public void TooltipTweaks$addEnchantmentsHeader(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter getter, CallbackInfo info) {
        if (TooltipTweaksConfig.getInstance().updateEnchantmentTooltips && !enchantments.isEmpty()) {
            consumer.accept(Component.translatable("tooltiptweaks.ui.enchantments").withStyle(ChatFormatting.GRAY));
        }
    }

    @WrapOperation(
            method = "addToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;",
                    ordinal = 0
            )
    )
    private Component TooltipTweaks$modifyEnchantmentNameColor1(Holder<Enchantment> holder, int i, Operation<Component> original) {
        return TooltipTweaks$modifyEnchantmentNameColor(holder, i, original);
    }

    @WrapOperation(
            method = "addToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;",
                    ordinal = 1
            )
    )
    private Component TooltipTweaks$modifyEnchantmentNameColor2(Holder<Enchantment> holder, int i, Operation<Component> original) {
        return TooltipTweaks$modifyEnchantmentNameColor(holder, i, original);
    }

    @Unique
    private static Component TooltipTweaks$modifyEnchantmentNameColor(Holder<Enchantment> holder, int i, Operation<Component> original) {
        Component component = original.call(holder, i);

        if (TooltipTweaksConfig.getInstance().updateEnchantmentTooltips) {
            TextColor color = component.getStyle().getColor();
            Component appended = component;

            if (color != null && color.equals(TextColor.fromLegacyFormat(ChatFormatting.GRAY))) {
                appended = component.copy().withStyle(ChatFormatting.BLUE);
            }

            component = CommonComponents.space().append(appended);
        }

        return component;
    }
}