package net.craftcitizen.imagemaps.clcore.command;

import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class SubCommand {
    private String permission;
    protected Plugin plugin;
    private final boolean console;

    public SubCommand(final String permission, final Plugin plugin, final boolean console) {
        this.permission = "";
        this.permission = permission;
        this.plugin = plugin;
        this.console = console;
    }

    public boolean checkSender(final CommandSender sender) {
        return (!(sender instanceof Player) && this.isConsoleCommand()) || this.getPermission().equals("")
                || sender.hasPermission(this.getPermission());
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public boolean isConsoleCommand() {
        return this.console;
    }

    public String getPermission() {
        return this.permission;
    }

    protected abstract String execute(final CommandSender p0, final Command p1, final String p2, final String[] p3);

    protected List<String> onTabComplete(final CommandSender sender, final String[] args) {
        return Collections.emptyList();
    }

    public abstract void help(final CommandSender p0);
}
