package com.slendy.dynamicwords.dynamicwords;

//import me.slendy.Core;
//import me.slendy.TextUtil;
//import me.slendy.util.UpdateEvent;
//import me.slendy.util.UpdateType;
//import me.slendy.util.UtilTime;
//import me.slendy.util.dynamicwords.utils.DynamicLine;
//import me.slendy.util.dynamicwords.utils.DynamicWord;
//import me.slendy.util.dynamicwords.utils.DynamicWordList;
//import me.slendy.util.prefs.YamlStorage;
import com.slendy.dynamicwords.Core;
import com.slendy.dynamicwords.dynamicwords.update.UpdateEvent;
import com.slendy.dynamicwords.dynamicwords.update.UpdateType;
import com.slendy.dynamicwords.dynamicwords.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
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
public class DynamicWords implements Listener {

    public YamlStorage WordStorage = Core.that.WordStorage;

    public static DynamicWords Instance;


    public void saveWords(){
        for(String s : WordStorage.getConfig().getKeys(false)){
            WordStorage.set(s, null);
        }
        for(DynamicWordList dwl : _words.values()){
            String name = (String)Core.getKeyFromValue(_words, dwl);
            WordStorage.set(name + ".interval", dwl.getInterval());
            WordStorage.set(name + ".totalPages", dwl.getWordMap().size());
            for(DynamicWord dw : dwl.getWordMap().values()){
                int i = (int)Core.getKeyFromValue(dwl.getWordMap(), dw);
                int lines = 0;
                for(DynamicLine l : dw.getWordMap().values()){
                    WordStorage.set(name + ".pages." + i + ".x", l.getX());
                    WordStorage.set(name + ".pages." + i + ".y", l.getY());
                    WordStorage.set(name + ".pages." + i + ".z", l.getZ());
                    WordStorage.set(name + ".pages." + i + ".world", l.getWorld().getName());
                    WordStorage.set(name + ".pages." + i + ".line." + l.getLine() + ".text", l.getText());
                    WordStorage.set(name + ".pages." + i + ".line." + l.getLine() + ".material", l.getMaterial());
                    WordStorage.set(name + ".pages." + i + ".line." + l.getLine() + ".data", l.getData());
                    WordStorage.set(name + ".pages." + i + ".line." + l.getLine() + ".face", l.getFace().name());
                    WordStorage.set(name + ".pages." + i + ".line." + l.getLine() + ".line", l.getLine());
                    lines++;
                }
                WordStorage.set(name + ".pages." + i + ".lines", lines);
            }
        }
    }



    public void loadWords(){
        HashMap<String, DynamicWordList> words = new HashMap<>();
        for(String s : WordStorage.getConfig().getKeys(false)){
            String name = s;
            ConfigurationSection defined = WordStorage.getConfig().getConfigurationSection(s);

                int interval = WordStorage.getConfig().getInt(name + ".interval");
                int pages = WordStorage.getConfig().getInt(name + ".totalPages")-1;
                HashMap<Integer, DynamicWord> wordMap = new HashMap<>();

                for(int i = 0; i <= pages; i++){
                    DynamicWord w = new DynamicWord();
                    double x = WordStorage.getConfig().getDouble(name + ".pages." + i + ".x");
                    double y = WordStorage.getConfig().getDouble(name + ".pages." + i + ".y");
                    double z = WordStorage.getConfig().getDouble(name + ".pages." + i + ".z");
                    String world = WordStorage.getConfig().getString(name + ".pages." + i + ".world");
                    int lines = WordStorage.getConfig().getInt(name + ".pages." + i + ".lines")-1;

                    for(int line = 0; line <= lines; line++){
                        String text = WordStorage.getConfig().getString(name + ".pages." + i + ".line." + line + ".text");
                        int material = WordStorage.getConfig().getInt(name + ".pages." + i + ".line." + line + ".material");

                        int data =  WordStorage.getConfig().getInt(name + ".pages." + i + ".line." + line + ".data");
                        String face = WordStorage.getConfig().getString(name + ".pages." + i + ".line." + line + ".face");
                        int line1 = WordStorage.getConfig().getInt(name + ".pages." + i + ".line." + line + ".line");
                        DynamicLine l = new DynamicLine(text, x, y, z, world, material, (byte)data, BlockFace.valueOf(face), TextUtil.TextAlign.CENTER, line1);

                        w.getWordMap().put(l.getLine(), l);
                    }

                    wordMap.put(i, w);
                }
            words.put(name, new DynamicWordList(wordMap, interval));
//            for(DynamicWordList dwl : words.values()){
//                player.sendMessage("Name: " + Core.getKeyFromValue(words, dwl));
//                player.sendMessage("Change Interval: " + dwl.getInterval());
//                player.sendMessage("Last auto change: " + dwl.getLastChange());
//                player.sendMessage("Current page: " + dwl.getCurrentPage());
//                player.sendMessage("Total pages: " + dwl.getWordMap().size());
//                player.sendMessage("Listing pages;");
//                for(int i = 0; i < dwl.getWordMap().size(); i++){
//                    DynamicWord w1 = dwl.getWordMap().get(i);
//                    for(DynamicLine l : w1.getWordMap().values()){
//                        player.sendMessage("Page " + i + ", Line " + l.getLine() + ", Align " + l.getAlign() + ": " + l.getText());
//                    }
//                }
//            }

        }
        _words = words;
    }


//    public void saveAlts(){
//        try{
//            JsonObject json = new JsonObject();
//
//            for(DynamicWordList dwl : _words.values()){
//                String name = (String)Core.getKeyFromValue(_words, dwl);)
//
//                dynamicWordList.addProperty("name", name);
//                dynamicWordList.addProperty("interval", dwl.getInterval());
//
//                json.add(dynamicWordList);
//            }
//        }catch(Exception e){
//            e.printStackTrace();;
//        }
//    }





