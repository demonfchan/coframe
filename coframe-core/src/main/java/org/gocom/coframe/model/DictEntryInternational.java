package org.gocom.coframe.model;

import static org.gocom.coframe.CoframeConstants.TABLE_CORE_PREFIX;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.gocom.coframe.core.model.AbstractFixedPersistentModel;
import org.gocom.coframe.core.model.AbstractSimpleModelCriteria;

/**
 * 字典国际化
 * @author Administrator
 *
 */

@Entity
@Table(name = TABLE_CORE_PREFIX + "DICT_ENTRY_I18N")
public class DictEntryInternational extends AbstractFixedPersistentModel{
	private static final long serialVersionUID = 1L;
	private String DictEntryName;
	private String locale;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_entry_id")
	private DictEntry dictEntry;
	
	
	/**
	 * @return the dictEntryName
	 */
	public String getDictEntryName() {
		return DictEntryName;
	}
	/**
	 * @param dictEntryName the dictEntryName to set
	 */
	public void setDictEntryName(String dictEntryName) {
		DictEntryName = dictEntryName;
	}
	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	/**
	 * @return the dictEntry
	 */
	public DictEntry getDictEntry() {
		return dictEntry;
	}
	/**
	 * @param dictEntry the dictEntry to set
	 */
	public void setDictEntry(DictEntry dictEntry) {
		this.dictEntry = dictEntry;
	}
	
	public static class Criteria  extends AbstractSimpleModelCriteria {
		private String locale;
		/**
		 * @return the locale
		 */
		public String getLocale() {
			return locale;
		}
		/**
		 * @param locale the locale to set
		 */
		public void setLocale(String locale) {
			this.locale = locale;
		}
	} 
}
