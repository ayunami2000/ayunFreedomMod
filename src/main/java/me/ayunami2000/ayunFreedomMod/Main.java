package me.ayunami2000.ayunFreedomMod;

import me.ayunami2000.ayunFreedomMod.admin.Admin;
import me.ayunami2000.ayunFreedomMod.command.VerifyCommand;
import me.ayunami2000.ayunFreedomMod.command.blocker.Blocker;
import me.ayunami2000.ayunFreedomMod.event.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageAuthor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static Main INSTANCE;

    public static Logger LOGGER;

    public static Blocker commandBlocker;

    public static DiscordApi discordApi = null;
    public static ServerTextChannel chatChannel = null;
    public static ServerTextChannel consoleChannel = null;

    @Override
    public void onLoad(){
        INSTANCE = this;
        LOGGER = this.getLogger();
        commandBlocker = new Blocker(this.getConfig().getStringList("blockedCommands"));
        boolean useDiscord = this.getConfig().getBoolean("discord.enabled");
        if (useDiscord) {
            try {
                discordApi = new DiscordApiBuilder().setToken(this.getConfig().getString("discord.token")).login().get();
                chatChannel = discordApi.getServerTextChannelById(this.getConfig().getString("discord.chatChannel")).orElse(null);
                if (chatChannel != null){
                    String discordPrefix = "§9 ⓓ§r ";
                    chatChannel.addMessageCreateListener(messageCreateEvent -> {
                        MessageAuthor messageAuthor = messageCreateEvent.getMessageAuthor();
                        if(!messageAuthor.isYourself()){
                            String messageContent = messageCreateEvent.getMessageContent();
                            Message message = messageCreateEvent.getMessage();
                            if (messageContent.toLowerCase().startsWith("ayun!l") || messageContent.toLowerCase().startsWith("ayun!who") || messageContent.toLowerCase().startsWith("ayun!online")) {
                                Player[] onlinePlayers = getServer().getOnlinePlayers();
                                StringBuilder playerList = new StringBuilder("**Online players** *(" + onlinePlayers.length + ")*");
                                for (Player player : onlinePlayers) playerList.append("\n> ").append(player.getName());
                                message.reply(playerList.toString());
                            }else if (messageContent.toLowerCase().startsWith("ayun!verify") || messageContent.toLowerCase().startsWith("ayun!v")){
                                Admin admin = Admin.admins.stream().filter(a -> a.discordUser.getId() == messageAuthor.getId()).findFirst().orElse(null);
                                if (admin == null) {
                                    message.reply("**You are not an admin!**");
                                }else if(admin.verified){
                                    message.reply("**You are already verified!**");
                                }else{
                                    admin.verifyAdmin();
                                    message.reply("**You are now verified!**");
                                }
                            }
                            StringBuilder res = new StringBuilder(discordPrefix);
                            res.append("<").append(messageAuthor.getDisplayName().replace("§", "")).append("> ");
                            res.append(messageContent.replace("§", "").replace("\n", "\n" + discordPrefix));
                            List<MessageAttachment> attachments = messageCreateEvent.getMessageAttachments();
                            for (MessageAttachment attachment : attachments) res.append(" §e§n").append(attachment.getUrl()).append("§r");
                            getServer().broadcastMessage(res.toString());
                        }
                    });
                    chatChannel.sendMessage("**Server has started**");
                }
                consoleChannel = discordApi.getServerTextChannelById(this.getConfig().getString("discord.consoleChannel")).orElse(null);
                if (consoleChannel != null) consoleChannel.addMessageCreateListener(messageCreateEvent -> {
                    MessageAuthor messageAuthor = messageCreateEvent.getMessageAuthor();
                    if(!messageAuthor.isYourself()){
                        String[] msgLines = messageCreateEvent.getMessageContent().split("\n");
                        for (String cmd : msgLines) getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
                        messageCreateEvent.deleteMessage();
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                discordApi = null;
                LOGGER.info("An error occurred with Discord. Using console-based verification!");
            }
        }else{
            LOGGER.info("Discord is disabled, using console-based verification!");
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
        this.getServer().getPluginManager().registerEvents(new CommandEvent(), this);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        this.getServer().getPluginManager().registerEvents(new FreezeEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ChatEvent(), this);
    }

    @Override
    public void onDisable(){
        if (chatChannel != null) chatChannel.sendMessage("**Server has stopped**");
        if (discordApi != null) discordApi.disconnect().join();
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
