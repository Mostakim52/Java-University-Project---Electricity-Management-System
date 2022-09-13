
import java.util.ArrayList;

public class UserandBuildingControls {
	
	//for checking if user is available in the records
	static boolean userCheck(WholeObject obj,String username,String password) {
		for (int i = 0;i<obj.u1.length;i++) {
			//checks if username is available in User u1[] array
			if (obj.u1[i].getUsername().equals(username)) {
				//if username is found then it checks for password validation
				if (obj.u1[i].getPassword().equals(password)) {
					// if password is valid then checks if username is Admin
					if (username.contains("Admin")) return true;
					for (int j = 0;j<obj.b1.length;j++) {
						//checks for the matching apartment in Building b1[] Array
						for (int k = 0;k<obj.b1[j].a.length;k++) {
							if (obj.b1[j].a[k].getOwner().equals(username)) {
								return true;
							}
						}
					}

					return false;
				}
			}
		}
		return false;
	}
	
	//for calculating average kWh of a full Building
	public static double averagekWh(WholeObject obj,int BuildinIndex) {
		double totalkWh = 0;
		for (int i = 0;i<obj.b1[BuildinIndex].getApartmentnum();i++) {
			totalkWh += obj.b1[BuildinIndex].a[i].getTotalELectricity();
		}
		return totalkWh/obj.b1[BuildinIndex].getApartmentnum();
	}
	
	//for removing a user
	public static void removeUser(WholeObject obj,String name) {
		User temp[] = new User[obj.u1.length-1];
		int i = 0;
		for (i = 0;i<obj.u1.length;i++) {
			if (obj.u1[i].getUsername().equals(name)) {
				for (int j = i;j<obj.u1.length-1;j++) {
					obj.u1[j] = obj.u1[j+1];
				}
				break;
			}
		}
		for (i = 0;i<temp.length;i++) {
			temp[i] = obj.u1[i];
		}
		obj.u1=temp;
	}
	
	// for adding a whole building
	public static void addBuilding(WholeObject obj,int Buildingnum, String address,int Apartmentnum,int sector) {
		Building temp[] = new Building[obj.b1.length+1];
		for (int i = 0;i<obj.b1.length;i++) {
			temp[i] = new Building(obj.b1[i]);
		}
		temp[temp.length-1] = new Building();
		temp[temp.length-1].setBuildingnum(Buildingnum);
		temp[temp.length-1].setBuildingAddress(address);
		temp[temp.length-1].setApartmentnum(Apartmentnum);
		temp[temp.length-1].setSector(sector);
		temp[temp.length-1].setApartments();
		
		obj.b1 = temp;
		
	}
	
	//for deleting a building
	public static void deleteBuilding(WholeObject obj,int del) {
		Building temp[] = new Building[obj.b1.length-1];
		for (int i = 0, k = 0; i<obj.b1.length; i++) { 
            if (i == del) { 
                continue; 
            } 
            temp[k++] = obj.b1[i]; 
        } 
		obj.b1 = temp;
	}
	
	//for adding a user
	public static void addUser(WholeObject obj,String username, String password,String email) {
		
		for(int i = 0;i<obj.u1.length;i++) {
			if (obj.u1[i].getUsername().equals(username)) {
				return;
			}
		}
		
		User temp[] = new User[obj.u1.length+1];
		for (int i = 0;i<obj.u1.length;i++) {
			temp[i] = new User(obj.u1[i]);
		}
		temp[temp.length-1] = new User();
		temp[temp.length-1].setUsername(username);
		temp[temp.length-1].setPassword(password);
		temp[temp.length-1].setEmail(email);
		
		obj.u1=temp;
		obj.f.writeUserFile(obj.u1);
	}
	
//Added by Mostakim Hossain
	//for initializing all Threads at start of the program
	static void initialize(WholeObject obj) {
//		obj.f = new ElectricityFile("C:/Users/guyaw/Desktop/CSE215Project/");
		obj.f = new ElectricityFile("");
		
		//initializing User u1[] Array
		obj.u1 = obj.f.readuserfile();
		//initializing Building b1[] Array
		obj.b1 = obj.f.readBuildingfile();
		

	}
	
	
//Added by Mostakim Hossain
	//for turning a full sector on or off
	static void controlSector(WholeObject obj,ArrayList<ArrayList <MultiThreadThing>> allThreads,int sector,boolean onoff) {
		
		for (int i = 0;i<allThreads.size();i++) {
			for (int j = 0;j<allThreads.get(i).size();j++){
				if (obj.b1[i].a[j].getOwner().equals("Raspberry PI")) continue;
					if (allThreads.get(i).get(j).threadofsector==sector) {
						if (onoff) {
							//resumes the Threads that were paused
							MultiThreadThing m = allThreads.get(i).get(j);
							m.resumeThreads();
						}
						else {
							//suspends the Threads that are generating rates
							MultiThreadThing m = allThreads.get(i).get(j);
							m.suspendThreads();
						}
					}
			}
			
		}
		
		for (int i = 0;i<obj.b1.length;i++) {
			if (obj.b1[i].getSector()==sector) {
				for (int j = 0;j<obj.b1[i].a.length;j++) {
					
					//only for Raspberry PI ↓↓↓↓↓
					if (obj.b1[i].a[j].getOwner().equals("Raspberry PI")) {
					    if (RasPi.isConnected) RasPi.controldownPi(onoff);
						continue;
					}
					//only for Raspberry PI ↑↑↑↑↑
					
					if (!onoff) obj.b1[i].a[j].setElectricityrate(0);
				}
			}
		}
	}
}
