import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class Draggg{
	private double x,y;
	public void makedrag(Circle c,Label l) {
		c.setOnMousePressed(e ->{
			x = e.getX();
			y = e.getY();
		});
		c.setOnMouseDragged(e ->{
			c.setLayoutX(e.getSceneX()-this.x-150);
			c.setLayoutY(e.getSceneY()-this.y-50);
			l.setLayoutX(c.getLayoutX()-5);
			l.setLayoutY(c.getLayoutY()-5);
		});
	}
}