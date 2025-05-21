package net.minecraft.module.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


@Pseudo
@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Redirect(method = "updateResult", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z", ordinal = 0))
    private boolean method_1(PlayerAbilities instance) {
        return false;
    }

    @Redirect (method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
    private int method_2(Enchantment instance) {
        return 2147483647;
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40, ordinal = 1))
    private int method_3(int constant) {
        return 2147483646;
    }

    @Redirect (method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AnvilScreenHandler;getNextCost(I)I", ordinal = 0))
    private int method_5(int cost) {
        return (int)((float)cost * 0.5f + 1.0f);
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 39, ordinal = 0))
    private int method_6(int constant) {
        return 2147483647;
    }
}

