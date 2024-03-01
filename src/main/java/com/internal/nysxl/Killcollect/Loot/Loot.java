package com.internal.nysxl.Killcollect.Loot;

import com.internal.nysxl.Killcollect.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a loot container for players. This class manages the creation and handling
 * of inventories for players to collect loot items. It supports splitting large quantities
 * of loot across multiple inventories if necessary.
 */
public class Loot implements InventoryHolder {

    private Inventory inv;
    private ItemStack slotItem;

    /**
     * Creates a Loot object with a given set of items. If the items exceed the inventory size,
     * additional Loot objects are created recursively to accommodate all items.
     *
     * @param inventoryName The name of the inventory, typically associated with the loot source.
     * @param player The player who will receive the loot.
     * @param items A list of ItemStack objects representing the loot to be added.
     */
    public Loot(String inventoryName, Player player, List<ItemStack> items, ItemStack slotItem) {
        this.inv = Bukkit.createInventory(this, 54, inventoryName);
        this.slotItem = slotItem;

        List<ItemStack> remainingItems = new ArrayList<>();

        for (ItemStack item : items) {
            if (this.inv.firstEmpty() == -1) { // Check if inventory is full
                remainingItems.add(item); // Add to remaining items if there's no space
            } else {
                this.inv.addItem(item); // Add item to inventory
            }
        }

        // Add the loot to the player immediately for the first batch
        addLootToPlayer(player);

        // Handle any remaining items by creating a new Loot object
        if (!remainingItems.isEmpty()) {
            new Loot(inventoryName, player, remainingItems, slotItem); // Recursively create a new Loot for the remainder
        }
    }

    /**
     * Opens the loot inventory for the specified player.
     *
     * @param player The player for whom to open the inventory.
     */
    public void openInv(Player player) {
        player.openInventory(inv);
    }

    /**
     * Adds this Loot object to the player's collection of Loot objects.
     * This method ensures that each player has their own collection of Loot.
     *
     * @param player The player to whom the loot is to be added.
     */
    private void addLootToPlayer(Player player) {
        main.playerLoot.computeIfAbsent(player, k -> new ArrayList<>()).add(this);
    }

    /**
     * @return returns the player skull item
     */
    public ItemStack getSlotItem() {
        return slotItem;
    }

    /**
     * Gets the inventory associated with this Loot object. This method is part of the
     * InventoryHolder interface.
     *
     * @return The Inventory object representing the loot container.
     */
    @Override
    public Inventory getInventory() {
        return this.inv;
    }

}
