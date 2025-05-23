package com.cobin.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Pseudo
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyConstant(method = "calculateRequiredExperienceLevel", constant = @Constant(intValue = 15, ordinal = 1))
    private static int method(int constant, @Local(index = 1, argsOnly = true) int bookshelfCount) {
        return Math.clamp(bookshelfCount, 0, 2147483647);
    }
}
