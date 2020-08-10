package com.robertx22.mine_and_slash.vanilla_mc.items.gearitems.armor.plate;

import com.robertx22.mine_and_slash.vanilla_mc.items.gearitems.bases.BaseArmorItem;
import net.minecraft.entity.EquipmentSlot;

public class PlateBootsItem extends BaseArmorItem {

    public PlateBootsItem(String locname, boolean isunique) {
        super(Type.PLATE, locname, EquipmentSlot.FEET, isunique);
    }
}
