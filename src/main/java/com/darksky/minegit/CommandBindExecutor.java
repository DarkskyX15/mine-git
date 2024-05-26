package com.darksky.minegit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandBindExecutor implements CommandExecutor {

    private final MineGit javaPlugin;
    private ArrayList<RepoInstance> repoInstances;
    private HashMap<String, Integer> repoMap;
    private HashMap<String, String> langMap;

    public CommandBindExecutor(MineGit plugin) {
        javaPlugin = plugin;
        langMap = plugin.getLang();
        repoInstances = plugin.getRepos();
        repoMap = new HashMap<>();
        for (int i = 0; i < repoInstances.size(); ++i) {
            repoMap.put(repoInstances.get(i).getName(), i);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
                             @NotNull String s, @NotNull String[] strings) {
        if (commandSender.isOp()) {
            // /git
            if (strings.length == 0) {
                commandSender.sendMessage(langMap.get("command.empty"));
                return true;
            } else {
                // /git <repo>
                if (!repoMap.containsKey(strings[0])) {
                    commandSender.sendMessage(langMap.get("command.no_repo.1") +
                            strings[0] + langMap.get("command.no_repo.2"));
                    return true;
                } else {
                    if (strings.length == 1) {
                        commandSender.sendMessage(langMap.get("command.no_option"));
                        return true;
                    }
                    RepoInstance target = repoInstances.get(repoMap.get(strings[0]));
                    switch (strings[1]) {
                        case "info":
                            List<String> ec = target.getExecuteConfigs();
                            ArrayList<String> content = new ArrayList<>();
                            content.add(langMap.get("info.1") + ec.get(5));
                            content.add(langMap.get("info.2") + ec.get(0));
                            content.add(langMap.get("info.3") + ec.get(1));
                            content.add(langMap.get("info.4") + ec.get(2));
                            content.add(langMap.get("info.5") + ec.get(4));
                            for (String line : content) {
                                commandSender.sendMessage(line);
                            }
                            break;
                        case "init":
                            if (!target.initRepoInstance(javaPlugin, commandSender)) {
                                commandSender.sendMessage(langMap.get(target.getFailInfo()));
                            } else {
                                commandSender.sendMessage(langMap.get("command.init.run") + target.getName());
                            }
                            break;
                        case "pull":
                            if (!target.pullRepoInstance(javaPlugin, commandSender)) {
                                commandSender.sendMessage(langMap.get(target.getFailInfo()));
                            } else {
                                commandSender.sendMessage(langMap.get("command.pull.run") + target.getName());
                            }
                            break;
                        case "reload":
                            boolean noRunning = true;
                            for (RepoInstance repo : repoInstances) {
                                if (repo.isRunTask()) {
                                    noRunning = false;
                                    break;
                                }
                            }
                            if (noRunning) {
                                commandSender.sendMessage(langMap.get("command.reload.tip.1"));
                                commandSender.sendMessage(langMap.get("command.reload.tip.2"));
                                if (!javaPlugin.updateRepos()) {
                                    commandSender.sendMessage(langMap.get("command.reload.fail"));
                                } else {
                                    repoInstances = javaPlugin.getRepos();
                                    ArrayList<String> tabRepos = new ArrayList<>();
                                    for (RepoInstance repo : repoInstances) {
                                        tabRepos.add(repo.getName());
                                    }
                                    javaPlugin.getTabCompleter().updateExistRepos(tabRepos);
                                    commandSender.sendMessage(langMap.get("command.reload.success"));
                                }
                            } else {
                                commandSender.sendMessage(langMap.get("command.reload.forbid"));
                            }
                            break;
                        default:
                            commandSender.sendMessage(langMap.get("command.unknown_option") + strings[1]);
                            break;
                    }
                }
            }
        } else {
            commandSender.sendMessage(langMap.get("command.forbid"));
            return true;
        }
        return true;
    }

}
