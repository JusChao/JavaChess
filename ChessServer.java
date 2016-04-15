package connectchess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChessServer {
	private Socket socket;
	private ServerSocket server;
	private Vector<Socket> vector = new Vector<Socket>();
	private int i = 0;

	public void getserver() {
		try {
			server = new ServerSocket(12345);
			while (true) {
				if (i < 2) {
					socket = server.accept();
					i++;
					System.out.println(i + "位玩家连接到服务器");
					vector.add(socket);
					getClientInfo(socket);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getClientInfo(Socket socket2) {
		final Socket socket = socket2;
		new Thread() {
			public void run() {
				String s = null;
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					while ((s = reader.readLine()) != null) {
						for (int i = 0; i < vector.size(); i++) {
							Socket socket = vector.get(i);
							PrintWriter writer;
							writer = new PrintWriter(socket.getOutputStream(),
									true);
							writer.println(s + " " + (i + 1));
						}
					}

					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	public static void main(String[] args) {
		ChessServer cs = new ChessServer();
		cs.getserver();
	}
}
