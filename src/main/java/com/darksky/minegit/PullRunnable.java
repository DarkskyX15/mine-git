package com.darksky.minegit;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class PullRunnable extends BukkitRunnable {
    private final Logger serverLogger;
    private final HashMap<String, String> langMap;
    private final RepoInstance source;
    private final String localDir, username, password, remoteUri;
    private final CommandSender commandSender;
    private final String gitPrefix;

    public PullRunnable(RepoInstance sourceRepo, MineGit plugin, CommandSender sender) {
        langMap = plugin.getLang();
        serverLogger = plugin.getLogger();
        gitPrefix = plugin.gitPrefix;
        source = sourceRepo;
        List<String> ce = sourceRepo.getExecuteConfigs();
        localDir = ce.get(1);
        username = ce.get(2);
        password = ce.get(3);
        remoteUri = ce.get(0);
        commandSender = sender;
    }

    @Override
    public void run() {
        try (Repository repo = new FileRepositoryBuilder().setMustExist(true)
                .setGitDir(new File(localDir + gitPrefix)).readEnvironment()
                .findGitDir().build()) {
            try (Git git = new Git(repo)) {
                PullResult result = git.pull()
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                            .setTimeout(15).call();
                git.clean().setCleanDirectories(true).call();
                commandSender.sendMessage(result.getFetchResult().getMessages());
                List<String> conflicts = Objects.requireNonNullElse(
                        result.getMergeResult().getCheckoutConflicts(), new ArrayList<>());
                if (conflicts.size() > 0) {
                    commandSender.sendMessage(langMap.get("pull.conflicts"));
                    for (String conflict: conflicts) {
                        commandSender.sendMessage(conflict);
                    }
                } else {
                    commandSender.sendMessage(langMap.get("pull.no_conflict"));
                }
                commandSender.sendMessage(langMap.get("pull.success") + remoteUri);
            } catch (Exception e) {
                serverLogger.warning(e.getMessage());
                commandSender.sendMessage(langMap.get("init.jgit_error") + e.getMessage());
            }
        } catch (RepositoryNotFoundException e) {
            serverLogger.warning(e.getMessage());
            commandSender.sendMessage(langMap.get("pull.not_repo"));
            source.setInitialized(false);
        } catch (IOException e) {
            serverLogger.warning(e.getMessage());
            commandSender.sendMessage(langMap.get("pull.io_error"));
        } catch (Exception e) {
            serverLogger.warning(e.getMessage());
            commandSender.sendMessage(langMap.get("init.jgit_error") + e.getMessage());
        } finally {
            source.setRunTask(false);
        }
    }
}
