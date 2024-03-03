package com.internal.nysxl.Killcollect.ConfigManager;

import com.internal.nysxl.Killcollect.Loot.Loot;
import com.internal.nysxl.Killcollect.main;
import com.internal.nysxl.NysxlUtilities.ConfigManager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class Configs {

    ConfigManager configManager = new ConfigManager(main.getInstance());

    /**
     * serializes the loot for all players.
     * @param loot the loot item to serialize.
     * @return returns a map of strings and objects to be saved into the configs.
     */
    public Map<String, Object> serialize(Loot loot) {
        Map<String, Object> serializedLoot = new HashMap<>();
        List<Map<String, Object>> serializedItems = new ArrayList<>();
        for (ItemStack item : loot.getInventory().getContents()) {
            if (item != null) {
                serializedItems.add(item.serialize());
            }
            serializedLoot.put("items", serializedItems);
        }
        return serializedLoot;
    }

    /**
     * saves the serialized loot
     */
    public void savePlayerLoot() {
        FileConfiguration config = configManager.getConfig("playerLoot.yml");
        for (Map.Entry<UUID, ArrayList<Loot>> entry : main.playerLoot.entrySet()) {
            String playerUUID = entry.getKey().toString();
            List<Map<String, Object>> lootList = entry.getValue().stream()
                    .map(s -> this.serialize(s))
                    .collect(Collectors.toList());
            config.set(playerUUID, lootList);
        }
        configManager.saveConfig("playerLoot.yml");
    }

    /**
     * deserializes the config to be used.
     * @param serializedLoot the serialized loot string from the configs.
     * @return returns a loot item.
     */
    public Loot deserialize(Map<String, Object> serializedLoot) {
        // Example: Deserialize a list of ItemStacks
        List<ItemStack> items = new ArrayList<>();
        List<Map<String, Object>> serializedItems = (List<Map<String, Object>>) serializedLoot.get("items");
        for (Map<String, Object> serializedItem : serializedItems) {
            items.add(ItemStack.deserialize(serializedItem));
        }
        // Reconstruct the Loot object (you'll need to adjust this according to how Loot is constructed)
        return new Loot("Loot Inventory", null, items, null); // Placeholder for player and slotItem
    }

    /**
     * loads the loot from the configs.
     */
    public void loadPlayerLoot() {
        FileConfiguration config = configManager.getConfig("playerLoot.yml");
        for (String playerUUID : config.getKeys(false)) {
            List<Map<String, Object>> serializedLootList = (List<Map<String, Object>>) config.getList(playerUUID);
            ArrayList<Loot> lootList = serializedLootList.stream()
                    .map(this::deserialize)
                    .collect(Collectors.toCollection(ArrayList::new));
            // Use Bukkit to get the Player object from UUID
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (player != null) {
                main.playerLoot.put(player.getUniqueId()    , lootList);
            }
        }
    }

}
