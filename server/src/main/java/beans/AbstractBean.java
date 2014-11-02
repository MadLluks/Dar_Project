package main.java.beans;

public abstract class AbstractBean {
    protected boolean modified;
    protected boolean exists=false;
	
    public boolean save() {
	boolean success = true;
	if(!exists()){ 
	    System.out.println("ABSTRACT : ABOUT TO INSERT");
	    success = insert();
	}
	else if(modified){
	    System.out.println("ABSTRACT : ABOUT TO UPDATE");
	    success = update();
	}
	return success;
    }
	
    protected abstract boolean update();
    protected abstract boolean insert();
    protected abstract boolean exists();
	
}
