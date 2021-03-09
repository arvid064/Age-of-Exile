package com.robertx22.age_of_exile.player_skills.items.fishing;

import com.robertx22.age_of_exile.aoe_data.datapacks.models.IAutoModel;
import com.robertx22.age_of_exile.aoe_data.datapacks.models.ItemModelManager;
import com.robertx22.age_of_exile.database.base.CreativeTabs;
import com.robertx22.age_of_exile.database.data.currency.base.IShapelessRecipe;
import com.robertx22.age_of_exile.mmorpg.ModRegistry;
import com.robertx22.age_of_exile.player_skills.items.backpacks.IGatheringMat;
import com.robertx22.age_of_exile.player_skills.items.foods.SkillItemTier;
import com.robertx22.age_of_exile.uncommon.interfaces.IAutoLocName;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

public class ScribeInkItem extends Item implements IAutoLocName, IAutoModel, IGatheringMat, IShapelessRecipe {

    SkillItemTier tier;

    public ScribeInkItem(SkillItemTier tier) {
        super(new Settings().group(CreativeTabs.Professions));
        this.tier = tier;
    }

    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(tier.format);
    }

    @Override
    public void generateModel(ItemModelManager manager) {
        manager.generated(this);
    }

    @Override
    public AutoLocGroup locNameGroup() {
        return AutoLocGroup.Misc;
    }

    @Override
    public String locNameLangFileGUID() {
        return Registry.ITEM.getId(this)
            .toString();
    }

    @Override
    public String locNameForLangFile() {
        return tier.word + " Ink";
    }

    @Override
    public String GUID() {
        return "ink/" + tier.tier;
    }

    @Override
    public ShapelessRecipeJsonFactory getRecipe() {
        if (this.tier.higherTier() == null) {
            return null;
        }
        ShapelessRecipeJsonFactory fac = ShapelessRecipeJsonFactory.create(this, 2);
        fac.input(ModRegistry.INSCRIBING.INK_TIER_MAP.get(tier.higherTier()), 1);
        return fac.criterion("player_level", trigger());
    }
}

