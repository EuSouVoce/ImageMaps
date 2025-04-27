package net.craftcitizen.imagemaps.clcore;

import org.bukkit.scheduler.BukkitRunnable;

public class LambdaRunnable extends BukkitRunnable {
    private final Runnable function;

    public LambdaRunnable(final Runnable function) {
        this.function = function;
    }

    @Override
    public void run() {
        this.function.run();
    }
}
