package com.internal.nysxl.Killcollect.Commands;

import com.internal.nysxl.Killcollect.Loot.LootInventory;
import com.internal.nysxl.NysxlUtilities.Utility.Commands.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Spoils implements CommandInterface {
    /**
     * spoils command.
     * @param commandSender player that executed the command.
     * @param strings arguments made after the command.
     * @return returns true if the command executed correctly
     */
    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof Player player) {
            if(strings.length == 0) {
                new LootInventory(player, player);
                return true;
            }

            if(strings.length == 1) {
                // Check if the player has the 'killcollect.admin' permission
                if(! player.hasPermission("killcollect.admin")) {
                    player.sendMessage("You do not have permission to open another player's inventory.");
                    return false;
                }

                // If the player has permission, proceed to find the target player and open their inventory
                Optional.ofNullable(strings[0])
                        .flatMap(arg -> Bukkit.getOnlinePlayers().stream()
                                .filter(s -> s.getDisplayName().equalsIgnoreCase(arg))
                                .findFirst())
                        .ifPresent(targetPlayer -> new LootInventory(player, targetPlayer));
                return true;
            }
        }
        return false;
    }

    /**
     * command tab completer.
     * @param commandSender the player typing the command.
     * @param strings the current args typed.
     * @return returns a list of valid arguments.
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (strings.length == 1) {
            // Return a list of player names for tab completion, filtering based on the current input
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getDisplayName)
                    .filter(name -> name.toLowerCase().startsWith(strings[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Return an empty list if there are no relevant completions
        return new ArrayList<>();
    }

    /**
     * returns true if the player has the permission to execute the command.
     * @param commandSender the player executing the command.
     * @return returns true or false depending on if the player has the permission to execute the command.
     */
    @Override
    public boolean hasPermission(CommandSender commandSender) {
        return true;
    }
}
