package com.slendy.dynamicwords.dynamicwords.update;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * ************************************************************************
 * Copyright Slendy (c) 2017. All Right Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Slendy. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * Thanks
 * ************************************************************************
 */
public class Update implements Runnable {

    private JavaPlugin _plugin;

    public Update(JavaPlugin plugin){
        this._plugin = plugin;
        this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this._plugin, this, 0L, 1L);
    }

    public void run(){
        for(UpdateType updateType : UpdateType.values()){
            if(updateType.Elapsed()){
                this._plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
            }
        }

    }


}


