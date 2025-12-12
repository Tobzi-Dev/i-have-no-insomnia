package com.tobzi.ihavenoinsomnia.mixin;

import com.tobzi.ihavenoinsomnia.util.InsomniaManager;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;players()Ljava/util/List;"
            )
    )
    private List<ServerPlayer> filterPlayerListForPhantomSpawning(ServerLevel world) {
        // Get the original, complete list of players from the world.
        List<ServerPlayer> originalPlayers = world.players();

        // Create a new list containing only the players who have NOT disabled insomnia.
        // This is safe and has no side effects on the original list.
        return originalPlayers.stream()
                .filter(player -> !InsomniaManager.isInsomniaDisabled(player.getUUID()))
                .collect(Collectors.toList());
    }
}