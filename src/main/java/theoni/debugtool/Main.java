package theoni.debugtool;

import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import theoni.debugtool.commands.*;
import theoni.debugtool.listener.*;

import cn.nukkit.blockstate.exception.InvalidBlockStateException;

public class Main extends PluginBase implements Listener {

    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents((Listener)new EventListener(this), (Main)this);
        this.getServer().getCommandMap().register("help", (Command) new DebugToolCommand(this));
    }

    public static void updateBlock(Block block) {
        int id = block.getId();
        int meta = block.getDamage();
        try {
            block.getLevel().setBlock(block.getLocation(), Block.get(id, meta + 1));
        } catch (IndexOutOfBoundsException | InvalidBlockStateException ignored) {
            block.getLevel().setBlock(block.getLocation(), Block.get(id, 0));
        }
        
    }
}
