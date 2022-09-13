
public abstract class SectorCircleProperties {
	private int sector;
	private double circleX;
	private double circleY;
	
	SectorCircleProperties(){}
	SectorCircleProperties(int sector){
		this.sector = sector;
	}
	
	public void setSector(int sector) {
		this.sector = sector;
	}
	public void setCircleX(double x) {
		this.circleX = x;
	}
	public void setCircleY(double y) {
		this.circleY = y;
	}
	
	public int getSector() {
		return this.sector;
	}
	
	public double getCircleY() {
		return this.circleY;
	}
	public double getCircleX() {
		return this.circleX;
	}
}
