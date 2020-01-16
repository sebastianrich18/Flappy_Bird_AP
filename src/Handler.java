import java.awt.Graphics;
import java.util.ArrayList;

public class Handler { // this class handles all the object's rendering and ticking

	public static ArrayList<GameObj> objs = new ArrayList<GameObj>(); // holds all objects

	public static State state = State.Menu;

	public Handler() {
	}

	public static void setState(State s, Menu m) {
		state = s;
	}

	public static void restart() {
		state = State.Game;
		objs = new ArrayList<GameObj>();
	}

	public void tick() { // ticks all objects
		for (int i = 0; i < objs.size(); i++) {
			GameObj temp = objs.get(i);
			temp.tick(state);
		}
	}

	public void render(Graphics g) { // renders all objects
		for (int i = 0; i < objs.size(); i++) {
			// System.out.println(state);
			objs.get(i).render(g, state);
		}
		Menu.render(g, state);
	}

	public void addObj(GameObj obj) {
		objs.add(obj);
	}

	public void removeObj(GameObj obj) {
		objs.remove(obj);
	}
}
