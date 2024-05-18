package com.darksky.minegit;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class InitRunnable extends BukkitRunnable {

    private final Logger serverLogger;
    private final RepoInstance source;
    private final CommandSender commandSender;
    private final HashMap<String, String> langMap;
    private final String remoteUrl, localDir, password, username;
    public InitRunnable(RepoInstance sourceRepo, MineGit plugin, CommandSender sender) {
        serverLogger = plugin.getLogger();
        langMap = plugin.getLang();
        source = sourceRepo;
        commandSender = sender;
        List<String> ec = sourceRepo.getExecuteConfigs();
        remoteUrl = ec.get(0);
        localDir = ec.get(1);
        username = ec.get(2);
        password = ec.get(3);
    }

    @Override
    public void run() {
        try {
            Git git = Git.cloneRepository().setDirectory(new File(localDir)).setURI(remoteUrl)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .setTimeout(15).call();
            git.getRepository().close();
        } catch (Exception e) {
            serverLogger.warning(e.getMessage());
            commandSender.sendMessage(langMap.get("init.jgit_error") +
                    Objects.requireNonNullElse(e.getMessage(), "null"));
            source.setRunTask(false);
            return;
        }
        commandSender.sendMessage(langMap.get("init.success") + remoteUrl);
        source.setInitialized(true);
        source.setRunTask(false);
    }
}
