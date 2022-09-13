
public abstract class ApplicationFile {
	private String path;
	
	ApplicationFile(String path){
		this.path = path;
	}
	
	public abstract void OpenFile();
	
	public void setpath(String path) {
		this.path = path;
	}
	public String getpath() {
		return this.path;
	}
}
