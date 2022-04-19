package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.Main;
import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandEvent implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if (!event.getPlayer().isOp() && !event.getMessage().equalsIgnoreCase("/opme")) { // opme does not exist yet lol, for now op players on FIRST join and admins can deop for bad behavior
            event.setCancelled(true);
            return;
        }
        Admin admin = Admin.getAdmin(event.getPlayer().getName());
        if (admin != null && !admin.verified && event.getMessage().startsWith("/verify")) {
            event.setCancelled(true);
            return;
        }
        if (Main.commandBlocker.isBlocked(event.getMessage().substring(1).trim().replaceAll(" {2,}", " "), admin != null)){
            event.setCancelled(true);
        }
    }
}
