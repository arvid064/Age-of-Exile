package com.robertx22.age_of_exile.database.data.stats.types.resources;

import com.robertx22.age_of_exile.database.data.stats.StatScaling;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;

public class MagicShieldRegen extends BaseRegenClass {

    public static String GUID = "magic_shield_regen";

    private MagicShieldRegen() {
        this.scaling = StatScaling.SCALING;
    }

    public static MagicShieldRegen getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public String getIconPath() {
        return "regen/magic_shield_regen";
    }

    @Override
    public StatGroup statGroup() {
        return StatGroup.Main;
    }

    @Override
    public String GUID() {
        return GUID;
    }

    @Override
    public Elements getElement() {
        return null;
    }

    @Override
    public boolean IsPercent() {
        return false;
    }

    @Override
    public String locNameForLangFile() {
        return "Magic Shield Regen";
    }

    private static class SingletonHolder {
        private static final MagicShieldRegen INSTANCE = new MagicShieldRegen();
    }
}
