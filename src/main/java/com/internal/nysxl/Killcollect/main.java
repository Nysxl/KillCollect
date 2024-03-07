package com.internal.nysxl.Killcollect;

import com.internal.nysxl.Killcollect.Commands.Spoils;
import com.internal.nysxl.Killcollect.ConfigManager.Configs;
import com.internal.nysxl.Killcollect.Listener.EntityKill;
import com.internal.nysxl.Killcollect.Listener.InventoryEvents;
import com.internal.nysxl.Killcollect.Loot.Loot;
import com.internal.nysxl.NysxlUtilities.CommandManager.CommandManager;
import com.internal.nysxl.NysxlUtilities.ConfigManager.ConfigManager;
import com.internal.nysxl.NysxlUtilities.GUIManager.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class main extends JavaPlugin {

    public static Map<UUID, ArrayList<Loot>> playerLoot = new HashMap<>();
    public CommandManager commandManager = new CommandManager(this);
    private static Plugin instance;

    public static Configs configs;

    public void onEnable(){
        instance = this;
        configs = new Configs();
        configs.loadPlayerLoot();
        registerCommands();
        registerEvents();
    }

    public void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new EntityKill(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
        //Bukkit.getPluginManager().registerEvents(new GUIManager(this), this);
    }

    public void registerCommands(){
            commandManager.registerCommand("spoils", new Spoils());
    }

    public static Plugin getInstance(){
        return instance;
    }
}
