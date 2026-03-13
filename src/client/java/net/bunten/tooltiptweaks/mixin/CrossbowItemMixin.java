package net.bunten.tooltiptweaks.mixin;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.CrossbowDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem {

    public CrossbowItemMixin(Properties settings) {
        super(settings);
    }

    // I'm not sure if this causes any issues, but otherwise it just seems they left it out unintentionally
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void reimplementSuper(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type, CallbackInfo ci) {
        super.appendHoverText(stack, context, tooltip, type);
    }

    @ModifyArg(method = "appendHoverText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private Object recolorProjectileText(Object object) {
        return TooltipTweaksConfig.getInstance().updateCrossbowTooltips != CrossbowDisplay.DISABLED ? ((Component) object).copy().withStyle(ChatFormatting.GRAY) : object;
    }

    @ModifyArg(method = "appendHoverText", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"))
    private Component recolorItemName(Component text) {
        return TooltipTweaksConfig.getInstance().updateCrossbowTooltips == CrossbowDisplay.GRAY_ITEM_TEXT ? text.copy().withStyle(ChatFormatting.GRAY) : text;
    }
}