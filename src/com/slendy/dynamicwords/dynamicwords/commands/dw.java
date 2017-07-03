package com.slendy.dynamicwords.dynamicwords.commands;

//import me.slendy.Core;
//import me.slendy.TextUtil;
//import me.slendy.util.command.Command;
//import me.slendy.util.dynamicwords.DynamicWords;
//import me.slendy.util.dynamicwords.utils.DynamicLine;
//import me.slendy.util.dynamicwords.utils.DynamicWord;
//import me.slendy.util.dynamicwords.utils.DynamicWordList;
import com.slendy.dynamicwords.Core;
import com.slendy.dynamicwords.dynamicwords.DynamicWords;
import com.slendy.dynamicwords.dynamicwords.utils.DynamicLine;
import com.slendy.dynamicwords.dynamicwords.utils.DynamicWord;
import com.slendy.dynamicwords.dynamicwords.utils.DynamicWordList;
import com.slendy.dynamicwords.dynamicwords.utils.TextUtil;
import com.slendy.dynamicwords.dynamicwords.utils.command.Command;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
public class dw extends Command {

    public dw() {
        super("core.dw", "/dw help", "DynamicWords", new String[]{"dw", "dynamicwords"});
    }

    public void sendHelp(Player player){
        player.sendMessage(Core.main("DW", "Listing help;"));
        player.sendMessage(Core.help("/dw help", "Displays this menu"));
        player.sendMessage(Core.help("/dw list", "Lists all DynamicWords"));
        player.sendMessage(Core.help("/dw write <text>", "Writes text (not a dynamic word)"));
        player.sendMessage(Core.help("/dw movehere <name>", "Moves a dynamic word to where you are standing and the direction you are facing (Moves all lines and pages)"));
        player.sendMessage(Core.help("/dw create <name>", "Creates a new dynamic word for you to edit (Faces your direction)"));
        player.sendMessage(Core.help("/dw delete <name>", "Deletes a dynamic word and all of its lines/pages"));
        player.sendMessage(Core.help("/dw tp <name>", "Teleports you to the specified word"));
        player.sendMessage(Core.help("/dw info <name>", "Gives you info about the specified word"));
        player.sendMessage(Core.help("/dw edit <name>", "Displays a help message for editing commands"));

        player.sendMessage(Core.main("TIP", "When you are writing text face the direction where you want the text to be readable from"));
        return;
    }



