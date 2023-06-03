package xyz.nucleoid.fantasy.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("NullableProblems")
public class VoidChunkGenerator extends ChunkGenerator {
    public static final Codec<VoidChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Biome.CODEC.stable().fieldOf("biome").forGetter(g -> g.biome)
    ).apply(instance, instance.stable(VoidChunkGenerator::new)));

    private static final Blockreader EMPTY_SAMPLE = new Blockreader(new BlockState[0]);

    private final Supplier<Biome> biome;

    public VoidChunkGenerator(Supplier<Biome> biome) {
        super(new SingleBiomeProvider(biome), new DimensionStructuresSettings(Optional.empty(), Collections.emptyMap()));
        this.biome = biome;
    }

    @SuppressWarnings("unused")
    public VoidChunkGenerator(Registry<Biome> biomeRegistry) {
        this(biomeRegistry, Biomes.THE_VOID);
    }

    public VoidChunkGenerator(Registry<Biome> biomeRegistry, RegistryKey<Biome> biome) {
        this(() -> biomeRegistry.get(biome));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long pSeed) {
        return this;
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion pLevel, IChunk pChunk) {}

    @Override
    public void fillFromNoise(IWorld p_230352_1_, StructureManager accesor, IChunk chunk) {}

    @Override
    public int getBaseHeight(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
        return 0;
    }

    @Override
    public IBlockReader getBaseColumn(int p_230348_1_, int p_230348_2_) {
        return EMPTY_SAMPLE;
    }

    @Override
    public boolean hasStronghold(ChunkPos chunkPos) {
        return false;
    }
}
