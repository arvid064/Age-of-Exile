package com.robertx22.age_of_exile.database.data.player_skills;

import com.robertx22.age_of_exile.database.data.MinMax;
import com.robertx22.age_of_exile.uncommon.interfaces.IWeighted;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SkillDropReward implements IWeighted {

    public int lvl_req = 1;
    public int weight = 1000;
    public String item_id = "";
    public MinMax count = new MinMax(1, 1);

    public SkillDropReward(int lvlreq, int weight, Item item, MinMax count) {
        this.lvl_req = lvlreq;
        this.weight = weight;
        this.item_id = Registry.ITEM.getId(item)
            .toString();
        this.count = count;
    }

    public ItemStack getRewardStack() {
        ItemStack stack = new ItemStack(Registry.ITEM.get(new Identifier(item_id)));
        stack.setCount(count.random());
        return stack;
    }

    public SkillDropReward() {
    }

    @Override
    public int Weight() {
        return weight;
    }
}