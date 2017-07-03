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
//        Class<?>[] classes = ClassEnumerator.getInstance().getClassesFromThisJar(
//                Plugin);
//        if (classes == null || classes.length == 0)
//        {
//            return;
//        }
//        for (Class<?> c : classes)
//        {
//            try
//            {
//                if (Command.class.isAssignableFrom(c) && (!(c.equals(Command.class))))
//                {
//                    Command command = (Command) c.newInstance();
//                    Core.that.getLogger().log(Level.INFO, "Registered Command: " + command.executor());
////                        System.out.println("Found class: " + c.getSimpleName());
//                    addCommand(command);
//                }
//            } catch (IllegalAccessException | InstantiationException e) {
//                e.printStackTrace();
//            }
//        }
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
//        addCommands();
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

//        Commands.put(command.executor().toLowerCase(), command);
    }

//    public <T extends Command> addCommand(Command command){
//        aliveCheck("addCommand(Command)");
//        MainCommands.put(command.executor(), command);
//
//        for(String commandRoot : command.aliases()){
//            Commands.put(commandRoot.toLowerCase(), command);
//        }
//
////        Commands.put(command.executor().toLowerCase(), command);
//    }

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
    public void handleConsoleCommands(ServerCommandEvent event){
        CommandSender sender = event.getSender();
        String _commandRaw =  event.getCommand().toLowerCase();
        String[] _args = null;
        if(_commandRaw.contains(" "))
        {
            _commandRaw = _commandRaw.split(" ")[0];
            _args = event.getCommand().substring(event.getCommand().indexOf(' ') + 1).split(" ");
        }
        if(_args == null) _args = new String[0];

        if(Commands.containsKey(_commandRaw))
        {
            event.setCancelled(true);
            Command command = Commands.get(_commandRaw);
            if(command.console()) {
                final String[] _argsFinal = _args;

                if (!command.async()) {
                    command.execute((Player) sender, _args);
                } else {
                    doAsync(() -> {
                        command.execute((Player) sender, _argsFinal);
                    });
                }
            } else {
                sender.sendMessage("§cOnly players can use this command.");
            }
        }
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
//                try {
                _command.execute(_player, argsFinal);
//                }catch(CustomException e) {
//                    e.printStackTrace();
//                    if (Core.that.GetStorage().getConfig().get(_player.getUniqueId().toString() + ".stackTraces") == null || !Core.that.GetStorage().getConfig().getBoolean(_player.getUniqueId().toString() + ".stackTraces")) {
//                        _player.sendMessage(Core.main("Core", "An exception has occurred. Please contact the server administrators if the error persists."));
//                    } else if(Core.that.GetStorage().getConfig().get(_player.getUniqueId().toString() + ".stackTraces") != null && Core.that.GetStorage().getConfig().getBoolean(_player.getUniqueId().toString() + ".stackTraces")) {
//                        _player.sendMessage("§4§k0§r §c§lSTACK TRACE §4§k0");
//                        _player.sendMessage("§o"  + Core.getRootCause(e).getMessage());
//
//                        _player.sendMessage("");
//
//                        int i = 0;
//                        for(StackTraceElement el : Core.getRootCause(e).getStackTrace()){
//                            _player.sendMessage(el.getClassName() + "." + el.getMethodName() + "(" + el.getFileName() + ":" + el.getLineNumber() + ")");
//                            i++;
//                            if(i==2)
//                                break;
//                        }
//                        _player.sendMessage("§4§k0§r §c§lEND OF STACK TRACE §4§k0");
//                    }
//                }
            }else{

                doAsync(() ->{
//                        try {
                    _command.execute(_player, argsFinal);
//                        }catch(CustomException e) {
//                            if (!_player.getName().equalsIgnoreCase("Slendy")) {
//                                _player.sendMessage(Core.main("Core", "An exception has occurred."));
//                                _player.sendMessage(Core.mBody + "Please contact the server administrators if you believe that this is in error.");
//                            } else {
//                                _player.sendMessage(Arrays.toString(e.getStackTrace()) + "");
//                            }
//                        }
                });



            }
        }
    }


}


