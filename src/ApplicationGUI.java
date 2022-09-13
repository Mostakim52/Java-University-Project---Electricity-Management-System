
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;

class Console extends OutputStream {

    private TextArea output;

    public Console(TextArea ta) {
        this.output = ta;
    }

    @Override
    public void write(int i) throws IOException {
    	String str = String.valueOf((char) i);
        output.appendText(str);
    }
}

public class ApplicationGUI extends Application{
	
	Stage window;
	Scene scene1, scene2, scene3,scene4;
	TextField user;
	PasswordField password;
	ImageView imageview[];

	ArrayList<CircleData> circles = new ArrayList<>();
	static WholeObject obj;
	static boolean isDone;
	static boolean sectorstatus[];
	static Simulation s;
		
	void sendEmail(int BuildinIndex,int y) {
		Stage primaryStage = new Stage();
		TextArea ta = new TextArea();
        Console console = new Console(ta);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);
        Scene app = new Scene(ta);

        primaryStage.setScene(app);
        primaryStage.setTitle("Sending mail....       Please be patient.....");
        primaryStage.show();

        for (int o = 0;o<obj.u1.length;o++) {
        	if (obj.u1[o].getUsername().equals(obj.b1[BuildinIndex].a[y].getOwner())){
        		String str = "Hello " + obj.u1[o].getUsername() + ".\nWe would like to inform you that you have a payment due of " + String.format("%.4f",5.614*obj.b1[BuildinIndex].a[y].getTotalELectricity()) + "tk as a result of total kWh: " + String.format("%.4f",obj.b1[BuildinIndex].a[y].getTotalELectricity()) + ".\nPlease kindly clear it by end of month.";
        		String str2 = "\nNot clearing dues may result in power shutdown for your home.\n\n\nThe Electricity Comapany - Uttara";
        		str = str+str2;
        		for (char chr : SendEmail.send(obj.u1[o].getEmail(),str).toCharArray()) {
                    try {
						console.write(chr);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
                ps.close();
        	}
        }
        ps.close();
		AlertBox.display("Success!", "If email exists then user recieved it.");
	}
	void BuildingInfo(int sectornum,int BuildinIndex) {
		Stage stageBldgInfo = new Stage();
		GridPane aprt = new GridPane();
		GridPane gpane = new GridPane();
		
		Text text1 = new Text(20, 20, String.valueOf(obj.b1[BuildinIndex].getApartmentnum()));
		gpane.add(new Label("Number of Apartments: "),0,0);
		Text text2 = new Text(20, 20, String.valueOf(UserandBuildingControls.averagekWh(obj,BuildinIndex)));
		gpane.add(new Label("Total kWh: "),0,1);
		text1.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 15));
		text2.setFont(Font.font("Courier", FontWeight.BOLD,FontPosture.ITALIC, 15));
		gpane.add(text1, 1, 0);
		gpane.add(text2, 1, 1);
		
