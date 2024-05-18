package com.darksky.minegit;

import org.bukkit.ChatColor;

import java.util.HashMap;

public class LanguageResource {

    public final HashMap<String, HashMap<String, String>> resource;

    public LanguageResource() {
        resource = new HashMap<>();
        // zh_CN
        HashMap<String, String> zh_CN = new HashMap<>();
        zh_CN.put("command.empty", ChatColor.RED + "使用方法: /git <仓库实例名> <info/init/pull>");
        zh_CN.put("command.no_repo.1", ChatColor.RED + "指定的仓库实例'");
        zh_CN.put("command.no_repo.2", "'不存在！");
        zh_CN.put("command.no_option", ChatColor.RED + "需要选项参数！(info/init/pull)");
        zh_CN.put("command.forbid", ChatColor.RED + "你没有权限执行该命令！");
        zh_CN.put("command.unknown_option", ChatColor.RED + "未知的选项参数：");
        zh_CN.put("command.init.already", ChatColor.RED + "目标已被初始化！");
        zh_CN.put("command.init.no_dir", ChatColor.RED + "目标路径不存在！");
        zh_CN.put("command.init.working", ChatColor.RED + "目标仓库实例正执行任务！");
        zh_CN.put("init.jgit_error", ChatColor.RED + "内部错误：");
        zh_CN.put("command.init.run", ChatColor.YELLOW + "在仓库实例上执行初始化任务：");
        zh_CN.put("init.success", ChatColor.GREEN + "成功从指定源克隆仓库：");
        zh_CN.put("info.1", "仓库实例名：");
        zh_CN.put("info.2", "远程仓库地址：");
        zh_CN.put("info.3", "本地路径：");
        zh_CN.put("info.4", "远程仓库用户名：");
        zh_CN.put("info.5", "是否已初始化：");
        zh_CN.put("command.pull.run", ChatColor.YELLOW + "在仓库实例上执行拉取任务：");
        zh_CN.put("command.pull.no_dir", ChatColor.RED + "仓库路径不存在，重设为未初始化。");
        zh_CN.put("pull.io_error", ChatColor.RED + "IOException: ");
        zh_CN.put("pull.not_repo", ChatColor.RED + "目标路径不是仓库，重设为未初始化。");
        zh_CN.put("pull.success", ChatColor.GREEN + "成功从远端仓库拉取：");
        zh_CN.put("command.pull.not_init", ChatColor.RED + "需要先初始化该仓库实例！");
        zh_CN.put("pull.no_conflict", ChatColor.GRAY + "无冲突。");
        zh_CN.put("pull.conflicts", ChatColor.YELLOW + "存在冲突：");
        resource.put("zh_CN", zh_CN);

        // en_US
        HashMap<String, String> en_US = new HashMap<>();
        en_US.put("command.empty", ChatColor.RED + "Usage: /git <repo> <info/init/pull>");
        en_US.put("command.no_repo.1", ChatColor.RED + "Repo instance '");
        en_US.put("command.no_repo.2", "' doesn't exist!");
        en_US.put("command.no_option", ChatColor.RED + "Need option argument! (info/init/pull)");
        en_US.put("command.forbid", ChatColor.RED + "You are not allowed to use this command!");
        en_US.put("command.unknown_option", ChatColor.RED + "Unknown option argument: ");
        en_US.put("command.init.already", ChatColor.RED + "Target has been initialized!");
        en_US.put("command.init.no_dir", ChatColor.RED + "Target path doesn't exist!");
        en_US.put("command.init.working", ChatColor.RED + "Target is performing tasks!");
        en_US.put("init.jgit_error", ChatColor.RED + "Internal error: ");
        en_US.put("command.init.run", ChatColor.YELLOW + "Start init task on repo: ");
        en_US.put("init.success", ChatColor.GREEN + "Successfully cloned from: ");
        en_US.put("info.1", "Repo instance name: ");
        en_US.put("info.2", "Remote repo url: ");
        en_US.put("info.3", "Local path: ");
        en_US.put("info.4", "Remote username: ");
        en_US.put("info.5", "Has been initialized: ");
        en_US.put("command.pull.run", ChatColor.YELLOW + "Start pull task on repo: ");
        en_US.put("command.pull.no_dir", ChatColor.RED +
                "Repo path doesn't exist! Set repo instance back to uninitialized.");
        en_US.put("pull.io_error", ChatColor.RED + "IOException: ");
        en_US.put("pull.not_repo", ChatColor.RED + "Target path is not a repo! Set repo instance back to uninitialized.");
        en_US.put("pull.success", ChatColor.GREEN + "Successfully pulled from remote: ");
        en_US.put("command.pull.not_init", ChatColor.RED + "You need to init this repo instance first!");
        en_US.put("pull.no_conflict", ChatColor.GRAY + "No conflicts.");
        en_US.put("pull.conflicts", ChatColor.YELLOW + "Found conflicts:");
        resource.put("en_US", en_US);
    }

    public HashMap<String, String> get(String languageSign) {
        if (!resource.containsKey(languageSign)) {
            languageSign = "en_US";
        }
        return resource.get(languageSign);
    }
}
