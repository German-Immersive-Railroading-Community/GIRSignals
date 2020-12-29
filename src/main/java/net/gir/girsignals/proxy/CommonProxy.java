package net.gir.girsignals.proxy;

import net.gir.girsignals.init.Blocks;
import net.gir.girsignals.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preinit(FMLPreInitializationEvent event) {
		Items.ItemInit();
		Blocks.BlockInit();
		
		MinecraftForge.EVENT_BUS.register(Items.class);
		MinecraftForge.EVENT_BUS.register(Blocks.class);
		
	}

	public void init(FMLInitializationEvent event) {
		
	}

	public void postinit(FMLPostInitializationEvent event) {
		
	}

}