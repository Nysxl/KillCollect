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

    private ConfigManager configManager = new ConfigManager(main.getInstance());

    /**
     * Serializes the loot for all players.
     * This method assumes Loot contains methods to serialize its inventory and slotItem.
     */
    public Map<String, Object> serializeLoot(Loot loot) {
        Map<String, Object> serializedLoot = new HashMap<>();
        List<Map<String, Object>> serializedItems = Arrays.stream(loot.getInventory().getContents())
                .filter(Objects::nonNull)
                .map(ItemStack::serialize)
                .collect(Collectors.toList());
        serializedLoot.put("items", serializedItems);
        // Serialize slotItem if necessary
        if (loot.getSlotItem() != null) {
            serializedLoot.put("slotItem", loot.getSlotItem().serialize());
        }
        return serializedLoot;
    }

    public void savePlayerLoot(UUID playerUUID, List<Loot> loots) {
        if (playerUUID == null) {
            System.err.println("Cannot save player loot: playerUUID is null.");
            return;
        }

        FileConfiguration config = configManager.getConfig("playerLoot.yml");
        List<Map<String, Object>> serializedLoots = loots.stream()
                .map(this::serializeLoot)
                .collect(Collectors.toList());
        config.set(playerUUID.toString(), serializedLoots);
        configManager.saveConfig("playerLoot.yml");
    }

    /**
     * Deserializes the config to be used.
     */
    public Loot deserializeLoot(Map<String, Object> serializedLoot) {
        List<ItemStack> items = new ArrayList<>();
        Object itemsObj = serializedLoot.get("items");

        if (itemsObj instanceof List<?>) {
            for (Object itemObj : (List<?>)itemsObj) {
                if (itemObj instanceof Map<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> itemMap = (Map<String, Object>)itemObj;
                    items.add(ItemStack.deserialize(itemMap));
                }
            }
        }

        ItemStack slotItem = null;
        if (serializedLoot.containsKey("slotItem")) {
            Object slotItemObj = serializedLoot.get("slotItem");
            if (slotItemObj instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked") // Safe cast after instanceof check
                Map<String, Object> slotItemMap = (Map<String, Object>)slotItemObj;
                slotItem = ItemStack.deserialize(slotItemMap);
            }
        }

        UUID playerUUID = null;
        String inventoryName = "Loot Inventory";

        return new Loot(inventoryName, playerUUID, items, slotItem);
    }


    /**
     * Loads the loot from the configs.
     */
    public void loadPlayerLoot() {
        FileConfiguration config = configManager.getConfig("playerLoot.yml");
        for (String playerUUIDString : config.getKeys(false)) {
            UUID playerUUID = UUID.fromString(playerUUIDString);
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) continue; // Skip if player is not online

            List<?> rawList = config.getList(playerUUIDString);
            if (rawList == null) continue; // Skip if no loot is stored for this player

            List<Map<String, Object>> serializedLootList = new ArrayList<>();
            for (Object item : rawList) {
                if (item instanceof Map<?, ?>) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) item;
                    serializedLootList.add(map);
                } else {
                }
            }

            List<Loot> lootList = serializedLootList.stream()
                    .map(this::deserializeLoot)
                    .toList();

            main.playerLoot.put(playerUUID, new ArrayList<>(lootList));
        }
    }
}
