package net.bunten.tooltiptweaks.mixin;

import net.bunten.tooltiptweaks.config.TooltipTweaksConfig;
import net.bunten.tooltiptweaks.config.options.ContainerStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends Block {
    public ShulkerBoxBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void appendToolTip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options, CallbackInfo info) {
        if (TooltipTweaksConfig.getInstance().containerStyle != ContainerStyle.VANILLA) {
            info.cancel();
            super.appendHoverText(stack, context, tooltip, options);
        }
    }
}