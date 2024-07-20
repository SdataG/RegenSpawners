package org.sdatag.regenspawners;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.sdatag.regenspawners.Block.*;
import org.slf4j.Logger;


@Mod.EventBusSubscriber(modid = RegenSpawners.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@Mod(RegenSpawners.MOD_ID)
public class RegenSpawners {

    public static final String MOD_ID = "regenspawners";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RegenSpawners() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        BlockInit.BLOCKS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModConfig.loadConfig();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SPAWNER_INFO_BLOCK_ENTITY.get(), SpawnerBlockEntityRenderer::new);

    }
}