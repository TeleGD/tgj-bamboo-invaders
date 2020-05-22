package games.bambooInvaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import app.AppLoader;

import static java.lang.Math.floor;

public class Grid {

	private static String[][] grids;

	private String name;
	private Cell[][] cells;

	public static void load(String filename) {
		String json = AppLoader.loadData(filename);
		JSONArray array = new JSONArray();
		try {
			array = new JSONArray(json);
		} catch (JSONException error) {}
		int length = array.length();
		String[][] grids = new String[length][];
		for (int i = 0; i < length; ++i) {
			JSONObject object = new JSONObject();
			try {
				object = array.getJSONObject(i);
			} catch (JSONException error) {}
		   String name = "";
		   try {
			   name = object.getString("name");
		   } catch (JSONException error) {}
			String path = "";
			try {
				path = object.getString("path");
			 } catch (JSONException error) {}
			grids[i] = new String[]{name, path};
		}
		Grid.grids = grids;
	}

	public static String[][] getGrids() {
		return Grid.grids;
	}

	public Grid(String name, String path) {
		this.name = name;
		String json = AppLoader.loadData(path);
		JSONObject object = new JSONObject();
		try {
			object = new JSONObject(json);
		} catch (JSONException error) {}
		int width = 0;
		try {
			width = object.getInt("width");
		} catch (JSONException error) {}
		int height = 0;
		try {
			height = object.getInt("height");
		} catch (JSONException error) {}
		Cell[][] cells = new Cell[height][width];
		JSONArray array = new JSONArray();
		try {
			array = object.getJSONArray("cells");
		} catch (JSONException error) {}
		for (int i = 0; i < height; ++i) {
			JSONArray subarray = new JSONArray();
			try {
				subarray = array.getJSONArray(i);
			} catch (JSONException error) {}
			for (int j = 0; j < width; ++j) {
				int type = 0;
				try {
					type = subarray.getInt(j);
				} catch (JSONException error) {}
				cells[i][j] = new Cell(type);
			}
		}
		this.cells = cells;
	}

	public void update(GameContainer container, StateBasedGame game, int delta) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		/* Méthode exécutée environ 60 fois par seconde */
		//TODO
	}

	/**
	 * @param row : row_axial
	 * @param col : col_axial
	 * @return [row_memory, col_memory]
	 */
	public static int[] convertAxialToMemoryCoord(int row, int col){
		return new int[] {row, col + (int) (floor(row/2))};
	}

	/**
	 * @param row : row_memory
	 * @param col : col_memory
	 * @return [row_axial, col_axial]
	 */
	public static int[] convertMemoryToAxialCoord(int row, int col){
		return new int[] {row, col - (int) floor(row/2)};
	}

}
