import java.awt.Graphics;

public abstract class GameObj {
	protected int x, y;

	public GameObj(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public abstract void tick(State state);

	public abstract void render(Graphics g, State state);
}
