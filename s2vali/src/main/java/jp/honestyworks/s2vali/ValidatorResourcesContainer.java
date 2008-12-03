package jp.honestyworks.s2vali;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorResources;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.URLUtil;
import org.xml.sax.SAXException;

public class ValidatorResourcesContainer {
	
	private final static Log log = LogFactory.getLog(ValidatorResourcesContainer.class);
	
	private ValidatorResources validatorResources;
	private ResourceBundle resourceBundle;
	
	// components
	private ValidatorConfig validatorConfig;
	
	/**
	 * コンポーネントを初期化します。
	 */
	public void init() {
		
		if (validatorConfig == null) {
			throw new InvalidConventionRuntimeException("ValidatorConfigが見つかりません。");
		}
		
		// ValidatorResourcesを作成する
		loadValidatorResources();
		
		// ResourceBundleを作成する
		loadResourceBundle();
		
		return;
	}

	/**
	 * ResourceBundleを作成します。
	 * TODO 多言語対応
	 */
	private void loadResourceBundle() {
		
		resourceBundle = ResourceBundle.getBundle(
				validatorConfig.getResourceBundleProperties(), Locale.JAPANESE);
		
		return;
	}

	/**
	 * ValidatorResourcesを作成します。
	 */
	private void loadValidatorResources() {
		log.debug("ValidatorResourcesを作成します。");

		
		String[] rules = validatorConfig.getValidationRules().split(",");
		InputStream[] ins = new InputStream[rules.length];
		for (int i=0; i<rules.length; i++) {
			log.debug("定義ファイル " + rules[i] + " を読込みます。");
			final URL url = ResourceUtil.getResourceNoException(rules[i].trim());
			if (url == null) {
				throw new InvalidConventionRuntimeException("定義ファイルが見つかりません：" + rules[i]);
			}
			ins[i] = URLUtil.openStream(url);
			if (ins[i] == null) {
				throw new InvalidConventionRuntimeException("定義ファイルをオープンできません：" + rules[i]);
			}
		}
		
		try {
			validatorResources = new ValidatorResources(ins);
		} catch (IOException e) {
			log.error("検証定義ファイルの読み込みに失敗しました。", e);
			throw new InvalidConventionRuntimeException("検証定義ファイルの読み込みに失敗しました。", e);
		} catch (SAXException e) {
			log.error("検証定義ファイルの読み込みに失敗しました。", e);
			throw new InvalidConventionRuntimeException("検証定義ファイルの読み込みに失敗しました。", e);
		}
		
		log.debug("ValidatorResourcesを作成しました。");
		
		return;
	}

	//----------------- 以下 getter/setter -------------------------//
	
	public ValidatorResources getValidatorResources() {
		return validatorResources;
	}

	public void setValidatorResources(ValidatorResources validatorResources) {
		this.validatorResources = validatorResources;
	}

	public void setValidatorConfig(ValidatorConfig validatorConfig) {
		this.validatorConfig = validatorConfig;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

}
