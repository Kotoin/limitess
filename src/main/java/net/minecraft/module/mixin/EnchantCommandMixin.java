package net.minecraft.module.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.command.EnchantCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.*;


@Pseudo
@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {
    @Redirect (method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
    private static int method_1(Enchantment enchantment) {
        return 2147483647;
    }
}