		Button addaprt = new Button("Add apartment");
		addaprt.setOnAction(e ->{
			Stage windowt = new Stage();
			windowt.initModality(Modality.APPLICATION_MODAL);
			
			GridPane pane = new GridPane();
			pane.add(new Label("Owner Name"), 0, 0);
			TextField name = new TextField();
			pane.add(name, 1, 0);
			pane.add(new Label("Apartment Name"), 0, 1);
			TextField aprtname = new TextField();
			pane.add(aprtname, 1, 1);
			pane.add(new Label("Total Electricity"), 0, 2);
			TextField totelec = new TextField();
			pane.add(totelec, 1, 2);

			CheckBox box = new CheckBox("Paid");
			pane.add(box, 1, 3);
			
			pane.add(new Label("Password"), 0, 4);
			TextField password = new TextField();
			pane.add(password, 1, 4);
			
			pane.add(new Label("Email Address"), 0, 5);
			TextField email = new TextField();
			pane.add(email, 1, 5);
			
			Button Add = new Button("Add");
			Add.setOnAction(c ->{
		
				
				if (name.getText().isEmpty()||aprtname.getText().isEmpty()||totelec.getText().isEmpty()||password.getText().isEmpty()||email.getText().isEmpty()) {
					AlertBox.display("Error", "Fields cannot be left empty!");
					return;
				}
				
				String Email = email.getText();
				if (!(Email.contains("@gmail.com") || Email.contains("@yahoo.com") || Email.contains("@hotmail.com") || Email.contains("@northsouth.edu") || Email.contains("@ois.edu.bd") || Email.contains(" "))) {
					AlertBox.display("Error", "Invalid Email!");
					return;
				}
				
				try {
					Double.parseDouble(totelec.getText());
				}
				catch(Exception a) {
					AlertBox.display("", "Fields are invalid");
					return;
				}
				if (obj.b1[BuildinIndex].getApartmentnum()==0) {
					aprt.getChildren().remove(0);
					ApartmentHeading.heading(aprt);
					aprt.setHgap(10);
				}
				obj.b1[BuildinIndex].addApartment(name.getText(), aprtname.getText(), Double.parseDouble(totelec.getText()),box.isSelected());
				UserandBuildingControls.addUser(obj,name.getText(),password.getText(),Email);
				obj.f.writeBuildingFile(obj.b1);
				obj.f.writeUserFile(obj.u1);
				s.addApartment(BuildinIndex, obj.b1[BuildinIndex].a.length-1);
				
				AlertBox.display("", "Apartment added!");

				s.addApartment(BuildinIndex,obj.b1[BuildinIndex].getApartmentnum()-1);
				
				Text apartmentname = new Text(20,20,aprtname.getText());
				Text Ownname = new Text(20,20,name.getText());
				Text elecrate = new Text(20,20,String.valueOf(obj.b1[BuildinIndex].a[obj.b1[BuildinIndex].getApartmentnum()-1].getElectricityrate()));
				Text totalelec = new Text(20,20,totelec.getText());
				
				int k = obj.b1[BuildinIndex].getApartmentnum()-1;
				
				CheckBox paid = new CheckBox("Paid");
				if (obj.b1[BuildinIndex].a[k].getIsPaid()) {
					paid.setSelected(true);
				}
				else {
					paid.setSelected(false);
				}
				
				int l = k;
				paid.setOnAction(g ->{
					if (paid.isSelected()) obj.b1[BuildinIndex].a[l].setIsPaid(true);
					if (!paid.isSelected()) obj.b1[BuildinIndex].a[l].setIsPaid(false);
					obj.f.writeBuildingFile(obj.b1);
				});
				//Circle c = new Circle();
				Button smsbt = new Button("Send Email");
				aprt.add(smsbt, 7, aprt.getColumnCount());
				smsbt.setOnAction(g->{
				    sendEmail(BuildinIndex,k);
				});
				
				HBox cntrls = new HBox();
				Button shutaprt = new Button("Shutdown Apartment");
				Button turnonaprt = new Button("Turn on Apartment");
				
				Text temp = new Text(20,20,"0.000 W");
				Text temp2 = new Text(20,20,String.format("%.2f",obj.b1[BuildinIndex].a[k].getTotalELectricity())+" kWh");
				shutaprt.setOnAction(g->{
					totalelec.setText(String.format("%.2f",obj.b1[BuildinIndex].a[k].getTotalELectricity())+" kWh");
					if (obj.b1[BuildinIndex].a[k].getOwner().equals("Raspberry PI")) {
						RasPi.controldownPi(false);
						AlertBox.display("Done!", "Apartment has been shut down.");
						shutaprt.setDisable(true);
						turnonaprt.setDisable(false);
						return;
					}
					aprt.getChildren().remove(elecrate);
					aprt.getChildren().remove(totalelec);
					MultiThreadThing m = s.allThreads.get(BuildinIndex).get(k);
					m.suspendThreads();
					aprt.add(temp, 2, aprt.getColumnCount());
					aprt.add(temp2, 3, aprt.getColumnCount());
					AlertBox.display("Done!", "Apartment has been shut down.");
					shutaprt.setDisable(true);
					turnonaprt.setDisable(false);
				});
				turnonaprt.setOnAction(g->{
					if (obj.b1[BuildinIndex].a[k].getOwner().equals("Raspberry PI")) {
						RasPi.controldownPi(true);
						AlertBox.display("Done!", "Apartment power has been restored");
						shutaprt.setDisable(false);
						turnonaprt.setDisable(true);
						return;
					}
					aprt.getChildren().remove(temp);
					aprt.getChildren().remove(temp2);
					//aprt.getChildren().remove(ctemp);
					MultiThreadThing m = s.allThreads.get(BuildinIndex).get(k);
					m.resumeThreads();
					aprt.add(elecrate, 2, aprt.getColumnCount());
					aprt.add(totalelec, 3, aprt.getColumnCount());
					//aprt.add(c, 5, k+1);
					AlertBox.display("Done!", "Apartment power has been restored");
					shutaprt.setDisable(false);
					turnonaprt.setDisable(true);
				});
				cntrls.getChildren().addAll(shutaprt,turnonaprt);
				cntrls.setSpacing(5);
				
				aprt.add(apartmentname, 0, aprt.getColumnCount());
				aprt.add(Ownname, 1, aprt.getColumnCount());
				aprt.add(elecrate, 2, aprt.getColumnCount());
				aprt.add(totalelec, 3, aprt.getColumnCount());
				aprt.add(cntrls, 6, aprt.getColumnCount());
				UpdateText o = new UpdateText(elecrate,totalelec,obj.b1[BuildinIndex],obj.b1[BuildinIndex].getApartmentnum()-1,text2);
				o.start();
				
				aprt.add(box, 4, aprt.getColumnCount());
				aprt.add(s.circles.get(BuildinIndex).apartmentcircles.get(obj.b1[BuildinIndex].getApartmentnum()-1), 5, aprt.getColumnCount());
				windowt.close();
				
			});
			pane.add(Add, 1, 7);
			pane.setVgap(10);
			pane.setHgap(10);
			pane.setPadding(new Insets(10,10,10,10));
			
			Scene scenetemp = new Scene(pane,300,300);
			windowt.setScene(scenetemp);
			windowt.showAndWait();
		});
		
