import javafx.application.Platform;
import javafx.scene.text.Text;



public class UpdateText extends Thread{
	private Text l1,l2,total;
	private Building b;
	private int apartindex;
	UpdateText(Text l1,Text l2,Building b,int apartindex,Text total){
		this.l1 = l1;
		this.l2 = l2;
		this.total = total;
		this.b = b;
		this.apartindex = apartindex;
	}
	
	@Override
	public void run() {
		while (true) {
				Platform.runLater(()->
				{
					try {
						l1.setText(String.format("%.4f",b.a[apartindex].getElectricityrate())+" kW");
					}
					catch(Exception e) {
						
						this.stop();
						return;
					}
					
					l2.setText(String.format("%.4f",b.a[apartindex].getTotalELectricity())+" kWh");
					double d = Double.parseDouble(total.getText())+b.a[apartindex].getElectricityrate();
					total.setText(String.format("%.4f",d));
					
				});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				this.stop();
				e.printStackTrace();
				
			}
		}
	}
}