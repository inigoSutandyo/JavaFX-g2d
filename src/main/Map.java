package main;

public class Map {
	
	private final String[] MAZE = {
			"01000001010"
			,"01010010010"
			,"01000001000"
			,"00010001011"
			,"00000000001"
			,"11000011001"
			,"00000000000"
			,"11010010010"
			,"00000001010"
			,"10010100000"
			,"0000010101W"};
	
	public char[][] getMaze(int row, int column, int playerX, int playerY) {
		char[][] map = new char[column][row];
		for(int i = 0; i < MAZE.length; i++) {
			map[i] = MAZE[i].toCharArray();
		}
		
		// SET PLAYER
		map[playerY][playerX] = 'P';
		return map;
	}
}
