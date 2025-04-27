package net.craftcitizen.imagemaps;

import net.craftcitizen.imagemaps.clcore.command.CommandHandler;

public class ImageMapCommandHandler extends CommandHandler {
    public ImageMapCommandHandler(final ImageMaps plugin) {
        super(plugin);
        this.registerSubCommand("download", new ImageMapDownloadCommand(plugin));
        this.registerSubCommand("delete", new ImageMapDeleteCommand(plugin));
        this.registerSubCommand("place", new ImageMapPlaceCommand(plugin));
        this.registerSubCommand("info", new ImageMapInfoCommand(plugin));
        this.registerSubCommand("list", new ImageMapListCommand(plugin));
        this.registerSubCommand("reload", new ImageMapReloadCommand(plugin));
        this.registerSubCommand("cleanup", new ImageMapCleanupCommand(plugin));
        this.registerSubCommand("debuginfo", new ImageMapDebugInfoCommand(plugin));
        this.registerSubCommand("help", new ImageMapHelpCommand(plugin, this.getCommands()), "?");
    }
}