    public void setWords(HashMap<String, DynamicWordList> words) {
        _words = words;
    }

    private HashMap<String, DynamicWordList> _words = new HashMap<>();

    public DynamicWords(JavaPlugin plugin){
//        new WordStorage();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Instance = this;
        WordStorage = Core.that.WordStorage;
        loadWords();
        Core.that.getLogger().log(Level.INFO, "DynamicWords has been initialized.");
    }

    public void Disable() {
        saveWords();
        for (DynamicWordList list : _words.values()) {
            for (DynamicWord word : list.getWordMap().values()) {
                for (DynamicLine w : word.getWordMap().values()) {
                    removeText(w, w.getLine());
                }
            }
        }
        _words.clear();
    }

    public void makeText(DynamicLine w, int line){
        Location l = new Location(w.getWorld(), w.getX(), w.getY(), w.getZ());
        l.add(0, line * -6, 0);
        TextUtil.MakeText(w.getText(), l, w.getFace(), w.getMaterial(), w.getData(), w.getAlign());
    }

    public void removeText(DynamicLine w, int line){
        Location l = new Location(w.getWorld(), w.getX(), w.getY(), w.getZ());
        l.add(0, line * -6, 0);
        TextUtil.MakeText(w.getText(), l, w.getFace(), Material.AIR.getId(), (byte) 0, w.getAlign(), true);
    }

    public static DynamicWords getInstance() {
        return Instance;
    }

    public HashMap<String, DynamicWordList> getWords() {
        return _words;
    }


    Player p = Bukkit.getPlayer("Slendy");
    @EventHandler
    public void updateWords(UpdateEvent event) {
        if (event.getType() != UpdateType.FASTEST) {
            return;
        }
        HashMap<String, DynamicWordList> words = new HashMap<>(_words);
        for (DynamicWordList list : _words.values()) {
            String name = (String) Core.getKeyFromValue(words, list);
            if(list.getCurrentPage() > list.getWordMap().size()){
                words.get(name).setCurrentPage(0);
            }

            DynamicWord w = list.getWordMap().get(list.getCurrentPage());
            if(w == null){
                words.get(name).setCurrentPage(0);
                words.get(name).getWordMap().remove(Core.getKeyFromValue(_words.get(name).getWordMap(), w));
                continue;
            } else if(w.getWordMap() == null){
                words.get(name).setCurrentPage(0);
                words.get(name).getWordMap().remove(Core.getKeyFromValue(_words.get(name).getWordMap(), w));
                continue;
            } else if(w.getWordMap().size() == 0){
                words.get(name).setCurrentPage(0);
                words.get(name).getWordMap().remove(Core.getKeyFromValue(words.get(name).getWordMap(), w));
                continue;
            }
            for(DynamicLine l : w.getWordMap().values()){
                if (l.getDeleted()) {
                    words.get(name).getWordMap().get(list.getCurrentPage()).removeLine(l);
                    removeText(l, l.getLine());
                    for(int i = 0; i < words.get(name).getWordMap().size(); i++){
                        if (words.get(name).getWordMap().get(i).getWordMap().size() == 0) {
                            words.get(name).getWordMap().remove(i);
                            if(words.get(name).getCurrentPage() > words.get(name).getWordMap().size()){
                                words.get(name).setCurrentPage(0);
                            }
                        }
                    }
                    if(words.get(name).getWordMap().size() == 0){
                        words.remove(name);
                    }
                } else {
                    if(l.getOldX() != l.getX() || l.getOldY() != l.getY() || l.getOldZ() != l.getZ() || l.getOldWorld() != l.getWorld() || l.getOldFace() != l.getFace()){
                        TextUtil.MakeText(l.getLastText(), new Location(l.getOldWorld(), l.getOldX(), l.getOldX(), l.getOldZ()), l.getOldFace(), Material.AIR.getId(), (byte)0, l.getAlign(), true);
                        l.setLastText(l.getText());
                        TextUtil.MakeText(l.getText(), new Location(l.getOldWorld(), l.getOldX(), l.getOldX(), l.getOldZ()), l.getOldFace(), Material.AIR.getId(), (byte)0, l.getAlign(), true);
                        l.setOldX(l.getX());
                        l.setOldY(l.getY());
                        l.setOldZ(l.getZ());
                        l.setOldWorld(l.getWorld());
                        l.setOldFace(l.getFace());


                    } else if (!l.getLastText().equals(l.getText())) {
                        TextUtil.MakeText(l.getLastText(), new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()), l.getFace(), Material.AIR.getId(), (byte)0, l.getAlign(), true);
                        makeText(l, l.getLine());
                        l.setLastText(l.getText());
                    } else {
                        makeText(l, l.getLine());
                    }
                }    
            }

            if(list.getWordMap().size() > 1){
                if(list.getLastChange() == 0){
                    list.setLastChange(System.currentTimeMillis());
                }
                if(UtilTime.elapsed(list.getLastChange(), list.getInterval())){
                    if(list.getCurrentPage()+1 < list.getWordMap().size()){
                        words.get(name).setCurrentPage(list.getCurrentPage()+1);
                        for(DynamicLine l : w.getWordMap().values()){
                            removeText(l, l.getLine());
                        }
                    } else {
                        for(DynamicLine l : w.getWordMap().values()){
                            removeText(l, l.getLine());
                        }
                        words.get(name).setCurrentPage(0);
                    }
                    words.get(name).setLastChange(System.currentTimeMillis());
                }
            }
        }
        _words = words;
    }







}