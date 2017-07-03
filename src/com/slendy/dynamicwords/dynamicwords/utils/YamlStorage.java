package com.slendy.dynamicwords.dynamicwords.utils;

import com.slendy.dynamicwords.Core;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

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
public final class YamlStorage {

    private YamlConfiguration config;
    private File file;

    public YamlStorage(File file) {
        this.file = file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }

    // Creates and loads necessary storage files
    public void initialize() {
        this.config = new YamlConfiguration();
        if (!getFile().exists()) {
            try {
                if (getFile().createNewFile()) {
                    Core.that.getLogger().log(Level.INFO, "Created storage file: " + getFile().getName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            getConfig().load(getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Writes changes to disk
    public void save() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //getConfig.set(path, object)

    public void close() {
        save();
    }

    public void set(String path, Object object){
        getConfig().set(path, object);
        save();
    }

}


