package avelyn.nathan.apes.bungeechat;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.net.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BungeeChat extends Plugin{

    private ServerSocket serverSocket;

    public void onEnable() {
        try {
            serverSocket = new ServerSocket(25600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getProxy().getScheduler().schedule(this, () -> listenforServer(), 10L, TimeUnit.SECONDS);
    }

    private void listenforServer(){
        getLogger().info("Listening for Spigot Servers...Please start them.");
        boolean connected = false;
        while(true){
            try{
                Socket checkerSocket = new Socket(InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 1, (byte) 178}), 25566);
                connected = true;
                checkerSocket.close();
            } catch (UnknownHostException e) {}
            catch (ConnectException e){}
            catch (IOException e) {}
            if(connected){
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(socket.isBound())
                            recieveData(socket);
                    }
                }, 0L, 50L);
                getLogger().info("A Spigot Server has connected to the Bungee Network and is now sending Chat Input.");
                break;
            }
        }
    }

    private void recieveData(Socket socket){
        String message = "";
        char character;
        int readIn;
        try {
            InputStream in = socket.getInputStream();
            while((readIn = in.read()) > -1){
                character = (char) readIn;
                message = message.concat(String.valueOf(character));

                if(readIn == 0)
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
