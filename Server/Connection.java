import java.io.*;
import java.net.Socket;

/**
 * Created by Tristen Tulkens and Jacob Smith on 2017-02-20.
 */
public class Connection{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Connection(){}

    public void startConnection(Socket socket){
        this.socket = socket;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);
        }catch(IOException e){
            System.err.println("Unable to get reader/writer");
        }
    }

    public String recieve(){
        String text = null;
        try{
            text = in.readLine();
        }catch(IOException e){
            System.err.println("Unable to read from " + e.getMessage());
        }
        return text;
    }

    public void send(String output){
        out.println(output); //Doesn't ever throw IO exception
    }

    public void closeConnection(){
        try {
            out.close();
            in.close();
            socket.close();
        }catch (IOException e) {
            System.err.println("Unable to close writer, reader, or socket: " + e.getMessage());
        }
    }
}
