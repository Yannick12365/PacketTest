package test.test.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import test.test.manager.PacketReader;
import test.test.npc.NPCManager;

public class PlayerJoin implements Listener{
    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        new PacketReader(event.getPlayer()).inject();
        new NPCManager().sentJoinPacket(event.getPlayer());
    }
}
