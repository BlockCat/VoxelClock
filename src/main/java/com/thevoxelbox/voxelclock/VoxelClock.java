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
        c = Calendar.getInstance();
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    public String getFormattedTime(){
        return df.format(c.getTime());
    }
    
    public String getFormattedTime(int h, int m, int s){
        c.add(Calendar.HOUR, h);
        c.add(Calendar.MINUTE, m);
        c.add(Calendar.SECOND, s);
        return df.format(c.getTime());
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if(cmnd.getName().equalsIgnoreCase("clock")){
            cs.sendMessage(ChatColor.GOLD + "[VoxelTime] " + ChatColor.DARK_AQUA + getFormattedTime());
            return true;
        }
        else if(cmnd.getName().equalsIgnoreCase("whenis")){
            
            if(args.length == 0){
                cs.sendMessage(ChatColor.RED + "Please enter a time to lookup");
            }
            else{
                
                int hourSkip = 0;
                int minuteSkip = 0;
                int secondSkip = 0;
                
                for(int x = 0; x < args.length; x++){
                    if(args[x].startsWith("h")){
                        hourSkip = Integer.parseInt(args[x].replace("h", ""));
                    }
                    else if(args[x].startsWith("m")){
                        minuteSkip = Integer.parseInt(args[x].replace("m", ""));
                    }
                    else if(args[x].startsWith("s")){
                        secondSkip = Integer.parseInt(args[x].replace("s", ""));
                    }
                }
                
                cs.sendMessage(ChatColor.GOLD + "[VoxelTime] " + ChatColor.DARK_AQUA + getFormattedTime(hourSkip, minuteSkip, secondSkip));
                c.clear();
                
            }
            
            return true;
        }
       return false; 
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage(ChatColor.GOLD + "[VoxelTime] " + ChatColor.DARK_AQUA + getFormattedTime());
    }
    
    
}
