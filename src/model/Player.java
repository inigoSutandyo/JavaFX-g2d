package model;

public class Player extends Character {

	private int earnedCoin;
	public Player() {
		earnedCoin = 0;
	}
	
	public int getEarnedCoin() {
		return earnedCoin;
	}
	
	public void receiveCoin() {
		this.earnedCoin++;
	}
	
}
