package xyz.nucleoid.fantasy.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccess {
    @Accessor("levels")
    Map<RegistryKey<World>, ServerWorld> getWorlds();

    @Accessor("storageSource")
    SaveFormat.LevelSave getSession();
}
