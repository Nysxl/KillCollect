package com.internal.nysxl.Killcollect;

import com.internal.nysxl.Killcollect.Listener.EntityKill;
import com.internal.nysxl.Killcollect.Listener.InventoryEvents;
import com.internal.nysxl.Killcollect.Loot.Loot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class main extends JavaPlugin {

    public static Map<Player, ArrayList<Loot>> playerLoot = new HashMap<>();

    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(new EntityKill(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
    }




}