		Button delaprt = new Button("Remove apartment");
		delaprt.setOnAction(e->{
			Stage tempwindow = new Stage();
			BorderPane p = new BorderPane();
			p.setLeft(new Label("Enter apartment name"));
			TextField t = new TextField();
			p.setCenter(t);
			Button del = new Button("Delete");
			p.setBottom(del);
			BorderPane.setAlignment(del, Pos.CENTER);
			p.setPadding(new Insets(10,10,10,10));
			del.setOnAction(q->{
				if (!ConfirmBox.display("", "Are you sure you want to delete an apartment?")) return;
				for (int i = 0;i<obj.b1[BuildinIndex].getApartmentnum();i++) {
					if (t.getText().equalsIgnoreCase(obj.b1[BuildinIndex].a[i].getApartmentname())) {
						UserandBuildingControls.removeUser(obj,obj.b1[BuildinIndex].a[i].getOwner());
						obj.b1[BuildinIndex].deleteApartment(i);
						s.removeApartment(BuildinIndex,i);
						AlertBox.display("Success", "Apartment has been deleted!");
						AlertBox.display("","Go back and enter Building again to see refreshed values");
						obj.f.writeBuildingFile(obj.b1);
						tempwindow.close();
						return;
					}
				}
				AlertBox.display("Failed", "Apartment was not found.");
				tempwindow.close();
			});
			Scene s = new Scene(p,100,100);
			tempwindow.setScene(s);
			tempwindow.show();
		});
	    gpane.add(addaprt,0,2);
		gpane.add(delaprt,0,3);	
		
		if (obj.b1[BuildinIndex].getApartmentnum()==0) {
			aprt.add(new Label("No apartments have been set.\nUse 'Add Apartment' to add one."),0,0);
		}
		else {
			ApartmentHeading.heading(aprt);
			for (int i = 0;i<obj.b1[BuildinIndex].getApartmentnum();i++) {
				Text l1 = new Text(20,20,obj.b1[BuildinIndex].a[i].getApartmentname());
				Text l2 = new Text(20,20,obj.b1[BuildinIndex].a[i].getOwner());
				Text l3 = new Text(20,20,String.format("%.2f",obj.b1[BuildinIndex].a[i].getElectricityrate())+" W");
				Text l4 = new Text(20,20,String.format("%.2f",obj.b1[BuildinIndex].a[i].getTotalELectricity())+" kWh");
				UpdateText pmref;
				UpdateText pm = new UpdateText(l3,l4,obj.b1[BuildinIndex],i,text2);
				pm.start();
				pmref = pm;

				CheckBox paid = new CheckBox("Paid");
				if (obj.b1[BuildinIndex].a[i].getIsPaid()) {
					paid.setSelected(true);
				}
				else {
					paid.setSelected(false);
				}
				
				int l = i;
				paid.setOnAction(e ->{
					if (paid.isSelected()) obj.b1[BuildinIndex].a[l].setIsPaid(true);
					if (!paid.isSelected()) obj.b1[BuildinIndex].a[l].setIsPaid(false);
					obj.f.writeBuildingFile(obj.b1);
				});
				Circle c = new Circle();
				Button smsbt = new Button("Send Email");
				aprt.add(smsbt, 7, i+1);
				int y= i;
				smsbt.setOnAction(e->{
					sendEmail(BuildinIndex,y);
				});
				
				HBox cntrls = new HBox();
				Button shutaprt = new Button("Shutdown Apartment");
				Button turnonaprt = new Button("Turn on Apartment");
				turnonaprt.setDisable(true);
				int k = i;
				Text temp = new Text(20,20,"0.000 W");
				Text temp2 = new Text(20,20,String.format("%.2f",obj.b1[BuildinIndex].a[i].getTotalELectricity())+" kWh");
				Circle ctemp = new Circle();
				ctemp.setRadius(10);
				ctemp.setFill(Color.GRAY);
				shutaprt.setOnAction(e->{
					temp2.setText(String.format("%.2f",obj.b1[BuildinIndex].a[k].getTotalELectricity())+" kWh");
					if (obj.b1[BuildinIndex].a[k].getOwner().equals("Raspberry PI")) {
						RasPi.controldownPi(false);
						AlertBox.display("Done!", "Apartment has been shut down.");
						shutaprt.setDisable(true);
						turnonaprt.setDisable(false);
						return;
					}
					aprt.getChildren().remove(l3);
					aprt.getChildren().remove(l4);
					aprt.getChildren().remove(c);
					pm.suspend();
					aprt.add(temp, 2, k+1);
					aprt.add(temp2, 3, k+1);
					aprt.add(ctemp, 5, k+1);
					AlertBox.display("Done!", "Apartment has been shut down.");
					shutaprt.setDisable(true);
					turnonaprt.setDisable(false);
				});
				turnonaprt.setOnAction(e->{
					if (obj.b1[BuildinIndex].a[k].getOwner().equals("Raspberry PI")) {
						RasPi.controldownPi(true);
						AlertBox.display("Done!", "Apartment power has been restored");
						shutaprt.setDisable(false);
						turnonaprt.setDisable(true);
						return;
					}
					aprt.getChildren().remove(temp);
					aprt.getChildren().remove(temp2);
					aprt.getChildren().remove(ctemp);
					pm.resume();
					aprt.add(l3, 2, k+1);
					aprt.add(l4, 3, k+1);
					aprt.add(c, 5, k+1);
					AlertBox.display("Done!", "Apartment power has been restored");
					shutaprt.setDisable(false);
					turnonaprt.setDisable(true);
				});
				cntrls.getChildren().addAll(shutaprt,turnonaprt);
				cntrls.setSpacing(5);
				aprt.add(cntrls, 6, i+1);
				c.setRadius(10);
				c.setFill(Color.GRAY);
				aprt.add(l1,0,i+1);
				aprt.add(l2,1,i+1);
				
				aprt.add(paid,4,i+1);
				aprt.add(c, 5, i+1);
				MultiThreadThing m = new MultiThreadThing(c,obj.b1[BuildinIndex],i);
				aprt.add(l3,2,i+1);
				aprt.add(l4,3,i+1);
				aprt.setHgap(10);
						
			}
		}
		gpane.add(aprt,1,3);
		
