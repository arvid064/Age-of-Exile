package com.robertx22.age_of_exile.event_hooks.ontick;

import com.robertx22.age_of_exile.a_libraries.curios.MyCurioUtils;
import com.robertx22.age_of_exile.config.forge.ModConfig;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.Gear;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Arrays;
import java.util.List;

public class UnequipGear {

    // dont drop weapons becasuse then newbies can't use stuff like axes at low level!

    public static List<EquipmentSlot> SLOTS = Arrays.asList(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD, EquipmentSlot.OFFHAND);

    static void drop(PlayerEntity player, EquipmentSlot slot, ItemStack stack, MutableText txt) {
        ItemStack copy = stack.copy();

        player.equipStack(slot, ItemStack.EMPTY); // todo is this good?

        if (player.getEquippedStack(slot)
            .isEmpty()) {
            player.dropItem(copy, false);
            player.sendMessage(txt
                , false);
        } else {
            player.equipStack(slot, copy);
            System.out.print("Error in unequipping gear, weird!!!");
        }
    }

    static void drop(PlayerEntity player, ICurioStacksHandler handler, int number, ItemStack stack, MutableText txt) {

        ItemStack copy = stack.copy();
        handler.getStacks()
            .setStack(number, ItemStack.EMPTY);
        player.dropItem(copy, false);
        player.sendMessage(txt, false);
    }

    public static void onTick(PlayerEntity player) {

        int lvl = Load.Unit(player)
            .getLevel();

        int runewords = 0;
        int uniques = 0;

        for (EquipmentSlot slot : SLOTS) {

            ItemStack stack = player.getEquippedStack(slot);

            GearItemData gear = Gear.Load(stack);

            if (gear != null) {

                if (lvl < gear.level) {
                    drop(player, slot, stack, new LiteralText("You are too low level to use that item.").formatted(Formatting.RED));
                } else if (gear.isUnique()) {
                    uniques++;
                    if (uniques > ModConfig.get().Server.MAX_UNIQUE_GEARS_ON_PLAYER) {
                        drop(player, slot, stack, new LiteralText("You cannot equip that many unique items.").formatted(Formatting.RED));
                    }
                } else if (gear.hasRuneWord()) {
                    runewords++;
                    if (runewords > ModConfig.get().Server.MAX_RUNEWORD_GEARS_ON_PLAYER) {
                        drop(player, slot, stack, new LiteralText("You cannot equip that many runed items.").formatted(Formatting.RED));
                    }
                }
            }
        }

        for (ICurioStacksHandler handler : MyCurioUtils.getHandlers(player)) {

            for (int i = 0; i < handler
                .getSlots(); i++) {

                ItemStack stack = handler
                    .getStacks()
                    .getStack(i);

                if (!stack.isEmpty()) {
                    GearItemData gear = Gear.Load(stack);

                    if (gear != null) {
                        if (lvl < gear.level) {
                            drop(player, handler, i, stack, new LiteralText("You are too low level to use that item.").formatted(Formatting.RED));
                        } else if (gear.isUnique()) {
                            uniques++;
                            if (uniques > ModConfig.get().Server.MAX_UNIQUE_GEARS_ON_PLAYER) {
                                drop(player, handler, i, stack, new LiteralText("You cannot equip that many unique items.").formatted(Formatting.RED));
                            }
                        } else if (gear.hasRuneWord()) {
                            runewords++;
                            if (runewords > ModConfig.get().Server.MAX_RUNEWORD_GEARS_ON_PLAYER) {
                                drop(player, handler, i, stack, new LiteralText("You cannot equip that many runed items.").formatted(Formatting.RED));
                            }
                        }
                    }
                }

            }

        }
    }
}