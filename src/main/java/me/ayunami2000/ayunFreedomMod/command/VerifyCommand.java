package me.ayunami2000.ayunFreedomMod.command;

import me.ayunami2000.ayunFreedomMod.Main;
import me.ayunami2000.ayunFreedomMod.admin.Admin;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VerifyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Admin admin = Admin.getAdmin(player.getName());
            if (admin == null) {
                sender.sendMessage("Error: You are not an admin!");
                return true;
            }
            if (admin.verified){
                sender.sendMessage("Error: You are already verified!");
                return true;
            }
            if (args.length == 0){
                if (Main.discordApi == null){
                    sender.sendMessage("Error: Discord is not setup! You must manually verify from console.");
                }else{
                    if (Admin.verifyCodes.containsKey(admin)){
                        sender.sendMessage("Error: You have already generated a code!");
                    }else{
                        String code = RandomStringUtils.randomAlphanumeric(10);
                        Admin.verifyCodes.put(admin, code);
                        sender.sendMessage("A message has been sent to your discord with the verify command to proceed.");
                        admin.discordUser.sendMessage("Please run `/verify " + code + "` in-game to verify!");
                    }
                }
            }else{
                if (Admin.verifyCodes.containsKey(admin)){
                    if (Admin.verifyCodes.get(admin).equals(args[0])){
                        admin.verifyAdmin();
                        sender.sendMessage("You are now verified!");
                    }else{
                        Admin.verifyCodes.remove(admin);
                        player.kickPlayer("Verification failed!");
                    }
                }else{
                    sender.sendMessage("Error: You have not yet generated a verify code. Please run /verify!");
                }
            }
        }else{
            if (args.length == 0){
                sender.sendMessage("Error: You must specify the username of the player to manually verify!");
            }else{
                Admin admin = Admin.getAdmin(args[0]);
                if (admin == null) {
                    sender.sendMessage("Error: That player is not an admin!");
                    return true;
                }
                if (admin.verified){
                    sender.sendMessage("Error: That player is already verified!");
                    return true;
                }
                admin.verifyAdmin();
                sender.sendMessage("Manually verifying " + admin.username);
            }
        }
        return true;
    }
}
