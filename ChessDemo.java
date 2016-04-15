package connectchess;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessDemo extends JFrame {
	ChessBoard cb = new ChessBoard(); 
	public ChessDemo() {
		setTitle("chaochaoµÄÎå×ÓÆå");
		setBounds(500,150,560,580);
		add(cb);
		addMouseListener(cb);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		ChessDemo chessDemo = new ChessDemo();
		chessDemo.setVisible(true);
	}
}
