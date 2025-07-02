package com.chestdrop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Chest;
import org.bukkit.ChatColor;

import java.util.List;

public class ChestDrop extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ChestDrop plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestDrop plugin has been disabled!");
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            List<ItemStack> drops = event.getDrops();

            if (drops.isEmpty()) {
                return;
            }

            ItemStack chest = new ItemStack(Material.CHEST);
            
            if (hasSpace(killer.getInventory())) {
                Inventory chestInventory = Bukkit.createInventory(null, 27, "Mob Drops");
                
                for (ItemStack drop : drops) {
                    if (drop != null) {
                        chestInventory.addItem(drop);
                    }
                }

                drops.clear();

                killer.getInventory().addItem(chest);
                
                if (killer.getEnderChest().firstEmpty() != -1) {
                    for (ItemStack item : chestInventory.getContents()) {
                        if (item != null) {
                            killer.getEnderChest().addItem(item);
                        }
                    }
                }
                
                killer.sendMessage(ChatColor.GREEN + "Mob drops have been stored in a chest in your inventory!");
            } else {
                killer.sendMessage(ChatColor.RED + "Your inventory is full! Drops will fall on the ground.");
            }
        }
    }

    private boolean hasSpace(Inventory inventory) {
        return inventory.firstEmpty() != -1;
    }
} 