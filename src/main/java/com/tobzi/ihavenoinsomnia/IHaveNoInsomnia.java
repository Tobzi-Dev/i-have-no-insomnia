package com.tobzi.ihavenoinsomnia;

import com.tobzi.ihavenoinsomnia.command.InsomniaCommand;
import com.tobzi.ihavenoinsomnia.util.InsomniaManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IHaveNoInsomnia implements ModInitializer {
	public static final String MOD_ID = "i-have-no-insomnia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        LOGGER.info("I ain't gonna sleep with this mod");

        // Load the saved player data from the JSON file
        InsomniaManager.load();

        // Register the /insomnia command with the server
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            InsomniaCommand.register(dispatcher);
        });
	}
}