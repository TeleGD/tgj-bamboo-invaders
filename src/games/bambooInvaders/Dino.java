package games.bambooInvaders;

import app.AppFont;
import app.AppLoader;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class Dino {

	private Image dino;
	private Image gui;
	private AppFont bambooFont;

	private int bambooCounter;
	private int actionCountdown;
	private int i;
	private int j;
	private Grid grid;
	private int score;
	private boolean isRegurgitating;
	private int timeRegurgitating;

	private final int timeRegurgiteBamboo = 150; // 150 ms
	private final int countdownPerBamboo = 50; // 50 ms

	public Dino(Grid grid) {
		this.score = 0;
		this.grid = grid;
		int[] ij = grid.findNest();
		this.i = ij[0];
		this.j = ij[1];
		this.actionCountdown = 0;
		this.bambooCounter = 0;
		this.timeRegurgitating = 0;
		this.isRegurgitating = false;
		this.dino = AppLoader.loadPicture("/images/bambooInvaders/dino.png");
		this.gui = AppLoader.loadPicture("/images/bambooInvaders/GUI.png");
		this.bambooFont = AppLoader.loadFont(null, AppFont.BOLD, 42);
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		this.score += delta;

		this.actionCountdown -= delta;
		if (this.actionCountdown <= 0) this.actionCountdown = checkInput(container,delta);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		Point p = this.grid.getHexagonCenter(i, j);

		context.drawImage(
					dino,
					(float) p.getX() - Cell.getWidth()/3, (float) p.getY() - Cell.getHeight()/3, (float) p.getX() + Cell.getWidth()/3, (float) p.getY() + Cell.getHeight()/3,
					0, 0, dino.getWidth(), dino.getHeight()
				);
		// TODO : animation de déplacement ?
		
		context.drawImage(
				gui,
				0, container.getHeight() - gui.getHeight(),
				0, 0, gui.getWidth(), gui.getHeight()
			);
		context.setFont(bambooFont);
		context.setColor(new Color(0x5c913b));
		context.drawString(""+this.bambooCounter, 175, container.getHeight() - gui.getHeight() + 6);
		context.resetFont();
		context.setColor(new Color(0x565656));
		context.drawString("Score : "+this.score/1000, 125, container.getHeight() - gui.getHeight() + 72);
	}

	private int checkInput(GameContainer container, int delta) {
		Input input = container.getInput();

		if (this.isRegurgitating  && this.bambooCounter > 0) {
			regurgitate(delta);
		}
		this.isRegurgitating = false;

		if (input.isKeyDown(Input.KEY_S)) {
			// Regurgite si dans un nid
			Cell cell = grid.getCell(i, j);
			if (cell.getType() == 0  && this.bambooCounter > 0) {
				isRegurgitating = true;
			}
			// Mange les bambous s'il y en a
			else {
				int stage = cell.getBambooStage();
				if (stage > 0) {
					cell.setBambooGauge(0);
					cell.setBambooStage(0);
					return eat(stage);
				}
			}

		} else if (input.isKeyDown(Input.KEY_Z)) {
			return move(0);
		} else if (input.isKeyDown(Input.KEY_E)) {
			return move(1);
		} else if (input.isKeyDown(Input.KEY_D)) {
			return move(2);
		} else if (input.isKeyDown(Input.KEY_X)) {
			return move(3);
		} else if (input.isKeyDown(Input.KEY_W)) {
			return move(4);
		} else if (input.isKeyDown(Input.KEY_Q)) {
			return move(5);
		}

		if (!isRegurgitating) {
			regurgitate(0); // Reset de la régurgitation si elle est terminée
		}

		return 0; // Aucune action donc timeout de 0
	}

	private int move(int direction) {
		int cooldown = (int) ((this.bambooCounter * this.countdownPerBamboo + grid.getCell(i, j).getDinoActionDuration()) / grid.getCell(i, j).getDinoSpeedCoefficient()) ;
		if (cooldown > 3000) cooldown = 3000;

		int new_i = this.i;
		int new_j = this.j;
		switch (direction) {
		case 0:
			new_i = this.i-1;
			break;
		case 1:
			new_i = this.i-1;
			new_j = this.j+1;
			break;
		case 2:
			new_j = this.j+1;
			break;
		case 3:
			new_i = this.i+1;
			break;
		case 4:
			new_i = this.i+1;
			new_j = this.j-1;
			break;
		case 5:
			new_j = this.j-1;
			break;
		}

		if (grid.getCell(new_i, new_j).getType() == 4) return 0; // La case n'est pas accessible

		this.i = new_i;
		this.j = new_j;

		return cooldown;
	}

	private int eat(int stage) {
		this.bambooCounter += stage*1.5;
		return (int) 500*stage;
	}

	private void regurgitate(int delta) {
		if (delta > 0) {
			this.timeRegurgitating += delta;
			while (this.timeRegurgitating > this.timeRegurgiteBamboo && this.bambooCounter > 0) {
				this.timeRegurgitating -= this.timeRegurgiteBamboo;
				this.bambooCounter -= 1;
				this.score += 3000;
			}
		} else {
			this.timeRegurgitating = 0;
		}
	}
}