    public void execute(Player player, String[] args) {
        if(args.length == 0){
            sendHelp(player);
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Core.main("DW", "Listing all dynamic words:"));
            for (String list : DynamicWords.getInstance().getWords().keySet()) {
                player.sendMessage("§a" + list);
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sendHelp(player);
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("write")) {
            String text = getFinalArg(args, 1);
            if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR || !player.getItemInHand().getType().isBlock()) {
                player.sendMessage(Core.main("DW", "You cannot set the word material to air."));
                return;
            }
            DynamicWords.getInstance().makeText(new DynamicLine(text, player.getLocation().getX(), player.getLocation().getY()-1, player.getLocation().getZ(),
                    player.getLocation().getWorld().getName(), player.getItemInHand().getTypeId(), player.getItemInHand().getData().getData(),
                    TextUtil.yawToFace(player.getLocation().getYaw()), TextUtil.TextAlign.CENTER, 0), 0);
            player.sendMessage(Core.main("DW", "Successfully wrote text."));
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            DynamicWord w = dw.getWordMap().get(0);
            DynamicLine l = w.getWordMap().get(0);
            player.teleport(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()));
            player.sendMessage(Core.main("DW", "Teleported to word: §a" + args[1] + "§7."));
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("info")){
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            player.sendMessage(Core.main("DW", "Info for word: §a" + args[1] + "§7."));
            player.sendMessage(Core.value("Total Pages", String.valueOf(dw.getWordMap().size())));
            player.sendMessage(Core.value("Current Page", String.valueOf(dw.getCurrentPage()+1)));
            if(dw.getWordMap().size() > 1)
            player.sendMessage(Core.value("Change Interval", String.valueOf(dw.getInterval())));
                for (int i = 0; i < dw.getWordMap().size(); i++) {
                    DynamicWord w = dw.getWordMap().get(i);
                    player.sendMessage(Core.value("  Page §e" + String.valueOf(i+1) + "§f", ""));
                    player.sendMessage("    " + Core.value("Total Lines", String.valueOf(w.getWordMap().size())));
                    for (DynamicLine l : w.getWordMap().values()) {
                        player.sendMessage("      " + Core.value("Line §e" + ((int)Core.getKeyFromValue(w.getWordMap(), l)+1) + "§f", l.getText()));
                    }
                }
            }

        if (args.length == 2 && args[0].equalsIgnoreCase("movehere")) {
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            for (DynamicWord w : dw.getWordMap().values()) {
                for (DynamicLine l : w.getWordMap().values()) {
                    TextUtil.MakeText(l.getText(), new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()), l.getFace(), Material.AIR.getId(), (byte) 0, l.getAlign(), true);
                    l.setX(player.getLocation().getX());
                    l.setY(player.getLocation().getY()-1);
                    l.setZ(player.getLocation().getZ());
                    l.setWorld(player.getLocation().getWorld());
                    l.setFace(TextUtil.yawToFace(player.getLocation().getYaw()));
                }
            }
            player.sendMessage(Core.main("DW", "Successfully moved word§a " + args[1] + "§7."));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            HashMap<Integer, DynamicWord> wordMap = new HashMap<>();

            DynamicLine l = new DynamicLine("DynamicWord", player.getLocation().subtract(0,1,0), Material.STAINED_CLAY, (byte) 0, TextUtil.yawToFace(player.getLocation().getYaw(), true), TextUtil.TextAlign.CENTER, 0);
            wordMap.put(0, new DynamicWord(l));
            DynamicWords.getInstance().getWords().put(args[1], new DynamicWordList(wordMap, 0));
            player.sendMessage(Core.main("DW", "Successfully created word: §a" + args[1] + "§7."));
            player.sendMessage(Core.main("DW", "Type §a/dw edit " + args[1] + " §7to edit."));
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove"))) {
            DynamicWordList list = DynamicWords.getInstance().getWords().get(args[1]);
            if (list == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            for (DynamicWord w : list.getWordMap().values()) {
                for (DynamicLine l : w.getWordMap().values()) {
                    l.setDeleted(true);
                }
            }
            player.sendMessage(Core.main("DW", "Successfully deleted word: §a" + args[1] + "§7."));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            player.sendMessage(Core.main("DW", "Edit Commands;"));
            player.sendMessage(Core.help("/dw movehere §a" + args[1], "Moves a dynamic word to where you are standing and the direction you are facing (Moves all lines and pages)"));
            player.sendMessage(Core.help("/dw delete/remove §a" + args[1], "Deletes a dynamic word and all of its lines/pages"));
            player.sendMessage(Core.help("/dw setinterval §a" + args[1] + "§7 <interval>", "Sets the time it takes to change words"));
            player.sendMessage(Core.help("/dw removepage §a" + args[1] + "§7 <pgnm>", "Deletes a page of dynamic words"));
            player.sendMessage(Core.help("/dw addpage §a" + args[1] + " §7(interval) <string>", "Add a page of dynamic words (text animation) if there is more than one page there is no need to specify an interval"));
            player.sendMessage(Core.help("/dw addline §a" + args[1] + "§7 <pg num> <string>", "Adds a line of text to the specified page (If a custom block is desired hold the block when you type the command)"));
            player.sendMessage(Core.help("/dw setblock §a" + args[1] + "§7 <pg num> <ln num>", "Sets the material of the word to the block you are currently holding"));
            player.sendMessage(Core.help("/dw setline §a" + args[1] + "§7 <pg num> <ln num> <string>", "Set the text of the line (If a custom block is desired hold the block when you type the command)"));
            player.sendMessage(Core.main("TIP", "When you are writing text face the direction where you want the text to be readable from"));

        }

        if (args.length == 3 && (args[0].equalsIgnoreCase("removepage") || args[0].equalsIgnoreCase("deletepage"))) {//dw removepage <name> <pg>
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            if (!Core.isInt(args[2])) {
                player.sendMessage(Core.main("DW", "The page must be an integer."));
                return;
            }
            int page = Integer.parseInt(args[2]);
            if (dw.getWordMap().get(page) == null) {
                player.sendMessage(Core.main("DW", "That page does not exist."));
                return;
            }
            if (page == 0) {
                player.sendMessage(Core.main("DW", "You cannot delete page 0, alternatively to delete the word you can run §a/dw delete " + args[1] + "§7."));
                return;
            }
            for (DynamicLine l : dw.getWordMap().get(page).getWordMap().values()) {
                l.setDeleted(true);
            }
            player.sendMessage(Core.main("DW", "Successfully deleted page §a" + page + "§7."));
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("setinterval")){//dw setinterval <name> <interval>
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            if(dw.getWordMap().size() <= 1){
                player.sendMessage(Core.main("DW", "You cannot set the interval when there is only 1 page."));
                return;
            }
            if (!Core.isLong(args[2])) {
                player.sendMessage(Core.main("DW", "The interval must be a long in milliseconds."));
                return;
            }
            dw.setInterval(Long.parseLong(args[2]));
            player.sendMessage(Core.main("DW", "Successfully set change interval for word: §a" + args[1] + "§7 to §a" + args[2] + "ms§7."));
        }

        if(args.length == 4 && (args[0].equalsIgnoreCase("setblock") || args[0].equalsIgnoreCase("setmaterial"))){
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            if (!Core.isInt(args[2])) {
                player.sendMessage(Core.main("DW", "The page must be an integer."));
                return;
            }
            int page = Integer.parseInt(args[2]);
            if (dw.getWordMap().get(page) == null) {
                player.sendMessage(Core.main("DW", "That page does not exist."));
                return;
            }
            if (!Core.isInt(args[3])) {
                player.sendMessage(Core.main("DW", "The line must be an integer."));
                return;
            }
            int line = Integer.parseInt(args[3]);
            if (dw.getWordMap().get(page).getWordMap().get(line) == null) {
                player.sendMessage(Core.main("DW", "That line does not exist."));
                return;
            }
            Material m = player.getItemInHand().getType();

            if (m == null || m == Material.AIR || !player.getItemInHand().getType().isBlock()) {
                player.sendMessage(Core.main("DW", "The material must be a block."));
                return;
            }
            byte b = player.getItemInHand().getData().getData();
            dw.getWordMap().get(page).wordMap.get(line).setMaterial(m.getId());
            dw.getWordMap().get(page).wordMap.get(line).setData(b);
            player.sendMessage(Core.main("DW", "Successfully set material for word: §a" + args[1] + "§7."));
        }


        if (args.length >= 3 && args[0].equalsIgnoreCase("addpage")) {//dw addpage <name> <change interval> <string> if there is more than 1 page dont add change interval
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            boolean interval = false;

            if(dw.getInterval() != 0 && Core.isLong(args[2])){
                player.sendMessage(Core.main("DW", "There is already a set change interval, Instead type /dw addpage " + args[1] + " <text>"));
                return;
            }
            if (!Core.isLong(args[2])) {
                if(dw.getInterval() != 0){
                    String text = getFinalArg(args, 2);
                    DynamicLine test = dw.getWordMap().get(0).getWordMap().get(0);
                    DynamicLine l = new DynamicLine(text, test.getX(), test.getY(), test.getZ(), test.getWorld().getName(), test.getMaterial(), test.getData(), test.getFace(), TextUtil.TextAlign.CENTER, 0);
                    dw.getWordMap().put(dw.getWordMap().size(), new DynamicWord(l));
                    player.sendMessage(Core.main("DW", "Successfully added a page to word: §a" + args[1] + "§7."));
                    return;
                }
                player.sendMessage(Core.main("DW", "The interval must be a long in milliseconds."));
                return;
            }
            dw.setInterval(Long.parseLong(args[2]));
            if (dw.getWordMap().size() == 0) {
                player.sendMessage("an error has occurred. Please contact Slendy");
                return;
            }
            String text = getFinalArg(args, 3);
            DynamicLine test = dw.getWordMap().get(0).getWordMap().get(0);
            DynamicLine l = new DynamicLine(text, test.getX(), test.getY(), test.getZ(), test.getWorld().getName(), test.getMaterial(), test.getData(), test.getFace(), TextUtil.TextAlign.CENTER, 0);
            dw.getWordMap().put(dw.getWordMap().size(), new DynamicWord(l));
            player.sendMessage(Core.main("DW", "Successfully added a page to word: §a" + args[1] + "§7 with an interval of §a" + Long.parseLong(args[2]) + "§7."));
        }
        if (args.length >= 3 && args[0].equalsIgnoreCase("addline")) {
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            if (!Core.isInt(args[2])) {
                player.sendMessage(Core.main("DW", "The page must be an integer."));
                return;
            }
            int page = Integer.parseInt(args[2]);
            if (dw.getWordMap().get(page) == null) {
                player.sendMessage(Core.main("DW", "That page does not exist."));
                return;
            }


            boolean changeMaterial = true;
            Material m = player.getItemInHand().getType();

            if (m == null || m == Material.AIR || !player.getItemInHand().getType().isBlock()) {
                changeMaterial = false;
            }
            byte b = player.getItemInHand().getData().getData();
            String text = getFinalArg(args, 3);
            DynamicLine test = dw.getWordMap().get(page).getWordMap().get(0);
            DynamicLine l = new DynamicLine(text, test.getX(), test.getY(), test.getZ(), test.getWorld().getName(), test.getMaterial(), test.getData(), test.getFace(), dw.getWordMap().get(page).getWordMap().size());


            l.setLine(dw.getWordMap().get(page).getWordMap().size());
            text = text.trim();
            if (text.equals("[blank]")) {
                text = "";
            }
            l.setText(text);
            l.setLastText(text);
            if (changeMaterial) {
                l.setMaterial(m.getId());
                l.setData(b);
            }
            HashMap<Integer, DynamicLine> test1 = dw.getWordMap().get(page).getWordMap();
            dw.getWordMap().get(page).getWordMap().put(test1.size(), l);
        }
        if (args.length >= 5 && args[0].equalsIgnoreCase("setline")) {//dw setline <name> <pg nm> <line> <string>
            DynamicWordList dw = DynamicWords.getInstance().getWords().get(args[1]);
            if (dw == null) {
                player.sendMessage(Core.main("DW", "That word doesn't exist."));
                return;
            }
            if (!Core.isInt(args[2])) {
                player.sendMessage(Core.main("DW", "The page must be an integer."));
                return;
            }
            int page = Integer.parseInt(args[2]);
            if (dw.getWordMap().get(page) == null) {
                player.sendMessage(Core.main("DW", "That page does not exist."));
                return;
            }
            if (!Core.isInt(args[3])) {
                player.sendMessage(Core.main("DW", "The line must be an integer."));
                return;
            }
            int line = Integer.parseInt(args[3]);
            if (dw.getWordMap().get(page).getWordMap().get(line) == null) {
                player.sendMessage(Core.main("DW", "That line does not exist."));
                return;
            }


            boolean changeMaterial = true;
            Material m = player.getItemInHand().getType();

            if (m == null || m == Material.AIR || !player.getItemInHand().getType().isBlock()) {
                changeMaterial = false;
            }
            byte b = player.getItemInHand().getData().getData();
            String text = getFinalArg(args, 4);
            if (text.trim().equals("[blank]"))
                text = "";
            dw.getWordMap().get(page).wordMap.get(line).setText(text);
            if (changeMaterial) {
                dw.getWordMap().get(page).wordMap.get(line).setMaterial(m.getId());
                dw.getWordMap().get(page).wordMap.get(line).setData(b);
            }
            player.sendMessage(Core.main("DW", "Successfully set text" + (changeMaterial ? " and material" : "") + " for word: §a" + args[1] + "§7."));
        }
    }
}
