package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.Main;
import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandEvent implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        Admin admin = Admin.getAdmin(player.getName());
        if (admin != null && !admin.verified && !event.getMessage().startsWith("/verify")) {
            player.sendMessage("You must verify before using commands!");
            event.setCancelled(true);
            return;
        }
        if (Main.commandBlocker.isBlocked(event.getMessage().substring(1).trim().replaceAll(" {2,}", " "), admin != null)){
            event.setCancelled(true);
            player.sendMessage("That command is blocked!");
        }
    }
}
