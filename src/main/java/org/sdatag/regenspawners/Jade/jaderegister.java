package org.sdatag.regenspawners.Jade;

import org.sdatag.regenspawners.Block.SpawnerInfoBlock;
import org.sdatag.regenspawners.Block.SpawnerInfoBlockEntity;
import snownee.jade.api.*;

@WailaPlugin
public class jaderegister implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SpawnerInfoProvider.INSTANCE, SpawnerInfoBlock.class);
    }
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(SpawnerInfoProvider.INSTANCE, SpawnerInfoBlockEntity.class);
    }
}
