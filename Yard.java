package greedsnake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


class Yard extends Frame {
	
	private static final long serialVersionUID = -1749798633173891133L;
	// 活动区域
	private static final int ROWS = 30;// 行数
	private static final int COLS = 30;// 列数
	private static final int BLOCK_SIZE = 15;// 格子尺寸
	
	private Font fontGameOver = new Font("微软雅黑", Font.BOLD, 50);//字体
	private int score = 0;// 分数

	Image offScreenImage = null;// 创建一个位图，防止闪烁

	Snake snake = new Snake(this);// 创建一个蛇？？？
	Egg e = new Egg();// 随机创建一个蛋；

	PaintThread paintThread = new PaintThread();
	private boolean gameOver=false;//判断游戏是否结束

	public static int getROWS() {
		return ROWS;
	}

	public static int getCOLS() {
		return COLS;
	}

	public static int getBLOCK_SIZE() {
		return BLOCK_SIZE;
	}

	public int getScore() {
		return score;
	}
	// 设置分数
	public void setScore(int score) {
		this.score = score;
	}

	public void launch() {// 设置窗体
		this.setLocation(300, 0);// 窗口出现位置
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);// 窗口大小
		// 添加关闭事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		this.setTitle("贪吃蛇大作战");
		this.setVisible(true);// 窗口是否显示
		this.addKeyListener(new KeyMonitor());// 监听按键
		new Thread(paintThread).start();//启动线程
		
	}

	public static void main(String[] args) {
		new Yard().launch();
	}

	

	@Override
	public void paint(Graphics g) {// 画

		// 设置背景颜色
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);

		// 设置线的颜色
		g.setColor(Color.DARK_GRAY);

		// 画线
		for (int i = 1; i < ROWS; i++) {
			g.drawLine(0, i * BLOCK_SIZE, COLS * BLOCK_SIZE, BLOCK_SIZE * i);
		}
		for (int i = 1; i < ROWS; i++) {
			g.drawLine(i * BLOCK_SIZE, 0, BLOCK_SIZE * i, ROWS * BLOCK_SIZE);
		}

		g.setColor(Color.YELLOW);
		g.drawString("score:" + score, 10, 100);// 画分数

		if(gameOver) {
			g.setFont(fontGameOver);
			g.drawString("Game Over!", 100, 180);
			
			paintThread.pause();
		}
		
		g.setColor(c);

		snake.eat(e);
		snake.draw(g);// 画蛇
		e.draw(g);// 画蛋
	}

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		}
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void stop() {
		gameOver=true;//游戏结束
	}

	private class PaintThread implements Runnable {
		private boolean running = true;
		private boolean pause = false;
		@Override
		public void run() {
			while(running) {
				if(pause) continue; 
				else repaint();
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void pause() {
			this.pause = true;
		}
		
		public void reStart() {
			this.pause = false;
			snake = new Snake(Yard.this);
			gameOver = false;
		}
		
		public void gameOver() {
			running = false;
		}
		
	}

	private class KeyMonitor extends KeyAdapter {
		// 监听键值
		@Override
		public void keyPressed(KeyEvent e) {
			 int key = e.getKeyCode();
			 if(key == KeyEvent.VK_F5) {
			 paintThread.reStart();
			 }
			snake.keyPressed(e);
		}
		
	}
}
