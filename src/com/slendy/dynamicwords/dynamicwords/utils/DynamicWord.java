package com.slendy.dynamicwords.dynamicwords.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

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
public class DynamicWord {

    public HashMap<Integer, DynamicLine> getWordMap() {
        return wordMap;
    }

    public HashMap<Integer, DynamicLine> wordMap = new HashMap<>();//int = line dynamicline = text amd xyz info

    public DynamicWord(DynamicLine... line){
        int i = 0;
        for(DynamicLine l : line){
            wordMap.put(i, l);
            i++;
        }
    }


    public void addLine(Location l, String text, Material m, byte b, BlockFace face, TextUtil.TextAlign align, int line)
    {
        getWordMap().computeIfAbsent(line, k -> new DynamicLine(text, l, m, b, face, align, line));
    }

    public void addLine(Location l, String text, Material m, byte b, BlockFace face, TextUtil.TextAlign align)
    {
        getWordMap().computeIfAbsent(getWordMap().size()+1, k -> new DynamicLine(text, l, m, b, face, align, getWordMap().size()+1));
    }

    public void removeLine(int line)
    {
        getWordMap().remove(line);
    }

    public void removeLine(DynamicLine w){
        getWordMap().remove(w.getLine());
    }

}


