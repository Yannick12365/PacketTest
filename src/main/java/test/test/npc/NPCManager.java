package test.test.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import test.test.Test;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class NPCManager {
    private static final ArrayList<NPC> NPClist = new ArrayList<>();

    public ArrayList<NPC> getNPClist(){
        return NPClist;
    }
    public void createNPC(Player p, String n) {
        boolean exist = false;

        for (NPC existNPC : NPClist) {
            if (existNPC.getName().equalsIgnoreCase(n)) {
                exist = true;
            }
        }
        if (!exist) {

            DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();

            WorldServer world = ((CraftWorld) Objects.requireNonNull(Bukkit.getWorld(p.getWorld().getName()))).getHandle();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), n);

            EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile, null);
            entityPlayer.b(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());

            NPC npc = new NPC(entityPlayer, gameProfile, world, n);

            sentNPCPacket(npc.getEntityplayer());
            NPClist.add(npc);
        } else {
            p.sendMessage("Ein NPC mit diesem Namen existiert schon!");
        }
    }

    private void sentNPCPacket(EntityPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));
            connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.getBukkitYaw() * 256 /360)));
        }
    }

    public void sentJoinPacket(Player player) {
        for (NPC npc : NPClist) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc.getEntityplayer()));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc.getEntityplayer()));
            connection.a(new PacketPlayOutEntityHeadRotation(npc.getEntityplayer(), (byte) (npc.getEntityplayer().getBukkitYaw() * 256 /360)));
        }
    }
}
