<!--
 * @Date: 2024-05-18 18:17:15
 * @Author: DarkskyX15
 * @LastEditTime: 2024-05-19 20:15:13
-->
# MineGit

为Minecraft服务器提供简单的`git pull`支持

## 安装

*目前只有Paper服务端的1.20.4 Release，JDK版本17*  

将插件文件放入服务器根目录下的`plugins`文件夹中  

## 配置

若`plugins\MineGit\config.yml`文件不存在，则创建默认配置文件于此路径。

默认配置文件应如下所示：  

```yml
repos:
- ==: RepoInstanceData
  repo-password: your_password
  local-bind-dir: ./local/dir/path
  instance-name: example
  repo-username: your_username
  remote-url: remote.url.of.repo
  initialized: false
lang: en_US
git-dir: \.git
```

**选项说明：**  

- `repos` 由仓库实例数据组成的列表
  - `instance-name` 仓库实例名
    该名称与仓库名无关，只作插件内部的标识符
  - `repo-password & repo-username` 分别是密码和用户名
    访问远端仓库时将使用该设置进行身份验证
  - `remote-url` 远端仓库地址，仅支持web URL
  - `local-bind-dir` 本地仓库地址
    该地址应为一个存在的空文件夹，若非空则在仓库初始化时会遇到问题
  - `initialized` 是否已被初始化
    该选项一般由插件本身维护，在仓库出现问题时可以手动修改重置
- `lang` 语言选择，目前只有`zh_CN`和`en_US`两个选项
- `git-dir` git设置文件夹相对于仓库的路径，默认为`\.git`且一般无需修改  

## 使用  

对于每个仓库实例，提供了如下操作：  
**所有命令都只能由op执行**  

### info  

命令：`git <instance> info`   
在聊天栏现实指定的仓库实例的信息（不包括密码）  

### init  

命令：`git <instance> init`  
初始化仓库实例，相当于`git clone`  

### pull  

命令：`git <instance> pull`   
拉取远端仓库的更新，并删除不再追踪的文件和文件夹  
相当于`git pull`  

