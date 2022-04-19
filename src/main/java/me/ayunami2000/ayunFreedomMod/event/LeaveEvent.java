package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null) {
            admin.verified = false;
            Admin.verifyCodes.remove(admin);
        }
    }
}
