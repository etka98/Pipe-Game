import javafx.scene.layout.Pane;

public class Cell extends Pane{
public double length, x, y;
private boolean selected;

public Cell(double length, double x, double y) {
	this.length = length;
	this.x = x;
	this.y = y;	
	selected = false;
}
//this method checks that when press the mouse,this area includes the that cell
public Cell includes(double d, double e) {
	if(d > this.x && (d < this.x + length) && e > this.y && e < (this.y + length) ) {
		return this;
	}
	else
		return null;
	
}

public double getLength() {
	return length;
}

public void setLength(double length) {
	this.length = length;
}

public double getX() {
	return x;
}

public void setX(double x) {
	this.x = x;
}

public double getY() {
	return y;
}

public void setY(double y) {
	this.y = y;
}

public boolean isSelected() {
	return selected;
}

public void setSelected(boolean selected) {
	this.selected = selected;
}

}
