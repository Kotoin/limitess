package com.cobin.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Pseudo
@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @ModifyArg(method = "<init>(Lnet/minecraft/world/World;DDDI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;setValue(I)V", ordinal = 0), index = 0)
    private int method(int amount) {
        return amount * 50;
    }
}
