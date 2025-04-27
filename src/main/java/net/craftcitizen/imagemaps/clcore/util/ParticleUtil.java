package net.craftcitizen.imagemaps.clcore.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class ParticleUtil {
    private ParticleUtil() {
    }

    public static void spawnParticleSingleBeam(final Location startingLocation) {
        ParticleUtil.spawnSingleParticleBeam(startingLocation, Color.WHITE);
    }

    public static void spawnSingleParticleBeam(final Location startingLocation, final Color color) {
        final Location center = startingLocation.clone();
        final Particle.DustOptions particle = new Particle.DustOptions(color, 1.0f);
        center.setX(center.getX() + 0.5);
        center.setZ(center.getZ() + 0.5);
        for (double i = startingLocation.getY(); i < startingLocation.getY() + 1.0; i += 0.05) {
            center.setY(i);
            center.getWorld().spawnParticle(Particle.DUST, center, 1, (Object) particle);
        }
    }

    public static void spawnParticleCircle(final Location startingLocation) {
        ParticleUtil.spawnParticleCircle(startingLocation, 1);
    }

    public static void spawnParticleCircle(final Location startingLocation, final int radius) {
        ParticleUtil.spawnParticleCircle(startingLocation, radius, Color.WHITE);
    }

    public static void spawnParticleCircle(final Location startingLocation, final int radius, final Color color) {
        final World world = startingLocation.getWorld();
        final Particle.DustOptions particle = new Particle.DustOptions(color, 1.0f);
        final double increment = 0.041887902047863905;
        for (int i = 0; i < 150; ++i) {
            final double angle = i * increment;
            final double x = startingLocation.getX() + 0.5 + radius * Math.cos(angle);
            final double z = startingLocation.getZ() + 0.5 + radius * Math.sin(angle);
            world.spawnParticle(Particle.DUST, new Location(world, x, startingLocation.getY(), z), 1,
                    (Object) particle);
        }
    }

    public static void spawnParticleRect(final Location start, final Location end) {
        ParticleUtil.spawnParticleRect(start, end, Color.WHITE);
    }

    public static void spawnParticleRect(final Location pos1, final Location pos2, final Color color) {
        if (pos1.getWorld() != pos2.getWorld()) {
            return;
        }
        final World w = pos1.getWorld();
        final Particle.DustOptions particle = new Particle.DustOptions(color, 1.0f);
        final BoundingBox bb = new BoundingBox(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(),
                pos2.getZ());
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMinZ()),
                new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMinZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMinZ()),
                new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMinZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMaxZ()),
                new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMaxZ()),
                new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMinZ()),
                new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMinZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMinZ()),
                new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMinZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMaxZ()),
                new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMaxZ()),
                new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMinZ()),
                new Location(w, bb.getMinX(), bb.getMinY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMinZ()),
                new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMinZ()),
                new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMaxZ()), particle);
        ParticleUtil.spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMinZ()),
                new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()), particle);
    }

    private static void spawnParticleLine(final Location start, final Location end,
            final Particle.DustOptions particle) {
        final double step = 0.1;
        final Vector direction = end.toVector().subtract(start.toVector());
        final double numParticles = direction.length() / step;
        direction.multiply(1.0 / numParticles);
        final Location current = start.clone();
        for (int i = 0; i < numParticles; ++i) {
            current.getWorld().spawnParticle(Particle.DUST, current, 1, (Object) particle);
            current.add(direction);
        }
    }
}
