package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class KeyboardReader implements Runnable {
	public static OutputStream socketOutput;
	public KeyboardReader(OutputStream socketOutput) {
		this.socketOutput = socketOutput;
	}
	public void run() {
		try{
			int readByte;
			while ((readByte = System.in.read()) != -1) {
				socketOutput.write(readByte);
				socketOutput.flush();
			}
		} catch (IOException io) {}
		}
	}

	class ServerReader implements Runnable {
		public static InputStream socketInput;
		public ServerReader(InputStream socketInput) {
			this.socketInput = socketInput;
		}

		public void run()  {
			try {
				int socketByte;
				while((socketByte = socketInput.read()) != -1) {
					System.out.write(socketByte);
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

			Socket socket = new Socket("localhost", PORT_NUMBER);

			InputStream socketInputStream = socket.getInputStream();
			OutputStream socketOutputStream = socket.getOutputStream();
			int readByte;

			Thread output = new Thread(new KeyboardReader(socketOutputStream));
			output.start();

			Thread input = new Thread(new ServerReader(socketInputStream));
			input.start();

			output.join();
			input.join();

			System.out.flush();

			socket.close();
		} catch (InterruptedException iex) {
			System.out.println("Exception in thread: "+iex.getMessage());
		}

	}
}
