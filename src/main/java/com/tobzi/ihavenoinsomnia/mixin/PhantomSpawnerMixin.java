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
            method = "spawn(Lnet/minecraft/server/world/ServerWorld;ZZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;getPlayers()Ljava/util/List;"
            )
    )
    private List<ServerPlayerEntity> filterPlayerListForPhantomSpawning(ServerWorld world) {
        // Get the original, complete list of players from the world.
        List<ServerPlayerEntity> originalPlayers = world.getPlayers();

        // Create a new list containing only the players who have NOT disabled insomnia.
        // This is safe and has no side effects on the original list.
        return originalPlayers.stream()
                .filter(player -> !InsomniaManager.isInsomniaDisabled(player.getUuid()))
                .collect(Collectors.toList());
    }
}