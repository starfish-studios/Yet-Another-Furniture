package com.starfish_studios.yaf;

import com.starfish_studios.yaf.block.entity.MailboxBlockEntity;
import com.starfish_studios.yaf.client.model.*;
import com.starfish_studios.yaf.client.gui.screens.DrawerScreen;
import com.starfish_studios.yaf.client.gui.screens.MailboxScreen;
import com.starfish_studios.yaf.client.renderer.SeatRenderer;
import com.starfish_studios.yaf.client.renderer.blockentity.*;
import com.starfish_studios.yaf.registry.YAFBlockEntities;
import com.starfish_studios.yaf.registry.YAFBlocks;
import com.starfish_studios.yaf.registry.YAFEntities;
import com.starfish_studios.yaf.registry.YAFMenus;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;

@Environment(EnvType.CLIENT)
public class YetAnotherFurnitureClient {

    public static void init() {
        EntityRendererRegistry.register(YAFEntities.SEAT, SeatRenderer::new);

        EntityModelLayerRegistry.register(FanBlockEntityModel.LAYER_LOCATION, FanBlockEntityModel::createBodyLayer);
        EntityModelLayerRegistry.register(ChimeBlockEntityModel.LAYER_LOCATION, ChimeBlockEntityModel::createBodyLayer);
        EntityModelLayerRegistry.register(DrawerCountertopModel.LAYER_LOCATION, DrawerCountertopModel::createBodyLayer);
        EntityModelLayerRegistry.register(TableBlockEntityModel.LAYER_LOCATION, TableBlockEntityModel::createBodyLayer);
        EntityModelLayerRegistry.register(TableclothModel.LAYER_LOCATION, TableclothModel::createBodyLayer);
        EntityModelLayerRegistry.register(ChairBlockEntityModel.LAYER_LOCATION, ChairBlockEntityModel::createBodyLayer);
        EntityModelLayerRegistry.register(ChairCushionModel.LAYER_LOCATION, ChairCushionModel::createBodyLayer);
        EntityModelLayerRegistry.register(TallStoolBlockEntityModel.LAYER_LOCATION, TallStoolBlockEntityModel::createBodyLayer);

        BlockEntityRendererRegistry.register(YAFBlockEntities.SHELF.get(), ShelfBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.FLOWER_BASKET.get(), FlowerBasketRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.FAN.get(), FanBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.CHIME.get(), ChimeBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.TABLE.get(), TableBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.CHAIR.get(), ChairBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.TALL_STOOL.get(), TallStoolBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.DRAWER.get(), DrawerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(YAFBlockEntities.CABINET.get(), DrawerBlockEntityRenderer::new);

        MenuRegistry.registerScreenFactory(YAFMenus.DRAWER.get(), DrawerScreen::new);
        MenuRegistry.registerScreenFactory(YAFMenus.GENERIC_1X5.get(), MailboxScreen::new);

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, MailboxBlockEntity.packetChannel, ((buf, context) -> {
            var pos = buf.readBlockPos();
            var state = buf.readBoolean();

            var client = Minecraft.getInstance();
            client.execute(() -> {
                assert client.level != null;
                var be = client.level.getBlockEntity(pos);
                if (be instanceof MailboxBlockEntity mailbox) {
                    mailbox.failedToSend = state;
                    mailbox.setChanged();
                }
            });


        }));

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, MailboxBlockEntity.packetChannel2, ((buf, context) -> {
            var pos = buf.readBlockPos();
            var client = Minecraft.getInstance();
            
            client.execute(() -> {
                assert client.level != null;
                var be = client.level.getBlockEntity(pos);
                if (be instanceof MailboxBlockEntity mailbox) {
                    mailbox.targetString = "";
                    mailbox.setChanged();
                }
            });
        }));

        YAFBlocks.LAMPS.forEach((key, value) -> RenderTypeRegistry.register(RenderType.translucent(), value.get()));
        YAFBlocks.MAIL_BOXES.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));
        YAFBlocks.FANS.forEach((key, value) -> RenderTypeRegistry.register(RenderType.translucent(), value.get()));
        YAFBlocks.CURTAINS.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));


        YAFBlocks.BENCHES.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));
        YAFBlocks.FLOWER_BASKETS.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));
        YAFBlocks.SHELVES.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));
        YAFBlocks.TABLES.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));
        YAFBlocks.CHAIRS.forEach((key, value) -> RenderTypeRegistry.register(RenderType.cutout(), value.get()));

        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.SPIGOT.get());
        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.AMETHYST_WIND_CHIMES.get());
        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.BAMBOO_WIND_CHIMES.get());
        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.BAMBOO_STRIPPED_WIND_CHIMES.get());
        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.BONE_WIND_CHIMES.get());
        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.COPPER_WIND_CHIMES.get());
        RenderTypeRegistry.register(RenderType.cutout(), YAFBlocks.ECHO_SHARD_WIND_CHIMES.get());

    }
}
