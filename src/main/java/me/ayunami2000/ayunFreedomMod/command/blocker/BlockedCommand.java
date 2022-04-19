package me.ayunami2000.ayunFreedomMod.command.blocker;

public abstract class BlockedCommand {
    public final boolean canAdminUse;

    public BlockedCommand(boolean cau){
        canAdminUse = cau;
    }
}
