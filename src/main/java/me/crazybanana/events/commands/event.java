package me.crazybanana.events.commands;

import me.crazybanana.events.data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class event implements CommandExecutor {

    // Vars
    boolean eventLive = false;
    Location eventJoinSpawnCords;
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    //Data
    data data = new data();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // **Help Command**
        if (args == null || args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
            HelpHandler(sender);
        }

        // **Setup Help Command*
        else if (args.length == 1 && (args[0].equalsIgnoreCase("setup")) || args.length == 2 && ((args[0].equalsIgnoreCase("setup") && (args[1].equalsIgnoreCase("help"))))) {
            SetupHelpHandler(sender, args);
        }

        // **Setup Name Handler**
        else if(args.length == 2 && args[0].equalsIgnoreCase("setup")) {
            SetupNameHandler(sender, args);
        }

        // **Setup Cords Handler**
        else if(args.length == 3 && args[0].equalsIgnoreCase("setup") && args[1].equalsIgnoreCase("spawn")) {
            SetupSpawnHandler(sender, args);
        }

        // **Host Command**
        else if(args[0].equalsIgnoreCase("host")) {
            HostHandler(sender, args);
        }

        sender.sendMessage(RED + "An error occurred. Please type, '" + GOLD + "/event help" + RED + "' to see all commands!");
        return false;
    }







    public void HelpHandler(CommandSender sender) {
        // Checking Permission *event.help*
        if(sender.hasPermission("event.help") || sender.hasPermission("event.*")) {
            sender.sendMessage(DARK_PURPLE + "----------------------");
            sender.sendMessage(GOLD + "/event help" + BLACK + " - " + AQUA + "Open this help menu" + BLACK + " - " + LIGHT_PURPLE + "Aliases: " + GOLD + "/e, /e help, /e h, /event, /event help, /event h");
            sender.sendMessage(GOLD + "/event setup" + BLACK + " - " + AQUA + "Open this help menu" + BLACK + " - " + LIGHT_PURPLE + "Aliases: " + GOLD + "/e setup, /event setup");
            sender.sendMessage(GOLD + "command" + BLACK + " - " + AQUA + "description" + BLACK + " - " + LIGHT_PURPLE + "Aliases: " + GOLD + "alias");
            sender.sendMessage(DARK_PURPLE + "----------------------");
        }
    }

    public void SetupHelpHandler(CommandSender sender, String[] args) {
        // Checking if Player
        if (sender instanceof Player) {
            // Check permission *event.setup.help*
            if (sender.hasPermission("event.setup.help") || sender.hasPermission("event.*")) {
                // Code
                sender.sendMessage(DARK_PURPLE + "----------------------");
                sender.sendMessage(GOLD + "/event setup" + BLACK + " - " + AQUA + "Open this help menu" + BLACK + " - " + LIGHT_PURPLE + "Aliases: " + GOLD + "/e setup, /e setup help, /event setup, /event setup help");
                sender.sendMessage(GOLD + "/event setup <name>" + BLACK + " - " + AQUA + "Setup an event using this command" + BLACK + " - " + LIGHT_PURPLE + "Aliases: " + GOLD + "/e setup <name>, /event setup help");
                sender.sendMessage(GOLD + "/event setup spawn <name>" + BLACK + " - " + AQUA + "description" + BLACK + " - " + LIGHT_PURPLE + "Aliases: " + GOLD + "alias");
                sender.sendMessage(DARK_PURPLE + "----------------------");
            // Permission Case
            } else {
                sender.sendMessage(RED + "You don't have permission to run that command!");
            }
        // Console Case
        } else {
            sender.sendMessage(RED + "You have to be a " + GOLD + "PLAYER " + RED + "to run that command!");
        }
    }

    public void SetupNameHandler(CommandSender sender, String [] args) {
        // Checking if Player
        if(sender instanceof Player) {
            // Check Permission *event.setup.name*
            if(sender.hasPermission("event.setup.name") || sender.hasPermission("event.*")) {
                // Check if exists
                if(args.length == 2 && args[1]!=null && !data.eventTypes.contains(args[1])) {
                    // Creating EventType
                    data.eventTypes.add(args[1]);
                    sender.sendMessage(AQUA + "Added that event to the event list! To finish the process please type " + GOLD + "/event setup spawn <event name>" + AQUA + " to setup spawn for events!");
                // Already Exists
                } else {
                    sender.sendMessage(RED + "An error occurred, please type " + GOLD + "/event help " + RED + "to see all commands!");
                }
            }
        // Console Case
        } else {
            sender.sendMessage(RED + "You have to be a " + GOLD + "PLAYER " + RED + "to run that command!");
        }
    }

    public void SetupSpawnHandler(CommandSender sender, String[] args) {
        // Checking if Player
        if(sender instanceof Player) {
            // Checking Permission *event.setup.spawn*
            if(sender.hasPermission("event.setup.spawn") || sender.hasPermission("event.*")) {
                // Replacing
                if(data.spawn.containsKey(args[2])) {
                    data.spawn.replace(args[2], ((Player) sender).getLocation());
                }
                // Creating
                if(!data.spawn.containsKey(args[2])) {
                    data.spawn.put(args[2], ((Player) sender).getLocation());
                }
            }
        }
    }

    public void HostHandler(CommandSender sender, String[] args) {
        // Checking if Player
        if(sender instanceof Player) {
            // Checking permission *events.host*
            if(sender.hasPermission("events.host") || sender.hasPermission("event.*")) {
                // Checking if event live
                if(eventLive == false) {
                    eventLive = true;
                    // Checking if event is there
                    if(data!=null && data.eventTypes!=null && args.length>=2 && args[1]!=null && data.eventTypes.contains(args[1])) {
                        // Code
                        eventJoinSpawnCords = data.spawn.get(args[1]);
                        Bukkit.dispatchCommand(console, "announce &bAn&6 event&b is being&6 hosted&b! To join the event type, '&6/event join&b' or click the npc at the&6 lobby&b!");
                        // Non-Event Case
                    } else {
                        sender.sendMessage(RED + "Event " + AQUA + "doesn't exist or you have typed the command incorrectly" + RED + "! Type, '" + GOLD + "/event setup help" + RED + "' to see how to " + AQUA + "setup an event" + RED + "!");
                    }
                    // Live Case
                } else {
                    // Error Log
                    sender.sendMessage(RED + "An event is " + AQUA + "already live" + RED + "!");
                }
                // Permission Case
            } else {
                sender.sendMessage(RED + "You do not have permission to run that command!");
            }
            // Console Case
        } else {
            // Error Log
            sender.sendMessage(RED + "You Have to be a " + AQUA + "PLAYER " + RED + "to run that command!");
        }
    }
}
