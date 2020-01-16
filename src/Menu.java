import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Menu {

	private static BufferedImage title, start, over, end, restart;
	private static int playBtnX = 175;
	private static int playBtnY = Game.HEIGHT / 2;
	public static boolean gameIsOver = false;
	public static int highScore;

	public Menu() {
		load();
	}

	public static void gameOver() {
		try {
			File file = new File("highScore.txt");
			Scanner sc = new Scanner(file);
			highScore = Integer.parseInt(sc.nextLine());
//			System.out.println(highScore);
			if (highScore < Pipes.score) {
				file = new File("highScore.txt");
				FileWriter fr = new FileWriter(file);
				fr.write(String.valueOf(Pipes.score));
				fr.write(System.getProperty("line.separator")); // makes a new line
				fr.close();
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("record update error");
		}
		gameIsOver = true;
	}

	public static void mouseClicked() {
		Point mousePos;
		try {
			System.out.println("checking click");
			mousePos = Input.getMousePos(); // returns null if mouse is off screen
		} catch (NullPointerException e) {
			mousePos = null;
			System.out.println("mouse off screen");
		}

		// System.out.println(mousePos);
		if (inBounds(mousePos, 188, 288, 338, 369)) { // checks if mouse is on start button
			Handler.state = State.Game;
		} else if (inBounds(mousePos, 180, 345, 180 + restart.getWidth(), 345 + restart.getHeight())) {
			Game.restart();
		}
	}

	public void tick(State state) {

	}

	public static void restart() {
		gameIsOver = false;
	}

	public static void render(Graphics g, State state) {
		// System.out.println("Game Over = " + gameIsOver);
		Font font = new Font("Comic Sans MS", Font.BOLD, 30);
		g.setFont(font);
		if (state == State.Game) {
			g.drawString(Integer.toString(Pipes.score), 250, 40); // draw score
		}
		if (state == State.Menu) {
			g.drawImage(title, 125, 50, null);
			g.drawImage(start, playBtnX, playBtnY, null);
		} else if (state == State.Debug) {

		} else if (gameIsOver) {
			g.drawImage(over, 40, -30, null);
			g.drawImage(end, 30, 50, null);
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(Pipes.score), 270, 230); // draw score
			g.drawString(Integer.toString(highScore), 270, 320); // draw high score
			g.drawImage(restart, 180, 345, null);
		}
	}

	public static boolean inBounds(Point mousePos, int x1, int y1, int x2, int y2) {
		// System.out.println(x1 + "\t" + y1 + "\t" + x2 + "\t" + y2);
		return ((mousePos.getX() >= x1 && mousePos.getX() <= x2) // check if mouse is in bounds
				&& (mousePos.getY() >= y1 && mousePos.getY() <= y2));
	}

	public static void load() {
		try {
			title = ImageIO.read(new File("images\\title.png"));
			start = ImageIO.read(new File("images\\play_button.png"));
			over = ImageIO.read(new File("images\\Game_Over.png"));
			end = ImageIO.read(new File("images\\end_screen.png"));
			restart = ImageIO.read(new File("images\\restart.png"));
		} catch (Exception e) {
			System.out.println("menu file error");
			e.getStackTrace();
			title = null;
			start = null;
			over = null;
			end = null;
		}
	}

}
