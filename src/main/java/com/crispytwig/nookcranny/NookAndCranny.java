package com.crispytwig.nookcranny;

import com.crispytwig.nookcranny.blocks.entities.MailboxBlockEntity;
import com.crispytwig.nookcranny.client.gui.screens.widget.LockTargetMailboxWidget;
import com.crispytwig.nookcranny.events.*;
import com.crispytwig.nookcranny.inventory.MailboxMenu;
import com.crispytwig.nookcranny.registry.NCEntities;
import com.crispytwig.nookcranny.registry.*;
import com.google.common.reflect.Reflection;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.mehvahdjukaar.every_compat.EveryCompat;


public class NookAndCranny implements ModInitializer{
	public static final String MOD_ID = "nookcranny";

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, NCConfig.class);

		if(FabricLoader.getInstance().isModLoaded("everycomp")){
			EveryCompat.ACTIVE_MODULES.put(MOD_ID, new NCModule(MOD_ID));
		}

		Reflection.initialize(
				NCCreativeModeTab.class,
				NCSoundEvents.class,
				NCMenus.class,
				NCItems.class,
				NCBlocks.class,
				NCBlockEntities.class,
				NCEntities.class

		);

		UseBlockCallback.EVENT.register(new CushionableEvents());

		UseBlockCallback.EVENT.register(new DyeSofa());
		UseBlockCallback.EVENT.register(new ChairInteractions());
		UseBlockCallback.EVENT.register(new ShelfInteractions());
		UseBlockCallback.EVENT.register(new TableInteractions());
		UseBlockCallback.EVENT.register(new LampInteractions());
//		UseBlockCallback.EVENT.register(new PlateInteractions());

		NCVanillaIntegration.serverInit();

		ServerPlayNetworking.registerGlobalReceiver(MailboxMenu.packetChannel, (server, serverPlayer, listener, buf, packetSender) -> {
			var name =  buf.readUtf();
			var pos = buf.readBlockPos();
			server.execute(() -> {
				var be = serverPlayer.level().getBlockEntity(pos);
				if (be instanceof MailboxBlockEntity mailboxBlockEntity) {
					mailboxBlockEntity.targetString = name;
					mailboxBlockEntity.setChanged();
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(LockTargetMailboxWidget.packetChannel, (server, serverPlayer, listener, buf, packetSender) -> {
			var posi = buf.readBlockPos();
			var lockTarget = buf.readBoolean();
			server.execute(() -> {
				var be = serverPlayer.level().getBlockEntity(posi);
				if (be instanceof MailboxBlockEntity mailboxBlockEntity) {
					mailboxBlockEntity.lockTarget = lockTarget;
					mailboxBlockEntity.setChanged();
				}
			});
		});
	}


}