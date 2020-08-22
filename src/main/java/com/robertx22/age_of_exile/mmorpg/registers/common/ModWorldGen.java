package com.robertx22.age_of_exile.mmorpg.registers.common;

import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.world_gen.towers.TowerFeature;
import com.robertx22.age_of_exile.world_gen.towers.TowerPieces;
import com.robertx22.age_of_exile.world_gen.towers.processors.BiomeProcessor;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ModWorldGen {

    public static ModWorldGen INSTANCE = new ModWorldGen();

    public StructureProcessorType<BiomeProcessor> BIOME_PROCESSOR = StructureProcessorType.register("biome_processor", BiomeProcessor.CODEC);

    public final StructureFeature<DefaultFeatureConfig> TOWER = new TowerFeature(DefaultFeatureConfig.CODEC);
    public final ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> CONFIGURED_TOWER = TOWER.configure(new DefaultFeatureConfig());
    public final StructurePieceType TOWER_PIECE = TowerPieces.Piece::new;

    public ModWorldGen() {

        FabricStructureBuilder.create(new Identifier(Ref.MODID, "tower"), TOWER)
            .step(GenerationStep.Feature.SURFACE_STRUCTURES)
            .defaultConfig(6, -1, 378235)
            .superflatFeature(CONFIGURED_TOWER)
            .register();

        Registry.register(Registry.STRUCTURE_PIECE, new Identifier(Ref.MODID, "tower_piece"), TOWER_PIECE);

    }

}