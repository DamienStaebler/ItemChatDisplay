package damienstaebler.itemchatdisplay;

import damienstaebler.itemchatdisplay.Events.messageEventHandler;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.jetbrains.annotations.NotNull;

public class ItemChatDisplay extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ItemChatDisplay(@NotNull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, messageEventHandler::onPlayerChat);
    }
}
