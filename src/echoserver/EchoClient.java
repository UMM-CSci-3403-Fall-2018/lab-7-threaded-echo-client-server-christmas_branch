package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class KeyboardReader implements Runnable {
	public OutputStream socketOutput;
	public Socket socket;

	// Takes socket and pushes data from standard input into the OutputStream
	// Shuts down outputStream when done to let server know when to start
	public KeyboardReader(Socket socket) throws IOException {
		this.socketOutput = socket.getOutputStream();
		this.socket = socket;
	}
	public void run() {
		try{
			int readByte;
			while ((readByte = System.in.read()) != -1) {
				socketOutput.write(readByte);
				socketOutput.flush();
			}
			socket.shutdownOutput();
		} catch (IOException io) {}
		}
	}

// Takes an inputStream and puts data into standard output
class ServerReader implements Runnable {
	public InputStream socketInput;
	public ServerReader(InputStream socketInput) {
		this.socketInput = socketInput;
	}

	public void run()  {
		try {
			int socketByte;
			while((socketByte = socketInput.read()) != -1) {
				System.out.write(socketByte);
				System.out.flush();
			}
		} catch (IOException io) {}
		}
	}



public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {

		try {
			// Creates socket
			Socket socket = new Socket("localhost", PORT_NUMBER);
			// Creates inputStream
			InputStream socketInputStream = socket.getInputStream();
			// Starts thread that handles the output to the server
			Thread output = new Thread(new KeyboardReader(socket));
			output.start();
			// Starts thread that handles the input from the server
			Thread input = new Thread(new ServerReader(socketInputStream));
			input.start();
			// Ends the input just in case
			input.join();
			// Flushes out standard output just in case
			System.out.flush();
			// Closes socket thus disconnecting from the server
			socket.close();
		} catch (InterruptedException iex) {
			System.out.println("Exception in thread: "+iex.getMessage());
		}

	}
}
