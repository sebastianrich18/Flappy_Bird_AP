import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input implements KeyListener, MouseListener {

	public Bird b;

	public Input(Bird b) {
		this.b = b;
	}

	public void keyPressed(KeyEvent k) {
		// System.out.println(k.getKeyCode());
		if (k.getKeyCode() == 32) { // key code for space bar is 32
			Game.didJump = 1;
			b.jump();
			b.wingUp();
		} else if (k.getKeyCode() == 27) { // escape is 27
			Game.restart();
		} else if (k.getKeyCode() == 192) { // 192 is `
			if (Handler.state == State.Debug) {
				Handler.state = State.Game;
			} else {
				Handler.state = State.Debug;
			}
		} else {
			Game.didJump = 0;
		}
	}

	public void keyReleased(KeyEvent k) {
		if (k.getKeyCode() == 32) {
			b.wingDown();
		}
	}

	public void mouseClicked(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) { // event for left mouse click
			// System.out.println("left clicked");
			Menu.mouseClicked();
		}
	}

	public static Point getMousePos() {
		return Window.getFrame().getMousePosition();
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void mousePressed(MouseEvent me) {
	}

	public void mouseReleased(MouseEvent me) {
	}

	public void mouseEntered(MouseEvent me) {
	}

	public void mouseExited(MouseEvent me) {
	}

}
