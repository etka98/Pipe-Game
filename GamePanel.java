import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;


public class GamePanel extends GridPane{
	PipeCell[] container = new PipeCell[16];
	GridPane pane = new GridPane();
	BorderPane panel;
	BorderPane score;
	Label movement;
	PipeCell pipe1;
	PipeCell pipe2;
	int loc1;
	int loc2;
	int count = 0;
	int levelChanged = 1;
	boolean end;
	Circle circle;
	Path path;
	boolean finished = false;
	boolean check = true;
	
	public GamePanel(int level) throws FileNotFoundException {
		build(level);
		circle = new Circle();
		path = new Path();
		//add the new event to grid pane to move cells and use includes method to check the location
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				for(int i = 0; i < 16; i++) {
					if((Cell)container[i].includes(event.getX(), event.getY()) != null) {
						pipe1 = container[i];
						loc1 = i;
					}
				}
				
			}
			
		});
		panel = new BorderPane();
		movement = new Label("" + count);
		score = new BorderPane();
		score.setCenter(movement);
		score.setStyle("-fx-background-color: rgba(17,137,21, 0.90)");
		panel.setCenter(pane);
		panel.setBottom(score);
		panel.getChildren().add(circle);
		//add the new event to grid pane to move cells and use includes method to check the location
		pane.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				for(int i = 0; i < 16; i++) {
					if((Cell)container[i].includes(event.getX(), event.getY()) != null) {
						pipe2 = container[i];
						loc2 = i;
					}
				}
				if(checkEnd() == false) {
					swap();
				}
				repaint();	
				//use checkEnd method to understand level is finish or not and add animation and change levels
				if(checkEnd() && check) {
					check = false;
					count = 0;
					circle.setFill(Color.CYAN);
					circle.setVisible(true);
					PathTransition pathTrans = new PathTransition();
					move();
					pathTrans.setNode(circle);
					pathTrans.setPath(path);
					pathTrans.setDuration(Duration.millis(5000));
					pathTrans.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
					pathTrans.setCycleCount(1);
					pathTrans.setAutoReverse(false);
					pathTrans.play();
					if(levelChanged < 5) {
						levelChanged++;
					}
					else {
						finished = true;
					}
					pathTrans.setOnFinished(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event1) {
							// TODO Auto-generated method stub
							try {
								path.getElements().clear();
								if(!finished) {
									circle.setVisible(false);
									build(levelChanged);
									check = true;
								}
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					});
					
					
				}
			}
			
			
		});
	}
	//its reads the text, fill the PipeCell constructor and add grid pane
	public void build(int level) throws FileNotFoundException {
		File txt = new File("level" + level + ".txt");
		Scanner scan = new Scanner(txt);
		
		for(int i = 0; scan.hasNext() && i < 16; i++) {
			String line = scan.nextLine();
			String[] arr = line.split(",");
			int location = Integer.parseInt(arr[0]) ;
			String feature = arr[1];
			String statu = arr[2];
			container[i] = new PipeCell(feature, statu, location);
			container[i].draw();
		}
		for(int i = 0; i < container.length; i++) {
			GridPane.setConstraints(container[i].pipe, (i % 4), (i / 4));
		}
		for(int i = 0; i < container.length; i++) {
			pane.getChildren().add(container[i].pipe);	
		}
		if(movement != null) {
			movement.setText(count + "");
		}
		scan.close();	
	}
	//it swaps the cell depends on the basic swap algorithms, counts number of moves and change the color of the pane depends moves
	public void swap() {
			
		if(pipe1 != null && pipe2 != null && ((Math.abs(pipe1.getLocation()-pipe2.getLocation())) == 4 || (Math.abs(pipe1.getLocation()-pipe2.getLocation())) == 1))  {
			if(!(pipe1.getFeature().equals("Starter") || pipe1.getFeature().equals("End") || pipe1.getState().equals("Free") || pipe1.getFeature().equals("PipeStatic")
				||	!(pipe2.getState().equals("Free")) )) {
				PipeCell temp = container[loc1];
				container[loc1] = container[loc2];
				container[loc2] = temp;
				int temploc = pipe1.getLocation();
				pipe1.setLocation(pipe2.getLocation());
				pipe2.setLocation(temploc);
				double tempX = pipe1.getX();
				double tempY = pipe1.getY();
				pipe1.setX(pipe2.getX());
				pipe1.setY(pipe2.getY());
				pipe2.setX(tempX);
				pipe2.setY(tempY);
				pipe1 = null;
				pipe2 = null;	
				count++;
				if(count > 8 && count < 12) {
					score.setStyle("-fx-background-color: rgba(239,239,10, 0.90)");
				}
				else if(count >= 12) {
					score.setStyle("-fx-background-color: rgba(194, 36, 16, 0.90)");
				}
				else {
					score.setStyle("-fx-background-color: rgba(17,137,21, 0.90)");
				}
				movement.setText(count + ""); 
			}
		}
	}
	//this method helps us to repaint the window when level is finished
	public void repaint() {
		for(int i = 0; i < 16; i++) {
			container[i].draw();
		}
		for(int i = 0; i < container.length; i++) {
			GridPane.setConstraints(container[i].pipe, (i % 4), (i / 4));

		}
		for(int i = 0; i < container.length; i++) {
			pane.getChildren().add(container[i].pipe);
			
		}
		
		
		
	}
	//this method checks level is complete or not
	public boolean checkEnd() {
		PipeCell pipe = null;
		PipeCell prev = null;
		int pipeloc = -1;
		for(int i = 0; i < 16; i++ ) {
			if(container[i].getFeature().equals("Starter")) {
				pipe = container[i];
				pipeloc = i;
				
			}
		}
		while(pipeloc != -1 && pipe != null && !(pipe.getFeature().equals("End")) ) {
			if(pipe.getState().equals("Horizontal")){
				if((pipeloc % 4) < 3) {
					prev = pipe;
					pipeloc = pipeloc + 1;
					pipe = container[pipeloc];
					
				}
				else
					return false;
			}
			else if(pipe.getState().equals("Vertical")){
				if((pipeloc / 4) < 3){
					prev = pipe;
					pipeloc = pipeloc + 4;
					pipe = container[pipeloc];
					
				}
				else
					return false;
			}
			else if(pipe.getState().equals("00")){
				if((pipeloc % 4) > 0 && prev.getState().equals("Vertical")) {
					prev = pipe;
					pipeloc = pipeloc - 1;
					pipe = container[pipeloc];
					
					if(!pipe.getState().equals("Horizontal")) {
						return false;
					}		
				}
				else if((pipeloc / 4) > 0 && prev.getState().equals("Horizontal")) {
					prev = pipe;
					pipeloc = pipeloc - 4;
					pipe = container[pipeloc];
					
					if(!pipe.getState().equals("Vertical")) {
						return false;
					}	
				}
				else
					return false;
			}
			else if(pipe.getState().equals("01")){
				if((pipeloc % 4) < 3 && prev.getState().equals("Vertical")) {
					prev = pipe;
					pipeloc = pipeloc + 1;
					pipe = container[pipeloc];;
					
					if(!pipe.getState().equals("Horizontal")) {
						return false;
					}
				}
				else if((pipeloc / 4) > 0 && prev.getState().equals("Horizontal")) {
					prev = pipe;
					pipeloc = pipeloc - 4;
					pipe = container[pipeloc];
					
					if(!pipe.getState().equals("Vertical")) {
						return false;
					}
				}
				else
					return false;
			}
			else if(pipe.getState().equals("10")){
				if((pipeloc % 4) > 0 && prev.getState().equals("Vertical")) {
					prev = pipe;
					pipeloc = pipeloc - 1;
					pipe = container[pipeloc];
					
					if(!pipe.getState().equals("Horizontal")) {
						return false;
					}
				}
				else if((pipeloc / 4) < 3 && prev.getState().equals("Horizontal")) {
					prev = pipe;
					pipeloc = pipeloc + 4;
					pipe = container[pipeloc];
					
					if(!pipe.getState().equals("Vertical")) {
						return false;
					}
				}
				else
					return false;
			}
			else if(pipe.getState().equals("11")){
				if((pipeloc % 4) < 3 && prev.getState().equals("Vertical")) {
					prev = pipe;
					pipeloc = pipeloc + 1;
					pipe = container[pipeloc];;
					
					if(!pipe.getState().equals("Horizontal")) {
						return false;
					}
				}
				else if((pipeloc / 4) < 3 && prev.getState().equals("Horizontal")) {
					prev = pipe;
					pipeloc = pipeloc + 4;
					pipe = container[pipeloc];
					
					if(!pipe.getState().equals("Vertical")) {
						return false;
					}
				}
				else
					return false;
			}
			else {
				return false;			
			}	
		}
		return true;
	}
	// this method's aims is that create the path for animation
	public void move() {
		PipeCell pipe = null;
		PipeCell prev = null;
		MoveTo moveTo;
		LineTo lineTo;
		int pipeloc = -1;
		for(int i = 0; i < 16; i++ ) {
			if(container[i].getFeature().equals("Starter")) {
				pipe = container[i];
				pipeloc = i;
				circle.setCenterX(container[i].x + 100);
				circle.setCenterY(container[i].y + 150);
				circle.setRadius(8);
				moveTo = new MoveTo(container[i].x + 100, container[i].y + 150);
				path.getElements().add(moveTo);
				lineTo = new LineTo(container[i].x + 100, container[i].y + 200);
				path.getElements().add(lineTo);
				
			}
		}
		while(pipeloc != -1 && pipe != null && !(pipe.getFeature().equals("End")) ) {
			if(pipe.getState().equals("Horizontal")){
				if((pipeloc % 4) < 3) {
					if(!pipe.getFeature().equals("Starter")) {
						moveTo = new MoveTo(pipe.x, pipe.y + 50);
						path.getElements().add(moveTo);
						lineTo = new LineTo(pipe.x + 200, pipe.y + 50);
						path.getElements().add(lineTo);
					}	
					prev = pipe;
					pipeloc = pipeloc + 1;
					pipe = container[pipeloc];
					
				}
			}
			else if(pipe.getState().equals("Vertical")){
				if((pipeloc / 4) < 3){
					if(!pipe.getFeature().equals("Starter")) {
						moveTo = new MoveTo(pipe.x + 100, pipe.y);
						path.getElements().add(moveTo);
						lineTo = new LineTo(pipe.x + 100, pipe.y + 200);
						path.getElements().add(lineTo);
					}
					prev = pipe;
					pipeloc = pipeloc + 4;
					pipe = container[pipeloc];
					
				}
			}
			else if(pipe.getState().equals("00")){
				if((pipeloc % 4) > 0 && prev.getState().equals("Vertical")) {
					moveTo = new MoveTo(pipe.x + 100, pipe.y);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x, pipe.y + 100);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc - 1;
					pipe = container[pipeloc];		
				}
				else if((pipeloc / 4) > 0 && prev.getState().equals("Horizontal")) {
					moveTo = new MoveTo(pipe.x, pipe.y + 50);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 50);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 50);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc - 4;
					pipe = container[pipeloc];	
				}
			}
			else if(pipe.getState().equals("01")){
				if((pipeloc % 4) < 3 && prev.getState().equals("Vertical")) {
					moveTo = new MoveTo(pipe.x + 100, pipe.y);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 50);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 50);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 200, pipe.y + 50);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc + 1;
					pipe = container[pipeloc];
				}
				else if((pipeloc / 4) > 0 && prev.getState().equals("Horizontal")) {
					moveTo = new MoveTo(pipe.x + 200, pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc - 4;
					pipe = container[pipeloc];
				}
			}
			else if(pipe.getState().equals("10")){
				if((pipeloc % 4) > 0 && prev.getState().equals("Vertical")) {
					moveTo = new MoveTo(pipe.x + 100, pipe.y + 200);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x, pipe.y + 100);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc - 1;
					pipe = container[pipeloc];
				}
				else if((pipeloc / 4) < 3 && prev.getState().equals("Horizontal")) {
					moveTo = new MoveTo(pipe.x, pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 200);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc + 4;
					pipe = container[pipeloc];
				}
			}
			else if(pipe.getState().equals("11")){
				if((pipeloc % 4) < 3 && prev.getState().equals("Vertical")) {
					moveTo = new MoveTo(pipe.x + 100, pipe.y + 200);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 200, pipe.y + 100);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc + 1;
					pipe = container[pipeloc];
				}
				else if((pipeloc / 4) < 3 && prev.getState().equals("Horizontal")) {
					moveTo = new MoveTo(pipe.x + 200, pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 100);
					path.getElements().add(lineTo);
					moveTo = new MoveTo(pipe.x + 100 , pipe.y + 100);
					path.getElements().add(moveTo);
					lineTo = new LineTo(pipe.x + 100, pipe.y + 200);
					path.getElements().add(lineTo);
					prev = pipe;
					pipeloc = pipeloc + 4;
					pipe = container[pipeloc];
				}
			}	
		}
		if(pipe.getState().equals("Vertical")) {
			moveTo = new MoveTo(pipe.x + 100, pipe.y + 200);
			path.getElements().add(moveTo);
			lineTo = new LineTo(pipe.x + 100, pipe.y + 150);
			path.getElements().add(lineTo);
		}
		else {
			moveTo = new MoveTo(pipe.x, pipe.y + 50);
			path.getElements().add(moveTo);
			lineTo = new LineTo(pipe.x + 50, pipe.y + 50);
			path.getElements().add(lineTo);
		}
	}	
}


