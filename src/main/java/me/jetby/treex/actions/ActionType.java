package me.jetby.treex.actions;

import lombok.Getter;
import me.jetby.treex.actions.impl.standard.*;
import org.jetbrains.annotations.Nullable;

@Getter
public enum ActionType {

    MESSAGE(new Message()),
    MINI_MESSAGE(new MiniMessage()),
    MM(new MiniMessage()),
    DELAY(new Delay()),
    MSG(new Message()),
    SOUND(new Sound()),
    EFFECT(new Effect()),
    ACTIONBAR(new ActionBar()),
    TITLE(new Title()),
    CONSOLE(new Console()),
    PLAYER(new Player()),
    BROADCASTMESSAGE(new BroadcastMessage()),
    BROADCAST_MESSAGE(new BroadcastMessage()),
    MESSAGE_ALL(new BroadcastMessage()),
    BC(new BroadcastMessage()),
    BROADCAST(new BroadcastMessage()),
    MSG_ALL(new BroadcastMessage()),
    BROADCASTSOUND(new BroadcastSound()),
    BROADCAST_SOUND(new BroadcastSound()),
    SOUND_ALL(new BroadcastSound()),
    BROADCASTTITLE(new BroadcastTitle()),
    BROADCAST_TITLE(new BroadcastTitle()),
    BROADCASTACTIONBAR(new BroadcastActionBar()),
    BROADCAST_ACTIONBAR(new BroadcastActionBar());

    private final Action action;

    ActionType(Action action) {
        this.action = action;
    }

    @Nullable
    public static ActionType getType(String name) {
        if (name == null) return null;

        try {
            return ActionType.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
