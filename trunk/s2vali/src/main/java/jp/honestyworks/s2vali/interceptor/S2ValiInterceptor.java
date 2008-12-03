package jp.honestyworks.s2vali.interceptor;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import jp.honestyworks.s2vali.ErrorMessage;
import jp.honestyworks.s2vali.InvalidConventionRuntimeException;
import jp.honestyworks.s2vali.ValidatorResourcesContainer;
import jp.honestyworks.s2vali.logic.ValidationLogic;
import jp.honestyworks.s2vali.util.Util;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResults;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * インターフェース定義に従ってcommons-validatorを実行するインターセプターです。
 * TODO indexedListPropertyのテスト
 * TODO 戻り値を便利にする
 * (TODO アノテーション対応)
 * @author miztaka
 *
 */
public class S2ValiInterceptor extends AbstractInterceptor {
	
	private static final Log log = LogFactory.getLog(S2ValiInterceptor.class);

	protected static final long serialVersionUID = 1019696917818295869L;
	
	protected static final String METHOD_PREFIX = "validate";
	
	protected ValidatorResourcesContainer validatorResourcesContainer;
	
	protected ValidationLogic validationLogic;
	

	/**
	 * インターセプターを実行します。
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
        Method method = invocation.getMethod();
        if (! MethodUtil.isAbstract(method)) {
        	log.debug("Abstractではないためスルーします。");
            return invocation.proceed();
        }		
		
		// メソッド名: validate + "フォーム名称" という想定で、
		// 対象となる設定ファイルを取得する。
		if (! method.getName().startsWith(METHOD_PREFIX)) {
			log.debug("対象となるメソッド名ではないためスルーします。");
			return invocation.proceed();
		}
		String formName = getFormName(invocation);
		
		// 検証するBeanを取り出す。
		Object targetBean = getBean(invocation);
		if (targetBean == null) {
			throw new InvalidConventionRuntimeException("検証対象のBeanが見つかりません。");
		}
		
		// ValidatorResourcesを取得する。
		ValidatorResources rs = validatorResourcesContainer.getValidatorResources();
		if (rs == null) {
			throw new InvalidConventionRuntimeException("検証定義ファイルがセットされていません。");
		}
		
		// 検証ロジック実行
		Locale locale = null;
		if (validatorResourcesContainer.getResourceBundle() != null) {
			locale = validatorResourcesContainer.getResourceBundle().getLocale();
		}
		ValidatorResults results = validationLogic.execValidate(rs, formName, targetBean, locale);
		
		// 戻り値作成
		return getReturnValue(invocation, results);
	}
	
	/**
	 * 検証結果を返します。
	 * @param invocation
	 * @param results
	 * @return
	 */
    protected Object getReturnValue(MethodInvocation invocation,
			ValidatorResults results) {
    	
    	if (results == null) {
			return null;
		}
    	
    	Class returnType = invocation.getMethod().getReturnType();
    	if (returnType == ValidatorResults.class) {
    		return results;
    	}
    	if (returnType == String[].class) {
    		return getStringArrayReturnValue(results);
    	}
    	if (returnType == ErrorMessage[].class) {
    		return validationLogic.makeErrorMessages(validatorResourcesContainer.getValidatorResources(), results);
    	}

    	throw new InvalidConventionRuntimeException("戻り値の型が不正です。");
	}
    
    /**
     * エラーメッセージを配列で返します。
     * 
     * @param results
     * @return
     */
	protected String[] getStringArrayReturnValue(ValidatorResults results) {
		
		ValidatorResources resources = 
			validatorResourcesContainer.getValidatorResources();
		ResourceBundle rb = validatorResourcesContainer.getResourceBundle();
		
		ErrorMessage errorMessages[] = validationLogic.makeErrorMessages(resources, results);
		if (errorMessages == null) {
			return null;
		}
		
		String[] resultValue = new String[errorMessages.length];
		for(int i=0; i<errorMessages.length; i++) {
			resultValue[i] = Util.formatMessage(errorMessages[i].getMessage(), errorMessages[i].getArgs(), rb.getLocale());
		}
		
		return resultValue;
	}

	/**
	 * 検証対象のBeanを取得します。
	 * @param invocation
	 * @return
	 */
	protected Object getBean(MethodInvocation invocation) {
        Object[] arguments = invocation.getArguments();
        if (arguments == null || arguments.length == 0) {
            return null;
        }
        return arguments[0];
    }	

	/**
	 * 検証対象となるformNameを取得します。
	 * @param invocation
	 * @return
	 */
	protected String getFormName(MethodInvocation invocation) {
		
		String methodName = invocation.getMethod().getName();
		if (! methodName.startsWith(METHOD_PREFIX)) {
			throw new InvalidConventionRuntimeException("メソッド名がvalidateで始まっていません。");
		}
		
		// suffixを外す
		String[] buff = methodName.split("_");
		
		return StringUtil.decapitalize(buff[0].substring(8));
	}

	//------------------- 以下 setter/getter -----------------------//
	
	public void setValidatorResourcesContainer(
			ValidatorResourcesContainer validatorResourcesContainer) {
		this.validatorResourcesContainer = validatorResourcesContainer;
	}

	public void setValidationLogic(ValidationLogic validationLogic) {
		this.validationLogic = validationLogic;
	}

}
