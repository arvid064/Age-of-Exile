package com.robertx22.age_of_exile.uncommon.effectdatas;

import com.robertx22.age_of_exile.database.data.spells.components.Spell;
import com.robertx22.age_of_exile.saveclasses.unit.AllShieldsData;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.LivingEntity;

public class GiveShieldEvent extends EffectEvent {

    @Nullable
    public Spell spell;

    public int seconds = 0;

    public GiveShieldEvent(float amount, int seconds, LivingEntity caster, LivingEntity target) {
        super(amount, caster, target);
        this.seconds = seconds;
    }

    @Override
    protected void activate() {

        if (this.data.isCanceled()) {
            return;
        }

        if (target.isAlive()) {

            this.targetData.getResources()
                .shields.giveShield(new AllShieldsData.ShieldData(data.getNumber(), seconds * 20));

        }
    }

    public static String ID = "on_absorb_damage";

    @Override
    public String GUID() {
        return ID;
    }
}