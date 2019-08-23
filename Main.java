import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{
	Stage window;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("3000 TIMES");
		StackPane mainMenu = new StackPane();
		//set the background image in main menu
		BackgroundImage backgroundImage = new BackgroundImage(new Image("iron.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainMenu.setBackground(new Background(backgroundImage));
		//create the new button and add image 
		Button playButton = new Button("", new ImageView(new Image("play.jpg")));
		mainMenu.getChildren().add(playButton);
		GamePanel level = new GamePanel(1);	
		Scene scene = new Scene(mainMenu, 1025, 750);
		window.setScene(scene);
		//add new action play button to pass the other page
		playButton.setOnAction(e -> {
			primaryStage.setScene(new Scene(level.panel));
		});
		window.show();
		
	}

	public static void main(String[] args) {
		launch(args);

	}


}
