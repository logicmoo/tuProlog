package alice.tuprolog.json.test;

import java.net.*;

import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.Prolog;

import java.io.*;

public class ServerTest extends Thread {
	private ServerSocket serverSocket;

	public ServerTest(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(60000);
	}

	public void run() {
		while(true) {
			try {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
         
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
         
				String state = in.readUTF();
				//System.out.println(state);
				Prolog prolog = Prolog.fromJSON(state);
				
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				try {
					out.writeUTF(prolog.solveNext().toString());
					out.writeUTF(prolog.solveNext().toString());
				} catch (NoMoreSolutionException e) {
					e.printStackTrace();
				}
				server.close();
         
			}catch(SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String[] args) {
		int port = 40000;
		try {
			Thread t = new ServerTest(port);
			t.start();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}