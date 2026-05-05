package com.wayfarer.immersivemaps.util;

public class ColorHelper {
    public static int getHexFromMarkerColor(String type) {
        if (type.contains("white"))
            return 0xFFFFFF;
        if (type.contains("orange"))
            return 0xD87F33;
        if (type.contains("magenta"))
            return 0xB24EBD;
        if (type.contains("light_blue"))
            return 0x6699D8;
        if (type.contains("yellow"))
            return 0xE5E533;
        if (type.contains("lime"))
            return 0x7FCC19;
        if (type.contains("pink"))
            return 0xF27FA5;
        if (type.contains("gray"))
            return 0x4C4C4C;
        if (type.contains("light_gray"))
            return 0x999999;
        if (type.contains("cyan"))
            return 0x4C7F99;
        if (type.contains("purple"))
            return 0x7F3FB2;
        if (type.contains("blue"))
            return 0x334CB2;
        if (type.contains("brown"))
            return 0x664C33;
        if (type.contains("green"))
            return 0x667F33;
        if (type.contains("red"))
            return 0x993333;
        if (type.contains("black"))
            return 0x191919;
        return 0xFFFFFF;
    }
}
