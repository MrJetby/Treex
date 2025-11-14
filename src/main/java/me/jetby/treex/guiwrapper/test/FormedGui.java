package me.jetby.treex.guiwrapper.test;

import me.jetby.treex.gui.Button;
import me.jetby.treex.gui.ButtonCommand;
import me.jetby.treex.gui.requirements.ClickRequirement;
import me.jetby.treex.gui.requirements.Requirements;
import me.jetby.treex.gui.requirements.ViewRequirement;
import me.jetby.treex.guiwrapper.GuiForm;
import me.jetby.treex.guiwrapper.XGui;
import me.jetby.treex.guiwrapper.item.wrappers.LegacyWrapper;
import me.jetby.treex.text.Colorize;
import me.jetby.treex.actions.ActionContext;
import me.jetby.treex.actions.ActionExecutor;
import me.jetby.treex.actions.ActionRegistry;
import me.jetby.treex.text.Papi;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FormedGui extends XGui {

    private final List<Integer> freeSlots = new ArrayList<>();

    public FormedGui(@NotNull GuiForm form) {
        super(form);
        registerButtons(form);
    }

    private void registerButtons(GuiForm form) {
        freeSlots.clear();

        Map<Integer, List<Button>> buttonsBySlot = new HashMap<>();
        for (Button button : form.getMenu().buttons()) {
            buttonsBySlot.computeIfAbsent(button.slot(), k -> new ArrayList<>()).add(button);
        }

        for (Map.Entry<Integer, List<Button>> entry : buttonsBySlot.entrySet()) {
            int slot = entry.getKey();
            List<Button> slotButtons = entry.getValue();

            slotButtons.sort(Comparator.comparingInt(Button::priority).reversed());

            Button selectedButton = null;
            boolean anyFreeSlot = false;

            for (Button button : slotButtons) {
                boolean visible = true;
                boolean freeSlotFromRequirements = false;

                if (!button.viewRequirements().isEmpty()) {
                    for (ViewRequirement requirement : button.viewRequirements()) {
                        boolean passed = Requirements.check(player, requirement);
                        if (!passed) {
                            if (requirement.freeSlot()) {
                                freeSlotFromRequirements = true;
                            } else {
                                visible = false;
                                break;
                            }
                        }
                    }
                }

                if (visible) {
                    selectedButton = button;
                    break;
                } else if (freeSlotFromRequirements) {
                    anyFreeSlot = true;
                }
            }

            if (selectedButton == null && anyFreeSlot) {
                freeSlots.add(slot);
                continue;
            }

            if (selectedButton != null) {
                registerButton(selectedButton);
            }
        }
    }

    private void registerButton(Button button) {

        boolean isFreeSlot = button.freeSlot();
        if (!isFreeSlot) {
            for (ViewRequirement requirement : button.viewRequirements()) {
                boolean passed = Requirements.check(player, requirement);
                if (!passed && requirement.freeSlot()) {
                    isFreeSlot = true;
                    break;
                }
            }
        }

        if (isFreeSlot) {
            freeSlots.add(button.slot());
            return;
        }
        LegacyWrapper wrapper = LegacyWrapper.builder(button.itemStack().getType()).build();
        wrapper.slot(button.slot());

        wrapper.displayName(Papi.set(player, Colorize.text(button.displayName())));
        wrapper.lore(Papi.set(player, Colorize.list(button.lore())));

        if (button.customModelData() > 0) {
            wrapper.customModelData(button.customModelData());
        }

        List<ItemFlag> itemFlags = new ArrayList<>();
        if (button.hideAttributes()) itemFlags.add(ItemFlag.HIDE_ATTRIBUTES);
        if (button.hideEnchantments()) itemFlags.add(ItemFlag.HIDE_ENCHANTS);

        if (itemFlags.size() == 2) {
            wrapper.flags(itemFlags.get(0), itemFlags.get(1));
        } else if (itemFlags.size() == 1) {
            wrapper.flags(itemFlags.get(0));
        }

        wrapper.onClick(event -> {
            event.setCancelled(true);
            ClickType clickType = event.getClick();

            for (ButtonCommand cmd : button.buttonCommands()) {
                if (cmd.clickType() == clickType || cmd.anyClick()) {
                    boolean allRequirementsPassed = true;

                    if (!cmd.clickRequirements().isEmpty()) {
                        for (ClickRequirement clickRequirement : cmd.clickRequirements()) {
                            if ((clickRequirement.anyClick() || clickRequirement.clickType() == clickType)) {
                                if (!Requirements.check(player, clickRequirement)) {
                                    ActionContext ctx = new ActionContext(player);
                                    ctx.put("button", button);

                                    List<String> denyCommands = clickRequirement.deny_commands().stream()
                                            .map(Colorize::text)
                                            .toList();

                                    ActionExecutor.execute(ctx, ActionRegistry.transform(denyCommands));
                                    allRequirementsPassed = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (allRequirementsPassed) {
                        ActionContext ctx = new ActionContext(player);
                        ctx.put("button", button);

                        List<String> actions = cmd.actions().stream()
                                .map(Colorize::text)
                                .toList();

                        ActionExecutor.execute(ctx, ActionRegistry.transform(actions));
                        break;
                    }
                }
            }
        });

        registerItem(wrapper);
    }
}