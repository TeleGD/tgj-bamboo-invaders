package games.bambooInvaders;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class World extends BasicGameState {

	private int ID;
	private int state;
	private Grid grid;
	private Dino firstDino;
	private Dino lastDino;

	public World(int ID) {
		this.ID = ID;
		this.state = 0;
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au chargement du programme */
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à l'apparition de la page */
		if (this.state == 0) {
			this.play(container, game);
		} else if (this.state == 2) {
			this.resume(container, game);
		}
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée à la disparition de la page */
		if (this.state == 1) {
			this.pause(container, game);
		} else if (this.state == 3) {
			this.stop(container, game);
			this.state = 0; // TODO: remove
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		Input input = container.getInput();
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			this.setState(1);
			game.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
		this.grid.update(container, game, delta);
		this.firstDino.update(container, game, delta);
		this.lastDino.update(container, game, delta);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		int width = container.getWidth();
		int height = container.getHeight();
		Point firstPoint = this.firstDino.getPoint();
		Point lastPoint = this.lastDino.getPoint();
		context.setClip(0, 0, width / 2, height);
		this.grid.render(container, game, context, firstPoint.x + width / 4, firstPoint.y);
		this.lastDino.render(container, game, context, firstPoint.x - lastPoint.x - width / 4, firstPoint.y - lastPoint.y - height / 2, false);
		this.firstDino.render(container, game, context, -width / 4, -height / 2, true);
		context.setClip(width / 2, 0, width / 2, height);
		this.grid.render(container, game, context, lastPoint.x - width / 4, lastPoint.y);
		this.firstDino.render(container, game, context, lastPoint.x - firstPoint.x - width * 3 / 4, lastPoint.y - firstPoint.y - height / 2, false);
		this.lastDino.render(container, game, context, -width * 3 / 4, -height / 2, true);
		context.setClip(0, 0, width, height);
		context.setColor(new Color(0, 0, 0));
		context.setLineWidth(20);
		context.drawLine(width / 2, 0, width / 2, height);
	}

	public void play(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois au début du jeu */
		this.firstDino = new Dino(grid, false);
		this.lastDino = new Dino(grid, true);
	}

	public void pause(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la mise en pause du jeu */
	}

	public void resume(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée lors de la reprise du jeu */
	}

	public void stop(GameContainer container, StateBasedGame game) {
		/* Méthode exécutée une unique fois à la fin du jeu */
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

}
