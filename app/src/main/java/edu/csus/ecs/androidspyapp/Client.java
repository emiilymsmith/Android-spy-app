package edu.csus.ecs.androidspyapp;

import android.util.Log;

import java.net.*;
import java.io.*;

public class Client {

    // initialize socket and input output streams
    private      Socket           socket = null;
    private      DataOutputStream out    = null;

    // constructor to put ip address and port
    public Client(String address, int port)
    {

        // establish a connection
        try
        {
            Log.i("SPY", "Attempting connection.");
            socket = new Socket(address, port);
//          System.out.println("Connected");
//          Log.i("SPY", "Connected and stuff.");

            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }

    public void sendMessage(String input) {

        Log.i("SPY", "Message = "+ input);

        try
        {
            //this.out.writeChars(input);
            this.out.write(input.getBytes("UTF-8"));
        }
        catch (IOException i)
        {
            System.out.println(i);
        }

    }

    public void closeConnection() {

        // close the connection
        try
        {
            //input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }

}
