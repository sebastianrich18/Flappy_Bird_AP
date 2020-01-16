import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Bird extends GameObj {

	private static BufferedImage wingUp, wingDown;
	private final int width = 32, height = 32;
	private boolean wingIsDown = true;
	private boolean isDead = false;
	private Rectangle rect;
	private int yVel;

	public Bird(int x, int y) {
		super(x, y);
		this.yVel = 1;
		this.rect = new Rectangle(x, y, width, height);
	}

	public void restart() {
		x = 130;
		y = 300;
		yVel = 1;
	}

	public void jump() {
		if (!Menu.gameIsOver)
			yVel = -10;
	}

	public int getYVel() {
		return yVel;
	}

	public Rectangle getRect() {
		return this.rect;
	}

	public void tick(State state) {
		// System.out.println("Bird tick state: " + state);
		if (Handler.state != State.Menu) {
			y += yVel;
			yVel += 1;
			this.rect = new Rectangle(x, y, width, height);
			if (Handler.state != State.Debug && y > Game.HEIGHT - 129) {
				isDead = true;
				y = Game.HEIGHT - 129;
				yVel = 0;
				Menu.gameOver();
			}
		}
	}

	public boolean isDead() {
		return isDead;
	}

	public void wingUp() {
		wingIsDown = false;
	}

	public void wingDown() {
		wingIsDown = true;
	}

	public static void load() {
		try {
			wingDown = ImageIO.read(new File("images\\bird_wing_down.png"));
			wingUp = ImageIO.read(new File("images\\bird_wing_up.png"));
		} catch (Exception e) {
			System.out.println("bird file error");
			e.getStackTrace();
			wingDown = null;
			wingUp = null;
		}
	}

	public void render(Graphics g, State state) {
		// System.out.println("Bird render state: " + state);
		if (state == State.Game) {
			if (wingIsDown) {
				g.drawImage(wingDown, x, y, null);
			} else {
				g.drawImage(wingUp, x, y, null);
			}
		} else if (state == State.Debug) {
			g.setColor(Color.ORANGE);
			g.drawOval(x, y, 32, 32);
		}
	}
}