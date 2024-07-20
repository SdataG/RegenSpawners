package org.sdatag.regenspawners.Block;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SpawnerInfoBlock extends Block implements EntityBlock {

    public SpawnerInfoBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpawnerInfoBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ModBlockEntities.SPAWNER_INFO_BLOCK_ENTITY.get()) {
            return (lvl, pos, blkState, tile) -> {
                if (tile instanceof SpawnerInfoBlockEntity) {
                    ((SpawnerInfoBlockEntity) tile).tick();
                }
            };
        }
        return null;
    }
    // In your custom block class that corresponds to SpawnerInfoBlockEntity
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof SpawnerInfoBlockEntity) {
            ((SpawnerInfoBlockEntity)world.getBlockEntity(pos)).onCustomBlockRemoved(world, pos, newState);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}
