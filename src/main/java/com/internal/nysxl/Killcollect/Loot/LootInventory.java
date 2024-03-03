package com.internal.nysxl.Killcollect.Loot;

import com.internal.nysxl.Killcollect.main;
import com.internal.nysxl.NysxlUtilities.GUIManager.DynamicListGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class LootInventory {

    /**
     * Constructs a LootInventory instance and displays it to the player.
     * If the player has no loot, a message is sent to them. Otherwise,
     * the loot is displayed in a GUI.
     *
     * @param player The player for whom the loot inventory is to be displayed.
     * @param target The player who's gui to show.
     */
    public LootInventory(Player player, Player target) {
        List<Loot> playerLoot = main.playerLoot.computeIfAbsent(target.getUniqueId(), k -> new java.util.ArrayList<>());

        if(playerLoot.isEmpty()) {
            player.sendMessage("You don't currently have any loot.");
            return;
        }

        DynamicListGUI gui = new DynamicListGUI("Spoils", 54);
        populateGUIWithLoot(gui, playerLoot);
        gui.open(player);
    }


    /**
     * Populates the given GUI with the player's loot items. Each item in the loot list
     * is added to the GUI with an associated action that opens the inventory when clicked.
     *
     * @param gui      The GUI to be populated with loot items.
     * @param lootList The list of loot items to add to the GUI.
     */
    private void populateGUIWithLoot(DynamicListGUI gui, List<Loot> lootList) {
        for(Loot loot : lootList) {
            Consumer<Player> consumer = loot::openInv;
            ItemStack slotItem = loot.getSlotItem() != null ? loot.getSlotItem() : new ItemStack(Material.PLAYER_HEAD);
            gui.addItem(slotItem, consumer);
        }
    }



}