package me.jetby.treex.gui.requirements;

public record ViewRequirement(
        String type,
        String input,
        String output,
        boolean freeSlot,
        String permission
) implements Requirement { }
