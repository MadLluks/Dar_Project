package com.dar.beans;

public abstract class AbstractBean {
	protected boolean modified;
	protected boolean verified=false;
	
	public boolean save() {
		boolean success = true;
		if(!verified){ 
			success = insert();
		}
		else if(modified)
			success = update();
		return success;
	}
	
	protected abstract boolean update();
	protected abstract boolean insert();
	
}
