package com.darksky.minegit;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("RepoInstanceData")
public class RepoInstance implements ConfigurationSerializable {
    private final String localBindDir, repoUserName, repoPassword, remoteUrl, repoName;
    private String failInfo;
    private volatile boolean runTask;
    private volatile boolean initialized;

    public RepoInstance(String repoName, String localBindDir, String remoteUrl,
                        String repoUserName, String repoPassword) {
        this.localBindDir = localBindDir;
        this.repoPassword = repoPassword;
        this.repoUserName = repoUserName;
        this.remoteUrl = remoteUrl;
        this.repoName = repoName;
        initialized = false;
        runTask = false;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("local-bind-dir", localBindDir);
        resultMap.put("repo-password", repoPassword);
        resultMap.put("repo-username", repoUserName);
        resultMap.put("remote-url", remoteUrl);
        resultMap.put("instance-name", repoName);
        resultMap.put("initialized", initialized);
        return resultMap;
    }

    public static RepoInstance deserialize(Map<String, Object> dataMap) {
        RepoInstance temp = new RepoInstance(dataMap.get("instance-name").toString(),
                dataMap.get("local-bind-dir").toString(),
                dataMap.get("remote-url").toString(),
                dataMap.get("repo-username").toString(),
                dataMap.get("repo-password").toString());
        temp.initialized = Boolean.parseBoolean(dataMap.get("initialized").toString());
        return temp;
    }

    public boolean initRepoInstance(MineGit executeSource, CommandSender sender) {
        if (initialized) {
            failInfo = "command.init.already";
            return false;
        }
        File localFile = new File(localBindDir);
        if (!localFile.exists()) {
            failInfo = "command.init.no_dir";
            return false;
        }
        if (runTask) {
            failInfo = "command.init.working";
            return false;
        }
        runTask = true;
        new InitRunnable(this, executeSource, sender)
                .runTaskAsynchronously(executeSource);
        return true;
    }

    public boolean pullRepoInstance(MineGit executeSource, CommandSender sender) {
        if (runTask) {
            failInfo = "command.init.working";
            return false;
        }
        if (!initialized) {
            failInfo = "command.pull.not_init";
            return false;
        }
        File localFile = new File(localBindDir);
        if (!localFile.exists()) {
            failInfo = "command.pull.no_dir";
            initialized = false;
            return false;
        }
        runTask = true;
        new PullRunnable(this, executeSource, sender)
                .runTaskAsynchronously(executeSource);
        return true;
    }

    public String getFailInfo() {
        return failInfo;
    }
    public String getName() {
        return repoName;
    }
    public void setInitialized(boolean val) { initialized = val; }
    public void setRunTask(boolean val) { runTask = val; }
    public List<String> getExecuteConfigs() {
        List<String> info = new ArrayList<>();
        info.add(remoteUrl);
        info.add(localBindDir);
        info.add(repoUserName);
        info.add(repoPassword);
        info.add(Boolean.toString(initialized));
        info.add(repoName);
        return info;
    }
}
