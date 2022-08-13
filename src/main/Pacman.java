package main;


import java.io.File;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Ghost;
import model.Player;

public class Pacman extends Application{
	
	private boolean isOver;
	private boolean isLose;
	private boolean isWin;
	
	
	private final int WIDTH = 660, HEIGHT = 660;
	private final int ROW = 11, COLUMN = 11;
	private final int GRID_SIZE = WIDTH / ROW;
	private final int MAX_COIN = 10;
	private final int MAX_GHOST = 3;
	
	private char[][] map;
	private Image coinImage, ghostImage, playerImage;
	
	private ArrayList<Ghost> ghostList;
	private Player player;
	
	private Group group;
	private Canvas canvas;
	
	private double moveDelay;
	private Timeline tl;
	public void init() {
		isOver = false;
		isLose = false;
		isWin = false;
		player = new Player();
		player.newPosition(0, 0);
		ghostList = new ArrayList<>();
		Map m = new Map();
		map = m.getMaze(ROW, COLUMN, player.getX(), player.getY());
		
		loadAllImages();
		generateProps('C');
		generateProps('G');
		
		
		group = new Group();
		canvas = new Canvas(WIDTH, HEIGHT);
		moveDelay = 500;
		double duration = 100/6;
		tl = new Timeline(new KeyFrame(Duration.millis(duration), new EventHandler<ActionEvent>() {
		

			@Override
			public void handle(ActionEvent arg0) {
				moveDelay -= duration;
				draw();
				if (moveDelay <= 0) {
					moveDelay = 500;
				}
			}
			
		}));
		
		tl.setCycleCount(Animation.INDEFINITE);
		tl.play();
//		draw();
		group.getChildren().add(canvas);
		
	}

	private void printMap() {
		// TODO Auto-generated method stub
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}


	private void generateProps(char code) {
		int count = 0;
		int max = ROW;
		int min = code == 'C' ? 0 : 3;
		while (code == 'C' ? (count < MAX_COIN) : (count < MAX_GHOST)) {
			int x = randomInRange(min, max);
			int y = randomInRange(min, max);
			
			// 0 di map itu path
			if (map[y][x] == '0') {
				map[y][x] = code;
				count++;
				if (code == 'G') {
					Ghost ghost = new Ghost();
					ghost.newPosition(x, y);
					ghostList.add(ghost);
//					System.out.println("Position = " + x + " " + y);
//					System.out.println(ghost.getX() + " " + ghost.getY());
				}
			}
		}
	}
	
