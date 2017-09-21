package avelyn.nathan.apes.bungeechat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.*;

public class BungeeChat2 extends JavaPlugin implements Listener{

    private boolean chatting;

    private StatusLogger statusLogger;

    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
        //modifyConfig();
        StatusConsoleListener listener = new StatusConsoleListener(Level.INFO);
        statusLogger = StatusLogger.getLogger();
        statusLogger.registerListener(listener);
        chatting = false;
        //initialize();
    }

    private void initialize(){
        getServer().getScheduler().scheduleSyncRepeatingTask(this,
            () -> {
                byte[] sendingFeed;
                if(chatting){
                    chatting = false;
                    String chatFeed = statusLogger.getStatusData().get(statusLogger.getStatusData().size() - 1).getMessage().getFormattedMessage();
                    sendingFeed = new byte[chatFeed.length()];
                    for(int i = 0; i < chatFeed.length(); i++)
                        sendingFeed[i] = (byte) chatFeed.charAt(i);
                } else {
                    String blankFeed = "Testing";
                    sendingFeed = new byte[blankFeed.length()];
                    for(int i = 0; i < blankFeed.length(); i++)
                        sendingFeed[i] = (byte) blankFeed.charAt(i);
                }
                try{
                    Socket socket = new Socket(InetAddress.getByAddress((new byte[]{(byte) 192, (byte) 168, (byte) 1, (byte) 178})), 25600);
                    socket.getOutputStream().write(sendingFeed);
                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        ,0L, 1L);
    }

    private void modifyConfig(){
        StatusConfiguration statusConfig = new StatusConfiguration().withStatus(Level.INFO);
        statusConfig.initialize();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent chatEvent){
        chatting = true;
        getLogger().info("Test: " + statusLogger.getStatusData().size());
    }
}