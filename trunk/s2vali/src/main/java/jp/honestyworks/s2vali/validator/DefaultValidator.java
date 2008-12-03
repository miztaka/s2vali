package jp.honestyworks.s2vali.validator;

import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.UrlValidator;
import org.apache.commons.validator.util.ValidatorUtils;

/**
 * commons-validatorと連携するValidatorです。
 * @author miztaka
 *
 */
public class DefaultValidator {
	
	private final static Log log = LogFactory.getLog(DefaultValidator.class);
	
    public static final String FIELD_TEST_NULL = "NULL";
    public static final String FIELD_TEST_NOTNULL = "NOTNULL";
    public static final String FIELD_TEST_EQUAL = "EQUAL";
    
	/**
	 * 必須項目をチェックします。
	 * @param bean
	 * @param field
	 * @return
	 */
	public static boolean validateRequired(Object bean, Field field) {
		
		String value = getValueAsString(bean, field);
	    return ! GenericValidator.isBlankOrNull(value);
	}
	
	/**
	 * 特定条件下で必須をチェックします。
	 * @param bean
	 * @param field
	 * @return
	 */
    public static boolean validateRequiredIf(Object bean, Field field) {

    	/* TODO これが必要か謎
		Object form = validator
				.getParameterValue(org.apache.commons.validator.Validator.BEAN_PARAM);
				*/
    	
    	String value = getValueAsString(bean, field);
		boolean required = false;

		int i = 0;
		String fieldJoin = "AND";
		if (!GenericValidator.isBlankOrNull(field.getVarValue("fieldJoin"))) {
			fieldJoin = field.getVarValue("fieldJoin");
		}

		if (fieldJoin.equalsIgnoreCase("AND")) {
			required = true;
		}

		while (!GenericValidator.isBlankOrNull(field.getVarValue("field[" + i
				+ "]"))) {
			String dependProp = field.getVarValue("field[" + i + "]");
			String dependTest = field.getVarValue("fieldTest[" + i + "]");
			String dependTestValue = field.getVarValue("fieldValue[" + i + "]");
			String dependIndexed = field.getVarValue("fieldIndexed[" + i + "]");

			if (dependIndexed == null) {
				dependIndexed = "false";
			}

			String dependVal = null;
			boolean thisRequired = false;
			if (field.isIndexed() && dependIndexed.equalsIgnoreCase("true")) {
				String key = field.getKey();
				if ((key.indexOf("[") > -1) && (key.indexOf("]") > -1)) {
					String ind = key.substring(0, key.indexOf(".") + 1);
					dependProp = ind + dependProp;
				}
			}

			//dependVal = ValidatorUtils.getValueAsString(form, dependProp);
			dependVal = ValidatorUtils.getValueAsString(bean, dependProp);
			if (dependTest.equals(FIELD_TEST_NULL)) {
				if ((dependVal != null) && (dependVal.length() > 0)) {
					thisRequired = false;
				} else {
					thisRequired = true;
				}
			}

			if (dependTest.equals(FIELD_TEST_NOTNULL)) {
				if ((dependVal != null) && (dependVal.length() > 0)) {
					thisRequired = true;
				} else {
					thisRequired = false;
				}
			}

			if (dependTest.equals(FIELD_TEST_EQUAL)) {
				thisRequired = dependTestValue.equalsIgnoreCase(dependVal);
			}

			if (fieldJoin.equalsIgnoreCase("AND")) {
				required = required && thisRequired;
			} else {
				required = required || thisRequired;
			}

			i++;
		}
		
		return required && GenericValidator.isBlankOrNull(value) ? 
				false : true;
	}

	/**
	 * Regexでの検証を行います。
	 * @param bean
	 * @param field
	 * @return
	 */
	public static boolean validateMask(Object bean, Field field) {
		
	    String value = getValueAsString(bean, field);
	    if (StringUtils.isEmpty(value)) {
	    	return true;
	    }
	    
	    String regex = field.getVarValue("mask");
	    if (regex != null) {
	    	return value.matches(regex);
	    }
	    
	    return true;
	}

	/**
	 * lengthのチェックを行います。(min,max)
	 * @param bean
	 * @param field
	 * @return
	 */
	public static boolean validateLength(Object bean, Field field) {
		
	    String value = getValueAsString(bean, field);
	    if (StringUtils.isEmpty(value)) {
	    	return true;
	    }
	    
	    String max = field.getVarValue("maxlength");
	    String min = field.getVarValue("minlength");
	    
	    if (max != null) {
	    	if (value.length() > Integer.parseInt(max)) {
	    		return false;
	    	}
	    }
	    
	    if (min != null) {
	    	if (value.length() < Integer.parseInt(min)) {
	    		return false;
	    	}
	    }
	    
	    return true;
	}
	
