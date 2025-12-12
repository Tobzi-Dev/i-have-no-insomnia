package com.tobzi.ihavenoinsomnia.mixin;

import com.tobzi.ihavenoinsomnia.util.InsomniaManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @Redirect(
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;getPlayers()Ljava/util/List;"
            )
    )
    private List<ServerPlayerEntity> filterPlayerListForPhantomSpawning(ServerWorld world) {
        List<ServerPlayerEntity> originalPlayers = world.getPlayers();

        return originalPlayers.stream()
                .filter(player -> !InsomniaManager.isInsomniaDisabled(player.getUuid()))
                .collect(Collectors.toList());
    }
}