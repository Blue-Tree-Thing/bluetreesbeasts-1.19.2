package net.fabricmc.bluetreebeasts.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.bluetreebeasts.BTBStructures;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class BlueTreeBeastsStructures extends Structure {

    public static final Codec<BlueTreeBeastsStructures> CODEC =  RecordCodecBuilder.<BlueTreeBeastsStructures>mapCodec(instance ->
            instance.group(BlueTreeBeastsStructures.configCodecBuilder(instance),
            StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
            Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
            Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
            Heightmap.Type.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
            Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, BlueTreeBeastsStructures::new)).codec();

    private final RegistryEntry<StructurePool> startPool;
    private final Optional<Identifier> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Type> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public BlueTreeBeastsStructures(Structure.Config config,
                         RegistryEntry<StructurePool> startPool,
                         Optional<Identifier> startJigsawName,
                         int size,
                         HeightProvider startHeight,
                         Optional<Heightmap.Type> projectStartToHeightmap,
                         int maxDistanceFromCenter)
    {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }




    private static boolean extraSpawningChecks(Structure.Context context) {
        ChunkPos chunkpos = context.chunkPos();


        return context.chunkGenerator().getHeightInGround(
                chunkpos.getStartX(),
                chunkpos.getStartZ(),
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                context.world(),
                context.noiseConfig()) < 150;
    }

    public static Codec<BlueTreeBeastsStructures> CODEC() {
           return BlueTreeBeastsStructures.CODEC;
    }


    @Override
    public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {


        if (!BlueTreeBeastsStructures.extraSpawningChecks(context)) {
            return Optional.empty();
        }

        int startY = this.startHeight.get(context.random(), new HeightContext(context.chunkGenerator(), context.world()));

        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getStartX(), startY, chunkPos.getStartZ());

        Optional<StructurePosition> structurePiecesGenerator =
                StructurePoolBasedGenerator.generate(
                        context,
                        this.startPool,
                        this.startJigsawName,
                        this.size,
                        blockPos,
                        false,
                        this.projectStartToHeightmap,

                        this.maxDistanceFromCenter);


        return structurePiecesGenerator;
    }


    @Override
    public StructureType<?> getType() {
        return BTBStructures.BLUETREEBEASTS_STRUCTURE;
    }
}
