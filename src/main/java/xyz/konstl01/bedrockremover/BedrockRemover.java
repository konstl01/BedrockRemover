package xyz.konstl01.bedrockremover;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockRemover extends JavaPlugin {
    private final World world;
    private BukkitRunnable task;
    private boolean running = true;
    private int count = 0;

    public BedrockRemover() {
        world = getServer().getWorld("ClymCityWorldNewNewNew");
    }

    @Override
    public void onEnable() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (getServer().getTPS()[0] < 17 && running) {
                    getServer().getConsoleSender().sendMessage("Server TPS is low, pausing BedrockRemover task");
                    running = false;
                    cancel();
                } else if (getServer().getTPS()[0] >= 17 && !running) {
                    getServer().getConsoleSender().sendMessage("Server TPS has recovered, resuming BedrockRemover task");
                    running = true;
                    runTaskTimer(BedrockRemover.this, 0, 20);
                }

                if (!running) {
                    return;
                }

                int minX = (int) (world.getWorldBorder().getCenter().getBlockX() - world.getWorldBorder().getSize() / 2);
                int maxX = (int) (world.getWorldBorder().getCenter().getBlockX() + world.getWorldBorder().getSize() / 2);
                int minZ = (int) (world.getWorldBorder().getCenter().getBlockZ() - world.getWorldBorder().getSize() / 2);
                int maxZ = (int) (world.getWorldBorder().getCenter().getBlockZ() + world.getWorldBorder().getSize() / 2);

                for (Chunk chunk : world.getLoadedChunks()) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 10; y > -2; y--) {
                                Block block = chunk.getBlock(x, y, z);

                                if (block.getX() < minX || block.getX() > maxX ||
                                        block.getZ() < minZ || block.getZ() > maxZ) {
                                    continue;
                                }

                                if (block.getType() == Material.BEDROCK) {
                                    block.setType(Material.DEEPSLATE);
                                    count++;
                                }
                            }
                        }
                    }
                }

                if (count >= 10) {
                    getServer().getConsoleSender().sendMessage("Replaced " + count + " blocks of Bedrock.");
                    count = 0;
                }
            }
        };
        task.runTaskTimer(this, 0, 200);
    }
}