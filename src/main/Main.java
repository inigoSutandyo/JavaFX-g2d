package main;

import java.io.File;


import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{

	private Scene scene;
	private Group group;

	public void init() {
		group = new Group();
		scene = new Scene(group, 640, 480);
		
		Line line = new Line();
		line.setStartX(10);
		line.setStartY(5);
		line.setEndX(450);
		line.setEndY(250);
		
		Stop[] stops = new Stop[] {
				new Stop(0, Color.RED),
				new Stop(1, Color.BLUE)
		};
		
		LinearGradient lingrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
		
		Rectangle rect = new Rectangle();
		rect.setX(200);
		rect.setY(300);
		rect.setHeight(100);
		rect.setWidth(200);
		rect.setFill(lingrad);
		
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(rect);
		rotate.setByAngle(90);
		rotate.setDuration(Duration.seconds(3));
		rotate.setCycleCount(2);
		rotate.play();
		
		Circle circle = new Circle();
		circle.setCenterX(500);
		circle.setCenterY(300);
		circle.setRadius(50);
		circle.setFill(Color.AQUA);
		circle.setStroke(Color.RED);
		circle.setOpacity(0.75);
		
		TranslateTransition translate = new TranslateTransition();
		translate.setNode(circle);
		translate.setByX(-300);
		translate.setByY(0);
		translate.setDuration(Duration.seconds(5));
		translate.play();
		
		ScaleTransition scale = new ScaleTransition();
		scale.setNode(circle);
		scale.setByX(0.5);
		scale.setDuration(Duration.seconds(5));
		scale.play();
		
		Image img = new Image(new File("examples/img_example.png").toURI().toString());
		ImageView imgView = new ImageView(img);
		imgView.setX(250);
		imgView.setY(120);
		imgView.setFitHeight(300);
		imgView.setFitWidth(120);
		imgView.setPreserveRatio(false);
		
		
		Media audio = new Media(new File("examples/audio_example.wav").toURI().toString());
//		MediaPlayer audioPlayer = new MediaPlayer(audio);
//		audioPlayer.setStopTime(Duration.seconds(15));
//		audioPlayer.setVolume(0.1);
//		audioPlayer.play();
		
		Media video = new Media(new File("examples/video_example.mp4").toURI().toString());
		MediaPlayer videoPlayer = new MediaPlayer(video);
		videoPlayer.play();
		
		MediaView medView = new MediaView(videoPlayer);
		medView.setX(100);
		medView.setFitHeight(300);
		
		group.getChildren().add(imgView);
		group.getChildren().add(medView);
		
		
//		group.getChildren().add(line);
//		group.getChildren().add(rect);
//		group.getChildren().add(circle);
	}
	
	public Main() {
		
	}

	public static void main(String[] args) {
//		launch(args);
//		new Pacman().init();
		Pacman.launch(Pacman.class);
	}

	@Override
	public void start(Stage stage) throws Exception {
		init();
		stage.setScene(scene);
		stage.setTitle("h4 special");
		stage.show();
	}

}



