package jp.honestyworks.s2vali.validator;

import java.io.UnsupportedEncodingException;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.StringUtil;

public class S2GenericValidator {
	
    private S2GenericValidator() {
    }

    /**
     * バイト長が最小値より大きいかをチェックします。
     * 
     * @param value
     *            値
     * @param min
     *            最小値
     * @param charset
     *            チャーセット
     * @return 検証結果
     */
    public static boolean minByteLength(String value, int min, String charset) {
        return (getBytes(value, charset).length >= min);
    }

    /**
     * バイト長が最大値より小さいかをチェックします。
     * 
     * @param value
     *            値
     * @param max
     *            最大値
     * @param charset
     *            チャーセット
     * @return 検証結果
     */
    public static boolean maxByteLength(String value, int max, String charset) {
        return (getBytes(value, charset).length <= max);
    }

    private static byte[] getBytes(String str, String charset) {
        if (StringUtil.isEmpty(charset)) {
            return str.getBytes();
        }
        try {
            return str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new IORuntimeException(e);
        }
    }	

}
