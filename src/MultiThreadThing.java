import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
public class MultiThreadThing extends Thread{
	Thread t1,t3;
	public int threadofsector;
	MultiThreadThing(){}
	MultiThreadThing(WholeObject obj,int index,int apartmentnum){
		this.threadofsector = obj.b1[index].getSector();
		t1 = new Thread() {
			public void run() {
				double colorval;
				while (true) {
					Random r = new Random();
//					colorval = r.nextDouble(0.1,0.5);
					colorval = 0.1 + (0.4 * r.nextDouble());
					try {
						obj.b1[index].a[apartmentnum].setElectricityrate(colorval);
					}
					catch (Exception e) {
						System.out.println("Exception");
						this.stop();
						return;
					}
					obj.b1[index].a[apartmentnum].setTotalElectricity(obj.b1[index].a[apartmentnum].getTotalELectricity()+(colorval/1000));
					
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						System.out.println("exobj");
						this.stop();
						return;
					}
			}
			}
		};t1.start();
	}
	MultiThreadThing(Circle c, Building b){

		Thread t2 = new Thread() {
			public void run() {
				while (true) {
					double avgrate = 0;
					for (int i = 0;i<b.getApartmentnum();i++) {
						avgrate +=b.a[i].getElectricityrate();
					}
					avgrate = avgrate/b.getApartmentnum();
					if (avgrate>0.0 && avgrate<=0.3) {
						c.setFill(Color.GREEN);
					}
					else if(avgrate>0.3 && avgrate<0.45) {
						c.setFill(Color.ORANGE);
					}
					else if (avgrate>=0.45 && avgrate<=0.5) c.setFill(Color.RED);
					else c.setFill(Color.GRAY);
					
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						System.out.println("Ex");
						this.stop();
						return;
					}
				}
			}
		};t2.start();
	}
	MultiThreadThing(Building b, int apartmentnum){
		this.threadofsector = b.getSector();
		
		t3 = new Thread() {
			@Override
			public void run() {
				double colorval;
				while (true) {
					{
						Random r = new Random();
						colorval = 0.1 + (0.4 * r.nextDouble());
						try {
							b.a[apartmentnum].setElectricityrate(colorval);
						}
						catch (Exception e) {
							System.out.println("Ex");
							this.stop();
							return;
						}
						
						b.a[apartmentnum].setTotalElectricity(b.a[apartmentnum].getTotalELectricity()+(colorval/1000));
		
						try {
							Thread.sleep(1000);
						}
						catch (InterruptedException e) {
							System.out.println("Ex");
							this.stop();
							return;
						}
					}
				}
			}
		};t3.start();
	}
	MultiThreadThing(Circle c, Building b, int apartmentnum){
		
		Thread t4 = new Thread() {
			public void run() {
				double colorval = 0;
				while (true) {
					try {
						colorval = b.a[apartmentnum].getElectricityrate();
					}
					catch(Exception e) {
						System.out.println("Ex1c");
						this.stop();
						return;
					}
					if (colorval>0.0 && colorval<=0.3) {
						c.setFill(Color.GREEN);
					}
					else if(colorval>0.3 && colorval<0.45) {
						c.setFill(Color.ORANGE);
					}
					else if (colorval>=0.45 && colorval<=0.5) c.setFill(Color.RED);
					else c.setFill(Color.GRAY);
					
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e) {
						System.out.println("Ex2c");
						this.stop();
						return;
					}
				}
			}
		};t4.start();
	}

	public void suspendThreads() {
		//t1.suspend();
		t3.suspend();
	}
	public void resumeThreads() {
		//t1.resume();
		t3.resume();
	}
	public static void startMulti(Building b1[],ArrayList <MultiThreadThing> allThreads) {
		for (int j = 0;j<b1.length;j++) {
			for (int i = 0;i<b1[j].getApartmentnum();i++) {
				if (b1[j].a[i].getOwner().equals("Raspberry PI")) {
					int k = j;int l = i;
					Thread t = new Thread() {
						public void run() {
							RasPi.runRaspberryPi(b1,k,l);
						}
					};
					t.start();
					continue;
				}
				MultiThreadThing p = new MultiThreadThing(b1[j],i);
				allThreads.add(p);
			}
		}
	}
	
	public static void apartmentAdd(WholeObject obj,ArrayList <MultiThreadThing> allThreads,int BuildinIndex,Circle l) {
		MultiThreadThing t = new MultiThreadThing(obj.b1[BuildinIndex],obj.b1[BuildinIndex].getApartmentnum()-1);
		allThreads.add(t);
		MultiThreadThing n = new MultiThreadThing(l,obj.b1[BuildinIndex],obj.b1[BuildinIndex].getApartmentnum()-1);
	}
}
