package com.thevoxelbox.voxelclock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
    
    private List<ClockPlayer> loginQue;
    
    private boolean usingDelayedBoradcast;
    public String reminderMessage;
    public int loginMessageDelayTime;
    
    @Override
    public void onEnable(){
        c = Calendar.getInstance();
        getServer().getPluginManager().registerEvents(this, this);
        loginQue = new ArrayList<ClockPlayer>();
        
        FileConfiguration config = getConfig();
         config.addDefault("LoginMessage.enabled", true);
         config.addDefault("LoginMessage.delay", 2);
         config.addDefault("LoginMessage.message", "PSA: THIS IS IMPORTANT");
         
        config.options().copyDefaults(true);
        saveConfig();
        
        usingDelayedBoradcast = config.getBoolean("LoginMessage.enabled");
        reminderMessage = ChatColor.translateAlternateColorCodes('&', config.getString("LoginMessage.message"));
        loginMessageDelayTime = config.getInt("LoginMessage.delay");
        
        if(usingDelayedBoradcast){
        
            getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

                @Override
                public void run() {

                    if(!loginQue.isEmpty()){

                        for(ClockPlayer player: loginQue){
                            player.update(1);
                            if(player.loginTimer == loginMessageDelayTime){
                                player.getPlayer().sendMessage(reminderMessage);
                            }
                        }

                    }

                }
            }, 0L, 20L);            
            
        }
                
        
    }
    
    public String getFormattedTime(){
        return df.format(c.getTime());
    }
    
    public String getFormattedTime(int h, int m, int s){
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), h, m, s);
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
                c = Calendar.getInstance();
                
            }
            
            return true;
        }
       return false; 
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage(ChatColor.GOLD + "[VoxelTime] " + ChatColor.DARK_AQUA + getFormattedTime());
        
        if(usingDelayedBoradcast)
        loginQue.add(new ClockPlayer(event.getPlayer()));
    }
    
    private class ClockPlayer {
        
        private Player player;
        private int loginTimer;
        
        public ClockPlayer(Player p){
            this.player = p;
            this.loginTimer = 0;
        }
        
        public void update(int timeToAdd){
            loginTimer += timeToAdd;
        }

        public int getLoginTimer() {
            return loginTimer;
        }

        public Player getPlayer() {
            return player;
        }
        
    }
    
}
