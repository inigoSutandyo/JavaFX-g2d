package model;

public abstract class Character {

	private int x,y;
	
	public Character() {

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void newPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
