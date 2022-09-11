package test.test.anvil;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class anvilcommand implements CommandExecutor, Listener {

    public static Inventory inv;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack item = new ItemStack(Material.COBBLESTONE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("0");
            item.setItemMeta(meta);

            ItemStack[] items = new ItemStack[3];
            items[0] = new ItemStack(item);

            new AnvilMenuManager().createAnvilMenu(p, items, "Quest Item Menge");
        }
        return false;
    }
}
