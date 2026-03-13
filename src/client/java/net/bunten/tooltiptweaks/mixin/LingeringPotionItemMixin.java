package net.bunten.tooltiptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.TooltipFlag;

@Mixin(LingeringPotionItem.class)
public abstract class LingeringPotionItemMixin extends Item {

    public LingeringPotionItemMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type, CallbackInfo info) {
        info.cancel();
        super.appendHoverText(stack, context, tooltip, type);
    }
}