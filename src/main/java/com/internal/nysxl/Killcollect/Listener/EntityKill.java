package com.internal.nysxl.Killcollect.Listener;

import com.internal.nysxl.Killcollect.Loot.Loot;
import com.internal.nysxl.Killcollect.main;
import com.internal.nysxl.NysxlUtilities.ItemBuilder.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles events related to player deaths, creating custom loot objects on PvP kills.
 */
public class EntityKill implements Listener {

    /**
     * Responds to player deaths, transferring the deceased player's inventory to a custom loot system for the killer.
     *
     * @param e The event triggered when a player dies.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = player.getKiller();

        // Only proceed if this is a PvP kill
        if (killer != null) {
            List<ItemStack> loot = Arrays.stream(player.getInventory().getContents())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Clear the default drops
            e.getDrops().clear();

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            ItemStack playerHead = new ItemFactory(item).withSkullOfPlayer(player);


            // Create the Loot object for the killer
            new Loot(player.getDisplayName(), killer, loot, playerHead);
        }
    }

    /**
     * on death event, only meant for testing
     * @param e the event
     */
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        List<ItemStack> loot = Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        e.getDrops().clear();

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemStack playerHead = new ItemFactory(item).withSkullOfPlayer(player);

        new Loot(player.getDisplayName(), player, loot, playerHead);

        main.configs.savePlayerLoot();
    }
}
