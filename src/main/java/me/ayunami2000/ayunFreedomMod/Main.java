package me.ayunami2000.ayunFreedomMod;

import me.ayunami2000.ayunFreedomMod.admin.Admin;
import me.ayunami2000.ayunFreedomMod.command.VerifyCommand;
import me.ayunami2000.ayunFreedomMod.command.blocker.Blocker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Main INSTANCE;

    public static Logger LOGGER;

    public static Blocker commandBlocker;

    public static DiscordApi discordApi = null;

    @Override
    public void onLoad(){
        INSTANCE = this;
        LOGGER = this.getLogger();
        commandBlocker = new Blocker(this.getConfig().getStringList("blockedCommands"));
        try {
            discordApi = new DiscordApiBuilder().setToken(this.getConfig().getString("discordToken")).login().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (String admin : this.getConfig().getStringList("admins")) {
            String[] adminPieces = admin.split(":", 2);
            if (adminPieces.length != 2){
                LOGGER.info("The admin entry \"" + admin + "\" is invalid!");
                continue;
            }
            try {
                new Admin(adminPieces[0], adminPieces[1]);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        this.getCommand("verify").setExecutor(new VerifyCommand());
    }

    @Override
    public void onDisable(){

    }

    public static CommandMap getCommandMap() {
        Object commandMap = getField(Bukkit.getServer().getPluginManager(), "commandMap");
        if (commandMap instanceof CommandMap) return (CommandMap) commandMap;
        return null;
    }

    public static <T> T getField(Object from, String name) {
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        while (checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));
        return null;
    }
}
