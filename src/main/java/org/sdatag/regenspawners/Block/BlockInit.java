package org.sdatag.regenspawners.Block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.sdatag.regenspawners.RegenSpawners;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RegenSpawners.MOD_ID);

    public static final RegistryObject<Block> SPAWNER_INFO_BLOCK = BLOCKS.register("spawner_info_block", () -> new SpawnerInfoBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noCollission().noOcclusion().lightLevel((state) -> 10).strength(600, 600)));
}
