import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Pipes extends GameObj {

	private static ArrayList<Pipes> pipes = new ArrayList<Pipes>(); // holds all pipes currently on screen
	private Rectangle[] rects = new Rectangle[2];
	private static BufferedImage top, bottom;
	public static final int height = 350;
	private boolean keepingScore = true;
	public static int pipeGap = 110;
	private final int width = 60;
	public static int score = 0;
	private int tickCount = 0;
	public static int speed = 4;
	private static Handler h;

	public Pipes(int x, int y, Handler han) {
		super(x, y);
		updateRects(x, y);
		pipes.add(this);
		h = han;
	}

	public static ArrayList<Pipes> getPipes() {
		return pipes;
	}

	public static void setPipes(ArrayList<Pipes> pipes) {
		Pipes.pipes = pipes;
	}

	public boolean checkColision(Bird b) { // returns true if bird and either pipe are intersecting
		return rects[0].intersects(b.getRect()) || rects[1].intersects(b.getRect()) || b.y >= 383;
	}

	public static void restart() {
		score = 0;
		pipes = new ArrayList<Pipes>();
		Random r = new Random();
		Pipes p = new Pipes(Game.WIDTH + 60, r.nextInt((20 - -150) + 1) - 150, h);
		h.addObj(p);
	}

	public static void load() {
		try {
			top = ImageIO.read(new File("images\\top_pipe.png"));
			bottom = ImageIO.read(new File("images\\bottom_pipe.png"));
		} catch (Exception e) {
			System.out.println("pipe file error");
			e.getStackTrace();
			top = null;
			bottom = null;
		}
	}

	public void speedUp() {
		if (Handler.state != State.Menu && score % 5 == 0) {
			// speed++;
			System.out.println("speed up (disabled)");
		}
	}

	public void tick(State state) {
		if (keepingScore && x < 130) {
			keepingScore = false;
			score++;
			speedUp();
		}

		updateRects(x, y);
		// System.out.println(pipes.size());
		if (!Menu.gameIsOver) {
			x -= speed;
			Random r = new Random();
			if (tickCount == 80) { // spawns new pipe on right side of the screen
				int pipeY = r.nextInt((20 - -150) + 1) - 150;
				h.addObj(new Pipes(Game.WIDTH + 60, pipeY, h));
				System.out.println("spawned pipe at y = " + pipeY);
			}

			if (x < -61) { // removes pipe when it leaves the window
				System.out.println("removed pipe");
				pipes.remove(this);
				h.removeObj(this);
			}
		}
		tickCount++;
	}

	public void updateRects(int x, int y) {
		Rectangle rect1 = new Rectangle(x, y - pipeGap, width, height); // rectangles surround the pipe, its used to
																		// check colisions
		Rectangle rect2 = new Rectangle(x, y + height, width, height);
		rects[0] = rect1;
		rects[1] = rect2;
	}

	public void render(Graphics g, State state) {
		// System.out.println("pipe render state: " + state);
		if (state == State.Debug) {
			g.setColor(Color.GREEN);
			g.drawRect(x, y - pipeGap, width, height);
			g.drawRect(x, y + height, width, height);
		} else if (state == State.Game) {
			g.drawImage(bottom, x, y + 350, null);
			g.drawImage(top, x, y - pipeGap, null);

		}
	}
}