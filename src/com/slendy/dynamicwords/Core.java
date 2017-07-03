package com.slendy.dynamicwords;

import com.slendy.dynamicwords.dynamicwords.DynamicWords;
import com.slendy.dynamicwords.dynamicwords.update.Update;
import com.slendy.dynamicwords.dynamicwords.utils.YamlStorage;
import com.slendy.dynamicwords.dynamicwords.utils.command.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

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
public class Core extends JavaPlugin {

    public YamlStorage WordStorage;

    public YamlStorage GetWordStorage(){
        return WordStorage;
    }

    public static Core that;

    public static String main(String module, String body)
    {
        return "§3" + module + "§7" + " >> " + body;
    }
    public static String value(String variable, String value)
    {
        return value(0, variable, value);
    }
    public static String value(int a, String variable, String value){
        String indent = "";
        while (indent.length() < a) {
            indent = indent + ChatColor.GRAY + ">";
        }
        return indent + listTitle + variable + ": " + listValue + value;
    }
    public static String elem(String elem)
    {
        return "§a" + elem + ChatColor.RESET + mBody;
    }
    public static String help(String command, String usage){
        return "§3" + command + ": §7" + usage;
    }
    public static String listTitle = "" + ChatColor.WHITE;
    public static String listValue = "" + ChatColor.YELLOW;
    public static String mElem = "" + ChatColor.GREEN;
    public static String mCount = "" + ChatColor.GREEN;
    public static String mBody = "" + ChatColor.GRAY;

    public static String nopermission = Core.main("Permissions", "You have insufficient permission.");

    public void onEnable(){
        that = this;
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Update(this), 1L, 1L);
        new CommandHandler(this);
        if(!(getDataFolder().exists())){
            getDataFolder().mkdir();
        }
        WordStorage = new YamlStorage(new File(getDataFolder(), "words.yml"));
        WordStorage.initialize();
        new DynamicWords(this);
    }

    public void onDisable(){
        WordStorage.close();
        DynamicWords.getInstance().Disable();
        that = null;
    }

    public static Object getKeyFromValue(HashMap hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}