package org.sdatag.regenspawners.Event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.sdatag.regenspawners.Block.BlockInit;
import org.sdatag.regenspawners.Block.SpawnerInfoBlockEntity;
import org.sdatag.regenspawners.ModConfig;
import org.sdatag.regenspawners.RegenSpawners;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mod.EventBusSubscriber(modid = RegenSpawners.MOD_ID)
public class SpawnerBreakHandler {

    @SubscribeEvent
    public static void onSpawnerBreak(BlockEvent.BreakEvent event) {
        if (event.getState().is(Blocks.SPAWNER)) {
            BlockPos pos = event.getPos();
            ServerLevel level = (ServerLevel) event.getLevel();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            CompoundTag entityTag = null;
            if (blockEntity instanceof SpawnerBlockEntity) {
                SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity) blockEntity;
                if (spawnerBlockEntity.getSpawner().nextSpawnData == null) {
                    LOGGER.info("Spawner had no mob set. Not regenerating.");
                } else {
                    // Your existing logic for regenerating the spawner
                    // Assuming nextSpawnData has been made public via access transformers
                    entityTag = spawnerBlockEntity.getSpawner().nextSpawnData.getEntityToSpawn();
                    // Proceed with your logic using entityTag
                }
                if (entityTag != null && !entityTag.isEmpty()) {
                    String entityId = entityTag.getString("id");
                    ResourceLocation entityType = new ResourceLocation(entityId);
                    EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(entityType);
                    if (type != null) {
                        String entityName = type.getDescription().getString();
                        LOGGER.info("Spawner at " + pos + " spawns: " + entityName);
                        replaceSpawnerWithInfoBlock(level, pos, entityType.toString());
                    } else {
                        LOGGER.warn("Entity type not found for ID: " + entityId);
                    }
                } else {
                    LOGGER.warn("EntityTag is empty for spawner at " + pos);
                }
            }
        }
    }

    private static void replaceSpawnerWithInfoBlock(ServerLevel level, BlockPos pos, String mobType) {
        level.getServer().submit(() -> {
            level.setBlock(pos, BlockInit.SPAWNER_INFO_BLOCK.get().defaultBlockState(), 3);
            BlockEntity newEntity = level.getBlockEntity(pos);
            if (newEntity instanceof SpawnerInfoBlockEntity) {
                ((SpawnerInfoBlockEntity) newEntity).setMobEntityType(mobType);
                ((SpawnerInfoBlockEntity) newEntity).setLifespan(ModConfig.LIFESPAN);
            }
        });
    }
}