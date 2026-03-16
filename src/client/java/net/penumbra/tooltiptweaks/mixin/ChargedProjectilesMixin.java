package net.penumbra.tooltiptweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.penumbra.tooltiptweaks.config.TooltipTweaksConfig;
import net.penumbra.tooltiptweaks.config.options.CrossbowDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChargedProjectiles.class)
public abstract class ChargedProjectilesMixin {

    @Unique
    private static final TooltipTweaksConfig CONFIG = TooltipTweaksConfig.getInstance();

    @WrapOperation(
            method = "addProjectileTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 0
            )
    )
    private static MutableComponent TooltipTweaks$recolorProjectilePrefix1(String string, Object[] objects, Operation<MutableComponent> original) {
        return TooltipTweaks$recolorProjectilePrefix(string, objects, original);
    }

    @WrapOperation(
            method = "addProjectileTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 1
            )
    )
    private static MutableComponent TooltipTweaks$recolorProjectilePrefix2(String string, Object[] objects, Operation<MutableComponent> original) {
        return TooltipTweaks$recolorProjectilePrefix(string, objects, original);
    }

    @WrapOperation(
            method = "addProjectileTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getDisplayName()Lnet/minecraft/network/chat/Component;",
                    ordinal = 0
            )
    )
    private static Component TooltipTweaks$recolorProjectileName1(ItemStack instance, Operation<Component> original) {
        return TooltipTweaks$recolorProjectileName(instance, original);
    }

    @WrapOperation(
            method = "addProjectileTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getDisplayName()Lnet/minecraft/network/chat/Component;",
                    ordinal = 1
            )
    )
    private static Component TooltipTweaks$recolorProjectileName2(ItemStack instance, Operation<Component> original) {
        return TooltipTweaks$recolorProjectileName(instance, original);
    }

    @Unique
    private static MutableComponent TooltipTweaks$recolorProjectilePrefix(String string, Object[] objects, Operation<MutableComponent> original) {
        MutableComponent component = original.call(string, objects);

        if (!CONFIG.crossbowDisplay.equals(CrossbowDisplay.DISABLED)) {
            return component.withStyle(ChatFormatting.GRAY);
        }

        return component;
    }

    @Unique
    private static Component TooltipTweaks$recolorProjectileName(ItemStack stack, Operation<Component> original) {
        Component component = original.call(stack);

        return switch (CONFIG.crossbowDisplay) {
            case WHITE_ITEM_TEXT -> component.copy().withStyle(ChatFormatting.WHITE);
            case GRAY_ITEM_TEXT -> component.copy().withStyle(ChatFormatting.GRAY);
            case DISABLED -> component;
        };
    }
}