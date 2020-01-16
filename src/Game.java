
// engine adapted from RealTutsGML on YT

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -4692329564653388891L;
	public static final int WIDTH = 284 * 2, HEIGHT = 512;
	public BufferedImage background, ground;
	public Random r = new Random();
	public int g1 = WIDTH - 568; // g1 and g2 are x values for 2 ground images
	public static int didJump;
	public int g2 = g1 + 568;
	public boolean running;
	private Thread thread;
	public Window window;
	private State state;
	private static Handler h;
	public static Menu menu;
	private Input in;
	private Pipes p;
	public static Bird b;

	public static void main(String[] args) {
		new Game();
	}

	public Game() {
		load();
		window = new Window(WIDTH, HEIGHT, "Flappy Bird", this);
		h = new Handler();
		b = new Bird(130, 300);
		in = new Input(b);
		this.addMouseListener(in);
		this.addKeyListener(in);
		h.addObj(b);
		Random r = new Random();
		p = new Pipes(WIDTH + 60, r.nextInt((20 - -150) + 1) - 150, h);
		h.addObj(p);
		menu = new Menu();
	}

	public void run() {
		// System.out.print("started");
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 30.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running)
				render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}

	public void tick() {
		state = Handler.state;
		for (Pipes p : Pipes.getPipes()) {
			if (Handler.state != State.Debug && p.checkColision(b)) {
				// popUp();
				Menu.gameOver();
			}
		}
		h.tick();
		if (state != State.Menu && !Menu.gameIsOver) {
			if (g1 < -568) { // moves ground
				g1 = g2 + 568;
			}
			if (g2 < -568) {
				g2 = g1 + 568;
			}
			g1 -= Pipes.speed;
			g2 -= Pipes.speed;
			// System.out.println(g1 + "\t" + g2);
		}
		// write();
		didJump = 0;
	}

	public void render() {

		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		if (state == State.Debug) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			h.render(g);
			g.setColor(Color.BLACK);
			g.drawRect(g1, HEIGHT - 100, 568, 110);
			g.setColor(Color.RED);
			g.drawRect(g2, HEIGHT - 100, 568, 110);
		} else if (state == State.Game) {
			g.drawImage(background, 0, 0, null);
			g.drawImage(background, WIDTH / 2, 0, null);
			h.render(g);
			g.drawImage(ground, g1, HEIGHT - 100, null);
			g.drawImage(ground, g2, HEIGHT - 100, null);
		} else if (state == State.Menu) {
			g.drawImage(background, 0, 0, null);
			g.drawImage(background, WIDTH / 2, 0, null);
			h.render(g);
			g.drawImage(ground, g1, HEIGHT - 100, null);
			g.drawImage(ground, g2, HEIGHT - 100, null);
		}

		g.dispose();
		bs.show();

	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void restart() {
		System.out.println("Restarting");
		Handler.restart();
		Pipes.restart();
		b.restart();
		h.addObj(b);
		Menu.restart();
	}

	public void write() { // writes data to txt file
		int xDist, yDist;
		try {
			if (Pipes.getPipes().get(0).getX() - b.getX() < 0) { // check if pipes are behind bird
				xDist = Pipes.getPipes().get(1).getX() - b.getX();
				yDist = Pipes.getPipes().get(1).getY() - b.getY() + Pipes.height;
			} else {
				xDist = Pipes.getPipes().get(0).getX() - b.getX();
				yDist = Pipes.getPipes().get(0).getY() - b.getY() + Pipes.height;
			}
		} catch (Exception e) {
			xDist = -6969;
			yDist = -6969;
			e.printStackTrace();
			System.out.println("Pipe out of bounds");
		}
		try {
			File file = new File("DATA.txt");
			FileWriter fr = new FileWriter(file, true);
			fr.write(xDist + "," + yDist + "," + b.getYVel() + "," + didJump);
			fr.write(System.getProperty("line.separator")); // makes a new line
			fr.close();
		} catch (Exception e) {
			System.out.println("Wtrie file error");
		}
	}

	public void load() {
		try {
			background = ImageIO.read(new File("images\\background.png"));
			ground = ImageIO.read(new File("images\\ground.png"));

		} catch (Exception e) {
			System.out.println("background or ground file error");
			e.getStackTrace();
			background = null;
			ground = null;

		}
		Bird.load();
		Pipes.load();
	}
}