	/**
	 * 文字列の最小長をチェックします。
	 * @param bean
	 * @param field
	 * @return
	 */
    public static boolean validateMinLength(Object bean, 	Field field) {

		String value = getValueAsString(bean, field);
		
		if (! GenericValidator.isBlankOrNull(value)) {
			try {
				int min = Integer.parseInt(field.getVarValue("minlength"));

				if (!GenericValidator.minLength(value, min)) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}
    
    /**
     * 文字列の最大長をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateMaxLength(Object bean, 	Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
		    try {
		        int max = Integer.parseInt(field.getVarValue("maxlength"));

		        if (!GenericValidator.maxLength(value, max)) {
		            return false;
		        }
		    } catch (Exception e) {
		        return false;
		    }    
		}

		return true;
	}
	
    /**
     * byteに変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateByte(Object bean, Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
			if (GenericTypeValidator.formatByte(value) == null) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * shortに変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateShort(Object bean, Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
			if (GenericTypeValidator.formatShort(value) == null) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * intに変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateInteger(Object bean, Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
			if (GenericTypeValidator.formatInt(value) == null) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * longに変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateLong(Object bean, Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
			if (GenericTypeValidator.formatLong(value) == null) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * floatに変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateFloat(Object bean, Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
			if (GenericTypeValidator.formatFloat(value) == null) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * doubleに変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateDouble(Object bean, Field field) {

    	String value = getValueAsString(bean, field);
		if (! GenericValidator.isBlankOrNull(value)) {
			if (GenericTypeValidator.formatDouble(value) == null) {
				return false;
			}
		}
		return true;
	}
    
    /**
     * 日付に変換できるかどうかをチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateDate(Object bean, Locale locale, Field field) {

    	String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}
		
	    String datePattern = field.getVarValue("datePattern");
	    String datePatternStrict = field.getVarValue("datePatternStrict");
	    
	    Date result = null;
	    try {
	        if (datePattern != null && datePattern.length() > 0) {
	            result = GenericTypeValidator.formatDate(value, datePattern, false);
	        } else if (datePatternStrict != null && datePatternStrict.length() > 0) {
	            result = GenericTypeValidator.formatDate(value, datePatternStrict, true);
	        } else {
	            result = GenericTypeValidator.formatDate(value, locale);
	        }
	        
	        return result != null ? true : false;
	        
	    } catch (Exception e) {
	    }
	    return false;
    }
    
    /**
     * integerの範囲をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateIntRange(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		try {
			int intValue = Integer.parseInt(value);
			int min = Integer.parseInt(field.getVarValue("min"));
			int max = Integer.parseInt(field.getVarValue("max"));

			if (!GenericValidator.isInRange(intValue, min, max)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
    
    /**
     * floatの範囲をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateFloatRange(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		try {
		    float floatValue = Float.parseFloat(value);
		    float min = Float.parseFloat(field.getVarValue("min"));
		    float max = Float.parseFloat(field.getVarValue("max"));    

			if (!GenericValidator.isInRange(floatValue, min, max)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
    
    /**
     * doubleの範囲をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateDoubleRange(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		try {
		    double doubleValue = Double.parseDouble(value);
		    double min = Double.parseDouble(field.getVarValue("min"));
		    double max = Double.parseDouble(field.getVarValue("max"));
		    
			if (!GenericValidator.isInRange(doubleValue, min, max)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
    
    /**
     * 正しいemail形式かどうかチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateEmail(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}
		
		return GenericValidator.isEmail(value) ? true : false;
    }
    
    /**
     * 正しいurl形式かどうかチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateUrl(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		// Get the options and schemes Vars
		boolean allowallschemes = "true".equalsIgnoreCase(field
				.getVarValue("allowallschemes"));
		int options = allowallschemes ? UrlValidator.ALLOW_ALL_SCHEMES : 0;

		if ("true".equalsIgnoreCase(field.getVarValue("allow2slashes"))) {
			options += UrlValidator.ALLOW_2_SLASHES;
		}

		if ("true".equalsIgnoreCase(field.getVarValue("nofragments"))) {
			options += UrlValidator.NO_FRAGMENTS;
		}

		String schemesVar = allowallschemes ? null : field
				.getVarValue("schemes");

		// No options or schemes - use GenericValidator as default
		if (options == 0 && schemesVar == null) {
			if (GenericValidator.isUrl(value)) {
				return true;
			} else {
				return false;
			}
		}

		// Parse comma delimited list of schemes into a String[]
		String[] schemes = null;
		if (schemesVar != null) {
			StringTokenizer st = new StringTokenizer(schemesVar, ",");
			schemes = new String[st.countTokens()];

			int i = 0;
			while (st.hasMoreTokens()) {
				schemes[i++] = st.nextToken().trim();
			}
		}

		// Create UrlValidator and validate with options/schemes
		UrlValidator urlValidator = new UrlValidator(schemes, options);
		if (urlValidator.isValid(value)) {
			return true;
		} else {
			return false;
		}
	}
    
    /**
     * バイト長の最小値をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateMinByteLength(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		try {
			int min = Integer.parseInt(field.getVarValue("minbytelength"));
			String charset = field.getVarValue("charset");
			if (!S2GenericValidator.minByteLength(value, min, charset)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
    
    /**
     * バイト長の最大値をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateMaxByteLength(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		try {
			int max = Integer.parseInt(field.getVarValue("maxbytelength"));
			String charset = field.getVarValue("charset");
			if (!S2GenericValidator.maxByteLength(value, max, charset)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
    
    /**
     * longの範囲をチェックします。
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateLongRange(Object bean, Field field) {

		String value = getValueAsString(bean, field);
		if (GenericValidator.isBlankOrNull(value)) {
			return true;
		}

		try {
			long longValue = Long.parseLong(value);
			long min = Long.parseLong(field.getVarValue("min"));
			long max = Long.parseLong(field.getVarValue("max"));
			if (!GenericValidator.isInRange(longValue, min, max)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}    
    
    /**
     * 値を文字列として取得します。
     */
    public static String getValueAsString(Object bean, Field field) {
    	
    	if (bean == null) {
    		return null;
    	}
    	if (String.class.isInstance(bean)) {
    		return (String)bean;
    	}
    	return ValidatorUtils.getValueAsString(bean, field.getProperty());
    }
	
}
