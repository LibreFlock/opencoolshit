// package org.libreflock.opencoolshit.server.driver;

// import org.libreflock.opencoolshit.common.Items;

// import li.cil.oc.api.network.EnvironmentHost;
// import li.cil.oc.api.network.ManagedEnvironment;
// import li.cil.oc.api.prefab.DriverItem;
// import net.minecraft.item.ItemStack;

// public class FlashDriver extends DriverItem {

//     @Override
//     public boolean worksWith(ItemStack stack) {
//         return stack.getItem().getRegistryName() == Items.FLASH_0.getId() || stack.getItem().getRegistryName() == Items.FLASH_1.getId()|| stack.getItem().getRegistryName() == Items.FLASH_0.getId(); // TODO: THIS IS TERRIBLE
//     }

//     @Override
//     public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
//         return Flash(stack.getItem().tier)
//     }

//     @Override
//     public String slot(ItemStack stack) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'slot'");
//     }

    
// }
