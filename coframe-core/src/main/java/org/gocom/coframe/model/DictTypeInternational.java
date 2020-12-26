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
@Table(name = TABLE_CORE_PREFIX + "DICT_TYPE_I18N")
public class DictTypeInternational extends AbstractFixedPersistentModel{
	private static final long serialVersionUID = 1L;
	private String DictTypeName;
	private String locale;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_type_id")
	private DictType dictType;
	
	
	/**
	 * @return the dictTypeName
	 */
	public String getDictTypeName() {
		return DictTypeName;
	}

	/**
	 * @param dictTypeName the dictTypeName to set
	 */
	public void setDictTypeName(String dictTypeName) {
		DictTypeName = dictTypeName;
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
	 * @return the dictType
	 */
	
	public DictType getDictType() {
		return dictType;
	}
	
	/**
	 * @param dictType the dictType to set
	 */
	public void setDictType(DictType dictType) {
		this.dictType = dictType;
	}
	
	public static class Criteria extends AbstractSimpleModelCriteria {
		private  String locale;
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
