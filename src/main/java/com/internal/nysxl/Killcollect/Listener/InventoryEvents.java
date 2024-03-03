package com.internal.nysxl.Killcollect.Listener;

import com.internal.nysxl.Killcollect.Loot.Loot;
import com.internal.nysxl.Killcollect.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Handles inventory events related to Loot objects.
 */
public class InventoryEvents implements Listener {

    /**
     * Handles the inventory close event, removing empty Loot inventories from the player's loot list.
     *
     * @param e The InventoryCloseEvent fired when a player closes an inventory.
     */
    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof Loot loot) {
            Player player = (Player) e.getPlayer();

            // Check if the player's loot list exists and the inventory is empty before removing
            if (e.getInventory().isEmpty() && main.playerLoot.containsKey(player)) {
                main.playerLoot.get(player).remove(loot);
            }
        }
    }
}
