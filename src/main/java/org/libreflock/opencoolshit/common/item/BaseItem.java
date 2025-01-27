package org.libreflock.opencoolshit.common.item;
import java.util.List;

// import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;
// import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
// import net.minecraft.util.text.ITextComponent;
// import net.minecraft.util.text.StringTextComponent;
// import net.minecraft.util.text.TextFormatting;
// import net.minecraft.world.World;
// import net.minecraftforge.api.distmarker.OnlyIn;
// import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;

import org.libreflock.opencoolshit.common.utils.Utils;

public abstract class BaseItem extends Item {

    public BaseItem(Properties properties) {
        super(properties);
    }

    public abstract List<Pair<String, String>> getData(ItemStack stack);
    public abstract int getTier(ItemStack stack);

    public String lore = "";

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Utils.getRarity(getTier(stack));
    }// he DIDNT lie

    // @Override
    // @OnlyIn(Dist.CLIENT)
    // public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    //     super.appendHoverText(stack, world, tooltip, flag);
    //     // if (GuiScreen.)
    //     if (lore.length() > 0) {
    //         tooltip.add(new StringTextComponent(lore).withStyle(TextFormatting.GRAY));
    //     }
    // }

    public abstract void register(RegistryEvent.Register<Item> event);

    
}
