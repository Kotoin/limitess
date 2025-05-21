package net.minecraft.module.mixin;

import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Pseudo
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    @Shadow
    private int amount;

    @Inject(method = "getExperienceAmount", at = @At("HEAD"), cancellable = true)
    private void method(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(amount * 30);
    }
}
