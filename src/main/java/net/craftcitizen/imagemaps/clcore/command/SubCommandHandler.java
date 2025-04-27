package net.craftcitizen.imagemaps.clcore.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class SubCommandHandler extends SubCommand {
    private final Map<String, SubCommand> commands;
    private final int depth;

    public SubCommandHandler(final String permission, final Plugin plugin, final boolean console, final int depth) {
        super(permission, plugin, console);
        this.commands = new HashMap<String, SubCommand>();
        Validate.isTrue(depth >= 1, "SubCommandHandler depth can't be smaller than 0!");
        this.depth = depth;
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length > this.getDepth() && this.commands.containsKey(args[this.getDepth()])) {
            return this.commands.get(args[this.getDepth()]).execute(sender, cmd, label, args);
        }
        if (this.commands.containsKey("help")) {
            return this.commands.get("help").execute(sender, cmd, label, args);
        }
        this.help(sender);
        return null;
    }

    protected int getDepth() {
        return this.depth;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final String[] args) {
        switch (args.length - this.depth) {
            case 0: {
                return Collections.emptyList();
            }
            case 1: {
                return this.commands.keySet().stream().filter(a -> a.startsWith(args[args.length - 1]))
                        .filter(a -> sender.hasPermission(this.commands.get(a).getPermission()))
                        .collect(Collectors.toList());
            }
            default: {
                if (!this.commands.containsKey(args[this.depth])) {
                    return Collections.emptyList();
                }
                return this.commands.get(args[this.depth]).onTabComplete(sender, args);
            }
        }
    }

    @Override
    public abstract void help(final CommandSender p0);

    public void registerSubCommand(final String name, final SubCommand command, final String... alias) {
        Validate.notNull((Object) command, "SubCommand can't be null!");
        Validate.notEmpty(name, "SubCommandname can't be empty!");
        Validate.isTrue(!this.commands.containsKey(name), "Command " + name + " is already defined");
        for (final String a : alias) {
            Validate.isTrue(!this.commands.containsKey(a), "Command " + a + " is already defined");
        }
        if (command instanceof SubCommandHandler) {
            Validate.isTrue(((SubCommandHandler) command).getDepth() == this.getDepth() + 1,
                    "The depth of a SubCommandHandler must be the depth of the previous Handler + 1!");
        }
        this.commands.put(name, command);
        for (final String s : alias) {
            this.commands.put(s, command);
        }
    }

    protected Map<String, SubCommand> getCommands() {
        return this.commands;
    }
}
