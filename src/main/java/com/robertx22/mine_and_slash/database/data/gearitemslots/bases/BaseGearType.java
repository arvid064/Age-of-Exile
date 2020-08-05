package com.robertx22.mine_and_slash.database.data.gearitemslots.bases;

import com.google.gson.JsonObject;
import com.robertx22.mine_and_slash.capability.entity.EntityCap;
import com.robertx22.mine_and_slash.database.base.Rarities;
import com.robertx22.mine_and_slash.database.data.StatModifier;
import com.robertx22.mine_and_slash.database.data.gearitemslots.weapons.mechanics.NormalWeaponMechanic;
import com.robertx22.mine_and_slash.database.data.gearitemslots.weapons.mechanics.WeaponMechanic;
import com.robertx22.mine_and_slash.database.data.stats.types.offense.AttackSpeed;
import com.robertx22.mine_and_slash.database.registry.SlashRegistry;
import com.robertx22.mine_and_slash.database.registry.SlashRegistryType;
import com.robertx22.mine_and_slash.datapacks.JsonUtils;
import com.robertx22.mine_and_slash.datapacks.bases.ISerializable;
import com.robertx22.mine_and_slash.datapacks.bases.ISerializedRegistryEntry;
import com.robertx22.mine_and_slash.datapacks.seriazables.SerializableBaseGearType;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.saveclasses.ExactStatData;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.Rarity;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.StatRequirement;
import com.robertx22.mine_and_slash.saveclasses.item_classes.GearItemData;
import com.robertx22.mine_and_slash.saveclasses.unit.StatData;
import com.robertx22.mine_and_slash.uncommon.effectdatas.interfaces.WeaponTypes;
import com.robertx22.mine_and_slash.uncommon.enumclasses.ModType;
import com.robertx22.mine_and_slash.uncommon.interfaces.IAutoLocName;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;

public abstract class BaseGearType implements IAutoLocName, ISerializedRegistryEntry<BaseGearType>, ISerializable<BaseGearType> {

    public float attacksPerSecond = 1;

    public abstract List<StatModifier> implicitStats();

    public abstract List<StatModifier> baseStats();

    public WeaponTypes weaponType() {
        return WeaponTypes.None;
    }

    public abstract TagList getTags();

    public abstract Item getItem();

    public abstract StatRequirement getStatRequirements();

    public final EquipmentSlot getVanillaSlotType() {

        if (getTags().contains(SlotTag.shield)) {
            return EquipmentSlot.OFFHAND;
        }
        if (getTags().contains(SlotTag.boots)) {
            return EquipmentSlot.FEET;
        }
        if (getTags().contains(SlotTag.chest)) {
            return EquipmentSlot.CHEST;
        }
        if (getTags().contains(SlotTag.pants)) {
            return EquipmentSlot.LEGS;
        }
        if (getTags().contains(SlotTag.helmet)) {
            return EquipmentSlot.HEAD;
        }
        if (isWeapon()) {
            return EquipmentSlot.MAINHAND;
        }

        return null;
    }

    public int Weight() {
        return 1000;
    }

    public static class Constants {
        public static float SWORD_ATK_SPEED = 0.75F;
        public static float WAND_ATK_SPEED = 1;
        public static float AXE_ATK_SPEED = 1.25F;
    }

    public final float getAttacksPerSecondCalculated(EntityCap.UnitData data) {
        return getAttacksPerSecondCalculated(data.getUnit()
            .peekAtStat(AttackSpeed.getInstance()));
    }

    public final float getAttacksPerSecondCalculated(StatData stat) {

        float multi = stat
            .getMultiplier();

        float f = multi * attacksPerSecond;

        return f;
    }

    public final float getAttacksPerSecondForTooltip(GearItemData gear) {
        return getAttacksPerSecondForTooltip(gear.GetAllStats(false, false));
    }

    public final float getAttacksPerSecondForTooltip(List<ExactStatData> stats) {
        // only show bonus atk speed from this item

        float speed = attacksPerSecond;

        for (ExactStatData x : stats) {
            if (x.getStat() instanceof AttackSpeed) {
                if (x.getType() == ModType.LOCAL_INCREASE) {
                    speed *= 1F + x.getAverageValue() / 100F;
                }
            }
        }

        return speed;
    }

    public final boolean hasUniqueItemVersions() {
        return !SlashRegistry.UniqueGears()
            .getFilterWrapped(x -> x.getBaseGearType()
                .GUID()
                .equals(GUID())).list.isEmpty();
    }

    public enum PlayStyle {
        INT, DEX, STR, NONE;

        public boolean isINT() {
            return this == INT;
        }

        public boolean isDEX() {
            return this == DEX;
        }

        public boolean isSTR() {
            return this == STR;
        }

    }

    public final boolean isWeapon() {
        return this.family()
            .equals(SlotFamily.Weapon);
    }

    public final boolean isMeleeWeapon() {
        return this.getTags()
            .contains(SlotTag.melee_weapon);
    }

    public boolean isShield() {
        return getTags().contains(SlotTag.shield);
    }

    public enum SlotTag {
        sword(SlotFamily.Weapon),
        axe(SlotFamily.Weapon),
        bow(SlotFamily.Weapon),
        wand(SlotFamily.Weapon),
        crossbow(SlotFamily.Weapon),

        boots(SlotFamily.Armor),
        helmet(SlotFamily.Armor),
        pants(SlotFamily.Armor),
        chest(SlotFamily.Armor),

        necklace(SlotFamily.Jewelry),
        ring(SlotFamily.Jewelry),
        shield(SlotFamily.OffHand),

