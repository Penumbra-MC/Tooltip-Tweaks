package net.bunten.tooltiptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {

    public ShieldItemMixin(Properties settings) {
        super(settings);
    }

    // I'm not sure if this causes any issues, but otherwise it just seems they left it out unintentionally
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void reimplementSuper(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type, CallbackInfo ci) {
        super.appendHoverText(stack, context, tooltip, type);
    }
}