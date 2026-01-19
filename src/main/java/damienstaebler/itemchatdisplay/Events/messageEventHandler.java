package damienstaebler.itemchatdisplay.Events;

import damienstaebler.itemchatdisplay.Processors.ItemProcessor;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class messageEventHandler {
    private static final ItemProcessor itemProcessor = new ItemProcessor();


    public static void onPlayerChat(PlayerChatEvent event) {
        PlayerRef sender = event.getSender();
        World world = Universe.get().getWorld(sender.getWorldUuid());
        assert world != null;

        CompletableFuture<String> future = new CompletableFuture<>();

        world.execute(() -> {
            String msg = event.getContent();

            msg = itemProcessor.process(sender, msg);

            future.complete(msg);
        });

        String finalMessage;

        try {
            finalMessage = future.get();
            event.setContent(finalMessage);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
