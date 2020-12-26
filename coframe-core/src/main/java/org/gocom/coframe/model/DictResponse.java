package org.gocom.coframe.model;

public class DictResponse {

	private int typeCount;
	private int entryCount;

	public DictResponse(int typeCount, int entryCount) {
		super();
		this.typeCount = typeCount;
		this.entryCount = entryCount;
	}
	
	public DictResponse() {
		
	}

	/**
	 * @return the typeCount
	 */
	public int getTypeCount() {
		return typeCount;
	}

	/**
	 * @param typeCount
	 *            the typeCount to set
	 */
	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}

	/**
	 * @return the entryCount
	 */
	public int getEntryCount() {
		return entryCount;
	}

	/**
	 * @param entryCount
	 *            the entryCount to set
	 */
	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

}
