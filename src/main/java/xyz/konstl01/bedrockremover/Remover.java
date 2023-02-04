package xyz.konstl01.bedrockremover;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Remover implements Runnable {
    private final World world = BedrockRemover.world;
    private final int minHeight = -1;
    private final int maxHeight = 10;

    @Override
    public void run() {
        int chunksToProcess = world.getChunkCount();
        for (int i = 0; i < chunksToProcess; i++) {
            Chunk chunk = world.getChunkAt(i, 0);
            if (!chunk.isLoaded()) {
                continue;
            }
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = minHeight; y < maxHeight; y++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block.getType() == Material.BEDROCK) {
                            block.setType(Material.DEEPSLATE);
                            BedrockRemover.count++;
                        }
                    }
                }
            }
        }
    }
}