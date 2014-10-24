package main.java.beans;

public abstract class AbstractBean {
	protected boolean modified;
	protected boolean exists=false;
	
	public boolean save() {
		boolean success = true;
		if(!exists()){ 
			success = insert();
		}
		else if(modified)
			success = update();
		return success;
	}
	
	protected abstract boolean update();
	protected abstract boolean insert();
	protected abstract boolean exists();
	
}
