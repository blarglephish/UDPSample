package udp.sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This program demonstrates how to implement a UDP server program
 * 
 * @author www.codejava.net
 */
public class UDPServer
{
    private DatagramSocket socket;
    private List<String> listQuotes = new ArrayList<String>();
    private Random random;
    
    public UDPServer(int port) throws SocketException
    {
        this.socket = new DatagramSocket(port);
        this.random = new Random();
    }
    
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Syntax: UDPServer <file> <port>");
            System.out.println();
            return;
        }
        
        String quoteFile = args[0].toLowerCase().trim();
        int port = Integer.parseInt(args[1]);
        try
        {
            UDPServer server = new UDPServer(port);
            server.loadQuotesFromFile(quoteFile);
            server.doService();
        }
        catch (SocketException e)
        {
            System.out.println("Socket error: " + e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("I/O Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void doService() throws IOException
    {
        while (true)
        {
            DatagramPacket request = new DatagramPacket(new byte[1], 1);
            socket.receive(request);
            
            String quote = getRandomQuote();
            byte[] buffer = quote.getBytes();
            
            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();
            
            DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(response);
        }
    }

    private void loadQuotesFromFile(String quoteFile) throws IOException
    {
        FileReader fr = new FileReader(quoteFile);
        BufferedReader br = new BufferedReader(fr);
        
        String quote;
        while ((quote = br.readLine()) != null)
        {
            listQuotes.add(quote);
        }
        
        br.close();
        fr.close();
    }

    private String getRandomQuote()
    {
        int randomIndex = random.nextInt(listQuotes.size());
        String randomQuote = listQuotes.get(randomIndex);
        
        return randomQuote;
    }
}
