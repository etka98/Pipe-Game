import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PipeCell extends Cell{
	Pane pipe;
	private String feature;
	private String statu;
	private int location;

	public PipeCell(String feature, String statu, int location) {
		super(200, ((location - 1) % 4) * 200, ((location - 1) / 4 ) * 200);
		this.feature = feature;
		this.statu = statu;
		this.location = location;	
	}
	//this method's aim is that it adds the image depends the text files;
	public void draw() {
		if(feature.equals("Starter")) {
			if(statu.equals("Horizontal")) {
				Image image = new Image("starterHorizontal.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
				
			}
			else if(statu.equals("Vertical")) {
				Image image = new Image("starterVertical.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}		
		}
		else if(feature.equals("End")) {
			if(statu.equals("Horizontal")) {
				Image image = new Image("endHorizontal.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("Vertical")) {
				Image image = new Image("endVertical.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
		}
		else if(feature.equals("Empty")) {
			if(statu.equals("none")) {
				Image image = new Image("empty.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("Free")) {
				Image image = new Image("free.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
		}
		else if(feature.equals("Pipe")) {
			if(statu.equals("Horizontal")) {
				Image image = new Image("horizontal.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("Vertical")) {
				Image image = new Image("vertical.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("00")) {
				Image image = new Image("00.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("01")) {
				Image image = new Image("01.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("10")) {
				Image image = new Image("10.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("11")) {
				Image image = new Image("11.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
		}
		else if(feature.equals("PipeStatic")) {
			if(statu.equals("Horizontal")) {
				Image image = new Image("staticHorizontal.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("Vertical")) {
				Image image = new Image("staticVertical.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
			else if(statu.equals("01")) {
				Image image = new Image("01static.jpg");
				ImageView imageView = new ImageView(image);
				pipe = new Pane(imageView);
			}
		}
	
	}
	
	public String getFeature() {
		return feature;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getState() {
		return statu;
	}

	public void setState(String state) {
		this.statu = state;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}


}
