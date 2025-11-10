package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Md5Button {

    private final Pattern TAG_PATTERN = Pattern.compile(
            "<hover:([a-z_]+):'?(.*?)'?>?<click:([a-z_]+?):(.*?)>(.*?)</click></hover>",
            Pattern.CASE_INSENSITIVE
    );


    public void send(Player player, String message) {
        player.spigot().sendMessage(parse(message));
    }

    private BaseComponent[] parse(String message) {
        ComponentBuilder builder = new ComponentBuilder();
        Matcher matcher = TAG_PATTERN.matcher(message);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                builder.append(message.substring(lastEnd, matcher.start()));
            }

            String hoverType = matcher.group(1).toLowerCase();
            String hoverValue = matcher.group(2);
            String clickType = matcher.group(3).toLowerCase();
            String clickValue = matcher.group(4);
            String displayText = matcher.group(5);

            TextComponent component = new TextComponent(displayText);

            HoverEvent.Action hoverAction = getHoverAction(hoverType);
            if (hoverAction != null && hoverValue != null && !hoverValue.isEmpty()) {
                component.setHoverEvent(new HoverEvent(hoverAction,
                        new ComponentBuilder(hoverValue).create()));
            }

            ClickEvent.Action clickAction = getClickAction(clickType);
            if (clickAction != null && clickValue != null && !clickValue.isEmpty()) {
                component.setClickEvent(new ClickEvent(clickAction, clickValue));
            }

            builder.append(component);
            lastEnd = matcher.end();
        }

        if (lastEnd < message.length()) {
            builder.append(message.substring(lastEnd));
        }

        return builder.create();
    }

    private HoverEvent.Action getHoverAction(String type) {
        return switch (type) {
            case "show_text" -> HoverEvent.Action.SHOW_TEXT;
            case "show_item" -> HoverEvent.Action.SHOW_ITEM;
            case "show_entity" -> HoverEvent.Action.SHOW_ENTITY;
            default -> null;
        };
    }

    private ClickEvent.Action getClickAction(String type) {
        return switch (type) {
            case "run_command" -> ClickEvent.Action.RUN_COMMAND;
            case "suggest_command" -> ClickEvent.Action.SUGGEST_COMMAND;
            case "open_url" -> ClickEvent.Action.OPEN_URL;
            case "copy_to_clipboard" -> ClickEvent.Action.COPY_TO_CLIPBOARD;
            default -> null;
        };
    }
}
