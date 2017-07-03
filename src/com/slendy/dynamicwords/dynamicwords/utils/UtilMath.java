package com.slendy.dynamicwords.dynamicwords.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Random;

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
public class UtilMath {

    public static double trim(int degree, double d)
    {
        String format = "#.#";
        for (int i = 1; i < degree; i++) {
            format = format + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d)).doubleValue();
    }

    public static Random random = new Random();

    public static int r(int i)
    {
        return random.nextInt(i);
    }

    public static double offset2d(Entity a, Entity b)
    {
        return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(Location a, Location b)
    {
        return offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(Vector a, Vector b)
    {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static double offset(Entity a, Entity b)
    {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(Location a, Location b)
    {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b)
    {
        return a.subtract(b).length();
    }



}


