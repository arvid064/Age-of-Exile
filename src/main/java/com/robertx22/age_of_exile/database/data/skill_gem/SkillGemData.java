package com.robertx22.age_of_exile.database.data.skill_gem;

import com.robertx22.age_of_exile.database.data.StatModifier;
import com.robertx22.age_of_exile.database.data.rarities.SkillGemRarity;
import com.robertx22.age_of_exile.database.registry.Database;
import com.robertx22.age_of_exile.saveclasses.ExactStatData;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.ITooltipList;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.age_of_exile.uncommon.utilityclasses.TooltipUtils;
import com.robertx22.library_of_exile.utils.LoadSave;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Storable
public class SkillGemData implements ITooltipList {

    @Store
    public String id = "";
    @Store
    public String rar = "";
    @Store
    public int stat_perc = 100;
    @Store
    public int lvl = 100;

    // only support gems should have random stats
    @Store
    public List<StatModifier> random_stats = new ArrayList<>();

    public SkillGem getSkillGem() {
        return Database.SkillGems()
            .get(id);
    }

    public SkillGemRarity getRarity() {
        return Database.SkillGemRarities()
            .get(rar);
    }

    public static SkillGemData fromStack(ItemStack stack) {
        try {
            SkillGemData gem = LoadSave.Load(SkillGemData.class, new SkillGemData(), stack.getTag(), "gem");
            return gem;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveToStack(ItemStack stack) {
        LoadSave.Save(this, stack.getOrCreateTag(), "gem");
    }

    @Override
    public List<Text> GetTooltipString(TooltipInfo info) {

        List<Text> list = new ArrayList<>();
        list.add(new LiteralText(""));

        List<ExactStatData> cStats = getSkillGem().getConstantStats(this);

        if (!cStats.isEmpty()) {
            list.add(new LiteralText("Support Gem Stats: "));

            cStats
                .forEach(x -> list.addAll(x.GetTooltipString(info)));
        }
        if (!random_stats.isEmpty()) {
            list.add(new LiteralText(""));
            list.add(new LiteralText("Random Stats: "));
            this.getSkillGem()
                .getRandomStats(this)
                .forEach(x -> list.addAll(x.GetTooltipString(info)));

        }

        list.add(new LiteralText(""));
        list.add(TooltipUtils.level(this.lvl));

        TooltipUtils.removeDoubleBlankLines(list);

        return list;

    }
}
