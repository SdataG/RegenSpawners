package org.sdatag.regenspawners.Block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class SpawnerBlockEntityRenderer implements BlockEntityRenderer<SpawnerInfoBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public SpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    @Override
    public void render(@NotNull SpawnerInfoBlockEntity spawnerInfoBlockEntity, float v, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        ItemStack itemStack = SpawnerInfoBlockEntity.getSpawnEggItemStack();
        if (itemStack != null && !itemStack.isEmpty()) {
            poseStack.pushPose();
                Level level = spawnerInfoBlockEntity.getLevel();
                if (level != null) {
                    BlockPos pos = spawnerInfoBlockEntity.getBlockPos();
                    BlockPos abovePos = pos.above();
                    BlockState aboveBlockState = level.getBlockState(abovePos);
                    if (aboveBlockState.isSolidRender(level, abovePos)) return;
                    i = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, abovePos), level.getBrightness(LightLayer.SKY, abovePos));
                } else {
                    i = LightTexture.FULL_BRIGHT;
                }
                poseStack.translate(0.5, 0.4, 0.5);
                poseStack.scale(1.5f, 1.5f, 1.5f);
                assert level != null;
                long gameTime = level.getGameTime();
                poseStack.mulPose(Axis.YP.rotationDegrees((gameTime * 4) % 360));
                this.context.getItemRenderer().renderStatic(
                        itemStack,
                        ItemDisplayContext.GROUND,
                        i,
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        multiBufferSource,
                        level,
                        0);
                poseStack.popPose();
            }
        }
    }
