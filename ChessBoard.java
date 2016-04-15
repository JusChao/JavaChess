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

	String[][] arr = new String[27][27]; // ��������

	String str = "1"; // ���ص���ұ��
	String rec = "3" + " " + "3" + " " + "3"; // ���������ص����ָ��
	String sss = null; // ���������ص����ӱ��

	BufferedReader reader;
	BufferedWriter writer;

	Socket socket;

	boolean ff = false; // ��Ӯ�ı��

	public ChessBoard() {
		connect();
		check();
	}

	// �ж��Ƿ�ʤ�����߳�
	public void check() { 
		new Thread() {
			public void run() {
				while (true) {
					win(arr);
				}
			};
		}.start();
	}

	//���ӷ���������
	public void connect() { 
		new Thread() {	
			public void run() {
				try { // ��׽�쳣
					socket = new Socket("127.0.0.1", 12345); // ʵ����Socket����
					while (true) {
						writer = new BufferedWriter(new OutputStreamWriter(
								socket.getOutputStream()));
						reader = new BufferedReader(new InputStreamReader(
								socket.getInputStream())); // ʵ����BufferedReader����
						getClientInfo();//���ý��շ�������Ϣ����
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	//���շ�������Ϣ����
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
				arr[Integer.parseInt(st[0])][Integer.parseInt(st[1])] = st[2]; //�����յ�����Ϣд������
				sss = st[2];
				str = st[3];
				repaint();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// �������������������Ϣ
	public void send(String s) { 
		try {
			writer.write(s + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) { //��ͼ����
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

	public void mousePressed(MouseEvent e) { //������
		if (str.equals(sss)) { //�ж��Ǹ�˭���ӣ���ֹ�����������
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
			if (arr[m][n] != null) { // ��ֹ�㱻����
			} else {					
				send(String.valueOf(m) + " " + String.valueOf(n) + " " + str); //���÷��ͷ���������õ����ݷ��͸�������
			}
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void win(String[][] arr) { //�ж���Ӯ����
		String s = "s";
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < 27; i++) {
			for (int j = 0; j < 27; j++) {  //����ÿ���㡣��ÿ��������ж�

				// �����ж�
				for (int m = 4; i > 3 && m > -1; m--) {
					if (arr[i - m][j] != null) {
						sb.append(arr[i - m][j]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}

				// �����ж�
				for (int m = 4; j > 3 && m > -1; m--) {
					if (arr[i][j - m] != null) {
						sb.append(arr[i][j - m]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}

				// �����ж�
				for (int m = 4; i > 3 && m > -1 && j > 3; m--) {
					if (arr[i - m][j - m] != null) {
						sb.append(arr[i - m][j - m]);
					}
				}
				ff = isWin(sb);
				if (ff) {
					return;
				}

				// �����ж�
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

	public boolean isWin(StringBuilder sb) { // �ж���Ӯ��Ĳ���
		int i = 100;
		if (sb.toString().equals("s11111")) {
			i = JOptionPane.showConfirmDialog(null, "�췽Ӯ���Ƿ����");
		}
		if (sb.toString().equals("s22222")) {
			i = JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ���Ƿ����");
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
