package net.craftcitizen.imagemaps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.craftcitizen.imagemaps.clcore.LambdaRunnable;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;

public class ImageMapDownloadCommand extends ImageMapSubCommand {

    public ImageMapDownloadCommand(final ImageMaps plugin) {
        super("imagemaps.download", plugin, true);
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!this.checkSender(sender)) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "You can't run this command.");
            return null;
        }

        if (args.length < 3) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                    "You must specify a file name and a download link.");
            return null;
        }

        final String filename = args[1];
        final String url = args[2];

        if (filename.contains("/") || filename.contains("\\") || filename.contains(":")) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                    "Filename contains illegal character.");
            return null;
        }

        new LambdaRunnable(() -> this.download(sender, url, filename)).runTaskAsynchronously(this.plugin);
        return null;
    }

    private void download(final CommandSender sender, final String input, final String filename) {
        try {
            final URL srcURL = new URI(input).toURL();

            if (!srcURL.getProtocol().startsWith("http")) {
                MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "Download URL is not valid.");
                return;
            }

            final URLConnection connection = srcURL.openConnection();

            if (!(connection instanceof HttpURLConnection)) {
                MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "Download URL is not valid.");
                return;
            }

            connection.setRequestProperty("User-Agent", "ImageMaps/0");

            if (((HttpURLConnection) connection).getResponseCode() != 200) {
                MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                        String.format("Download failed, HTTP Error code %d.",
                                ((HttpURLConnection) connection).getResponseCode()));
                return;
            }

            final String mimeType = connection.getHeaderField("Content-type");
            if (!(mimeType.startsWith("image/"))) {
                MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                        String.format("Download is a %s file, not image.", mimeType));
                return;
            }

            try (InputStream str = connection.getInputStream()) {
                final BufferedImage image = ImageIO.read(str);
                if (image == null) {
                    MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                            "Downloaded file is not an image!");
                    return;
                }

                final File outFile = new File(this.plugin.getDataFolder(), "images" + File.separatorChar + filename);
                final boolean fileExisted = outFile.exists();
                ImageIO.write(image, "PNG", outFile);

                if (fileExisted) {
                    MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                            "File already exists, overwriting!");
                    this.getPlugin().reloadImage(filename);
                }
            } catch (final IllegalArgumentException ex) {
                MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "Received no data");
                return;
            }
        } catch (final MalformedURLException | URISyntaxException ex) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "Malformatted URL");
        } catch (final IOException ex) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.ERROR,
                    "An IO Exception happened, see server log");
            ex.printStackTrace();
        }
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "Downloads an image from an URL.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO,
                "Usage: /imagemap download <filename> <sourceURL>");
    }
}
