import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ApartmentHeading {
	public static void heading(GridPane p) {
		Text text3 = new Text(30,30,"Name");
		text3.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Text text4 = new Text(30,30,"Owner");
		text4.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Text text5 = new Text(30,30,"Rate");
		text5.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Text text6 = new Text(30,30,"Total consumption");
		text6.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Text text7 = new Text(30,30,"Last month paid?");
		text7.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Text text8 = new Text(30,30,"Usage");
		text8.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		Text text9 = new Text(30,30,"Controls");
		text9.setFont(Font.font("Times New Roman", FontWeight.BOLD,FontPosture.ITALIC, 15));
		text9.setTextAlignment(TextAlignment.JUSTIFY);
		p.add(text3, 0, 0);
		p.add(text4, 1, 0);
		p.add(text5, 2, 0);
		p.add(text6, 3, 0);
		p.add(text7, 4, 0);
		p.add(text8, 5, 0);
		p.add(text9, 6, 0);
		p.setHgap(10);
	}
}