		Button backButton = new Button("Back");
		backButton.setOnAction(e ->{
			window.setScene(scene3);
			window.setTitle("Sector " + (sectornum+1));
		});
		gpane.add(backButton,1,4);
		gpane.add(colorusage.set(), 0, 5);
		gpane.setVgap(10);
		gpane.setHgap(10);
		gpane.setPadding(new Insets(20,20,20,20));
		scene4 = new Scene(gpane,1100,400);
		window.setScene(scene4);
		window.setTitle("Building : " + obj.b1[BuildinIndex].getBuildingnum());
		window.show();
	}
	
	static void addBuildinginSector(int Buildingnum, String address,int Apartmentnum,int sector) {
		
		Stage addaprt = new Stage();

		addaprt.initModality(Modality.APPLICATION_MODAL);
		addaprt.setTitle("Add Apartments");
		addaprt.setMinWidth(350);
		
		for (int i = 0;i<obj.b1.length;i++) {
			if (obj.b1[i].getSector()==sector && obj.b1[i].getBuildingnum()==Buildingnum) {
				AlertBox.display("Duplication error.","This building is already in the sector.");
				return;
			}
		}

		UserandBuildingControls.addBuilding(obj,Buildingnum, address, Apartmentnum, sector); isDone = true;
		s.addBuilding();
		
		Button savebutton = new Button("Save");
		Button backbutton = new Button("Go back");
	
		GridPane WholePane = new GridPane();
		WholePane.setPadding(new Insets(10,10,10,10));
		WholePane.setHgap(10);
		WholePane.setVgap(20);
		
		TextField allTextFields[][] = new TextField[Apartmentnum][5];
	
		ChoiceBox isPaid[] = new ChoiceBox[Apartmentnum];

		int xright = 0;
		int ydown = 0;
		for (int i =0;i<Apartmentnum;i++) {
			GridPane tempPane = new GridPane();
			tempPane.add(new Label("Owner name"), 0, 0);
			allTextFields[i][0] = new TextField();
			tempPane.add(allTextFields[i][0], 1, 0);
			tempPane.add(new Label("Apartment name"), 0, 1);
			allTextFields[i][1] = new TextField();
			tempPane.add(allTextFields[i][1], 1, 1);
			tempPane.add(new Label("Total kWh used"), 0, 2);
			allTextFields[i][2] = new TextField();
			tempPane.add(allTextFields[i][2], 1, 2);
			tempPane.add(new Label("This month bill paid?"), 0, 3);
			isPaid[i] = new ChoiceBox<String>();
			isPaid[i].getItems().add("Yes");
			isPaid[i].getItems().add("No");
			isPaid[i].setValue("Yes");
			tempPane.add(isPaid[i], 1, 3);
			allTextFields[i][3] = new TextField();
			tempPane.add(new Label("Password"), 0, 4);
			tempPane.add(allTextFields[i][3], 1, 4);
			allTextFields[i][4] = new TextField();
			tempPane.add(new Label("Email Address"), 0, 5);
			tempPane.add(allTextFields[i][4], 1, 5);
			tempPane.setHgap(5);tempPane.setVgap(5);
			WholePane.add(tempPane, xright, ydown);
			xright++;
			if (xright%5==0) {xright=0;ydown++;}
		}
		WholePane.add(savebutton, xright+1, ydown+1);
		WholePane.add(backbutton, 0, ydown+1);
		
		savebutton.setOnAction(e->{
			for (int i = 0;i<allTextFields.length;i++) {
				String tempstr[] = new String[4];
				
				if (allTextFields[i][0].getText().isEmpty() || allTextFields[i][1].getText().isEmpty() ||allTextFields[i][2].getText().isEmpty() || allTextFields[i][3].getText().isEmpty()) {
					AlertBox.display("Error", "Fields cannot be left empty!");
					return;
				}
				tempstr[0] = allTextFields[i][0].getText();
				tempstr[1] = allTextFields[i][1].getText();
				tempstr[2] = allTextFields[i][2].getText();
				if (isPaid[i].getValue().equals("Yes")) tempstr[3] = "true";
				else tempstr[3] = "false";
				obj.b1[obj.b1.length-1].setApartment(tempstr, i);
				UserandBuildingControls.addUser(obj,tempstr[0], allTextFields[i][3].getText(),allTextFields[i][4].getText());

				s.addApartment(obj.b1.length-1, i);	
			}
				addaprt.close();
				AlertBox.display("", "Saved!");
				obj.f.writeBuildingFile(obj.b1);
				isDone = true;
				return;
			});
		backbutton.setOnAction(l ->{
			addaprt.close();
			return;
		});

		if (Apartmentnum>0) {
			Scene AddApartmentScene = new Scene(WholePane); 
			addaprt.setScene(AddApartmentScene);
			addaprt.showAndWait();
		}
		else AlertBox.display("", "Saved!");
		
		addaprt.setOnCloseRequest(e->{
			return;
		});
	}
		
	public static void starter(String[] args) {
		launch(args);
	}
	
	public void BuildingBox(int sectornum) {

		Stage BoxWindow = new Stage();
		BoxWindow.initModality(Modality.APPLICATION_MODAL);
		BoxWindow.setTitle("Add Building");
		BoxWindow.setMinWidth(350);
		GridPane pane = new GridPane();
		TextField buildingnumber = new TextField();
		TextField buildingaddress = new TextField();
		TextField apartmentnum = new TextField();
		Button savebutton = new Button("Save");
		Button cancel = new Button("cancel");
		
		savebutton.setOnAction(e ->{
			if (!buildingnumber.getText().isEmpty() && !buildingaddress.getText().isEmpty() && !apartmentnum.getText().isEmpty()) {
				try {
					Integer.parseInt(buildingnumber.getText());
					Integer.parseInt(apartmentnum.getText());
				}
				catch(Exception p){
					AlertBox.display("Error", "Invalid Fields!");
				}
				isDone = false;
				addBuildinginSector(Integer.parseInt(buildingnumber.getText()),buildingaddress.getText(),Integer.parseInt(apartmentnum.getText()),sectornum);
				if (isDone) BoxWindow.close();
				else UserandBuildingControls.deleteBuilding(obj, obj.b1.length-1);
				isDone = false;
				return;
			}
			else {
				AlertBox.display("Empty Fields", "Fields cannot be left empty.");
			}
		});
		cancel.setOnAction(e ->{
			BoxWindow.close();
		});
		
		pane.add(new Label("Building number"), 0, 0);
		pane.add(new Label("Building address"), 0, 1);
		pane.add(new Label("Number of Apartments"), 0, 2);
		pane.add(buildingnumber, 1, 0);
		pane.add(buildingaddress, 1, 1);
		pane.add(apartmentnum, 1, 2);
		pane.add(savebutton, 1, 3);
		savebutton.setAlignment(Pos.CENTER);
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(10,10,10,10));
			
		Scene BoxScene = new Scene(pane,300,200);
		BoxWindow.setScene(BoxScene);
		BoxWindow.showAndWait();
	}
	
	public void adminpane() {
		Rectangle2D screenSize = Screen.getPrimary().getBounds();
		Button button2 = new Button("Sign out");
		button2.setOnAction(e -> {
			window.setScene(scene1);
			window.setTitle("Log in");
		});
		BorderPane layout2 = new BorderPane();
		layout2.setBottom(button2);
		BorderPane.setAlignment(button2, Pos.BOTTOM_CENTER);
		Pane p = sectors();
		layout2.setCenter(p);
		layout2.setLeft(scene2leftbuttons());
		layout2.setRight(scene2rightbuttons());
		Label label2 = new Label("Darkened color means no electricity is supplied there.");
		label2.setPadding(new Insets(30,0,0,0));
		BorderPane.setAlignment(label2, Pos.TOP_CENTER);
		layout2.setTop(label2);
		scene2 = new Scene(layout2, screenSize.getWidth()*0.6, screenSize.getWidth()*0.5);
	}
	
	public void customerpane() {
		int i = 0,j = 0;
		boolean isFound = false;
		for (i = 0;i<obj.b1.length;i++) {
			for (j = 0;j<obj.b1[i].getApartmentnum();i++) {
				if (user.getText().equals(obj.b1[i].a[j].getOwner())) {
					isFound=true;
					break;
				}
			}
			if(isFound) break;
		}

		if (isFound==false) {
			AlertBox.display("Invalid Credentials", "Check your details and try again.");
			window.setScene(scene1);
			return;
		}
		
		GridPane p = new GridPane();
		p.setPadding(new Insets(20,20,20,20));
		
		Text yourapart = new Text();
		yourapart.setText("Your Apartment");
		yourapart.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
		p.add(yourapart,0,0);
		
		Text apartname = new Text();
		apartname.setText("Apartment name: ");
		apartname.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
		p.add(apartname, 0, 1);
		p.add(new Text(obj.b1[i].a[j].getApartmentname()), 1, 1);
		
		Text ownername = new Text();
		ownername.setText("Owner : ");
		ownername.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
		p.add(ownername, 0, 2);
		p.add(new Text(obj.b1[i].a[j].getOwner()), 1, 2);

		Text elecrate = new Text();
		elecrate.setText("Electricity rate: ");
		elecrate.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
		p.add(elecrate, 0, 3);
		Text elecratechange = new Text(String.format("%.3f", (obj.b1[i].a[j].getElectricityrate())));
		p.add(elecratechange, 1, 3);
		Text totalelec = new Text();
		totalelec.setText("Total electricity: ");
		totalelec.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
		p.add(totalelec, 0, 4);
		Text totalelecchange = new Text(String.format("%.3f", (obj.b1[i].a[j].getTotalELectricity())));
		p.add(totalelecchange, 1, 4);
		UpdateText q = new UpdateText(elecratechange,totalelecchange,obj.b1[i],j,new Text("0"));
		q.start();
		Circle l = s.circles.get(i).apartmentcircles.get(j);
		Text usage = new Text("Usage ");
		usage.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
		p.add(usage, 0, 5);
		p.add(l, 1, 5);
		
		p.add(colorusage.set(), 0, 6);
		
		Button backbutton = new Button("Sign out!");
		backbutton.setOnAction(e->{
			window.setScene(scene1);
		});
		backbutton.setPadding(new Insets(10,10,10,10));
		p.add(backbutton, 1, 7);
		scene2 = new Scene(p,300,300);
		window.setScene(scene2);
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		sectorstatus = new boolean[14];
		
		obj = new WholeObject();
		UserandBuildingControls.initialize(obj);
		
		s = new Simulation(obj);
		
		
		window = stage;
		
		Label label1 = new Label("Enter your Username and Password.");
		Button button1 = new Button("Sign in");
			
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(5.5);
		pane.setVgap(10.5);
		pane.add(label1,0,0);
		pane.add(new Label("Username"), 0, 1);
		pane.add(new Label("Password"), 0, 2);
		user = new TextField();
		pane.add(user, 1, 1);
		password = new PasswordField();
		pane.add(password, 1, 2);
		pane.add(button1, 0, 3);
		GridPane.setHalignment(button1,HPos.RIGHT);
		scene1 = new Scene(pane, 400, 200);
		
		Rectangle2D screenSize = Screen.getPrimary().getBounds();

		button1.setOnAction(e ->{
			boolean details_correct = UserandBuildingControls.userCheck(obj,user.getText(),password.getText());
			
			if (details_correct) {
				boolean isAdmin = false;
				if (user.getText().contains("Admin")) isAdmin=true;
				
				if (isAdmin) {
					adminpane();
					window.setScene(scene2);
					window.setTitle("Map of Uttara");
				}
				else {
					customerpane();
					window.setTitle("Your Apartment");
				}
			}
			else {
				AlertBox.display("Login failed","Your credentials are incorrect!");
				return;
			}
		});
		
		window.setScene(scene1);
		window.setTitle("Log in");
		window.show();
		
		window.setOnCloseRequest(e->{
			obj.f.writeBuildingFile(obj.b1);
			System.exit(0);
		});
		
	}
	
	public void lowerbrightness(int index) {
		ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-0.1);
        if (!sectorstatus[index])imageview[index].setEffect(blackout);
        imageview[index].setCache(true);
        imageview[index].setCacheHint(CacheHint.SPEED);
	}
	public void increasebrightness(int index) {
		ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(0.1);
        if (!sectorstatus[index])imageview[index].setEffect(blackout);
        imageview[index].setCache(true);
        imageview[index].setCacheHint(CacheHint.SPEED);
	}
	
	public void setupsector(int index) {
		String path = "Sector buttons/";
		imageview[index] = new ImageView(new Image(path + "Sector "+ (index + 1)+".png"));
		imageview[index].setOnMouseClicked(e -> loadScene3(index));
		imageview[index].setOnMouseEntered(e -> lowerbrightness(index));
		imageview[index].setOnMouseExited(e -> increasebrightness(index));
	}
	
	public Pane sectors() {
		imageview = new ImageView[14];
		Image image = new Image("map.png");
		ImageView imagemap = new ImageView(image);
		Rectangle2D screenSize = Screen.getPrimary().getBounds();
		imagemap.setFitHeight(screenSize.getHeight()*0.72);
		imagemap.setPreserveRatio(true);
	
		int s = 356,t = 77;
		for (int i = 0;i<14;i++)setupsector(i);
		imageview[0].setX(679-s);
		imageview[0].setY(563-t);
		imageview[1].setX(728-s);
		imageview[1].setY(592-t);
		imageview[2].setX(641-s);
		imageview[2].setY(473-t);
		imageview[3].setX(712-s);
		imageview[3].setY(476-t);
		imageview[4].setX(533-s);
		imageview[4].setY(459-t);
		imageview[5].setX(709-s);
		imageview[5].setY(384-t);
		imageview[6].setX(644-s);
		imageview[6].setY(394-t);
		imageview[7].setX(719-s);
		imageview[7].setY(319-t);
		imageview[8].setX(646-s);
		imageview[8].setY(316-t);
		imageview[9].setX(534-s);
		imageview[9].setY(166-t);
		imageview[10].setX(486-s);
		imageview[10].setY(324-t);
		imageview[11].setX(479-s);
		imageview[11].setY(398-t);
		imageview[12].setX(514-s);
		imageview[12].setY(391-t);
		imageview[13].setX(504-s);
		imageview[13].setY(457-t);
		
		for (int i = 0;i<imageview.length;i++) {
			
			imageview[i].setFitHeight(screenSize.getHeight()*0.11);
			imageview[i].setPreserveRatio(true);
		}
		imageview[9].setFitHeight(screenSize.getHeight()*0.25);
		imageview[6].setFitHeight(screenSize.getHeight()*0.13);
		imageview[5].setFitHeight(screenSize.getHeight()*0.14);
		imageview[4].setFitHeight(screenSize.getHeight()*0.16);
		imageview[3].setFitHeight(screenSize.getHeight()*0.17);
		imageview[2].setFitHeight(screenSize.getHeight()*0.135);
		
		Pane imagebox = new Pane();
		imagemap.setTranslateX(76);
		imagemap.setTranslateY(54);
		imagebox.getChildren().addAll(imagemap);
		imagebox.getChildren().addAll(imageview);
		
		return imagebox;
	}
	
	public void loadScene3(int sectornum) {
		
		BorderPane gpane = new BorderPane();
		Label toplabel = new Label("Sector Settings");
		BorderPane.setAlignment(toplabel, Pos.TOP_CENTER);
		toplabel.setPadding(new Insets(20,0,20,0));
		gpane.setTop(toplabel);
		Image image = new Image("Sectors/Sector " + (sectornum+1) + ".png");
		ImageView imageview = new ImageView(image);
		Rectangle2D screenSize = Screen.getPrimary().getBounds();
		imageview.setFitHeight(screenSize.getHeight()*0.67);
		imageview.setPreserveRatio(true);
		
		Pane stuff = new Pane();
		stuff.getChildren().add(imageview);
		gpane.setCenter(stuff);
		
		VBox buttonbox = new VBox();
		
		int j;
		for (j = 0;j<obj.b1.length;j++) {
			if ((sectornum+1) == obj.b1[j].getSector()) {
				Circle c = s.circles.get(j).buildingCircle;
				Label l = s.circles.get(j).buildinglabel;
				Circle cover = s.circles.get(j).cover;
				
				l.setOnMouseEntered(e -> cover.setVisible(true));
				l.setOnMouseExited(e -> cover.setVisible(false));

				int k = j;

				l.setOnMousePressed(e-> {
					BuildingInfo(sectornum,k);
					});
				c.setOnMousePressed(e-> {
					BuildingInfo(sectornum,k);
					});
				c.setLayoutX(obj.b1[j].getCircleX());
				c.setLayoutY(obj.b1[j].getCircleY());
				
				cover.setLayoutX(c.getLayoutX());
				cover.setLayoutY(c.getLayoutY());
				
				l.setLayoutX(c.getLayoutX()-7);
				l.setLayoutY(c.getLayoutY()-7);
				stuff.getChildren().addAll(c,l,cover);
			}
		}
		
		Button setpos = new Button("Set Position ✔");
		Button cancelpos = new Button("Cancel ✘");
		VBox vbox = new VBox();
		vbox.getChildren().addAll(setpos,cancelpos,colorusage.set());
		vbox.setSpacing(10);
		gpane.setRight(vbox);
		vbox.setAlignment(Pos.CENTER);
		setpos.setVisible(false);
		cancelpos.setVisible(false);
		
		Button addbldg = new Button("Add building");
		Button removebldg = new Button("Remove building");
		
		setpos.setOnAction(e ->{
			obj.b1[obj.b1.length-1].setCircleX(s.circles.get(s.circles.size()-1).buildingCircle.getLayoutX());
			obj.b1[obj.b1.length-1].setCircleY(s.circles.get(s.circles.size()-1).buildingCircle.getLayoutY());
			obj.f.writeBuildingFile(obj.b1);
			AlertBox.display("","Saved!");
			setpos.setVisible(false);
			cancelpos.setVisible(false);
			addbldg.setDisable(false);
			removebldg.setDisable(false);
		});
		cancelpos.setOnAction(e->{
			UserandBuildingControls.deleteBuilding(obj,obj.b1.length-1);
			stuff.getChildren().remove(s.circles.get(s.circles.size()-1).buildingCircle);
			stuff.getChildren().remove(s.circles.get(s.circles.size()-1).buildinglabel);
			AlertBox.display("", "You havent set Building position so its values have been removed.");
			setpos.setVisible(false);
			cancelpos.setVisible(false);
			addbldg.setDisable(false);
			removebldg.setDisable(false);
		});
		
		Draggg d = new Draggg();
		
		addbldg.setOnAction(e -> {
			int oldb1len = obj.b1.length;
			BuildingBox(sectornum + 1);
			if(oldb1len<obj.b1.length) {
				Circle c = s.circles.get(obj.b1.length-1).buildingCircle;
				Label l = s.circles.get(obj.b1.length-1).buildinglabel;
				c.setLayoutX(20);
				c.setLayoutY(20);
				Circle cover = s.circles.get(obj.b1.length-1).cover;

				l.setOnMousePressed(o-> {
					BuildingInfo(sectornum,obj.b1.length-1);
					});
				c.setOnMousePressed(o-> {
					BuildingInfo(sectornum,obj.b1.length-1);
					});
				cover.setVisible(false);
				l.setOnMouseEntered(g -> cover.setVisible(true));
				l.setOnMouseExited(g -> cover.setVisible(false));
				
				d.makedrag(c, l);
				stuff.getChildren().addAll(c,l);
				
				setpos.setVisible(true);
				cancelpos.setVisible(true);
				addbldg.setDisable(true);
				removebldg.setDisable(true);
			}
		});
		
		removebldg.setOnAction(e ->{
			Stage deletebldg = new Stage();
			Label l = new Label("Enter the Building number to delete:");
			TextField t = new TextField();
			GridPane gp = new GridPane();
			Button deleteb = new Button("Delete");
			deleteb.setOnAction(k ->{
				if (!ConfirmBox.display("", "Are you sure you want to delete a Building?")) return;
				if (t.getText().isEmpty()) {
					AlertBox.display("Failed!", "Field was left blank.");
					return;
				}
				boolean isDeleted = false;
				for (int i = 0;i<obj.b1.length;i++) {
					if ((sectornum+1)==obj.b1[i].getSector() && Integer.parseInt(t.getText())==obj.b1[i].getBuildingnum()) {
						UserandBuildingControls.deleteBuilding(obj,i);
						isDeleted = true;
						for (int x = 0;x<s.circles.size();x++) {
							if (s.circles.get(x).circleindex==i) {
								stuff.getChildren().remove(s.circles.get(x).buildingCircle);
								stuff.getChildren().remove(s.circles.get(x).buildinglabel);
								s.removeBuilding(i);
							}
						}
						break;
					}
				}
				if (isDeleted) AlertBox.display("Success!", "Building has been deleted from sector.");
				else AlertBox.display("Failed!", "The entered building number was incorrect.");
				obj.f.writeBuildingFile(obj.b1);
				deletebldg.close();
				isDeleted = false;
			});
			
			gp.add(l,0,0);
			gp.add(t, 1, 0);
			gp.add(deleteb, 1, 1);
	
			deletebldg.initModality(Modality.APPLICATION_MODAL);
			deletebldg.setTitle("Delete Building");
			Scene deletebldgscene = new Scene(gp);
			deletebldg.setScene(deletebldgscene);
			deletebldg.showAndWait();
		});
		Button backbutton = new Button("Go back");
		backbutton.setPadding(new Insets(20,20,20,20));
		BorderPane.setAlignment(backbutton,Pos.BOTTOM_CENTER);
		backbutton.setOnAction(e ->{
			window.setScene(scene2);
			window.setTitle("Map of Uttara");
		});
		
		buttonbox.getChildren().add(addbldg);
		buttonbox.getChildren().add(removebldg);
		buttonbox.setAlignment(Pos.CENTER);
		buttonbox.setPadding(new Insets(0,30,0,30));
		
		gpane.setLeft(buttonbox);
		gpane.setBottom(backbutton);
		scene3 = new Scene(gpane);
		window.setScene(scene3);
		window.setTitle("Sector " + (sectornum+1));
		
	}
	
	public void reducecontrast(int index) {
		ColorAdjust blackout = new ColorAdjust();
        blackout.setContrast(-0.8);

        if (!sectorstatus[index])imageview[index].setEffect(blackout);
        imageview[index].setCache(true);
        imageview[index].setCacheHint(CacheHint.SPEED);
	}
	public void increasecontrast(int index) {
		ColorAdjust blackout = new ColorAdjust();
        blackout.setContrast(0.8);

        if (!sectorstatus[index])imageview[index].setEffect(blackout);
        imageview[index].setCache(true);
        imageview[index].setCacheHint(CacheHint.SPEED);
	}
	
	public VBox scene2leftbuttons() {
		//for scene2
				VBox leftbox = new VBox();
				Button shutsectors[] = new Button[15];
				
				for (int i = 0;i<14;i++) {
					shutsectors[i] = new Button("Shutdown Sector " + (i+1));
					int j = i;
					shutsectors[i].setOnAction(e -> {
						if(ConfirmBox.display("Shutdown Sector "+(j+1),"Are you sure?")) {
							reducecontrast(j);
							sectorstatus[j]=true;
							UserandBuildingControls.controlSector(obj,s.allThreads,j+1,false);
						}
					});
				}
				
				shutsectors[14] = new Button("Shutdown all Sectors");
				shutsectors[14].setOnAction(e -> {
					if(ConfirmBox.display("Shutdown all Sectors","Are you sure?")) {
						for (int i = 0;i<14;i++) {
							reducecontrast(i);
							sectorstatus[i]=true;
							UserandBuildingControls.controlSector(obj,s.allThreads,i+1,false);
							}
					}
				});
				
				leftbox.getChildren().addAll(shutsectors);
				leftbox.setAlignment(Pos.CENTER);
				leftbox.setPadding(new Insets(0,0,0,30));
				leftbox.setSpacing(10);
				
				return leftbox;
	}
	
	public VBox scene2rightbuttons() {
		//for scene2
				VBox rightbox = new VBox();
				Button onsectors[] = new Button[15];
				
				for (int i = 0;i<14;i++) {
					onsectors[i] = new Button("Turn on Sector "+ (i+1));
					int j = i;
					onsectors[i].setOnAction(e -> {
						if(ConfirmBox.display("Turn on Sector "+(j+1),"Are you sure?")) {
							sectorstatus[j]=false;
							increasecontrast(j);
							UserandBuildingControls.controlSector(obj,s.allThreads,j+1,true);
						}
					});
				}
		
				onsectors[14] = new Button("Turn on all Sectors");
				onsectors[14].setOnAction(e -> {
					if(ConfirmBox.display("Turn on all Sectors","Are you sure?")) {
						for (int j = 0;j<14;j++) {
							sectorstatus[j]=false;
							increasecontrast(j);
							UserandBuildingControls.controlSector(obj,s.allThreads,j+1,true);
							}
					}
				});
				
				rightbox.getChildren().addAll(onsectors);
				rightbox.setAlignment(Pos.CENTER);
				rightbox.setPadding(new Insets(0,30,0,30));
				rightbox.setSpacing(10);
				
				return rightbox;
	}
	

}
