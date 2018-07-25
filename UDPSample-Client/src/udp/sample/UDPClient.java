package udp.sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * This program demonstrates how to implement a UDP Client program.
 * 
 * @author www.codejava.net
 */
public class UDPClient
{
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Syntax: UDPClient <hostname> <port>");
            System.out.println();
            return;
        }

        String hostname = args[0].toLowerCase().trim();
        int port = Integer.parseInt(args[1]);
        try
        {
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();
            while (true)
            {
                // Send packet
                DatagramPacket request = new DatagramPacket(new byte[1], 1, address, port);
                socket.send(request);

                // Receive response
                byte[] buffer = new byte[512];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);

                // Print out response message
                String quote = new String(buffer, 0, response.getLength());
                System.out.println(quote);
                System.out.println();
                Thread.sleep(10000); // 10 seconds
            }
        } catch (SocketTimeoutException e)
        {
            System.out.println("Timeout error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
