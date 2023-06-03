package xyz.nucleoid.fantasy;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat;
import org.apache.commons.io.FileUtils;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;

import java.io.File;
import java.io.IOException;

final class RuntimeWorldManager {
    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;

    RuntimeWorldManager(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;
    }

    RuntimeWorld add(RegistryKey<World> worldKey, RuntimeWorldConfig config, RuntimeWorld.Style style) {
        getDimensionsRegistry(this.server).register(RegistryKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location()), config.createDimensionOptions(this.server), Lifecycle.stable());

        RuntimeWorld world = new RuntimeWorld(this.server, worldKey, config, style);

        this.serverAccess.getWorlds().put(world.dimension(), world);
        ServerWorldEvents.LOAD.invoker().onWorldLoad(this.server, world);

        // tick the world to ensure it is ready for use right away
        world.tick(() -> true);

        return world;
    }

    void delete(ServerWorld world) {
        RegistryKey<World> dimensionKey = world.dimension();

        if (this.serverAccess.getWorlds().remove(dimensionKey, world)) {
            ServerWorldEvents.UNLOAD.invoker().onWorldUnload(this.server, world);

            SimpleRegistry<Dimension> dimensionsRegistry = getDimensionsRegistry(this.server);
            RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.getRegistryName());

            SaveFormat.LevelSave session = this.serverAccess.getSession();
            File worldDirectory = session.getDimensionPath(dimensionKey);
            if (worldDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(worldDirectory);
                } catch (IOException e) {
                    Fantasy.LOGGER.warn("Failed to delete world directory", e);
                    try {
                        FileUtils.forceDeleteOnExit(worldDirectory);
                    } catch (IOException ignored) {}
                }
            }
        }
    }


    @SuppressWarnings("NullableProblems")
    void unload(ServerWorld world) {
        RegistryKey<World> dimensionKey = world.dimension();

        if (this.serverAccess.getWorlds().remove(dimensionKey, world)) {
            world.save(new IProgressUpdate() {
                @Override public void progressStartNoAbort(ITextComponent pComponent) {}
                @Override public void progressStart(ITextComponent pComponent) {}
                @Override public void progressStage(ITextComponent pComponent) {}
                @Override public void progressStagePercentage(int pProgress) {}

                @Override
                public void stop() {
                    ServerWorldEvents.UNLOAD.invoker().onWorldUnload(RuntimeWorldManager.this.server, world);

                    SimpleRegistry<Dimension> dimensionsRegistry = getDimensionsRegistry(RuntimeWorldManager.this.server);
                    RemoveFromRegistry.remove(dimensionsRegistry, dimensionKey.location());
                }
            }, true, false);
        }
    }

    private static SimpleRegistry<Dimension> getDimensionsRegistry(MinecraftServer server) {
        DimensionGeneratorSettings generatorOptions = server.getWorldData().worldGenSettings();
        return generatorOptions.dimensions();
    }
}
