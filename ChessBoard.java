package connectchess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessBoard extends JPanel implements MouseListener {
	int x = 2000;
	int y = 2000;

	String[][] arr = new String[27][27]; // 棋盘数组

	String str = "1"; // 返回的玩家编号
	String rec = "3" + " " + "3" + " " + "3"; // 服务器返回的清空指令
	String sss = null; // 服务器返回的棋子编号

	BufferedReader reader;
	BufferedWriter writer;

	Socket socket;

	boolean ff = false; // 输赢的标记

	public ChessBoard() {
		connect();
		check();
	}

	// 判断是否胜利的线程
	public void check() { 
		new Thread() {
			public void run() {
				while (true) {
					win(arr);
				}
			};
		}.start();
	}

	//连接服务器方法
	public void connect() { 
		new Thread() {	
			public void run() {
				try { // 捕捉异常
					socket = new Socket("127.0.0.1", 12345); // 实例化Socket对象
					while (true) {
						writer = new BufferedWriter(new OutputStreamWriter(
								socket.getOutputStream()));
						reader = new BufferedReader(new InputStreamReader(
								socket.getInputStream())); // 实例化BufferedReader对象
						getClientInfo();//调用接收服务器信息方法
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	//接收服务器信息方法
	private void getClientInfo() {  

		String s = null;
		try {
			while ((s = reader.readLine()) != null) {
				String[] st = s.split(" ");
				if (st[0].equals("3")) {
					for (int j = 0; j < arr.length; j++) {
						for (int j2 = 0; j2 < arr.length; j2++) {
							arr[j][j2] = null;
						}
					}
					repaint();
				}
				arr[Integer.parseInt(st[0])][Integer.parseInt(st[1])] = st[2]; //将接收到的信息写入数组
				sss = st[2];
				str = st[3];
				repaint();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 向服务器发送新增点信息
	public void send(String s) { 
		try {
			writer.write(s + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) { //绘图方法
		super.paint(g);
		g.setColor(Color.black);
		g.drawRect(10, 10, 520, 520);
		for (int i = 30; i < 550; i = i + 20) {
			g.drawLine(i, 10, i, 530);
			g.drawLine(10, i, 530, i);
		}
		for (int i = 0; i < 27; i++) {
			for (int j = 0; j < 27; j++) {
				if (arr[i][j] != null) {
					if (arr[i][j].equals("1")) {
						g.setColor(Color.red);
						g.fillOval(i * 20 - 10, j * 20 - 10, 20, 20);
					} else if (arr[i][j].equals("2")) {
						g.setColor(Color.black);
						g.fillOval(i * 20 - 10, j * 20 - 10, 20, 20);
					}
				}

			}
		}

		x = 0;
		y = 0;
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) { //鼠标监听
		if (str.equals(sss)) { //判断是该谁下子，防止玩家下两个子
		} else {
			int a = e.getX();
			int b = e.getY();
			if ((a / 10) % 2 == 1) {
				x = (a / 10 - 1) * 10 - 10;
			} else {
				x = (a / 10) * 10 - 10;
			}
			if ((b / 10) % 2 == 1) {
				y = (b / 10 - 1) * 10 - 30;
			} else {
				y = (b / 10) * 10 - 30;
			}
			int m = 0;
			int n = 0;
			m = x / 10 / 2 + 1;
			n = y / 10 / 2 + 1;
			if (arr[m][n] != null) { // 防止点被覆盖
			} else {					
				send(String.valueOf(m) + " " + String.valueOf(n) + " " + str); //调用发送方法，将获得的数据发送给服务器
			}
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void win(String[][] arr) { //判断输赢方法
		String s = "s";
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < 27; i++) {
			for (int j = 0; j < 27; j++) {  //遍历每个点。对每个点进行判断

				// 横向判断
				for (int m = 4; i > 3 && m > -1; m--) {
					if (arr[i - m][j] != null) {
						sb.append(arr[i - m][j]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}

				// 纵向判断
				for (int m = 4; j > 3 && m > -1; m--) {
					if (arr[i][j - m] != null) {
						sb.append(arr[i][j - m]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}

				// 右下判断
				for (int m = 4; i > 3 && m > -1 && j > 3; m--) {
					if (arr[i - m][j - m] != null) {
						sb.append(arr[i - m][j - m]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}

				// 左下判断
				for (int m = 4; i < 22 && m > -1 && j > 3; m--) {
					if (arr[i + m][j - m] != null) {
						sb.append(arr[i + m][j - m]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}
			}
		}
	}

	public boolean isWin(StringBuilder sb) { // 判断输赢后的操作
		int i = 100;
		if (sb.toString().equals("s11111")) {
			i = JOptionPane.showConfirmDialog(null, "红方赢，是否继续");
		}
		if (sb.toString().equals("s22222")) {
			i = JOptionPane.showConfirmDialog(null, "黑方赢，是否继续");
		}
		sb.delete(1, 5);
		if (i == 0) {
			try {
				writer.write(rec + "\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
