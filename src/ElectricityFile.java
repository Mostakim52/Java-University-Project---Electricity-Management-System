
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class ElectricityFile extends ApplicationFile{
	
	//constructor
	ElectricityFile(String path){
		super(path);
	}
	
	public void OpenFile() {
		//these check if files are available. If not, they are created.
		File file1 = new File(super.getpath() + "ElectricityFile.txt");
		try {
			file1.createNewFile();
			System.out.println("Files are created.");
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		File file2 = new File(super.getpath() + "ElectricityUsers.txt");
		try {
			file2.createNewFile();
			System.out.println("Files are created.");
		}
		catch(Exception e) {
			System.out.println(e);
		}File file3 = new File(super.getpath() + "Users.txt");
		try {file3.createNewFile();
		System.out.println("Files are created.");
		}catch(Exception e) { 
			System.out.print(e);
		}
	}
	
	
			
			
	//reads from ElectricityUsers.txt file and add them to User u1 array
	public User[] readuserfile() {
		
		Scanner scanner = null;
		
		try {
			File file = new File(super.getpath()+ "ElectricityUsers.txt");
			scanner = new Scanner(file);
			
		}catch(Exception e) {
			System.out.println(e);
		}
		
		int totalusers = scanner.nextInt();
		User users[] = new User[totalusers];
		
		int i = -1;
		while(scanner.hasNext()) {
			scanner.nextLine();
			i++;
			users[i] = new User();
			users[i].setUsername(scanner.next().replace("-", " "));
			scanner.nextLine();
			users[i].setPassword(scanner.next());
			scanner.nextLine();
			users[i].setEmail(scanner.next());
			scanner.nextLine();
			}
			
		scanner.close();
		
		return users;
	}
	
	//writes all users in User u1[] array to text file
	public void writeUserFile(User user[]) {
		
		Formatter f = null;
		try {
			f = new Formatter(super.getpath() + "ElectricityUsers.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		f.format("%s\n",user.length);
		
		for (int i = 0;i<user.length;i++) {
			f.format("\n");
			f.format("%s\n", user[i].getUsername().replace(" ", "-"));
			f.format("%s\n", user[i].getPassword());
			f.format("%s\n", user[i].getEmail());
		}
		f.close();
		
	}
	
	//reads from ElectricityFile.txt file and add them to Building b1 array
	public Building[] readBuildingfile(){

		Scanner scanner = null;
				
		try {
			File file = new File(super.getpath()+ "ElectricityFile.txt");
			scanner = new Scanner(file);
			
		}catch(Exception e) {
			System.out.println(e);
		}
		
		int totalbuildings = scanner.nextInt();
		Building buildings[] = new Building[totalbuildings];
		
		int i = -1;
		while(scanner.hasNext()) {
			scanner.nextLine();
			i++;
			buildings[i] = new Building();
			buildings[i].setSector(scanner.nextInt());
			buildings[i].setBuildingnum(scanner.nextInt());
			buildings[i].setBuildingAddress(scanner.next());
			scanner.nextLine();
			buildings[i].setApartmentnum(scanner.nextInt());
			buildings[i].setApartments();
			for (int k = 0;k<buildings[i].getApartmentnum();k++) {
				String temp = scanner.next();
				String temp2[] = temp.split("/");
				temp2[0] = temp2[0].replace("-", " ");
				temp2[1] = temp2[1].replace("-", " ");
				buildings[i].setApartment(temp2,k);
				scanner.nextLine();
			}
			buildings[i].setCircleX(scanner.nextDouble());
			buildings[i].setCircleY(scanner.nextDouble());
		}
		scanner.close();
		
		return buildings;
		
	}
	
	//writes all users in Building b1[] array to text file
	public void writeBuildingFile(Building bldg[]) {

		Formatter f = null;
		try {
			f = new Formatter(super.getpath() + "ElectricityFile.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		f.format("%s\n",bldg.length);
			
		for (int i = 0;i<bldg.length;i++) {
			f.format("\n");
			f.format("%s\n", bldg[i].getSector());
			f.format("%s\n", bldg[i].getBuildingnum());
			f.format("%s\n", bldg[i].getBuildingAddress());
			f.format("%s\n", bldg[i].getApartmentnum());
			for (int j = 0;j<bldg[i].getApartmentnum();j++) {
				f.format("%s/%s/%s/%s\n",bldg[i].a[j].getOwner().replace(" ", "-"), bldg[i].a[j].getApartmentname().replace(" ", "-"), bldg[i].a[j].getTotalELectricity(),bldg[i].a[j].getIsPaid());
			}
			f.format("%s %s\n", bldg[i].getCircleX(),bldg[i].getCircleY());
		}
		f.close();
	}
}

