package server;

/**
 * The Main class used to initialize the program.
 * 
 */
public class Main {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
	// TODO Auto-generated method stub

	View view = new View();
	Server server = new Server(5300);
        server.addObserver(view);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

}
