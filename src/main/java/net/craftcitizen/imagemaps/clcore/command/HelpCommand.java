package net.craftcitizen.imagemaps.clcore.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class HelpCommand extends SubCommand {
    private final Map<String, SubCommand> commands;

    public HelpCommand(final String permission, final Plugin plugin, final Map<String, SubCommand> map) {
        super(permission, plugin, true);
        this.commands = map;
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player && !sender.hasPermission(this.getPermission())) {
            return "Unknown command. Type \"/help\" for help.";
        }
        if (args.length >= 2 && this.commands.containsKey(args[1])) {
            this.commands.get(args[1]).help(sender);
        } else {
            this.help(sender);
        }
        return null;
    }

    @Override
    protected List<String> onTabComplete(final CommandSender sender, final String[] args) {
        switch (args.length) {
            case 2: {
                return this.commands.keySet().stream().filter(a -> a.startsWith(args[1])).collect(Collectors.toList());
            }
            default: {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public abstract void help(final CommandSender p0);
}
