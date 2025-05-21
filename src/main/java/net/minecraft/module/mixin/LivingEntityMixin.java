package net.minecraft.module.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected abstract int getExperienceToDrop(ServerWorld world);

    @Redirect(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getExperienceToDrop(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)I"))
    private int method(LivingEntity instance, ServerWorld world, Entity attacker) {
        return EnchantmentHelper.getMobExperience(world, attacker, (LivingEntity)(Object) this, getExperienceToDrop(world)) * 30;
    }

}