	private int randomInRange(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	private void loadAllImages() {
		coinImage = new Image(new File("game/coin.png").toURI().toString());
		ghostImage = new Image(new File("game/ghost.png").toURI().toString());
		playerImage = new Image(new File("game/pacman.png").toURI().toString());
		
	}
	
	private void moveGhost(Ghost ghost) {
		getGhostDirection(ghost);
		
		if (ghost.isLeft()) {
			checkGhostCollision(-1, 0, ghost);
		} else if (ghost.isRight()) {
			checkGhostCollision(1, 0, ghost);
		} else if (ghost.isUp()) {
			checkGhostCollision(0, -1, ghost);
		} else if (ghost.isDown()) {
			checkGhostCollision(0, 1, ghost);
		}
	}
	
	private void checkGhostCollision(int diffX, int diffY, Ghost ghost) {
		int oldX = ghost.getX();
		int oldY = ghost.getY();
		int newX = oldX + diffX;
		int newY = oldY + diffY;
		
		if (isViolateGrid(newX, newY)) return;
		
		if (map[newY][newX] != '0' && map[newY][newX] != 'P') {
			return;
		}
		
		 if (map[newY][newX] == 'P') {
			isLose = true;
		} 
		
		map[oldY][oldX] = '0';
		map[newY][newX] = 'G';
		
		ghost.newPosition(newX, newY);
	}

	private void getGhostDirection(Ghost ghost) {
		int rand = randomInRange(1, 5);
		
		ghost.setAllFalse();
		if (rand == 1) {
			ghost.setLeft(true);
		} else if (rand == 2) {
			ghost.setRight(true);
		} else if (rand == 3) {
			ghost.setUp(true);
		} else if (rand == 4) {
			ghost.setDown(true);
		}
	}
	
	
	private void movePlayer(int diffX, int diffY) {
		int oldX = player.getX();
		int oldY = player.getY();
		
		int newX = oldX + diffX;
		int newY = oldY + diffY;
		if (isViolateGrid(newX, newY)) {
			return;
		}
		
		checkPlayerCollision(oldX, oldY, newX, newY);
		
	}
	
	private void checkPlayerCollision(int oldX, int oldY, int newX, int newY) {
		if (map[newY][newX] == '1') {
			return;
		}
		
		if (map[newY][newX] == 'C') {
			player.receiveCoin();
			System.out.println("Coins = " + player.getEarnedCoin());

		} else if (map[newY][newX] == 'G') {
			isLose = true;
		} else if (map[newY][newX] == 'W') {
			isWin = true;
		}
		
		map[oldY][oldX] = '0';
		map[newY][newX] = 'P';
		player.newPosition(newX, newY);
	}

	private boolean isViolateGrid(int x, int y) {
		if (x < 0 || x > ROW-1) {
			return true;
		} else if (y < 0 || y > COLUMN - 1) {
			return true;
		}
		
		return false;
	}
	
	private void drawImage(GraphicsContext gc, int x, int y, Image image) {
		gc.drawImage(image, 
				x * GRID_SIZE + 2, 
				y * GRID_SIZE + 2, 
				GRID_SIZE - 5, 
				GRID_SIZE - 5);
	}
	
	private void drawBoard(GraphicsContext gc) {
		for (int y = 0; y < COLUMN; y++) {
			for (int x = 0; x < ROW; x++) {
				char code = map[y][x];
//				System.out.print(code);
				if (code == '1') {
					gc.setFill(Color.BLACK);
					gc.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
				
				} else if (code == 'C') {
					drawImage(gc, x, y, coinImage);
				
				} else if (code == 'P') {
					drawImage(gc, x, y, playerImage);
				
				} else if (code == 'G') {
					drawImage(gc, x, y, ghostImage);
					
				} else if (code == 'W') {
					gc.setFill(Color.LIGHTGREEN);
					gc.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
					
				}
			}
//			System.out.println();
		}
	}
	

	
	private void drawGrid(GraphicsContext gc) {
		gc.setStroke(Color.GRAY);
		
		for (int i = 0; i < ROW; i++) {
			gc.strokeLine(i * GRID_SIZE, 0, i * GRID_SIZE, GRID_SIZE * COLUMN);
		}
		
		for (int j = 0; j < COLUMN; j++) {
			gc.strokeLine(0, j * GRID_SIZE, GRID_SIZE * ROW, j * GRID_SIZE);
		}
		
	}
	
	private void draw() {
		if (isOver) {
			return;
		}
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Color color = Color.rgb(153, 255, 255);
		gc.setFill(color);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		
		drawGrid(gc);
		drawBoard(gc);
		
		if (moveDelay <= 0) {			
			for (Ghost ghost : ghostList) {
				moveGhost(ghost);
//				System.out.println(ghost.getX() + " " + ghost.getY());
			}
		}
		
		if (isLose) {
			tl.stop();
			showDialog("You Lose, you got eaten by ghost...");
			isOver = true;
		} else if (isWin) {
			tl.stop();
			showDialog("Congratulations you win!\n" + "Score : " + (player.getEarnedCoin() * 20));
			isOver = true;
		}
	}
	
	private void showDialog(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		alert.setOnHidden(e -> System.exit(0));
		alert.show();
	}

	@Override
	public void start(Stage stage) throws Exception {
		init();
		Scene scene = new Scene(group);
		addKeyListener(scene);
		stage.setScene(scene);
		stage.setTitle("Pacmon");
		stage.show();
	}
	
	private void addKeyListener(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.LEFT) {
					movePlayer(-1, 0);
				} else if (e.getCode() == KeyCode.RIGHT) {
					movePlayer(1, 0);
				} else if (e.getCode() == KeyCode.UP) {
					movePlayer(0, -1);
				} else if (e.getCode() == KeyCode.DOWN) {
					movePlayer(0, 1);
				}
			}
			
		});
		
	}

}
