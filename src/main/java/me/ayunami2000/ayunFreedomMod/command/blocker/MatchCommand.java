package me.ayunami2000.ayunFreedomMod.command.blocker;

public class MatchCommand extends BlockedCommand {
    public final String match;

    public MatchCommand(String m, boolean cau) {
        super(cau);
        match = m;
    }
}
