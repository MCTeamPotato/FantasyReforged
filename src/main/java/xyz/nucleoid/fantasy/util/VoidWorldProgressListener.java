package xyz.nucleoid.fantasy.util;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NullableProblems")
public final class VoidWorldProgressListener implements IChunkStatusListener {
    public static final VoidWorldProgressListener INSTANCE = new VoidWorldProgressListener();
    private VoidWorldProgressListener() {}
    @Override
    public void updateSpawnPos(ChunkPos pCenter) {}
    @Override public void onStatusChange(ChunkPos pChunkPosition, @Nullable ChunkStatus pNewStatus) {}
    @Override public void stop() {}
}
