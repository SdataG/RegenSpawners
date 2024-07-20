package org.sdatag.regenspawners.Block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.sdatag.regenspawners.RegenSpawners;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RegenSpawners.MOD_ID);

    public static final RegistryObject<BlockEntityType<SpawnerInfoBlockEntity>> SPAWNER_INFO_BLOCK_ENTITY = BLOCK_ENTITIES.register("spawner_info_block_entity",
            () -> BlockEntityType.Builder.of(SpawnerInfoBlockEntity::new, BlockInit.SPAWNER_INFO_BLOCK.get()).build(null));
}