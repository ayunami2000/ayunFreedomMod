package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.Main;
import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandEvent implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified && !event.getMessage().startsWith("/verify")) {
            event.getPlayer().sendMessage("You must verify before using commands!");
            event.setCancelled(true);
            return;
        }
        if (Main.commandBlocker.isBlocked(event.getMessage().substring(1).trim().replaceAll(" {2,}", " "), admin != null)){
            event.setCancelled(true);
        }
    }
}
