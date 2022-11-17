package com.yaps.common.model;

/**
 * Every domain object should extend this abstract class.
 */
public abstract class DomainObject {


	/**
	 * The unique identifier of the object.
	 */    
    protected String id = "";

	
	protected DomainObject() {
	}
	
	protected DomainObject(String id) {
		if (id == null)
			throw new NullPointerException();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id == null)
			throw new NullPointerException();
		this.id = id;
	}

	/**
	 * Returns a short (one-line) rendering of this object.
	 * @return
	 */
	abstract public String shortDisplay();

	/**
	 * Check the correctness of the data in this domain object.
	 * By defaults, checks that id is not null or empty.
	 * @throws CheckException
	 */
	public void checkData() throws CheckException{
		if (id.isEmpty()) {
			throw new CheckException("Id should not be null or empty");
		}
	}

}
