# MiniGamePlayerQueue
RBR's MiniGame Player Queue Plugin for Spigot Minecraft Servers.


This project is a shell and has not real functional behavior other than supporting some of the basic core components.


## How to use this project to create a new Plugin


Using GIT, clone the repo using this URL:

https://github.com/rbluer/MiniGamePlayerQueue.git


## Rename the packages

You must rename the packages to fit your new plugin.  


The package names must be unique, otherwise your plugin could experience difficult failures if ran on a server that has the same package and class names.  This is more so of a problem with other plugins that used this project as a starter.


The packages that are named `com.royalblueranger.<project>.blues.*`, with the `.blues.`, component in the pagckage name, are the core components of the plugin and should not need much modification other than renaming.


For the command handler, you need to customize the following, since it contains the plugins' command root and the fallback prefix:
`com.royalblueranger.mgpq.blue.commands.DefaultSettings`





