package com.wayfarer.immersivemaps;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String MOD_ID = "immersivemaps";
    public static final String MOD_NAME = "ImmersiveMaps";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final TagKey<Item> VALID_MAPS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "valid_maps"));
}