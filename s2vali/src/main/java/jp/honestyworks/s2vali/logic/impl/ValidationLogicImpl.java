package jp.honestyworks.s2vali.logic.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResult;
import org.apache.commons.validator.ValidatorResults;

import jp.honestyworks.s2vali.ErrorMessage;
import jp.honestyworks.s2vali.ValidatorResourcesContainer;
import jp.honestyworks.s2vali.logic.ValidationLogic;
import jp.honestyworks.s2vali.util.Util;

public class ValidationLogicImpl implements ValidationLogic {
	
	private final static Log log = LogFactory.getLog(ValidationLogicImpl.class);
	
	private ValidatorResourcesContainer validatorResourcesContainer;

	public ValidatorResults execValidate(ValidatorResources rs,
			String formName, Object targetBean, Locale locale) throws ValidatorException {
		
		Validator validator = new Validator(rs, formName);
		validator.setParameter(Validator.BEAN_PARAM, targetBean);
		if (locale != null) {
			validator.setParameter(Validator.LOCALE_PARAM, locale);
		}
		validator.setOnlyReturnErrors(true);
		
		return validator.validate();		
	}

	public ErrorMessage[] makeErrorMessages(ValidatorResources resources,
			ValidatorResults results) {
		
		List returnValue = new LinkedList();
		ResourceBundle bundle = this.validatorResourcesContainer.getResourceBundle();
		
		for (Iterator it = results.getPropertyNames().iterator(); it.hasNext();) {
			String propertyName = (String) it.next();
			ValidatorResult result = results.getValidatorResult(propertyName);
			for (Iterator it2 = result.getActions(); it2.hasNext();) {
				
				String actName = (String) it2.next();
				
				// msgを取得
				String messageKey = result.getField().getMsg(actName); 
				log.debug("Fieldから取得したMsg:" + messageKey);
				if (messageKey == null) {
					messageKey = resources.getValidatorAction(actName).getMsg();
					log.debug("Actionから取得したMsg:" + messageKey);
				}
				String message = Util.getMessageBundled(messageKey, bundle);
				log.debug("ResourceBundleから取得したmessage:" + message);
				
				// 引数を取得
				List argsList = new ArrayList();
				Arg[] args = result.getField().getArgs(actName);
				if (args != null) {
					for (int i=0; i<args.length; i++) {
						if (args[i] != null) {
							log.debug("Argsのkey:" + args[i].getKey());
							if (args[i].isResource()) {
								argsList.add((String)Util.getMessageBundled(args[i].getKey(), bundle));
							} else {
								argsList.add(args[i].getKey());
							}
						}
					}
				}
				
				ErrorMessage msgObject = new ErrorMessage();
				msgObject.setMessageKey(messageKey);
				msgObject.setMessage(message);
				msgObject.setArgs(argsList.toArray());
				
				returnValue.add(msgObject);
			}
		}
		
		return returnValue.size() > 0 ? (ErrorMessage[])returnValue.toArray(new ErrorMessage[1]) : null;
	}

	
	//------------- 以下 getter/setter ----------------//
	
	public void setValidatorResourcesContainer(
			ValidatorResourcesContainer validatorResourcesContainer) {
		this.validatorResourcesContainer = validatorResourcesContainer;
	}

}
