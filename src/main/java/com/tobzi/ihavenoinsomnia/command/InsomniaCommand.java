package com.tobzi.ihavenoinsomnia.command;

import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tobzi.ihavenoinsomnia.util.InsomniaManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class InsomniaCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
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

    private static int disableInsomnia(CommandContext<CommandSourceStack> context) {

        ServerPlayer player = context.getSource().getPlayer();

        assert player != null;
        InsomniaManager.addPlayer(player.getUUID());
        InsomniaManager.save();
        context.getSource().sendSuccess(() -> Component.literal("Phantom spawns have been disabled for you."), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int enableInsomnia(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        assert player != null;
        InsomniaManager.removePlayer(player.getUUID());
        InsomniaManager.save();
        context.getSource().sendSuccess(() -> Component.literal("Phantom spawns have been enabled for you."), false);
        return Command.SINGLE_SUCCESS;
    }
}