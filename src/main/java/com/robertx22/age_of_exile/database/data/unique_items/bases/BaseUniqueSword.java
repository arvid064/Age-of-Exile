package com.robertx22.age_of_exile.database.data.unique_items.bases;

import com.robertx22.age_of_exile.uncommon.enumclasses.WeaponTypes;
import com.robertx22.age_of_exile.vanilla_mc.items.gearitems.weapons.AoeSwordWeapon;

public final class BaseUniqueSword extends AoeSwordWeapon {

    public BaseUniqueSword() {
        super(WeaponTypes.sword);
    }

    @Override
    public boolean shouldRegisterLangName() {
        return false;
    }
}
