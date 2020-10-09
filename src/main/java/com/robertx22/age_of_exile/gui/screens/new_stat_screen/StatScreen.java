package com.robertx22.age_of_exile.gui.screens.new_stat_screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.robertx22.age_of_exile.capability.entity.EntityCap;
import com.robertx22.age_of_exile.capability.player.PlayerStatsCap;
import com.robertx22.age_of_exile.database.data.stats.IUsableStat;
import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.database.data.stats.types.UnknownStat;
import com.robertx22.age_of_exile.database.data.stats.types.core_stats.Dexterity;
import com.robertx22.age_of_exile.database.data.stats.types.core_stats.Intelligence;
import com.robertx22.age_of_exile.database.data.stats.types.core_stats.Strength;
import com.robertx22.age_of_exile.database.data.stats.types.core_stats.base.BaseCoreStat;
import com.robertx22.age_of_exile.database.data.stats.types.generated.AttackDamage;
import com.robertx22.age_of_exile.database.data.stats.types.generated.ElementalPenetration;
import com.robertx22.age_of_exile.database.data.stats.types.generated.ElementalResist;
import com.robertx22.age_of_exile.database.data.stats.types.generated.ElementalSpellDamage;
import com.robertx22.age_of_exile.database.data.stats.types.resources.*;
import com.robertx22.age_of_exile.gui.bases.BaseScreen;
import com.robertx22.age_of_exile.gui.bases.INamedScreen;
import com.robertx22.age_of_exile.mmorpg.Ref;
import com.robertx22.age_of_exile.saveclasses.unit.StatData;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.NumberUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.RenderUtils;
import com.robertx22.age_of_exile.vanilla_mc.items.misc.ResetStatPointsItem;
import com.robertx22.age_of_exile.vanilla_mc.packets.SpendStatPointsPacket;
import com.robertx22.age_of_exile.vanilla_mc.packets.sync_cap.PlayerCaps;
import com.robertx22.age_of_exile.vanilla_mc.packets.sync_cap.RequestSyncCapToClient;
import com.robertx22.library_of_exile.gui.HelpButton;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.library_of_exile.utils.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatScreen extends BaseScreen implements INamedScreen {

    static int sizeX = 228;
    static int sizeY = 166;

    MinecraftClient mc = MinecraftClient.getInstance();

    public enum StatType {
        MAIN, ELEMENTAL
    }

    boolean isMainScreen = true;
    StatType statToShow = StatType.MAIN;

    static HashMap<StatType, List<List<Stat>>> STAT_MAP = new HashMap<>();

    static void addTo(StatType type, List<Stat> stats) {

        if (!STAT_MAP.containsKey(type)) {
            STAT_MAP.put(type, new ArrayList<>());
        }
        STAT_MAP.get(type)
            .add(stats);
    }

    static {

        addTo(StatType.MAIN, Arrays.asList(Health.getInstance(), MagicShield.getInstance(), Mana.getInstance()));
        addTo(StatType.MAIN, Arrays.asList(HealthRegen.getInstance(), MagicShieldRegen.getInstance(), ManaRegen.getInstance()));

        addTo(StatType.ELEMENTAL, new AttackDamage(Elements.Elemental).generateAllPossibleStatVariations());
        addTo(StatType.ELEMENTAL, new ElementalSpellDamage(Elements.Elemental).generateAllPossibleStatVariations());
        addTo(StatType.ELEMENTAL, new ElementalResist(Elements.Elemental).generateAllPossibleStatVariations());
        addTo(StatType.ELEMENTAL, new ElementalPenetration(Elements.Elemental).generateAllPossibleStatVariations());

    }

    public StatScreen() {
        super(sizeX, sizeY);
    }

    @Override
    public Identifier iconLocation() {
        return new Identifier(Ref.MODID, "textures/gui/main_hub/icons/stat_overview.png");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int ticks) {

        buttons.forEach(b -> {
            if (GuiUtils.isInRectPoints(new Point(b.x, b.y), new Point(b.getWidth(), b.getWidth()),
                new Point((int) x, (int) y)
            )) {
                b.onClick(x, y);
            }
        });
        return super.mouseReleased(x, y, ticks);

    }

    @Override
    public Words screenName() {
        return Words.Stats;
    }

    @Override
    public void init() {
        super.init();

        this.buttons.clear();
        this.children.clear();

        EntityCap.UnitData data = Load.Unit(mc.player);
        PlayerStatsCap.IPlayerStatPointsData stats = Load.statPoints(mc.player);

        int XSPACING = 60;
        int YSPACING = 19;

        // CORE STATS

        int xpos = guiLeft + 95;
        int ypos = guiTop + 25;
        buttons.add(new StatButton(Dexterity.INSTANCE, xpos, ypos));
        buttons.add(new IncreaseStatButton(data, stats, Dexterity.INSTANCE, xpos - 19, ypos + 1));
        ypos += 20;
        buttons.add(new StatButton(Intelligence.INSTANCE, xpos, ypos));
        buttons.add(new IncreaseStatButton(data, stats, Intelligence.INSTANCE, xpos - 19, ypos + 1));
        ypos += 20;
        buttons.add(new StatButton(Strength.INSTANCE, xpos, ypos));
        buttons.add(new IncreaseStatButton(data, stats, Strength.INSTANCE, xpos - 19, ypos + 1));

        if (isMainScreen) {
            xpos = guiLeft + 12;
            ypos = guiTop + 90;
        } else {
            xpos = guiLeft + 12;
            ypos = guiTop + 12;
        }

        int ynum = 0;
        for (List<Stat> list : STAT_MAP.get(statToShow)) {
            for (Stat stat : list) {
                addButton(new StatButton(stat, xpos, ypos + (YSPACING * ynum)));
                ynum++;
            }
            ynum = 0;
            xpos += XSPACING;
        }

        List<Text> list = new ArrayList<>();
        list.add(new LiteralText("Allocate stats here"));
        list.add(new LiteralText(""));
        list.add(new LiteralText("These stats determine your playstyle."));
        list.add(new LiteralText("To wear gear that gives Armor, you need strength,"));
        list.add(new LiteralText("Magic shield > Intelligence, Dodge > Dexterity etc."));
        list.add(new LiteralText(""));
        list.add(new LiteralText("To reset stats, you need to craft:"));
        list.add(new LiteralText(new ResetStatPointsItem().locNameForLangFile()));
        this.addButton(new HelpButton(list, guiLeft + sizeX - 30, guiTop + 5));

    }

    private static final Identifier BACKGROUND = new Identifier(Ref.MODID, "textures/gui/stats.png");
    private static final Identifier WIDE_BACKGROUND = new Identifier(Ref.MODID, "textures/gui/full_stats_panel.png");

    @Override
    public void render(MatrixStack matrix, int x, int y, float ticks) {

        if (isMainScreen) {
            mc.getTextureManager()
                .bindTexture(BACKGROUND);
        } else {
            mc.getTextureManager()
                .bindTexture(WIDE_BACKGROUND);
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexture(matrix, mc.getWindow()
                .getScaledWidth() / 2 - sizeX / 2,
            mc.getWindow()
                .getScaledHeight() / 2 - sizeY / 2, 0, 0, sizeX, sizeY
        );

        super.render(matrix, x, y, ticks);

        buttons.forEach(b -> b.renderToolTip(matrix, x, y));

        int xe = guiLeft + 32;
        int ye = guiTop + 75;

        if (isMainScreen) {
            InventoryScreen.drawEntity(xe, ye, 30, xe - x, ye - y, mc.player);

            String str = "Level: " + Load.Unit(mc.player)
                .getLevel();
            GuiUtils.renderScaledText(matrix, xe, ye - 60, 0.6F, str, Formatting.YELLOW);

            int xpos = guiLeft + 95;
            int ypos = guiTop + 15;

            String points = "Points: " + Load.statPoints(mc.player)
                .getAvailablePoints(Load.Unit(mc.player));
            GuiUtils.renderScaledText(matrix, xpos, ypos, 1, points, Formatting.GREEN);
        }
    }

    private static final Identifier BUTTON_TEX = new Identifier(Ref.MODID, "textures/gui/button.png");
    static int STAT_BUTTON_SIZE_X = 18;
    static int STAT_BUTTON_SIZE_Y = 18;

    static int PLUS_BUTTON_SIZE_X = 13;
    static int PLUS_BUTTON_SIZE_Y = 13;

    public class StatButton extends TexturedButtonWidget {

        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        Stat stat;

        public StatButton(Stat stat, int xPos, int yPos) {
            super(xPos, yPos, STAT_BUTTON_SIZE_X, STAT_BUTTON_SIZE_Y, 0, 0, STAT_BUTTON_SIZE_Y, BUTTON_TEX, (button) -> {
            });

            this.stat = stat;
        }

        @Override
        public void renderToolTip(MatrixStack matrix, int x, int y) {
            if (isInside(x, y)) {
                List<Text> tooltip = new ArrayList<>();

                tooltip.add(stat
                    .locName()
                    .formatted(Formatting.GREEN));

                tooltip.addAll(stat
                    .getCutDescTooltip());

                GuiUtils.renderTooltip(matrix,
                    tooltip, x, y);

            }
        }

        public boolean isInside(int x, int y) {
            return GuiUtils.isInRect(this.x, this.y, STAT_BUTTON_SIZE_X, STAT_BUTTON_SIZE_Y, x, y);
        }

        @Override
        public void renderButton(MatrixStack matrix, int x, int y, float f) {
            if (!(stat instanceof UnknownStat)) {

                String str = getStatString(Load.Unit(mc.player)
                    .getUnit()
                    .getCalculatedStat(stat), Load.Unit(mc.player));

                Identifier res = stat
                    .getIconLocation();

                RenderUtils.render16Icon(matrix, res, this.x, this.y);

                StatScreen.this.drawStringWithShadow(matrix, font, str, this.x + STAT_BUTTON_SIZE_X, this.y + 2, Formatting.GOLD.getColorValue());

            }
        }

    }

    public static String getStatString(StatData data, EntityCap.UnitData unitdata) {
        Stat stat = data.GetStat();

        String v1 = NumberUtils.formatForTooltip(data
            .getFirstValue());
        String v2 = NumberUtils.formatForTooltip(data
            .getSecondValue());

        String str = "";

        if (stat.UsesSecondValue()) {
            str = v1 + "-" + v2;

        } else {
            str = v1;
        }

        if (stat.IsPercent()) {
            str += '%';
        }

        if (stat instanceof IUsableStat) {
            IUsableStat usable = (IUsableStat) stat;

            String value = NumberUtils.format(
                usable.getUsableValue((int) data
                    .getAverageValue(), unitdata.getLevel()) * 100);

            str = "" + value + "%";

        }
        return str;

    }

    class IncreaseStatButton extends TexturedButtonWidget {

        PlayerStatsCap.IPlayerStatPointsData data;
        Stat stat;
        EntityCap.UnitData unitdata;

        public IncreaseStatButton(EntityCap.UnitData unitdata, PlayerStatsCap.IPlayerStatPointsData data,
                                  Stat stat, int xPos, int yPos) {
            super(xPos, yPos, PLUS_BUTTON_SIZE_X, PLUS_BUTTON_SIZE_Y, 0, 0, PLUS_BUTTON_SIZE_Y, BUTTON_TEX, (button) -> {

                Packets.sendToServer(new SpendStatPointsPacket(stat));
                Packets.sendToServer(new RequestSyncCapToClient(PlayerCaps.STAT_POINTS));

            });

            this.data = data;
            this.stat = stat;
            this.unitdata = unitdata;

        }

        @Override
        public void renderToolTip(MatrixStack matrix, int x, int y) {
            if (isInside(x, y)) {

                List<Text> tooltip = new ArrayList<>();

                tooltip.add(
                    stat.locName()
                        .formatted(Formatting.BLUE));

                if (stat instanceof BaseCoreStat) {
                    BaseCoreStat core = (BaseCoreStat) stat;
                    tooltip.addAll(core.getCoreStatTooltip(unitdata, unitdata.getUnit()
                        .getCalculatedStat(stat)));
                }
                GuiUtils.renderTooltip(matrix, tooltip, x, y);

            }
        }

        public boolean isInside(int x, int y) {
            return GuiUtils.isInRect(this.x, this.y, PLUS_BUTTON_SIZE_X, PLUS_BUTTON_SIZE_Y, x, y);
        }

    }

}


