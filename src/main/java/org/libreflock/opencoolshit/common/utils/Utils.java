package org.libreflock.opencoolshit.common.utils;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.item.Rarity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

public class Utils {
    private static Rarity[] tiers = {Rarity.COMMON, Rarity.UNCOMMON, Rarity.RARE, Rarity.EPIC};
    private static String[] bytes = {"bytes", "KiB", "MiB", "GiB", "TiB"};

    public static String toHuman(Double number, Double divisor, String[] postfix, int decPlaces) {
        Double res = number;
        String last = postfix[postfix.length-1];
        for (String post : postfix) {
            if (res < divisor || post == last)
                return String.format("%.${decPlaces}f %s", res, post);
            res /= divisor;
        }
        throw new InternalError("how did we get here?");
    }

    public static String toHuman(Double number, Double divisor, String[] postfix) { //absolute laziness
        return toHuman(number, divisor, postfix, 2);
    }

    public static String toBytes(Double number, int decplaces) {
        return toHuman(number, 1024.0, bytes, decplaces);
    }

    public static String toBytes(Double number) {
        return toBytes(number, 2);
    }

    public static Rarity getRarity(int tier) {
        return tiers[Math.min(tier, 3)];
    }

    public static Collection<TextComponent> parseLore(String text) {
        Collection<TextComponent> coll = new HashSet<>();
        for (String sentence : text.split("\n")) {
            coll.add((TextComponent)(new StringTextComponent(sentence).withStyle(TextFormatting.GRAY)));
        }

        return coll;
    }
}
