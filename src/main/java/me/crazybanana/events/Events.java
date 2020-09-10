package me.crazybanana.events;

import me.crazybanana.events.commands.event;
import org.bukkit.plugin.java.JavaPlugin;

public final class Events extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("event").setExecutor(new event());
    }
}
