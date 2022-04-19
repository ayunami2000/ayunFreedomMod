package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified){
            event.setCancelled(true);
        }
    }
}
