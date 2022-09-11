package test.test.manager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity.c; //Eventuell EnumEntityUseAction
import net.minecraft.world.EnumHand;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class PacketReader {
    private final Player player;
    private Channel channel;

    public PacketReader(Player player) {
        this.player = player;
    }

    public void inject(){
        CraftPlayer cPlayer = (CraftPlayer)this.player;
        channel = cPlayer.getHandle().b.a().m;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {
                arg2.add(packet);
                readPacket(packet);
            }
        });
    }

    public void uninject(){
        if(channel.pipeline().get("PacketInjector") != null){
            channel.pipeline().remove("PacketInjector");
        }
    }

    public void readPacket(Packet<?> packet) {
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            if(getValue(packet,"b").toString().split("\\$")[1].charAt(0) == '1'){
                player.sendMessage("Linksklick");
            }else {
                player.sendMessage("Rechtsklick");
            }
        }else if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInWindowClick")) {
            if ((Integer) getValue(packet, "d") == 2) {     //Slot der geklickt wurde
                org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy((ItemStack) getValue(packet, "g"));   //Item auf dem Slot
                if (item.getType() != Material.AIR) {
                    player.sendMessage(item.getItemMeta().getDisplayName());
                    Inventory i = (Inventory) getValue(packet,"i");
                }
            }
        }
    }

    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception ignored){}
        return null;
    }
}