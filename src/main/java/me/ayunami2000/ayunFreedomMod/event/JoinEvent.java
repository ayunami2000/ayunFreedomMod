package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.Main;
import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            player.sendMessage("Welcome! You are now OP!");
            player.setOp(true); // might change in the future bc ppl can just change usernames to bypass stuff...
        }
        String uname = player.getName();
        if (Main.chatChannel != null) Main.chatChannel.sendMessage("**" + uname + " joined the server**");
        Admin admin = Admin.getAdmin(uname);
        if (admin != null){
            admin.verified = false;
            Bukkit.broadcastMessage(uname + " is an Admin! Please wait for them to verify before trusting them!");
            if (Main.chatChannel != null) Main.chatChannel.sendMessage("**" + uname + " is an admin and must verify!**");
            player.sendMessage("You must verify before proceeding! Please run " + (Main.discordApi == null ? "\"/verify " + uname + "\" in console" : "ayun!verify in discord server chat or /verify in game") + "!");
        }
    }
}
