package damienstaebler.itemchatdisplay.Processors;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.i18n.I18nModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemProcessor {
    // {Item} or {Item:1-9}
    private final Pattern ITEM_PATTERN = Pattern.compile("\\{Item(?::([1-9]))?\\}");

    public String process(PlayerRef sender, String message) {
        Player player = sender.getReference()
                .getStore()
                .getComponent(sender.getReference(), Player.getComponentType());

        StringBuilder result = new StringBuilder();
        Matcher matcher = ITEM_PATTERN.matcher(message);
        int lastEnd = 0;

        while (matcher.find()) {
            result.append(message, lastEnd, matcher.start());

            // get slot number if exists.
            byte slot = getSlot(player, matcher.group(1));

            // get item name
            String itemName = getHotbarItem(player, slot);

            // formatted item text
            result.append(buildItemMessage(itemName, slot));

            lastEnd = matcher.end();
        }

        if (lastEnd < message.length()) {
            result.append(message, lastEnd, message.length());
        }

        return result.toString();
    }

    private byte getSlot(Player player, String slotText) {
        if (slotText == null) { // current slot if no number provided
            return player.getInventory().getActiveHotbarSlot();
        }

        try {
            int slot = Integer.parseInt(slotText);
            if (slot >= 1 && slot <= 9) {
                return (byte) (slot - 1);
            }
        } catch (Exception ignored) {}

        return -1;
    }

    private String getHotbarItem(Player player, byte itemSlot) {
        if (itemSlot < 0 || itemSlot >= 9) return null;

        ItemStack stack = player.getInventory().getHotbar().getItemStack(itemSlot);
        if (stack == null || stack.isEmpty()) return null;

        String key = stack.getItem().getTranslationProperties().getName();
        return I18nModule.get().getMessage("en-US", key);
    }

    private String buildItemMessage(String itemName, byte slot) {
        if (slot == -1) return "[Invalid Slot]";
        if (itemName == null) return "[Empty]";
        return itemName;
    }
}
