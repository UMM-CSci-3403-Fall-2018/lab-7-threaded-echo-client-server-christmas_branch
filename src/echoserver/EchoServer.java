package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

// Takes a socket and creates both and input and output stream
// Takes data from input and moves it into the output, sends output back to client
class serverWork implements Runnable {
	public OutputStream socketOutput;
	public InputStream socketInput;
	public Socket socket;

	public serverWork(Socket socket) throws IOException {
		this.socket = socket;
		this.socketInput = socket.getInputStream();
		this.socketOutput = socket.getOutputStream();
	}

	public void run() {
		try {
			int b;
			while ((b = socketInput.read()) != -1) {
				socketOutput.write(b);
			}
			socket.shutdownOutput();
		} catch (IOException io) {}
	}
}

public class EchoServer {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		// Creates serverSocket
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		// Creates a thread pool of 100 threads to handle 100 connections
		ExecutorService executor = Executors.newFixedThreadPool(100);
		while (true) {
			// Accepts client request to connect
			Socket socket = serverSocket.accept();
			// Starts a thread from the thread pool
			executor.execute(new serverWork(socket));
			}
		}
	}
