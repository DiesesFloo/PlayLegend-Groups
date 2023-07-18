package net.playlegend.spigot.groupsystem.listener;

import net.playlegend.spigot.groupsystem.database.DatabaseRegistry;
import net.playlegend.spigot.groupsystem.database.util.DatabaseService;
import net.playlegend.spigot.groupsystem.groups.UserGeneric;
import net.playlegend.spigot.groupsystem.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        DatabaseService service = DatabaseRegistry.getDatabase().getService();
        Optional<UserGeneric> userOptional;

        try {
            userOptional = service.getUser(player.getUniqueId()).get();
        } catch (InterruptedException | ExecutionException e) {
            return;
        }

        if (userOptional.isEmpty()) {
            return;
        }

        UserGeneric user = userOptional.get();

        Message msg = new Message("quit-message");
        msg.setUser(user);
        msg.setUsername(player.getName());
        msg.setUsePrefix(false);

        event.setQuitMessage(msg.get());
    }

}
