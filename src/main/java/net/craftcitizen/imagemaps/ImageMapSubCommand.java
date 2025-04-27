package net.craftcitizen.imagemaps;

import net.craftcitizen.imagemaps.clcore.command.SubCommand;

public abstract class ImageMapSubCommand extends SubCommand {

    public ImageMapSubCommand(final String permission, final ImageMaps plugin, final boolean console) {
        super(permission, plugin, console);
    }

    @Override
    public ImageMaps getPlugin() {
        return (ImageMaps) super.getPlugin();
    }
}
