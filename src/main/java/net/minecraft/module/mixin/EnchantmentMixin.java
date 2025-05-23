package net.minecraft.module.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Pseudo
@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Unique
    private static String method_1(int num) {
        final int[] values = {100000, 90000, 50000, 40000, 10000, 9000, 5000, 4000, 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final String[] symbols = {"ↈ","ↂↈ", "ↇ", "ↂↇ", "ↂ", "ↀↂ", "ↁ", "ↀↁ", "M", "CM", "D", "CD", "C", "XC", "L", "XL",  "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                sb.append(symbols[i]);
            }
        }
        return sb.toString();
    }

    @Redirect (method = "getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private static MutableText method_2(MutableText instance, Text text, @Local(argsOnly = true) int level) {
        return instance.append (level <= 10 ? Text.translatable("enchantment.level." + l) : l <= 399999 ? Text.translatable (method_1 (level)) : Text.translatable (String.valueOf (level)));
    }
} 
