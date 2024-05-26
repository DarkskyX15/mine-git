package com.darksky.minegit;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public final class MineGit extends JavaPlugin {
    private final String configPath = "./plugins/MineGit/config.yml";
    private final String pluginFolder = "./plugins/MineGit";
    private ArrayList<RepoInstance> repoInstances;
    private final Logger serverLogger;
    private final YamlConfiguration configuration;
    private HashMap<String, String> currentLanguageMap;
    private PluginCommand pluginCommand;
    private CommandBindTab tabCompleter;
    public String gitPrefix;

    public MineGit() {
        // Value init
        serverLogger = getLogger();
        configuration = new YamlConfiguration();
        repoInstances = new ArrayList<>();
        pluginCommand = null;
        tabCompleter = null;
        ConfigurationSerialization.registerClass(RepoInstance.class);

        // Config default
        ArrayList<RepoInstance> defaultRepos = new ArrayList<>();
        defaultRepos.add(new RepoInstance("example", "./local/dir/path",
                "remote.url.of.repo", "your_username", "your_password"));
        configuration.addDefault("repos", defaultRepos);
        configuration.addDefault("lang", "en_US");
        configuration.addDefault("git-dir", "\\.git");
        configuration.options().copyDefaults(true).parseComments(true);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        serverLogger.info("Written by Darksky");
        serverLogger.info("Registering commands...");
        try {
            pluginCommand = Objects.requireNonNull(this.getCommand("git"));
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        if (!new File(pluginFolder).exists()) {
            serverLogger.info("Config folder doesn't exist, create one.");
            new File(pluginFolder).mkdir();
        }

        // Load configs
        serverLogger.info("Loading configs...");
        try {
            configuration.load(configPath);
        } catch (IOException e) {
            serverLogger.info("Cannot find config file, creating default config...");
            try {
                configuration.save(configPath);
            } catch (IOException se) {
                serverLogger.warning("Can NOT create default config!");
            }
        } catch (InvalidConfigurationException e) {
            serverLogger.warning("Incorrect configuration, using default config.");
        }

        // Set prefix
        gitPrefix = configuration.getString("git-dir");

        // Change language
        currentLanguageMap = new LanguageResource().get(configuration.getString("lang"));

        // Make repo instances
        try {
            List<?> rawRepos = Objects.requireNonNull(configuration.getList("repos"));
            for (Object obj: rawRepos) {
                if (obj instanceof RepoInstance) {
                    repoInstances.add((RepoInstance) obj);
                }
            }
        } catch (NullPointerException | ClassCastException e) {
            serverLogger.warning("Can not get Repo instances from config!");
        }

        StringBuilder foundRepos = new StringBuilder("Repo instances:");
        List<String> existRepos = new ArrayList<>();
        for (RepoInstance repo: repoInstances) {
            foundRepos.append(' ').append(repo.getName());
            existRepos.add(repo.getName());
        }
        serverLogger.info(foundRepos.toString());

        tabCompleter = new CommandBindTab(existRepos);
        pluginCommand.setExecutor(new CommandBindExecutor(this));
        pluginCommand.setTabCompleter(tabCompleter);

        serverLogger.info("Finished!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        serverLogger.info("Disabling MineGit...");
        serverLogger.info("Saving config...");
        try {
            configuration.save(configPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverLogger.info("Goodbye and wish you a nice day :)");
    }

    public HashMap<String, String> getLang() {
        return currentLanguageMap;
    }
    public ArrayList<RepoInstance> getRepos() {
        return repoInstances;
    }
    public boolean updateRepos() {
        try {
            configuration.load(configPath);
        } catch (IOException | InvalidConfigurationException e) {
            serverLogger.warning("Can not reload config file!");
            return false;
        }
        try {
            ArrayList<RepoInstance> newRepos = new ArrayList<>();
            List<?> rawRepos = Objects.requireNonNull(configuration.getList("repos"));
            for (Object obj : rawRepos) {
                if (obj instanceof RepoInstance) {
                    newRepos.add((RepoInstance) obj);
                }
            }
            repoInstances = newRepos;
        } catch (NullPointerException | ClassCastException e) {
            serverLogger.warning("Can not load Repo instances from config!");
            return false;
        }
        serverLogger.info("Config reload success!");
        StringBuilder foundRepos = new StringBuilder("Found repos:");
        for (RepoInstance repo : repoInstances) {
            foundRepos.append(' ').append(repo.getName());
        }
        serverLogger.info(foundRepos.toString());
        return true;
    }
    public CommandBindTab getTabCompleter() { return tabCompleter; }
}