        cloth(SlotFamily.NONE),
        plate(SlotFamily.NONE),
        leather(SlotFamily.NONE),

        mage_weapon(SlotFamily.NONE), melee_weapon(SlotFamily.NONE), ranged_weapon(SlotFamily.NONE),

        magic_shield_stat(SlotFamily.NONE),
        armor_stat(SlotFamily.NONE),
        dodge_stat(SlotFamily.NONE),

        weapon_family(SlotFamily.NONE),
        armor_family(SlotFamily.NONE),
        jewelry_family(SlotFamily.NONE),
        offhand_family(SlotFamily.NONE),

        intelligence(SlotFamily.NONE),
        dexterity(SlotFamily.NONE),
        strength(SlotFamily.NONE);

        public SlotFamily family = SlotFamily.NONE;

        SlotTag(SlotFamily family) {
            this.family = family;
        }
    }

    public enum SlotFamily {
        Weapon,
        Armor,
        Jewelry,
        OffHand,
        NONE;

        public boolean isJewelry() {
            return this == Jewelry;
        }

        public boolean isArmor() {
            return this == Armor;
        }

        public boolean isWeapon() {
            return this == Weapon;
        }

        public boolean isOffhand() {
            return this == OffHand;
        }

    }

    public final SlotFamily family() {
        return getTags().getFamily();
    }

    public final WeaponMechanic getWeaponMechanic() {
        return new NormalWeaponMechanic();
    }

    private static HashMap<String, HashMap<Item, Boolean>> CACHED = new HashMap<>();

    // has to use ugly stuff like this cus datapacks.
    public static boolean isGearOfThisType(BaseGearType slot, Item item) {

        String id = slot.GUID();

        if (!CACHED.containsKey(id)) {
            CACHED.put(id, new HashMap<>());
        }
        if (CACHED.get(id)
            .containsKey(item)) {
            return CACHED.get(id)
                .get(item);
        }

        boolean bool = false;

        try {
            if (item instanceof ArmorItem) {
                if (slot.getVanillaSlotType() != null) {
                    if (slot.getVanillaSlotType()
                        .equals(EquipmentSlot.FEET)) {

                        bool = ((ArmorItem) item).getSlotType()
                            .equals(EquipmentSlot.FEET);

                    } else if (slot.getVanillaSlotType()
                        .equals(EquipmentSlot.CHEST)) {

                        bool = ((ArmorItem) item).getSlotType()
                            .equals(EquipmentSlot.CHEST);

                    } else if (slot.getVanillaSlotType()
                        .equals(EquipmentSlot.HEAD)) {

                        bool = ((ArmorItem) item).getSlotType()
                            .equals(EquipmentSlot.HEAD);

                    } else if (slot.getVanillaSlotType()
                        .equals(EquipmentSlot.LEGS)) {

                        bool = ((ArmorItem) item).getSlotType()
                            .equals(EquipmentSlot.LEGS);
                    }
                }
            } else if (slot.getTags()
                .contains(SlotTag.sword)) {
                bool = item instanceof SwordItem;
            } else if (slot.getTags()
                .contains(SlotTag.bow)) {
                bool = item instanceof BowItem;
            } else if (slot.getTags()
                .contains(SlotTag.axe)) {
                bool = item instanceof AxeItem;
            } else if (slot.getTags()
                .contains(SlotTag.shield)) {
                bool = item instanceof ShieldItem;
            } else if (slot.getTags()
                .contains(SlotTag.crossbow)) {
                bool = item instanceof CrossbowItem;
            }

            CACHED.get(slot.GUID())
                .put(item, bool);

            return bool;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public int getRarityRank() {
        return 0;
    }

    @Override
    public Rarity getRarity() {
        return Rarities.Gears.get(getRarityRank());
    }

    @Override
    public SlashRegistryType getSlashRegistryType() {
        return SlashRegistryType.GEAR_TYPE;
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Gear_Slots;
    }

    @Override
    public String locNameLangFileGUID() {
        return Ref.MODID + ".gear_type." + formattedGUID();
    }

    public final boolean isMageWeapon() {
        return getTags().contains(SlotTag.mage_weapon);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = getDefaultJson();

        JsonUtils.addStats(implicitStats(), json, "implicit_stats");
        JsonUtils.addStats(baseStats(), json, "base_stats");

        json.add("tag_list", getTags().toJson());
        json.add("stat_req", getStatRequirements().toJson());
        json.addProperty("item_id", Registry.ITEM.getId(getItem())
            .toString());
        json.addProperty("weapon_type", weaponType().toString());

        return json;
    }

    @Override
    public BaseGearType fromJson(JsonObject json) {

        SerializableBaseGearType o = new SerializableBaseGearType();

        o.identifier = this.getGUIDFromJson(json);
        o.weight = this.getWeightFromJson(json);
        o.lang_name_id = this.getLangNameStringFromJson(json);

        o.stat_req = StatRequirement.EMPTY.fromJson(json.getAsJsonObject("stat_req"));
        o.base_stats = JsonUtils.getStats(json, "base_stats");
        o.implicit_stats = JsonUtils.getStats(json, "implicit_stats");
        o.item_id = json.get("item_id")
            .getAsString();

        try {
            o.weapon_type = WeaponTypes.valueOf(json.get("weapon_type")
                .getAsString());
        } catch (Exception e) {
            o.weapon_type = WeaponTypes.None;
        }

        o.tags = new TagList().fromJson(json.getAsJsonObject("tag_list"));

        return o;
    }
}
