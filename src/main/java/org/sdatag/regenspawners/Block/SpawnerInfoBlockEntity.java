package org.sdatag.regenspawners.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.sdatag.regenspawners.ModConfig;


public class SpawnerInfoBlockEntity extends BlockEntity {
    private static ItemStack spawnEggItemStack;
    private static String mobEntityType;
    public int lifespan;


    public SpawnerInfoBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPAWNER_INFO_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        assert this.level != null;
        if (!this.level.isClientSide) {
            if (--lifespan <= 0) {
                replaceWithSpawnerAndRemoveLightSources((ServerLevel) this.level, this.getBlockPos());
            } else if (lifespan == ModConfig.LIFESPAN - 1) {
                spawnAndSpinEgg((ServerLevel) this.level, this.getBlockPos());
            }
        }
    }
    private void replaceWithSpawnerAndRemoveLightSources(ServerLevel world, BlockPos pos) {
        String mobEntityType = SpawnerInfoBlockEntity.getMobEntityType();
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobEntityType));
        if (entityType != null) {
            ItemStack spawnEgg = SpawnEggItem.byId(entityType).getDefaultInstance();
            world.getEntitiesOfClass(ItemEntity.class, new AABB(pos.offset(-5, -5, -5), pos.offset(5, 5, 5)))
                    .stream()
                    .filter(itemEntity -> itemEntity.getItem().is(spawnEgg.getItem()))
                    .forEach(ItemEntity::discard); // discard is used to remove the entity safely
        }

        // Existing logic to replace the block and remove light sources
        world.removeBlockEntity(pos);
        world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3);
        BlockEntity newEntity = world.getBlockEntity(pos);
        if (newEntity instanceof SpawnerBlockEntity && entityType != null) {
            ((SpawnerBlockEntity) newEntity).getSpawner().setEntityId(entityType, world, world.getRandom(), pos);
        }
        removeLightSources(world, pos);
    }

    private void removeLightSources(ServerLevel world, BlockPos centerPos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int radius = ModConfig.LIGHT_CLEAR_RADIUS; // Use the configured radius
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(centerPos.getX() + x, centerPos.getY() + y, centerPos.getZ() + z);
                    if (world.getBlockState(mutablePos).getLightEmission() > 0) {
                        world.setBlock(mutablePos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    public void setMobEntityType(String mobType) {
        this.mobEntityType = mobType;
    }

    public static String getMobEntityType() {
        return mobEntityType;
    }

    private String formatLifespan(int lifespan) {
        int seconds = lifespan / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void spawnAndSpinEgg(ServerLevel world, BlockPos pos) {
        if (mobEntityType != null) {
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobEntityType));
            if (entityType != null) {
                SpawnEggItem spawnEggItem = SpawnEggItem.byId(entityType);
                if (spawnEggItem != null) {
                    this.spawnEggItemStack = new ItemStack(spawnEggItem);
                    if (this.spawnEggItemStack.isEmpty()) {
                        System.out.println("Warning: Spawn egg item stack is empty. No entity spawned.");
                    }
                } else {
                    System.out.println("Warning: No spawn egg found for entity type. No entity spawned.");
                }
            }
        } else {
            System.out.println("Warning: mobEntityType is null. No entity spawned.");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (mobEntityType != null) {
            tag.putString("MobEntityType", mobEntityType);
            tag.putInt("Lifespan", this.lifespan);
            if (!spawnEggItemStack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                spawnEggItemStack.save(itemTag);
                tag.put("SpawnEggItem", itemTag);
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("MobEntityType", Tag.TAG_STRING)) {
            mobEntityType = tag.getString("MobEntityType");
        }
        if (tag.contains("Lifespan", Tag.TAG_INT)) {
            this.lifespan = tag.getInt("Lifespan");
        }
        if (tag.contains("spawnEggItemStack", Tag.TAG_COMPOUND)) {
            spawnEggItemStack = ItemStack.of(tag.getCompound("spawnEggItemStack"));
        }
    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        if (!getSpawnEggItemStack().isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            getSpawnEggItemStack().save(itemTag);
            tag.put("SpawnEggItemStack", itemTag);
        }
        return tag;
    }

    @Override

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag);
        if (tag.contains("SpawnEggItemStack", Tag.TAG_COMPOUND)) {
            setSpawnEggItemStack(ItemStack.of(tag.getCompound("SpawnEggItemStack")));
            }
        }

    private void setSpawnEggItemStack(ItemStack spawnEggItemStack) {
        this.spawnEggItemStack = spawnEggItemStack;
    }

    public void onCustomBlockRemoved(Level world, BlockPos pos, BlockState newState) {
        if (!world.isClientSide) {
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobEntityType));
            if (entityType != null) {
                ItemStack spawnEgg = SpawnEggItem.byId(entityType).getDefaultInstance();
                world.getEntitiesOfClass(ItemEntity.class, new AABB(pos.offset(-5, -5, -5), pos.offset(5, 5, 5)))
                        .stream()
                        .filter(itemEntity -> itemEntity.getItem().is(spawnEgg.getItem()))
                        .forEach(ItemEntity::discard); // Remove the egg entity
            }
        }
    }





    public void setLifespan(int i) {
        this.lifespan = i;
        }

    public static ItemStack getSpawnEggItemStack() {
        return spawnEggItemStack;
    }

    public int getLifespan() {
        return this.lifespan;
    }
}
