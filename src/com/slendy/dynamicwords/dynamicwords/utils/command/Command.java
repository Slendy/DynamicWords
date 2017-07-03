package com.slendy.dynamicwords.dynamicwords.utils.command;

import org.apache.commons.lang3.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ************************************************************************
 * Copyright Slendy (c) 2016. All Right Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Slendy. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * Thanks
 * ************************************************************************
 */
public abstract class Command {

    private final String _mainExecutor;

    private final List<String> _aliases;

    private final String _permissionNode;

    private final String _usage;

    private final String _description;

    private final boolean _permissionRequired;

    private final boolean _async;

    private final boolean _console;

    public Command(String usage, String description, String... executors){
        this(null, usage, description, true, false, false, executors);
    }

    public Command(String permission, String usage, String description, String... executors){
        this(permission, usage, description, true, false, false, executors);
    }

    public Command(String permission, String usage, String description, boolean console, String... executors){
        this(permission, usage, description, true, false, console, executors);
    }

    public Command(String permission, String usage, String description, boolean async, boolean console, String... executors){
        this(permission, usage, description, false, async, console, executors);
    }

    public Command(String permission, String usage, String description, boolean permissionRequired, boolean async, boolean console, String... executors){
        _permissionRequired = permissionRequired;
        Validate.isTrue(executors.length > 0, "You must provide at least one executor for this command to use!");
        _mainExecutor = executors[0];
        _aliases = Arrays.asList(executors);
        _permissionNode = permission;
        _usage = usage;
        _description = description;
        _async = async;
        _console = console;
        CommandHandler.get().addCommand(this);
    }

    public final String executor(){
        return _mainExecutor;
    }

    public final boolean console(){return _console;}

    public final String permissionNode(){
        return _permissionNode;
    }

    public final String usage(){
        return _usage;
    }

    public final String description(){
        return _description;
    }

    public final boolean permissionRequired() {return _permissionRequired;}

    public final boolean async(){return _async;}

    public final String getFinalArg(String[] args, int start){
        final StringBuilder bldr = new StringBuilder();
        for(int i = start; i < args.length; i++){
            if(i != start){
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }



    public final List<String> aliases() {
        return Collections.unmodifiableList(_aliases);
    }

    public abstract void execute(Player player, String[] args);

    public void sendMessage(CommandSender sender, String message){
        sender.sendMessage(message);
    }



}
