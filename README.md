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


When you rename the packages, it's best to load them in to your IDE and then rename the package name.  This will enable the IDE to update all the references, such as what is the source members. Letting your IDE update all references will fix the package declarations in all of the source members, which will reduce a lot of manual edits and fixes.


There are 5 packages that start with `com.royalblueranger.mgpq.blues` so after renaming them there should be no errors.  At a minimum, change the **mgpq** to something to match your plugin's name.  You can even change the prefix up to the **.blues.**.  How you change is up to you, but if you keep the **.blues.** it will help identify that it's part of the core components.



##Setting up your own bstats


The code that setups up the bstats is located in the class:
```
com.royalblueranger.mgpq.bstats.InitializeBstats
```

The constant used for the bstats id is located in the plugin:
```
com.royalblueranger.mgpq.MiniGamePlayerQueue

BSTATS_ID__MINIGAMEPLAYERQUEUE = 15645;
```

Go to the bstats website.  Create an account, or login to yours.  Then add a new plugin.  You will get a new bstats ID and use that value to replace the above `BSTATS_ID__` and you can rename the suffix of that constant.



