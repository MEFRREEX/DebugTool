package theoni.debugtool.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import theoni.debugtool.Main;

public class DebugToolCommand extends Command {

    Main plugin;
    Config config;
    Config mobs;

    public DebugToolCommand(Main plugin) {
        super("debugtool", "Debug tool");
        this.setAliases(new String[]{"dt"});
        this.plugin = plugin;
        this.config = plugin.getConfig();

        commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[] {
                CommandParameter.newEnum("subcommand", new CommandEnum("Subcommand", "help", "about", "info"))
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getString("messages.ingame"));
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            if (player.hasPermission("debugtool.use")) {
                this.giveTool(player);
                player.sendMessage(config.getString("messages.received"));
            } else {
                player.sendMessage(config.getString("messages.permission"));
            }
        } else {
            switch(args[0]) {
                case "help":
                    sender.sendMessage("§fUsage:\n/debugtool §8- §7Get an info tool\n§f/debugtool about §8- §7About the plugin\n§f/debugtool help §8- §7Help\n\n§aUse the information tool on blocks or entities to get more information about them.");
                    break;
                case "about":
                case "info":
                    if (player.hasPermission("debugtool.about")) {
                        sender.sendMessage("§aUse the information tool on blocks or entities to get more information about them.\n\n§fThis plugin was written for free distribution and can be downloaded at §7https://cloudburstmc.org/resources/info-tool.862/.\n§fDeveloper: MEFRREEXX");
                    } else {
                        player.sendMessage(config.getString("messages.permission"));
                    }
                    break;
                default:
                    player.sendMessage(config.getString("messages.command-not-found"));
                    break;
            }
        }
        return false;
    }

    public void giveTool(Player player) {
        String[] s = config.getString("tool.id").split(":");
        Item item = Item.get(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
        item.setCustomName(config.getString("tool.name"));
        player.getInventory().addItem(item);
    }
}
