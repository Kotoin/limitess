package net.minecraft.module.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;


@Pseudo
@Mixin(EffectCommand.class)
public class EffectCommandMixin {
    @Shadow
    private static native int executeGive(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry<StatusEffect> statusEffect, @Nullable Integer seconds, int amplifier, boolean showParticles) throws CommandSyntaxException;

    @Shadow
    private static native int executeClear(ServerCommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException;

    @Shadow
    private static native int executeClear(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry<StatusEffect> statusEffect) throws CommandSyntaxException;

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void method(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CallbackInfo ci) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("effect").requires((source) -> source.hasPermissionLevel(2))).then(((LiteralArgumentBuilder)CommandManager.literal("clear").executes((context) -> executeClear((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(((RequiredArgumentBuilder)CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> executeClear((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets")))).then(CommandManager.argument("effect", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.STATUS_EFFECT)).executes((context) -> executeClear((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"))))))).then(CommandManager.literal("give").then(CommandManager.argument("targets", EntityArgumentType.entities()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)CommandManager.argument("effect", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.STATUS_EFFECT)).executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), (Integer)null, 0, true))).then(((RequiredArgumentBuilder)CommandManager.argument("seconds", IntegerArgumentType.integer(1, 1000000)).executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), IntegerArgumentType.getInteger(context, "seconds"), 0, true))).then(((RequiredArgumentBuilder)CommandManager.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), IntegerArgumentType.getInteger(context, "seconds"), IntegerArgumentType.getInteger(context, "amplifier"), false))).then(CommandManager.argument("hideParticles", BoolArgumentType.bool()).executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), IntegerArgumentType.getInteger(context, "seconds"), IntegerArgumentType.getInteger(context, "amplifier"), !BoolArgumentType.getBool(context, "hideParticles"))))))).then(((LiteralArgumentBuilder)CommandManager.literal("infinite").executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), -1, 0, false))).then(((RequiredArgumentBuilder)CommandManager.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), -1, IntegerArgumentType.getInteger(context, "amplifier"), false))).then(CommandManager.argument("hideParticles", BoolArgumentType.bool()).executes((context) -> executeGive((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), RegistryEntryReferenceArgumentType.getStatusEffect(context, "effect"), -1, IntegerArgumentType.getInteger(context, "amplifier"), !BoolArgumentType.getBool(context, "hideParticles"))))))))));
        ci.cancel();
    }
}

