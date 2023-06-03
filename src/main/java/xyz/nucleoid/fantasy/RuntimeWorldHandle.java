package xyz.nucleoid.fantasy;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("unused")
public final class RuntimeWorldHandle {
    private final Fantasy fantasy;
    private final ServerWorld world;

    RuntimeWorldHandle(Fantasy fantasy, ServerWorld world) {
        this.fantasy = fantasy;
        this.world = world;
    }

    public void setTickWhenEmpty(boolean tickWhenEmpty) {
        ((FantasyWorldAccess) this.world).fantasy$setTickWhenEmpty(tickWhenEmpty);
    }

    public void delete() {
        this.fantasy.enqueueWorldDeletion(this.world);
    }

    public void unload() {
        if (this.world instanceof RuntimeWorld) {
            RuntimeWorld runtimeWorld = (RuntimeWorld) this.world;
            if (runtimeWorld.style == RuntimeWorld.Style.TEMPORARY) {
                this.fantasy.enqueueWorldDeletion(this.world);
            }
        } else {
            this.fantasy.enqueueWorldUnloading(this.world);
        }
    }

    public ServerWorld asWorld() {
        return this.world;
    }

    public RegistryKey<World> getRegistryKey() {
        return this.world.dimension();
    }
}
