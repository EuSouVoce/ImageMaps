package net.craftcitizen.imagemaps;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import net.craftcitizen.imagemaps.clcore.LambdaRunnable;

public class ImageMapRenderer extends MapRenderer {
    private final ImageMaps plugin;

    private BufferedImage image = null;
    private boolean first = true;

    private final int x;
    private final int y;
    private final double scale;

    public ImageMapRenderer(final ImageMaps plugin, final BufferedImage image, final int x, final int y,
            final double scale) {
        this.plugin = plugin;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.recalculateInput(image);
    }

    public void recalculateInput(final BufferedImage input) {
        if (this.x * ImageMaps.MAP_WIDTH > Math.round(input.getWidth() * this.scale)
                || this.y * ImageMaps.MAP_HEIGHT > Math.round(input.getHeight() * this.scale))
            return;

        final int x1 = (int) Math.floor(this.x * ImageMaps.MAP_WIDTH / this.scale);
        final int y1 = (int) Math.floor(this.y * ImageMaps.MAP_HEIGHT / this.scale);

        final int x2 = (int) Math.ceil(Math.min(input.getWidth(), ((this.x + 1) * ImageMaps.MAP_WIDTH / this.scale)));
        final int y2 = (int) Math.ceil(Math.min(input.getHeight(), ((this.y + 1) * ImageMaps.MAP_HEIGHT / this.scale)));

        if (x2 - x1 <= 0 || y2 - y1 <= 0)
            return;

        this.image = input.getSubimage(x1, y1, x2 - x1, y2 - y1);

        if (this.scale != 1D) {
            final BufferedImage resized = new BufferedImage(ImageMaps.MAP_WIDTH, ImageMaps.MAP_HEIGHT,
                    input.getType() == 0 ? this.image.getType() : input.getType());
            final AffineTransform at = new AffineTransform();
            at.scale(this.scale, this.scale);
            final AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            this.image = scaleOp.filter(this.image, resized);
        }

        this.first = true;
    }

    @Override
    public void render(final MapView view, final MapCanvas canvas, final Player player) {
        if (this.image != null && this.first) {
            new LambdaRunnable(() -> canvas.drawImage(0, 0, this.image)).runTaskLater(this.plugin,
                    System.nanoTime() % 60);
            // spread out pseudo randomly in a very naive way
            this.first = false;
        }
    }

}
