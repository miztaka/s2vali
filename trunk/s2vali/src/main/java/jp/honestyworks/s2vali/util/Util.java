package jp.honestyworks.s2vali.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Util {

	public static String formatMessage(String message, Object[] args, Locale locale) {
		
		if (args == null || message == null) {
			return message;
		}
		
		return new MessageFormat(message, locale).format(args);
	}

	public static String getMessageBundled(String messageKey, ResourceBundle resourceBundle) {
		
		if (messageKey == null) {
			return null;
		}
		
		try {
			if (resourceBundle.getString(messageKey) != null) {
				return resourceBundle.getString(messageKey);
			}
		} catch (RuntimeException e) {
			// resource not found, so pass it.
		}
		
		return messageKey;
	}

}
