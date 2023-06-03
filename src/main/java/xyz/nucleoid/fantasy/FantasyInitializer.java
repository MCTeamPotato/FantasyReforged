package xyz.nucleoid.fantasy;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.Mod;
import xyz.nucleoid.fantasy.util.VoidChunkGenerator;

@Mod(Fantasy.ID)
public class FantasyInitializer {
    public FantasyInitializer() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Fantasy.ID, "void"), VoidChunkGenerator.CODEC);
    }
}