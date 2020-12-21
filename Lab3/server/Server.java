package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Observable;

/**
 * This class represents the server, it implements Runnable due to intended
 * thread usage in the main initializing class.
 * 
 */
public class Server extends Observable implements Runnable {

	/** The port number. */
	private int port;

	/** The socket. */
	private DatagramSocket socket;

	/** The byte buffer. */
	private byte[] buffer = new byte[1024];

	/**
	 * Instantiates a new server.
	 *
	 * @param port the port number
	 */
	public Server(int port) {
		this.port = port;
		try {
			this.socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The run method receives byte arrays from the client, converts them to
	 * int arrays and passes these packages onto any observer.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			try {
				System.out.println("lyssnar på port " + port);
				socket.receive(dp);
				byte[] clientBytes = dp.getData();
				int[] clientInts = toIntArray(clientBytes);
				setChanged();
				notifyObservers(clientInts);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Converts a byte array to an int array.
	 *
	 * @param bytes the bytes
	 * @return the int[]
	 */
	public int[] toIntArray(byte[] bytes) {
		IntBuffer intBuf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		int[] array = new int[intBuf.remaining()];
		intBuf.get(array);
		return array;
	}

}
