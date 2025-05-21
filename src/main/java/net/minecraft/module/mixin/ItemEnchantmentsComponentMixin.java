package net.minecraft.module.mixin;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Pseudo
@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 255))
    private int method_1(int value) {
        return 2147483647;
    }

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 255))
    private static int method_2(int value) {
        return 2147483647;
    }
}