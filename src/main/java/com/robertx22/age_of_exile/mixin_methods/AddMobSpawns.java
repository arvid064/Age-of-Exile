package com.robertx22.age_of_exile.mixin_methods;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.robertx22.age_of_exile.mmorpg.ModRegistry.ENTITIES;

public class AddMobSpawns {

    static class Spawns {

        List<SpawnEntry> all = new ArrayList<>();

        public SpawnEntry ARCANE_SLIME = of(ENTITIES.ARCANE_SLIME, 150, 1, 2);
        public SpawnEntry FIRE_SLIME = of(ENTITIES.FIRE_SLIME, 150, 1, 2);
        public SpawnEntry WATER_SLIME = of(ENTITIES.WATER_SLIME, 150, 1, 2);
        public SpawnEntry THUNDER_SLIME = of(ENTITIES.THUNDER_SLIME, 150, 1, 2);
        public SpawnEntry NATURE_SLIME = of(ENTITIES.NATURE_SLIME, 150, 1, 2);

        public SpawnEntry ARCANE_SPIDER = of(ENTITIES.ARCANE_SPIDER, 200, 2, 3);
        public SpawnEntry FIRE_SPIDER = of(ENTITIES.FIRE_SPIDER, 200, 2, 3);
        public SpawnEntry WATER_SPIDER = of(ENTITIES.WATER_SPIDER, 200, 2, 3);
        public SpawnEntry THUNDER_SPIDER = of(ENTITIES.THUNDER_SPIDER, 200, 2, 3);
        public SpawnEntry NATURE_SPIDER = of(ENTITIES.NATURE_SPIDER, 200, 2, 3);

        public SpawnEntry ARCANE_ZOMBIE = of(ENTITIES.ARCANE_ZOMBIE, 200, 2, 3);
        public SpawnEntry FIRE_ZOMBIE = of(ENTITIES.FIRE_ZOMBIE, 200, 2, 3);
        public SpawnEntry WATER_ZOMBIE = of(ENTITIES.WATER_ZOMBIE, 200, 2, 3);
        public SpawnEntry THUNDER_ZOMBIE = of(ENTITIES.THUNDER_ZOMBIE, 200, 2, 3);
        public SpawnEntry NATURE_ZOMBIE = of(ENTITIES.NATURE_ZOMBIE, 200, 2, 3);

        public SpawnEntry FIRE_MAGE = of(ENTITIES.FIRE_MAGE, 50, 1, 1);
        public SpawnEntry WATER_MAGE = of(ENTITIES.WATER_MAGE, 50, 1, 1);
        public SpawnEntry THUNDER_MAGE = of(ENTITIES.THUNDER_MAGE, 50, 1, 1);
        public SpawnEntry NATURE_MAGE = of(ENTITIES.NATURE_MAGE, 50, 1, 1);
        public SpawnEntry HEALER_MAGE = of(ENTITIES.HEALER_MAGE, 50, 1, 1);

        SpawnEntry of(EntityType type, int weight, int min, int max) {
            SpawnEntry entry = new SpawnEntry(type, weight, min, max);
            all.add(entry);
            return entry;
        }
    }

    static Spawns spawns = null;

    public static Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> my$onConstruct(Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> map) {
        if (spawns == null) {
            spawns = new Spawns();
        }
        List<SpawnEntry> list = new ArrayList<>(map.get(SpawnGroup.MONSTER));
        if (!list.contains(spawns.ARCANE_SLIME)) {
            list.addAll(spawns.all);
            map = new HashMap<>(map); // map is imutable too..
            map.put(SpawnGroup.MONSTER, list);
        }

        return map;
    }

}
