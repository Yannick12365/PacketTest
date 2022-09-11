package test.test;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import test.test.anvil.AnvilMenuManager;
import test.test.anvil.anvilcommand;
import test.test.manager.ProtocolLibReader;
import test.test.npc.npccommand;

public final class Test extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("anvil").setExecutor(new anvilcommand());
        getCommand("npc").setExecutor(new npccommand());
        Bukkit.getPluginManager().registerEvents(new AnvilMenuManager(),this);

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        new ProtocolLibReader().readNPCClickPacket(manager,this);
        new ProtocolLibReader().readWindowClickPacket(manager, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
