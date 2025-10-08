package com.tobzi.ihavenoinsomnia.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tobzi.ihavenoinsomnia.util.InsomniaManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class InsomniaCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("insomnia")
                .requires(source -> source.getPlayer() != null)
                .then(literal("disable")
                        .executes(InsomniaCommand::disableInsomnia)
                )
                .then(literal("enable")
                        .executes(InsomniaCommand::enableInsomnia)
                )
        );
    }

    private static int disableInsomnia(CommandContext<ServerCommandSource> context) {

        ServerPlayerEntity player = context.getSource().getPlayer();

        assert player != null;
        InsomniaManager.addPlayer(player.getUuid());
        InsomniaManager.save();
        context.getSource().sendFeedback(() -> Text.literal("Phantom spawns have been disabled for you."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int enableInsomnia(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        assert player != null;
        InsomniaManager.removePlayer(player.getUuid());
        InsomniaManager.save();
        context.getSource().sendFeedback(() -> Text.literal("Phantom spawns have been enabled for you."), false);
        return Command.SINGLE_SUCCESS;
    }
}