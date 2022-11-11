package com.yaps.utils.model;
import java.lang.reflect.Field;
import org.apache.commons.lang3.ArrayUtils;

import com.yaps.petstore.domain.annotation.propertyMetaData;

/**
 * Every domain object should extend this abstract class.
 */
public abstract class DomainObject {


	/**
	 * The unique identifier of the object.
	 */
	@propertyMetaData(order = 1, columnName = "id")
    protected String id;

	
	protected DomainObject() {
	}
	
	protected DomainObject(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns a short (one-line) rendering of this object.
	 * @return
	 */
	abstract public String shortDisplay();

	public void checkData() throws CheckException {
		Field[] declaredFields = this.getClass().getDeclaredFields();
        Field[] declaredParentFields = this.getClass().getSuperclass().getDeclaredFields();
		Field[] declaredAllFields = ArrayUtils.addAll(declaredParentFields, declaredFields);
		try {
            for (Field field : declaredAllFields) {
				field.setAccessible(true);
				Object fieldObj= field.get(this);
				propertyMetaData ann = field.getAnnotation(propertyMetaData.class);
				if (fieldObj instanceof DomainObject) {
					if (!checkProperty(((DomainObject) fieldObj).getId())) {
						throw new CheckException("Invalid "+ann.columnName().split("_")[0]);
					}
                }
                else {
                    if (!checkProperty(fieldObj)) {
						throw new CheckException("Invalid "+ann.columnName());
					}
                }
            } 
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
	}
	
	protected boolean checkProperty(Object obj) {
		if (obj instanceof String){
			String s = (String) obj;
			return ((s != null) && (!"".equals(s)));
		}
		else if (obj instanceof Double) {
			Double d = (Double) obj;
			return (d >= 0);
		}
		return false;
	}
}
