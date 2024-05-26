package com.darksky.minegit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandBindTab implements TabCompleter {

    private List<String> existRepos;
    private final List<String> secondTab;
    public CommandBindTab(List<String> existRepos) {
        this.existRepos = existRepos;
        secondTab = new ArrayList<>();
        secondTab.add("info");
        secondTab.add("init");
        secondTab.add("pull");
        secondTab.add("reload");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                                @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return existRepos;
        } else if (strings.length == 2) {
            return secondTab;
        } else {
            return null;
        }
    }

    public void updateExistRepos(List<String> stringList) {
        existRepos = stringList;
    }
}
