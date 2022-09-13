import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class CircleData extends Circle{
	Circle buildingCircle;
	ArrayList<Circle> apartmentcircles = new ArrayList<>();
	Circle cover;
	int circleindex;
	Label buildinglabel;
}
public class Simulation extends Thread{
	public static ArrayList<ArrayList<MultiThreadThing>> allThreads = new ArrayList<ArrayList<MultiThreadThing>>();
	public static ArrayList<CircleData> circles = new ArrayList<>();
	public RasPi r;
	
	WholeObject obj;
	
	Simulation(WholeObject obj){
		this.obj = obj;
		begin();
	}
	
	void addCircles(int index) {
		
		CircleData c = new CircleData();
		c.buildingCircle = new Circle();
		c.buildingCircle.setRadius(10);
		c.buildingCircle.setFill(Color.GRAY);
		
		for (int j = 0;j<obj.b1[index].a.length;j++) {
			Circle a = new Circle();
			a.setRadius(10);
			a.setFill(Color.GRAY);
			c.apartmentcircles.add(a);
			
			MultiThreadThing m2 = new MultiThreadThing(c.apartmentcircles.get(j),obj.b1[index],j);
		}
		
		MultiThreadThing m3 = new MultiThreadThing(c.buildingCircle,obj.b1[index]);
		
		c.cover = new Circle();
		c.cover.setRadius(12);
		c.cover.setStroke(Color.BLACK);
		c.cover.setStrokeWidth(5);
		c.cover.setFill(Color.TRANSPARENT);
		c.cover.setVisible(false);
		
		c.buildinglabel = new Label(String.valueOf(obj.b1[index].getBuildingnum()));
		c.buildinglabel.setOnMouseEntered(e -> c.cover.setVisible(true));
		c.buildinglabel.setOnMouseExited(e -> c.cover.setVisible(false));
		c.buildinglabel.setScaleX(1.25);
		c.buildinglabel.setScaleY(1.25);
		
		circles.add(c);
		circles.get(index).circleindex = index;
	}
	
	public void begin() {
		for (int i = 0;i<obj.b1.length;i++) {
			allThreads.add(new ArrayList<MultiThreadThing>());
			for (int j = 0;j<obj.b1[i].a.length;j++){
				if (obj.b1[i].a[j].getOwner().equals("Raspberry PI")) {
					r.runRaspberryPi(obj.b1, i, j);
					MultiThreadThing p = new MultiThreadThing();
					allThreads.get(i).add(p);
					continue;
				}
				MultiThreadThing m = new MultiThreadThing(obj.b1[i],j);
				allThreads.get(i).add(m);
			}
		}
		
		for(int i = 0;i<obj.b1.length;i++) {
			addCircles(i);
		}
	}
	
	public void addBuilding() {
		addCircles(obj.b1.length-1);
		allThreads.add(new ArrayList<MultiThreadThing>());
	}
	public void addApartment(int index,int apartindex) {
		Circle a = new Circle();
		a.setRadius(10);
		a.setFill(Color.GRAY);
		circles.get(index).apartmentcircles.add(a);
		
		MultiThreadThing m = new MultiThreadThing(obj.b1[index],apartindex);
		allThreads.get(index).add(m);
		
		MultiThreadThing m2 = new MultiThreadThing(circles.get(index).apartmentcircles.get(apartindex),obj.b1[index],apartindex);
	}
	
	public void removeBuilding(int index) {
		circles.remove(index);
		allThreads.remove(index);
	}
	public void removeApartment(int index,int apartindex) {
		circles.get(index).apartmentcircles.remove(apartindex);
		allThreads.get(index).remove(apartindex);
	}
}
