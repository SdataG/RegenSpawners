package org.sdatag.regenspawners.Jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import org.sdatag.regenspawners.Block.SpawnerInfoBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public class SpawnerInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final SpawnerInfoProvider INSTANCE = new SpawnerInfoProvider();
    public static final ResourceLocation UID = new ResourceLocation("regenspawners", "spawner_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof SpawnerInfoBlockEntity) {
            SpawnerInfoBlockEntity entity = (SpawnerInfoBlockEntity) accessor.getBlockEntity();
            IElementHelper elements = tooltip.getElementHelper();

            // Retrieve the mob entity type from server data
            String mobEntityType = accessor.getServerData().getString("MobEntityType");
            int colonIndex = mobEntityType.indexOf(":");
            if (colonIndex != -1) {
                mobEntityType = mobEntityType.substring(colonIndex + 1);
            }
            if (!mobEntityType.isEmpty()) {
                tooltip.add(elements.text(Component.literal("Type: " + mobEntityType)));
            }

            // Continue with the rest of your tooltip setup, such as lifespan and clock icon
            int lifespan = accessor.getServerData().getInt("Lifespan");
            String formattedLifespan = formatLifespan(lifespan);

            ItemStack clock = new ItemStack(Items.CLOCK);
            IElement icon = elements.item(new ItemStack(Items.CLOCK), 0.5f).size(new Vec2(10, 10)).translate(new Vec2(0, -1));
            tooltip.add(icon);

            tooltip.append(Component.literal(" " + formattedLifespan));
        }
    }

    private String formatLifespan(int lifespan) {
        int seconds = lifespan / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof SpawnerInfoBlockEntity) {
            SpawnerInfoBlockEntity entity = (SpawnerInfoBlockEntity) accessor.getBlockEntity();
            data.putInt("Lifespan", entity.lifespan);
        }
        if (accessor.getBlockEntity() instanceof SpawnerInfoBlockEntity) {
            SpawnerInfoBlockEntity entity = (SpawnerInfoBlockEntity) accessor.getBlockEntity();
            // Example server data
            data.putString("MobEntityType", entity.getMobEntityType());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}