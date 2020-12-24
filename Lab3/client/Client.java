package client;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


/**
 * The Client class creates simple input instructions for the user to manipulate
 * the game GUI on the server side. The client inputs gets inserted to a byte array
 * and is then sent to a selected IP-address and port.
 * 
 */
public class Client {
    
    /**
     * The main method initializes as soon as the client runs the program. 
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
	// TODO Auto-generated method stub

	// Sets IPv6 as the standard. InetAddress commands will select a IPv6 address
	// rather than IPv4 if one is available.
	System.setProperty("java.net.preferIPv6Addresses", "true");
	
	System.out.println("Enter three ints or type exit");
	
	DatagramSocket socket = new DatagramSocket();
	Scanner scan = new Scanner(System.in);
	InetAddress ip = InetAddress.getLoopbackAddress();
	
	while (true) {
	    System.out.println("Enter x-coordinate (0-200) or type exit: ");
	    String inp1 = scan.nextLine();
	    
	    if (inp1.equals("exit")) {
		break;
	    } 
	    
	    try {
	    	int x = Integer.parseInt(inp1);
		    
		    System.out.println("Enter y-coordinate (0-200): ");
		    String inp2 = scan.nextLine();
		    int y = Integer.parseInt(inp2);
		    
		    System.out.println("Enter a color (1-8): ");
		    String color = scan.nextLine();
		    int col = Integer.parseInt(color);
		    
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    DataOutputStream dos = new DataOutputStream(baos);
		    dos.writeInt(x);
		    dos.writeInt(y);
		    dos.writeInt(col);
		    byte[] bytes = baos.toByteArray();
		    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ip, 5300);
		    socket.send(packet);
	    } catch (NumberFormatException n) {
	    	System.out.println("Fel format");
	    	continue;
	    }
	}
	
	socket.close();
	scan.close();
    }

}
