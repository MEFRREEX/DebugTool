package theoni.debugtool.listener;

import java.util.HashMap;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import theoni.debugtool.Main;

public class EventListener implements Listener {

    Main plugin;
    Config config;
    Config mobs;

    HashMap<Player, Integer> delay;

    public EventListener(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        this.delay = new HashMap<Player, Integer>();
        Task bowTask = new Task() {
            public void onRun(final int i) {
                Set<Player> players = delay.keySet();
                if (delay.size() > 0) {
                    for (Player player : players) {
                        if (player.isOnline()) {
                            int count = delay.get(player) + 1;
                            delay.put(player, count);
                        }
                    }
                }
            }
        };
        plugin.getServer().getScheduler().scheduleRepeatingTask(bowTask, 1);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Item item = event.getItem();

        if (item.getName().equals(config.getString("tool.name"))) {
            if (!player.hasPermission("debugtool.use")) return;
            if (block instanceof BlockAir) return;

            if (delay.get(player) == null) {
                delay.put(player, 0);

                Main.updateBlock(block);
                player.sendActionBar(config.getString("updated"));

                Task task = new Task() {
                    public void onRun(final int i) {
                        if (player.isOnline() && delay.containsKey(player)) {
                            delay.remove(player);
                        }
                    }
                };
                plugin.getServer().getScheduler().scheduleDelayedTask(task, config.getInt("tool.delay") * 1);
            }
            event.setCancelled();
        }
    }
}
