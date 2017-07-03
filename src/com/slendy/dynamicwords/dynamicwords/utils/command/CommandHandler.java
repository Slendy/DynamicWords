package com.slendy.dynamicwords.dynamicwords.utils.command;

import com.slendy.dynamicwords.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import com.slendy.dynamicwords.dynamicwords.commands.dw;

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
public class CommandHandler implements Listener {

    private void doAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.that, runnable);
    }

    private void doThread(Thread r) {r.run();}

    public void registerCommands()
    {
        Command command = new dw();
        addCommand(command);
    }

    private static CommandHandler Instance;
    private final JavaPlugin Plugin;
    private final HashMap<String, Command> Commands;
    private final HashMap<String, Command> MainCommands;

    private String noPermissionMessage = Core.nopermission;

    public CommandHandler(JavaPlugin plugin) {
        if (Instance != null) {
            throw new RuntimeException("No no no, only one instance of CommandHandler");
        }

        Instance = this;
        Plugin = plugin;
        Commands = new HashMap<>();
        MainCommands = new HashMap<>();
        registerCommands();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static CommandHandler get(){
        aliveCheck("get()");

        return Instance;
    }

    public static void end(){
        aliveCheck("end()");

        Bukkit.getLogger().info("Someone has disabled our command handler. May wanna check that out?");
        HandlerList.unregisterAll(Instance);
        Instance = null;
    }

    public JavaPlugin plugin(){
        aliveCheck("plugin()");

        return Plugin;
    }

    public String message(){
        aliveCheck("message()");

        return noPermissionMessage;
    }

    public Command searchCommand(Player caller, String command, boolean inform)
    {
        LinkedList<Command> matchList = new LinkedList<>();
        for (Command cur : get().mainCommands())
        {
            if (cur.executor().equalsIgnoreCase(command)) {
                return cur;
            }
            if (cur.executor().toLowerCase().contains(command.toLowerCase())) {
                matchList.add(cur);
            }
        }
        if (matchList.size() != 1)
        {
            if (!inform) {
                return null;
            }
            caller.sendMessage(Core.main("Command Search",
                    Core.mCount + matchList.size() +
                            Core.mBody + " matches for [" +
                            Core.mElem + command +
                            Core.mBody + "]."));
            if (matchList.size() > 0)
            {
                String matchString = "";
                for (Command cur : matchList) {
                    matchString = matchString + Core.elem(cur.executor()) + ", ";
                }
                if (matchString.length() > 1) {
                    matchString = matchString.substring(0, matchString.length() - 2);
                }
                caller.sendMessage(Core.main("Command Search",
                        Core.mBody + "Matches [" +
                                Core.mElem + matchString +
                                Core.mBody + "]."));
            }
            return null;
        }
        return matchList.get(0);
    }

    public Collection<Command> commands(){
        aliveCheck("commands()");

        return Commands.values();
    }

    public Collection<Command> mainCommands(){
        aliveCheck("mainCommands()");

        return MainCommands.values();
    }

    public HashMap<String, Command> mainCommandsHashMap(){
        aliveCheck("mainCommands()");

        return MainCommands;
    }

    public CommandHandler message(String val){
        aliveCheck("message(String)");

        noPermissionMessage = ChatColor.translateAlternateColorCodes('&', val);
        return Instance;
    }

    public void addCommand(Command command){
        aliveCheck("addCommand(Command)");
        MainCommands.put(command.executor(), command);

        for(String commandRoot : command.aliases()){
            Commands.put(commandRoot.toLowerCase(), command);
        }
    }

    public CommandHandler removeCommand(Command command){
        aliveCheck("removeCommand(Command)");
        MainCommands.remove(command.executor());

        for(String commandRoot : command.aliases()){
            Commands.remove(commandRoot);
        }
        return Instance;
    }

    public Command findCommand(String executor){
        aliveCheck("findCommand(String)");

        return Commands.get(executor);
    }

    private static void aliveCheck(String method)
    {
        if (Instance == null)
            throw new UnsupportedOperationException("There is no instance of CommandHandler. Method: " + method);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleCommands(PlayerCommandPreprocessEvent event)
    {
        Player _player = event.getPlayer();
        String _commandRaw = event.getMessage().substring(1).toLowerCase();
        String[] _args = null;

        if (_commandRaw.contains(" "))
        {
            _commandRaw = _commandRaw.split(" ")[0];
            _args = event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ");
        }

        if (_args == null) _args = new String[0];

        if (Commands.containsKey(_commandRaw))
        {
            event.setCancelled(true);

            Command _command = Commands.get(_commandRaw);

            if (_command.permissionNode() != null)
                if (_command.permissionRequired() && !_player.hasPermission(_command.permissionNode()))
                {
                    _player.sendMessage(noPermissionMessage);
                    return;
                }
            final String[] argsFinal = _args;
            if(!_command.async()) {
                _command.execute(_player, argsFinal);
            }else{
                doAsync(() ->{
                    _command.execute(_player, argsFinal);
                });



            }
        }
    }


}


