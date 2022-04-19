package me.ayunami2000.ayunFreedomMod.event;

import me.ayunami2000.ayunFreedomMod.Main;
import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        String uname = event.getPlayer().getName();
        Admin admin = Admin.getAdmin(uname);
        if (admin != null && !admin.verified) event.setFormat("§c ⓤ§r <%s> %s");
        if (Main.chatChannel != null) Main.chatChannel.sendMessage("<" + uname + "> " + event.getMessage().replace("\\", "\\\\").replace("@", "\\@").replace("`", "\\`"));
    }
}
