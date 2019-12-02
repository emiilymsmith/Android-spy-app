package edu.csus.ecs.androidspyapp;

import java.net.*;
import java.io.*;

public class Client {

    // initialize socket and input output streams
    private Socket socket            = null;
    //private DataInputStream  input   = null;
    private DataOutputStream out     = null;

    // constructor to put ip address and port
    public Client(String address, int port)
    {

        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // TODO: MIGHT NOT NEED THE INPUT SINCE THE MESSAGES WILL BE SENT VIA THE SEND MESSAGE
            //input  = new DataInputStream(System.in);

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

        try
        {
            this.out.writeChars(input);
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
