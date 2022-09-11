package test.test.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import test.test.Test;
import test.test.anvil.AnvilMenuManager;
import test.test.npc.NPC;
import test.test.npc.NPCManager;

import java.lang.reflect.InvocationTargetException;

public class ProtocolLibReader {
    public void readNPCClickPacket(ProtocolManager pm, Test main) {
        if (pm != null) {
            pm.addPacketListener(new PacketAdapter(main, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    PacketContainer packet = event.getPacket();

                    for (NPC npc : new NPCManager().getNPClist()) {
                        if (packet.getIntegers().read(0) == npc.getEntityplayer().ae()) {
                            try {
                                EnumWrappers.Hand hand = packet.getEnumEntityUseActions().read(0).getHand();
                                EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                                if (hand == EnumWrappers.Hand.MAIN_HAND && action == EnumWrappers.EntityUseAction.INTERACT) {
                                    event.getPlayer().sendMessage("Right Click");
                                }
                            }catch (IllegalArgumentException exception){
                                event.getPlayer().sendMessage("Left Click");
                            }
                        }
                    }
                }
            });
        }
    }


    private String anvilInput;

    public String getAnvilInput(){
        return anvilInput;
    }

    public void readWindowClickPacket(ProtocolManager pm, Test main){
        if (pm != null) {
            pm.addPacketListener(new PacketAdapter(main, PacketType.Play.Client.WINDOW_CLICK) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    Inventory invFound = null;
                    for (Inventory inv: new AnvilMenuManager().getInvList()){
                        for (HumanEntity viewer: inv.getViewers()){
                            if (viewer == event.getPlayer()){
                                invFound = inv;
                            }
                        }
                    }

                    if (invFound != null){
                        PacketContainer packet = event.getPacket();

                        if (packet.getIntegers().read(2) == 2) {

                            event.getPlayer().sendMessage(packet.getItemModifier().read(0).getItemMeta().getDisplayName());
                            anvilInput = packet.getItemModifier().read(0).getItemMeta().getDisplayName();

                            PacketContainer container = new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW);
                            try {
                                pm.sendServerPacket(event.getPlayer(), container);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

        }
    }


}