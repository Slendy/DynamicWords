package com.slendy.dynamicwords.dynamicwords.utils;

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
public class DynamicWordList {

    public HashMap<Integer, DynamicWord> getWordMap() {
        return _wordMap;
    }

    public long getInterval() {
        return _changeInterval;
    }

    public void setInterval(long interval) {
        _changeInterval = interval;
    }

    private HashMap<Integer, DynamicWord> _wordMap = new HashMap<>();//int = page # dynamicword = set of words <can be one line or multiple
    private long _changeInterval = 0;
    private int _currentPage = 0;
    private long _lastChange = 0;

    public int getCurrentPage() {
        return _currentPage;
    }

    public void setCurrentPage(int currentLine) {
        _currentPage = currentLine;
    }

    public void addPage(DynamicWord w){
        if(w.getWordMap().size()==0)
            return;
        _wordMap.putIfAbsent(_wordMap.size(), w);
    }

    public void removePage(int page){
        _wordMap.remove(page);
    }

    public long getLastChange() {
        return _lastChange;
    }

    public void setLastChange(long lastChange) {
        _lastChange = lastChange;
    }

    public DynamicWordList(HashMap<Integer, DynamicWord> wordMap, long interval){
        _wordMap = wordMap;
        _changeInterval = interval;
    }





}