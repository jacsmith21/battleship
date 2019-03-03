import java.io.*;
import java.net.*;

public class ClientConnection extends Connection{
    
	public ClientConnection(){}
    
    public void createConnection(String host, int port){
        Socket socket = null;
		while(true){
			try{
				socket = new Socket(host, port);
				break;
			}catch(UnknownHostException e){
				System.err.println("Unknown host: " + host);
				System.exit(-1);
			}catch(IOException e){
				System.err.println("Unable to get I/O connection to: " + host + " on port: " + port);
				System.exit(-1);
			}
			port++;
		}
        super.establishReadWrite(socket);
    }
}
