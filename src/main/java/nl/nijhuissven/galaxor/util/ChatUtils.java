package nl.nijhuissven.galaxor.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorFlag;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ChatUtils {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final String SPECIAL_FONT =
            "abcdefghijklmnopqrstuvwxyz" +
                    "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀѕᴛᴜᴠᴡxʏᴢ";

    private final Map<Character, Character> SPECIAL_FONT_MAP = createSpecialFontMap();

    private Map<Character, Character> createSpecialFontMap() {
        Map<Character, Character> map = new HashMap<>();
        String regular = SPECIAL_FONT.substring(0, 26);
        String special = SPECIAL_FONT.substring(26);

        for (int i = 0; i < regular.length(); i++) {
            map.put(regular.charAt(i), special.charAt(i));
        }
        return map;
    }

    public Component color(String message) {
        return miniMessage.deserialize(message);
    }

    public Component prefixed(String message, TagResolver... placeholders) {
        Component prefix = color("<gradient:#9061FF:#763FF8>ɢᴀʟᴀxᴏʀ<white><bold>｜<reset>");
        return miniMessage.deserialize(prefix.append(color(message)).toString())
                .decoration(TextDecoration.ITALIC, false);
    }

    public String raw(Component component) {
        return miniMessage.serialize(component);
    }

    public TextColor colorFromMiniMessage(String message) {
        List<TextColor> colors = new ArrayList<>();
        color(message).iterator(ComponentIteratorType.BREADTH_FIRST, ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME)
                .forEachRemaining(component -> {
                    if (component.color() == null) return;
                    colors.add(component.color());
                });
        if (colors.isEmpty()) return TextColor.fromHexString("#FFFFFF");
        return colors.getFirst();
    }

    public String small(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        for (char c : text.toLowerCase().toCharArray()) {
            result.append(SPECIAL_FONT_MAP.getOrDefault(c, c));
        }
        return result.toString();
    }
}
