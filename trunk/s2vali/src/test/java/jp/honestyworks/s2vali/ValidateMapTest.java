package jp.honestyworks.s2vali;

import java.util.HashMap;
import java.util.Map;

import jp.honestyworks.s2vali.logic.ValiLogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.extension.unit.S2TestCase;

public class ValidateMapTest extends S2TestCase {
	
	private final static Log log = LogFactory.getLog(ValidateMapTest.class);
	
	private ValiLogic valiLogic;
	
	public void setUp() {
		include("app_test.dicon");
	}	
	
	/**
	 * minlength
	 */
	public void testMinLength() {

		/*
		ValidatorTestDto dto = new ValidatorTestDto();
		dto.setIntValue1(12345);
		dto.setLongValue1(12345L);
		dto.setFloatValue1(1111.22F);
		*/
		Map dto = new HashMap();
		dto.put("intValue1", 12345);
		dto.put("longValue1", 12345L);
		dto.put("floatValue1", 1111.22F);
		
		String[] errors = null;
		
		
		// strValue2がセットされている場合は、strValue1は必須
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		/*
		dto.put("srtValue2"," "); // whitespaceはnullでないとみなされる。
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		*/
		
		dto.put("strValue2", "1");
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		
		// minlength too short
		dto.put("strValue1", "1234"); // too short
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5文字以上で入力してください。", errors[0]);
		
		// minlength OK
		dto.put("strValue1", "12345"); // OK
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		dto.put("strValue2", null);
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		// int型のテスト
		dto.put("intValue1", 12);
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は3文字以上で入力してください。", errors[0]);
		dto.put("intValue1", 1111);
		
		// long型のテスト
		dto.put("longValue1", 12L);
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は3文字以上で入力してください。", errors[0]);
		dto.put("longValue1", 1111L);
		
		// float型のテスト
		dto.put("floatValue1", -1.3F);
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Fバリュー1は5文字以上で入力してください。", errors[0]);
		
		dto.put("floatValue1", -11.3F);
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		// Longのテスト
		dto.put("longObjValue1", new Long(12L));
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は3文字以上で入力してください。", errors[0]);
		dto.put("longObjValue1", null);
		
		// Floatのテスト
		dto.put("floatObjValue1", new Float(-1.3F));
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("FOバリュー1は5文字以上で入力してください。", errors[0]);
		
		dto.put("floatObjValue1", new Float(-11.3F));
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);		

	}
	
}
