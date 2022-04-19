package me.ayunami2000.ayunFreedomMod.command.blocker;

import me.ayunami2000.ayunFreedomMod.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Blocker {
    private final Set<BlockedCommand> blockedCommands = new HashSet<>();

    public Blocker(List<String> blockedCommandsRaw){
        CommandMap commandMap = Main.getCommandMap();

        if (commandMap == null) Main.LOGGER.info("Unable to fetch command map! Some aliases may not be blocked!");

        for (String blockedCommandRaw : blockedCommandsRaw) {
            String[] pieces = blockedCommandRaw.split(":", 3);
            if (pieces.length != 3) continue;
            boolean isRegex;
            if (pieces[0].equalsIgnoreCase("r") || pieces[0].equalsIgnoreCase("regex")){
                isRegex = true;
            }else if (pieces[0].equalsIgnoreCase("m") || pieces[0].equalsIgnoreCase("match")){
                isRegex = false;
            }else{
                skippedInvalid(blockedCommandRaw, "\"" + pieces[0] + "\" is not a valid option!");
                continue;
            }
            boolean canAdminUse;
            if (pieces[1].equalsIgnoreCase("y") || pieces[1].equalsIgnoreCase("yes") || pieces[1].equalsIgnoreCase("t") || pieces[1].equalsIgnoreCase("true")){
                canAdminUse = true;
            }else if (pieces[1].equalsIgnoreCase("n") || pieces[1].equalsIgnoreCase("no") || pieces[1].equalsIgnoreCase("f") || pieces[1].equalsIgnoreCase("false")){
                canAdminUse = false;
            }else{
                skippedInvalid(blockedCommandRaw, "\"" + pieces[1] + "\" is not a valid option!");
                continue;
            }
            if (isRegex){
                blockedCommands.add(new RegexCommand(Pattern.compile(pieces[2], Pattern.CASE_INSENSITIVE), canAdminUse));
            }else{
                if (commandMap == null){
                    blockedCommands.add(new MatchCommand(pieces[2], canAdminUse));
                }else{
                    String[] commandParts = pieces[2].split(" ", 2);

                    Command command = commandMap.getCommand(commandParts[0]);
                    if (command == null){
                        Main.LOGGER.info("Warning: The command \"" + commandParts[0] + "\" was not found on the server!");
                        blockedCommands.add(new MatchCommand(pieces[2], canAdminUse));
                        continue;
                    }
                    String commandArgs = commandParts.length == 2 ? " " + commandParts[1] : "";

                    blockedCommands.add(new MatchCommand(command.getName() + commandArgs, canAdminUse));
                    for (String commandAlias : command.getAliases()) {
                        blockedCommands.add(new MatchCommand(commandAlias + commandArgs, canAdminUse));
                    }
                }
            }
        }
    }

    private void skippedInvalid(String cmd, String reason){
        Main.LOGGER.info("The command \"" + cmd + "\" has been ignored as it is invalid! (Reason: " + reason + ")");
    }

    public boolean isBlocked(String command, boolean isAdmin){
        boolean blocked = false;
        for (BlockedCommand blockedCommand : blockedCommands) {
            if (isAdmin && blockedCommand.canAdminUse) continue;
            if (blockedCommand instanceof RegexCommand){
                RegexCommand regexCommand = (RegexCommand) blockedCommand;
                blocked = regexCommand.match.matcher(command).lookingAt();
            }else if (blockedCommand instanceof MatchCommand){
                MatchCommand matchCommand = (MatchCommand) blockedCommand;
                blocked = command.equalsIgnoreCase(matchCommand.match) || command.toLowerCase().startsWith(matchCommand.match.toLowerCase() + " ");
            }

            if (blocked) return true;
        }
        return false;
    }
}
