package com.thevoxelbox.voxelclock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Butters
 */
public class VoxelClock extends JavaPlugin implements CommandExecutor, Listener {
    
    private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Calendar c;
    
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    public String getFormattedTime(){
        return df.format(Calendar.getInstance().getTime());
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cmnd.getName().equalsIgnoreCase("clock")){
            cs.sendMessage(ChatColor.GOLD + "[VoxelTime] " + ChatColor.DARK_AQUA + getFormattedTime());
            return true;
        }
       return false; 
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage(ChatColor.GOLD + "[VoxelTime] " + ChatColor.DARK_AQUA + getFormattedTime());
    }
    
    
}
