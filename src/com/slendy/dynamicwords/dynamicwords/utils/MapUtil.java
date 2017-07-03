package com.slendy.dynamicwords.dynamicwords.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

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
public class MapUtil {

    public static void setBlockFast(Block b, int blockId, byte data) {
        net.minecraft.server.v1_8_R3.World w = ((CraftWorld) b.getWorld()).getHandle();
        net.minecraft.server.v1_8_R3.Chunk chunk = w.getChunkAt(b.getX() >> 4, b.getZ() >> 4);
        BlockPosition bp = new BlockPosition(b.getX(), b.getY(), b.getZ());
        int combined = blockId + (data << 12);
        IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
        chunk.a(bp, ibd);
        Chunk c = b.getChunk();
        c.getWorld().refreshChunk(c.getX(), c.getZ());

    }

    public static void setBlockFast(Block b, Material block, byte data) {
        net.minecraft.server.v1_8_R3.World w = ((CraftWorld) b.getWorld()).getHandle();
        net.minecraft.server.v1_8_R3.Chunk chunk = w.getChunkAt(b.getX() >> 4, b.getZ() >> 4);
        BlockPosition bp = new BlockPosition(b.getX(), b.getY(), b.getZ());
        int combined = block.getId() + (data << 12);
        IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
        chunk.a(bp, ibd);
        Chunk c = b.getChunk();
        c.getWorld().refreshChunk(c.getX(), c.getZ());

    }

    public static void ChunkBlockSet(World world, int x, int y, int z, int id, byte data, boolean notifyPlayers)
    {
        world.getBlockAt(x, y, z).setTypeIdAndData(id, data, notifyPlayers);
    }



}


