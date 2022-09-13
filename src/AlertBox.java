

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;


public class AlertBox {
	
	public static void display(String title, String message) {
		Stage window = new Stage();
		
		
		//blocking other input until the alert box is dealt with
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button okbutton = new Button("OK");
		
		okbutton.setOnAction(e -> window.close());
		
		VBox layout = new VBox();
		layout.getChildren().addAll(label,okbutton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		 
	}
	
}
