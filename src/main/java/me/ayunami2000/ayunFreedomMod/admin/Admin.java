package me.ayunami2000.ayunFreedomMod.admin;

import me.ayunami2000.ayunFreedomMod.Main;
import org.javacord.api.entity.user.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Admin {
    public static final Set<Admin> admins = new HashSet<>();

    public final String username;
    public final User discordUser;
    public boolean verified = false;

    public Admin(String u, String d) throws ExecutionException, InterruptedException {
        username = u;
        discordUser = Main.discordApi == null ? null : Main.discordApi.getUserById(d).get();
        admins.add(this);
    }

    public static boolean isAdmin(String u){
        return getAdmin(u) != null;
    }

    public static Admin getAdmin(String u){
        return admins.stream().filter(a -> a.username.equalsIgnoreCase(u)).findFirst().orElse(null);
    }
}