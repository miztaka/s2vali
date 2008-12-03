package jp.honestyworks.s2vali.logic;

import java.util.Locale;

import jp.honestyworks.s2vali.ErrorMessage;

import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResults;

public interface ValidationLogic {

	/**
	 * 検証を実行します。
	 * @param rs
	 * @param formName
	 * @param targetBean
	 * @param locale 
	 * @return
	 * @throws ValidatorException 
	 */	
	ValidatorResults execValidate(ValidatorResources rs, String formName,
			Object targetBean, Locale locale) throws ValidatorException;

	/**
	 * 検証結果からErrorMessagesオブジェクトを作成します。
	 * @param resources
	 * @param results
	 */
	ErrorMessage[] makeErrorMessages(ValidatorResources resources,
			ValidatorResults results);

}
