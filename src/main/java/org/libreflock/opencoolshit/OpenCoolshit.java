package org.libreflock.opencoolshit;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.libreflock.opencoolshit.common.item.Items;
import org.libreflock.opencoolshit.common.network.PacketHandler;
import org.libreflock.opencoolshit.common.CreativeTabs;
import org.libreflock.opencoolshit.common.assembler.PromWipeTemplate;
import org.libreflock.opencoolshit.common.assembler.SocTemplate;
import org.libreflock.opencoolshit.common.block.Blocks;
import org.libreflock.opencoolshit.server.internal.EepromDriver;
import org.libreflock.opencoolshit.server.internal.FlashDriver;
import org.libreflock.opencoolshit.server.internal.SocDriver;
import org.libreflock.opencoolshit.server.tile.Tiles;

import java.nio.file.Path;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("opencoolshit")
public class OpenCoolshit
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static Path root;

    // public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public OpenCoolshit() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        Blocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        Tiles.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());

        PacketHandler.registerMessages();

        ModLoadingContext.get().registerConfig(Type.COMMON, Settings.COMMON_SPEC, "opencoolshit.toml");
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerDriver() {
        li.cil.oc.api.Driver.add(new FlashDriver());
        li.cil.oc.api.Driver.add(new EepromDriver());
        li.cil.oc.api.Driver.add(new SocDriver());
    }

    private void registerTemplates() {
        SocTemplate.register();
        PromWipeTemplate.register();
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("FLASH >> {}", Items.FLASH_0.get().getRegistryName().toString());
        LOGGER.info("FLASH_TIER >> {}", Items.FLASH_0.get().getRegistryName().toString().substring("opencoolshit:ossm_flash_".length()));

        // event.enqueueWork(() -> {
        //     LOGGER.info("I BOUGHT A PROPERTY IN EGYPT");
        //     ItemModelsProperties.register(Items.FLASH.get(), new ResourceLocation("opencoolshit", "tier"), (stack, world, living) -> {
        //         return ((Flash)stack.getItem()).getTier(stack);
        //     });
        // });

        // testTemplate.init();
        
        registerDriver();
        registerTemplates();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("opencoolshit", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
        MinecraftServer server = event.getServer();
        root = server.getWorldPath(FolderName.ROOT);

    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        // @SubscribeEvent
        // public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        //     // // register a new block here
        //     // LOGGER.info("HELLO from Register Block");
        //     for (RegistryObject<Block> block : Blocks.BLOCKS.getEntries()) {
        //         block.get()
        //     }
        // }
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            for (RegistryObject<Block> block : Blocks.BLOCKS.getEntries()) {
                LOGGER.info(block.toString());
                Item.Properties props = new Item.Properties().tab(CreativeTabs.OSSM);
                event.getRegistry().register(new BlockItem(block.get(), props).setRegistryName(block.get().getRegistryName()));
            }
        }

        // @SubscribeEvent
        // public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
        //     // event.getRegistry().register(TileEntityType.Builder.create(FirstBlockTile::new, ModBlocks.FIRSTBLOCK).build(null).setRegistryName("firstblock"));
        //     // Supplier<? extends T> supp = (Supplier<? extends T>) () -> new IronNoteblockTile();
        //     event.getRegistry().register(.setRegistryName(Blocks.IRON_NOTEBLOCK.get().getRegistryName()));
        // }
    }
}
