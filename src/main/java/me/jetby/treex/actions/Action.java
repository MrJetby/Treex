package me.jetby.treex.actions;

import org.jetbrains.annotations.NotNull;

public interface Action {
    void execute(@NotNull ActionContext ctx);
}
