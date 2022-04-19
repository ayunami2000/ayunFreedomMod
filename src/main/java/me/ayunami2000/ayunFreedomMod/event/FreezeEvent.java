package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeEvent implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){
        Admin admin = Admin.getAdmin(event.getWhoClicked().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified) event.setCancelled(true);
    }
}
