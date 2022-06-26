package dev.jpb.autoreplace;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

import java.util.Arrays;
import java.util.HashSet;

public class EventListener implements Listener {
    private final AutoReplace autoReplace;

    public EventListener(AutoReplace autoReplace) {
        this.autoReplace = autoReplace;
    }

    private final Material[] TOOLS = {
            Material.WOODEN_SWORD,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_AXE,
            Material.WOODEN_SHOVEL,
            Material.WOODEN_HOE,
            Material.STONE_SWORD,
            Material.STONE_PICKAXE,
            Material.STONE_AXE,
            Material.STONE_SHOVEL,
            Material.STONE_HOE,
            Material.IRON_SWORD,
            Material.IRON_PICKAXE,
            Material.IRON_AXE,
            Material.IRON_SHOVEL,
            Material.IRON_HOE,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_AXE,
            Material.DIAMOND_SHOVEL,
            Material.DIAMOND_HOE,
            Material.NETHERITE_SWORD,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_AXE,
            Material.NETHERITE_SHOVEL,
            Material.NETHERITE_HOE,
    };

    @EventHandler (priority = EventPriority.MONITOR)
    public void onItemBreak(PlayerItemBreakEvent event) {
        if (Arrays.stream(TOOLS).noneMatch(tool -> event.getBrokenItem().getType().equals(tool))) {
            return;
        }

        PlayerInventory inventory = event.getPlayer().getInventory();
        int heldItemSlot = inventory.getHeldItemSlot();
        Material brokenItemType = event.getBrokenItem().getType();

        autoReplace.getServer().getScheduler().scheduleSyncDelayedTask(autoReplace, () -> {
            // They've got something in their hand somehow...
            if (inventory.getItem(heldItemSlot) != null) {
                return;
            }

            HashSet<InventoryItem> matchedItems = new HashSet<>();

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                ItemStack item = inventory.getItem(slot);
                if (item == null) {
                    continue;
                }

                if (item.getType().equals(brokenItemType)) {
                    matchedItems.add(new InventoryItem(item, slot));
                }
            }

            InventoryItem chosen = null;

            for (InventoryItem inventoryItem : matchedItems) {
                if (chosen == null) {
                    chosen = inventoryItem;
                }

                int itemDamage = ((Damageable) inventoryItem.getItemStack().getItemMeta()).getDamage();
                int chosenDamage = ((Damageable) chosen.getItemStack().getItemMeta()).getDamage();

                if (itemDamage > chosenDamage) {
                    chosen = inventoryItem;
                }
            }

            // No replacement candidate found.
            if (chosen == null) {
                return;
            }

            inventory.setItem(heldItemSlot, chosen.getItemStack());
            inventory.setItem(chosen.getSlot(), null);
        });
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        PlayerInventory inventory = event.getPlayer().getInventory();
        int heldItemSlot = inventory.getHeldItemSlot();
        Material blockItemType = event.getItemInHand().getType();

        autoReplace.getServer().getScheduler().scheduleSyncDelayedTask(autoReplace, () -> {
            // They've got something in their hand, don't do anything.
            if (inventory.getItem(heldItemSlot) != null) {
                return;
            }

            HashSet<InventoryItem> matchedItems = new HashSet<>();

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                ItemStack item = inventory.getItem(slot);
                if (item == null) {
                    continue;
                }

                if (item.getType().equals(blockItemType)) {
                    matchedItems.add(new InventoryItem(item, slot));
                }
            }

            InventoryItem chosen = null;

            for (InventoryItem inventoryItem : matchedItems) {
                if (chosen == null) {
                    chosen = inventoryItem;
                    continue;
                }

                if (inventoryItem.getItemStack().getAmount() < chosen.getItemStack().getAmount()) {
                    chosen = inventoryItem;
                }
            }

            // No replacement candidate found.
            if (chosen == null) {
                return;
            }

            inventory.setItem(heldItemSlot, chosen.getItemStack());
            inventory.setItem(chosen.getSlot(), null);
        });
    }
}
