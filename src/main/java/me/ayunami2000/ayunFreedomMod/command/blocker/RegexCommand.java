package me.ayunami2000.ayunFreedomMod.command.blocker;

import java.util.regex.Pattern;

public class RegexCommand extends BlockedCommand {
    public final Pattern match;

    public RegexCommand(Pattern m, boolean cau) {
        super(cau);
        match = m;
    }
}
