public class Building extends SectorCircleProperties{

	private int BuildingNumber;
	private String  BuildingAddress;
	private int Apartmentnum;
	
	Apartment a[];
	
	//inner class
	public class Apartment{
		private String apartmentname;
		private String owner;
		private double totalelectricity;
		private double electricityrate;	
		private boolean isPaid;
		
		Apartment(){}
		Apartment(String apartmentname,String owner,double totalelectricity,boolean isPaid){
			this.apartmentname = apartmentname;
			this.owner = owner;
			this.totalelectricity = totalelectricity;
			this.isPaid = isPaid;
		}
		public void setApartmentname(String name) {
			this.apartmentname = name;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public void setTotalElectricity(double elect) {
			this.totalelectricity = elect;
		}
		public void setElectricityrate(double electricityrate) {
			this.electricityrate = electricityrate;
		}
		public void setIsPaid(boolean isPaid) {
			this.isPaid = isPaid;
		}
		
		public String getApartmentname() {
			return this.apartmentname;
		}
		public String getOwner() {
			return this.owner;
		}
		public double getTotalELectricity() {
			return this.totalelectricity;
		}
		public double getElectricityrate() {
			return this.electricityrate;
		}
		public boolean getIsPaid() {
			return this.isPaid;
		}
		
	}
	
	Building(){}
	Building(Building b){
		super(b.getSector());
		this.BuildingNumber = b.getBuildingnum();
		this.BuildingAddress = b.getBuildingAddress();
		this.Apartmentnum = b.getApartmentnum();
		this.setApartments();
		
		for (int i = 0;i<Apartmentnum;i++) {
			a[i].setApartmentname(b.a[i].getApartmentname());
			a[i].setOwner(b.a[i].getOwner());
			a[i].setTotalElectricity(b.a[i].getTotalELectricity());
			a[i].setIsPaid(b.a[i].getIsPaid());
		}
		super.setCircleX(b.getCircleX());
		super.setCircleY(b.getCircleY());
	}
	Building(int sector,int BuildingNumber, String BuildingAddress, int Apartmentnum){
		this.BuildingNumber = BuildingNumber;
		this.BuildingAddress = BuildingAddress;
		this.Apartmentnum = Apartmentnum;
		super.setSector(sector);
		
	}
	public void setBuildingnum(int num) {
		this.BuildingNumber = num;
	}
	public void setBuildingAddress(String address) {
		this.BuildingAddress = address;
	}
	public void setApartmentnum(int num) {
		this.Apartmentnum = num;
	}
	
	@Override
	public void setSector(int sector) {
		super.setSector(sector);
	}
	
	@Override
	public void setCircleX(double x) {
		super.setCircleX(x);
	}
	
	@Override
	public void setCircleY(double y) {
		super.setCircleY(y);
	}
	
	public int getBuildingnum() {
		return this.BuildingNumber;
	}
	public String getBuildingAddress() {
		return this.BuildingAddress;
	}
	public int getApartmentnum() {
		return this.Apartmentnum;
	}
	
	@Override
	public int getSector() {
		return super.getSector();
	}
	
	@Override
	public double getCircleY() {
		return super.getCircleY();
	}
	
	@Override
	public double getCircleX() {
		return super.getCircleX();
	}
	
	public void setApartments() {
		a = new Apartment[this.Apartmentnum];
		for (int i = 0;i<a.length;i++) {
			a[i] = new Apartment();
		}
	}
	public void setApartment(String temp[],int i) {
		a[i] = new Apartment();
		a[i].setOwner(temp[0]);
		a[i].setApartmentname(temp[1]);
		a[i].setTotalElectricity(Double.parseDouble(temp[2]));
		a[i].setIsPaid(Boolean.parseBoolean(temp[3]));
	}
	
	public void addApartment(String owner,String apartmentname,double totalelectricity,boolean isPaid) {
		Apartment atemp[] = new Apartment[a.length+1];
		
		for (int i = 0;i<a.length;i++) {
			atemp[i] = new Apartment(a[i].getApartmentname(),a[i].getOwner(),a[i].getTotalELectricity(),a[i].getIsPaid());
		}
		
		atemp[atemp.length-1] = new Apartment();
		atemp[atemp.length-1].setOwner(owner);
		atemp[atemp.length-1].setApartmentname(apartmentname);
		atemp[atemp.length-1].setTotalElectricity(totalelectricity);
		atemp[atemp.length-1].setIsPaid(isPaid);
		
		a = atemp;
		Apartmentnum++;
	}
	public void deleteApartment(int index) {
		Apartment temp[] = new Apartment[a.length-1];
		for (int j = index;j<a.length-1;j++) {
			a[j] = a[j+1];
		}
		for (int k = 0;k<temp.length;k++) {
			temp[k] = a[k];
		}
		a=temp;
		Apartmentnum--;
	}
}