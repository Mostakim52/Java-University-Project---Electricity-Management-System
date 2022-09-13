import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class colorusage{
	
	public static VBox set() {
		Circle c[] = new Circle[4];
		Label l[] = new Label[4];
		HBox h[] = new HBox[4];
		VBox v = new VBox();
		l[0] = new Label("High Usage");
		l[1] = new Label("Medium Usage");
		l[2] = new Label("Low Usage");
		l[3] = new Label("No Usage");
		
		Text Key = new Text(25,25,"KEY");
		Key.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Key.setTextAlignment(TextAlignment.CENTER);
		v.getChildren().add(Key);
		
		for(int i = 0;i<4;i++) {
			c[i] = new Circle();
			c[i].setRadius(10);
			h[i] = new HBox();
			h[i].getChildren().addAll(c[i],l[i]);
			v.getChildren().add(h[i]);
		}
		c[0].setFill(Color.RED);
		c[1].setFill(Color.ORANGE);
		c[2].setFill(Color.GREEN);
		c[3].setFill(Color.GRAY);
		
		v.setPadding(new Insets(20,20,20,20));
		
		return v;
	}
	
}
