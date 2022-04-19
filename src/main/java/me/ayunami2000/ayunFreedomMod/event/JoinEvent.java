package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinEvent implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        if (!p.hasPlayedBefore()) {
            p.sendMessage("Welcome! You are now OP!");
            p.setOp(true); // might change in the future bc ppl can just change usernames to bypass stuff...
        }
        Admin admin = Admin.getAdmin(p.getName());
        if (admin != null){
            admin.verified = false;
            Bukkit.broadcastMessage(event.getPlayer().getName() + " is an Admin! Please wait for them to verify before trusting them!");
            event.getPlayer().sendMessage("You must verify before proceeding! Please run /verify and check discord!");
        }
    }
}
