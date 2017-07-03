package com.slendy.dynamicwords.dynamicwords.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

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
public class DynamicLine {

    private String _text;
    private String _lastText;
    private double _x, _y, _z;
    private double _oldX, _oldY, _oldZ;
    private World _world;
    private World _oldWorld;
    private int _material;
    private byte _data;
    private BlockFace _face;
    private BlockFace _oldFace;
    private TextUtil.TextAlign _align;
    private boolean _deleted = false;
    private int _line;

    public boolean getDeleted() {return _deleted;}

    public void setDeleted(boolean deleted) {_deleted = deleted;}

    public World getWorld() {return _world;}

    public void setWorld(World world) {_world = world;}

    public String getText() {
        return _text;
    }

    public String getLastText() {
        return _lastText;
    }

    public void setLastText(String lastText) {
        _lastText = lastText;
    }

    public void setText(String text) {

        _text = text;
    }

    public double getX() {
        return _x;
    }

    public void setX(double x) {
        _x = x;
    }

    public double getY() {
        return _y;
    }

    public void setY(double y) {
        _y = y;
    }

    public double getZ() {
        return _z;
    }

    public void setZ(double z) {
        _z = z;
    }

    public double getOldX() {
        return _oldX;
    }

    public void setOldX(double oldX) {
        _oldX = oldX;
    }

    public double getOldY() {
        return _oldY;
    }

    public void setOldY(double oldY) {
        _oldY = oldY;
    }

    public double getOldZ() {
        return _oldZ;
    }

    public void setOldZ(double oldZ) {
        _oldZ = oldZ;
    }

    public World getOldWorld() {
        return _oldWorld;
    }

    public void setOldWorld(World oldWorld) {
        _oldWorld = oldWorld;
    }

    public int getMaterial() {
        return _material;
    }

    public void setMaterial(int material) {
        _material = material;
    }

    public byte getData() {
        return _data;
    }

    public void setData(byte data) {
        _data = data;
    }

    public BlockFace getFace() {
        return _face;
    }

    public void setFace(BlockFace face) {
        _face = face;
    }

    public BlockFace getOldFace() {
        return _oldFace;
    }

    public void setOldFace(BlockFace oldFace) {
        _oldFace = oldFace;
    }

    public TextUtil.TextAlign getAlign() {
        return _align;
    }

    public void setAlign(TextUtil.TextAlign align) {
        _align = align;
    }

    public int getLine() {
        return _line;
    }

    public void setLine(int line) {
        _line = line;
    }

    public DynamicLine(String text, double x, double y, double z, String world, int material, byte data, BlockFace face, TextUtil.TextAlign align, int line){
        _text = text;
        _lastText = text;
        _x = x;
        _y = y;
        _z = z;
        _oldX = x;
        _oldY = y;
        _oldZ = z;
        _world = Bukkit.getWorld(world);
        _oldWorld = Bukkit.getWorld(world);
        _material = material;
        _data = data;
        _face = face;
        _oldFace = face;
        _align = align;
        _line = line;
    }

    public DynamicLine(String text, double x, double y, double z, String world, int material, byte data, BlockFace face, int line){
        _text = text;
        _lastText = text;
        _x = x;
        _y = y;
        _z = z;
        _oldX = x;
        _oldY = y;
        _oldZ = z;
        _world = Bukkit.getWorld(world);
        _oldWorld = Bukkit.getWorld(world);
        _material = material;
        _data = data;
        _face = face;
        _oldFace = face;
        _align = TextUtil.TextAlign.CENTER;
        _line = line;
    }

    public DynamicLine(String text, Location l, int material, byte data, BlockFace face, TextUtil.TextAlign align, int line){
        _text = text;
        _lastText = text;
        _x = l.getX();
        _y = l.getY();
        _z = l.getZ();
        _oldX = l.getX();
        _oldY = l.getY();
        _oldZ = l.getZ();
        _world = l.getWorld();
        _oldWorld = l.getWorld();
        _material = material;
        _data = data;
        _face = face;
        _oldFace = face;
        _align = align;
        _line = line;
    }

    public DynamicLine(String text, Location l, Material material, byte data, BlockFace face, TextUtil.TextAlign align, int line){
        _text = text;
        _lastText = text;
        _x = l.getX();
        _y = l.getY();
        _z = l.getZ();
        _world = l.getWorld();
        _oldWorld = l.getWorld();
        _material = material.getId();
        _data = data;
        _face = face;
        _oldFace = face;
        _align = align;
        _line = line;
    }
}