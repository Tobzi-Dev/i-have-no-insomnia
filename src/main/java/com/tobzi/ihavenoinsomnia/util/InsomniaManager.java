package com.tobzi.ihavenoinsomnia.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tobzi.ihavenoinsomnia.IHaveNoInsomnia;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class InsomniaManager {
    private static final Path CONFIG_DIR = Paths.get("config");
    private static final Path DATA_FILE = CONFIG_DIR.resolve("i-have-no-insomnia-players.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Set<UUID> playersWithInsomniaDisabled = new HashSet<>();

    public static void load() {
        if (!Files.exists(DATA_FILE)) return;
        try {
            String json = Files.readString(DATA_FILE, StandardCharsets.UTF_8);
            Type setType = new TypeToken<Set<String>>(){}.getType();
            Set<String> uuidStrings = GSON.fromJson(json, setType);
            playersWithInsomniaDisabled.clear();
            for (String s : uuidStrings) {
                playersWithInsomniaDisabled.add(UUID.fromString(s));
            }
            IHaveNoInsomnia.LOGGER.info("Loaded {} players with insomnia disabled.", playersWithInsomniaDisabled.size());
        } catch (IOException e) {
            IHaveNoInsomnia.LOGGER.error("Failed to load player data", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_DIR);
            Set<String> uuidStrings = playersWithInsomniaDisabled.stream()
                    .map(UUID::toString)
                    .collect(Collectors.toSet());
            String json = GSON.toJson(uuidStrings);
            Files.writeString(DATA_FILE, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            IHaveNoInsomnia.LOGGER.error("Failed to save player data", e);
        }
    }

    public static void addPlayer(UUID playerUuid) {
        playersWithInsomniaDisabled.add(playerUuid);
    }

    public static void removePlayer(UUID playerUuid) {
        playersWithInsomniaDisabled.remove(playerUuid);
    }

    public static boolean isInsomniaDisabled(UUID playerUuid) {
        return playersWithInsomniaDisabled.contains(playerUuid);
    }
}