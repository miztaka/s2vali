package jp.honestyworks.s2vali;

import jp.honestyworks.s2vali.dto.ValidatorTestDto;
import jp.honestyworks.s2vali.logic.ValiLogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.extension.unit.S2TestCase;

public class ValidatorTest extends S2TestCase {
	
	private final static Log log = LogFactory.getLog(ValidatorTest.class);
	
	private ValiLogic valiLogic;
	
	public void setUp() {
		include("app_test.dicon");
	}	
	
	/**
	 * minlength
	 */
	public void testMinLength() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		dto.setIntValue1(12345);
		dto.setLongValue1(12345L);
		dto.setFloatValue1(1111.22F);
		String[] errors = null;
		
		// strValue2がセットされている場合は、strValue1は必須
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		dto.setStrValue2(" "); // whitespaceはnullでないとみなされる。
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		
		dto.setStrValue2("1");
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		
		// minlength too short
		dto.setStrValue1("1234"); // too short
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5文字以上で入力してください。", errors[0]);
		
		// minlength OK
		dto.setStrValue1("12345"); // OK
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		dto.setStrValue2(null);
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		// int型のテスト
		dto.setIntValue1(12);
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は3文字以上で入力してください。", errors[0]);
		dto.setIntValue1(1111);
		
		// long型のテスト
		dto.setLongValue1(12L);
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は3文字以上で入力してください。", errors[0]);
		dto.setLongValue1(1111L);
		
		// float型のテスト
		dto.setFloatValue1(-1.3F);
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Fバリュー1は5文字以上で入力してください。", errors[0]);
		
		dto.setFloatValue1(-11.3F);
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
		// Longのテスト
		dto.setLongObjValue1(new Long(12L));
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は3文字以上で入力してください。", errors[0]);
		dto.setLongObjValue1(null);
		
		// Floatのテスト
		dto.setFloatObjValue1(new Float(-1.3F));
		errors = valiLogic.validateForm4(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("FOバリュー1は5文字以上で入力してください。", errors[0]);
		
		dto.setFloatObjValue1(new Float(-11.3F));
		errors = valiLogic.validateForm4(dto);
		assertNull(errors);
		
	}
	
	/**
	 * maxlength
	 */
	public void testMaxLength() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		// strValue2がセットされていない場合は、strValue1は必須
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		
		dto.setStrValue2(" ");
		errors = valiLogic.validateForm5(dto);
		assertNull(errors);
		
		dto.setStrValue2("1");
		errors = valiLogic.validateForm5(dto);
		assertNull(errors);
		
		// minlength too long
		dto.setStrValue1("123456"); // too short
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5文字以下で入力してください。", errors[0]);
		
		// minlength OK
		dto.setStrValue1("12345"); // OK
		errors = valiLogic.validateForm5(dto);
		assertNull(errors);
		
		dto.setStrValue2("1");
		errors = valiLogic.validateForm5(dto);
		assertNull(errors);
		
		// int型のテスト
		dto.setIntValue1(1234);
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は3文字以下で入力してください。", errors[0]);
		dto.setIntValue1(11);
		
		// long型のテスト
		dto.setLongValue1(1234L);
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は3文字以下で入力してください。", errors[0]);
		dto.setLongValue1(11L);
		
		// float型のテスト
		dto.setFloatValue1(-11.39F);
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Fバリュー1は5文字以下で入力してください。", errors[0]);
		
		dto.setFloatValue1(-11.3F);
		errors = valiLogic.validateForm5(dto);
		assertNull(errors);
		
		// Longのテスト
		dto.setLongObjValue1(new Long(1234L));
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は3文字以下で入力してください。", errors[0]);
		dto.setLongObjValue1(null);
		
		// Floatのテスト
		dto.setFloatObjValue1(new Float(-11.39F));
		errors = valiLogic.validateForm5(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("FOバリュー1は5文字以下で入力してください。", errors[0]);
		
		dto.setFloatObjValue1(new Float(-11.3F));
		errors = valiLogic.validateForm5(dto);
		assertNull(errors);
		
	}	
	
	/**
	 * minbytelength
	 */
	public void testMinByteLength() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		dto.setIntValue1(12345);
		dto.setLongValue1(12345L);
		dto.setFloatValue1(1111.22F);
		String[] errors = null;
		
		// intValue2が9の場合は、strValue1は必須
		errors = valiLogic.validateForm6(dto);
		assertNull(errors);
		
		dto.setIntValue2(9);
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		
		// minlength too short
		dto.setStrValue1("1234"); // too short
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5byte以上で入力してください。", errors[0]);
		
		// minlength OK
		dto.setStrValue1("12345"); // OK
		errors = valiLogic.validateForm6(dto);
		assertNull(errors);
		
		dto.setStrValue1("佐藤"); // SJISなら4byte = ng
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5byte以上で入力してください。", errors[0]);
		
		dto.setStrValue1(null);
		dto.setIntValue2(0);
		errors = valiLogic.validateForm6(dto);
		assertNull(errors);
		
		// int型のテスト
		dto.setIntValue1(12);
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は3byte以上で入力してください。", errors[0]);
		dto.setIntValue1(1111);
		
		// long型のテスト
		dto.setLongValue1(12L);
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は3byte以上で入力してください。", errors[0]);
		dto.setLongValue1(1111L);
		
		// float型のテスト
		dto.setFloatValue1(-1.3F);
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Fバリュー1は5byte以上で入力してください。", errors[0]);
		
		dto.setFloatValue1(-11.3F);
		errors = valiLogic.validateForm6(dto);
		assertNull(errors);
		
		// Longのテスト
		dto.setLongObjValue1(new Long(12L));
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は3byte以上で入力してください。", errors[0]);
		dto.setLongObjValue1(null);
		
		// Floatのテスト
		dto.setFloatObjValue1(new Float(-1.3F));
		errors = valiLogic.validateForm6(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("FOバリュー1は5byte以上で入力してください。", errors[0]);
		
		dto.setFloatObjValue1(new Float(-11.3F));
		errors = valiLogic.validateForm6(dto);
		assertNull(errors);
		
	}
	
	/**
	 * maxbytelength
	 */
	public void testMaxByteLength() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		// floatValue2が3.14の場合は、strValue1は必須
		dto.setFloatValue2(3.14F);
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1を入力してください。", errors[0]);
		
		dto.setFloatValue2(0F);
		errors = valiLogic.validateForm7(dto);
		assertNull(errors);
		
		// minlength too long
		dto.setStrValue1("123456"); // too long
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5byte以下で入力してください。", errors[0]);
		
		dto.setFloatValue2(3.14F);
		dto.setStrValue1("佐藤"); // UTF-8では6byte
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は5byte以下で入力してください。", errors[0]);
		
		dto.setStrValue1("佐1"); // UTF-8では6byte
		errors = valiLogic.validateForm7(dto);
		assertNull(errors);
		
		// minlength OK
		dto.setStrValue1("12345"); // OK
		errors = valiLogic.validateForm7(dto);
		assertNull(errors);
		
		// int型のテスト
		dto.setIntValue1(1234);
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は3byte以下で入力してください。", errors[0]);
		dto.setIntValue1(11);
		
		// long型のテスト
		dto.setLongValue1(1234L);
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は3byte以下で入力してください。", errors[0]);
		dto.setLongValue1(11L);
		
		// float型のテスト
		dto.setFloatValue1(-11.39F);
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Fバリュー1は5byte以下で入力してください。", errors[0]);
		
		dto.setFloatValue1(-11.3F);
		errors = valiLogic.validateForm7(dto);
		assertNull(errors);
		
		// Longのテスト
		dto.setLongObjValue1(new Long(1234L));
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は3byte以下で入力してください。", errors[0]);
		dto.setLongObjValue1(null);
		
		// Floatのテスト
		dto.setFloatObjValue1(new Float(-11.39F));
		errors = valiLogic.validateForm7(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("FOバリュー1は5byte以下で入力してください。", errors[0]);
		
		dto.setFloatObjValue1(new Float(-11.3F));
		errors = valiLogic.validateForm7(dto);
		assertNull(errors);
		
	}	

	/**
	 * intRange
	 */
	public void testIntRange() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		errors = valiLogic.validateForm8(dto);
		assertNull(errors);
		
		dto.setStrValue1("abcd"); // not int
		errors = valiLogic.validateForm8(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1はintegerに変換できませんでした。", errors[0]);
		
		dto.setStrValue1("1000"); // too big
		errors = valiLogic.validateForm8(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1以上999以下で入力してください。", errors[0]);
		
		dto.setStrValue1("-1"); // too small
		errors = valiLogic.validateForm8(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1以上999以下で入力してください。", errors[0]);
		
		dto.setStrValue1("1");
		errors = valiLogic.validateForm8(dto);
		assertNull(errors);

		dto.setStrValue1("999");
		errors = valiLogic.validateForm8(dto);
		assertNull(errors);

		dto.setIntValue1(1000);
		errors = valiLogic.validateForm8(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は-100以上999以下で入力してください。", errors[0]);
		dto.setIntValue1(999);
		
		dto.setLongValue1(-101L);
		errors = valiLogic.validateForm8(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は-100以上999以下で入力してください。", errors[0]);
		dto.setLongValue1(999L);
		
		dto.setLongObjValue1(new Long(-101L));
		errors = valiLogic.validateForm8(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は-100以上999以下で入力してください。", errors[0]);
		dto.setLongObjValue1(new Long(999L));
	}	

	/**
	 * longRange
	 */
	public void testLongRange() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		errors = valiLogic.validateForm9(dto);
		assertNull(errors);
		
		dto.setStrValue1("abcd"); // not int
		errors = valiLogic.validateForm9(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1はlongに変換できませんでした。", errors[0]);
		
		dto.setStrValue1("1000"); // too big
		errors = valiLogic.validateForm9(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1以上999以下で入力してください。", errors[0]);
		
		dto.setStrValue1("-1"); // too small
		errors = valiLogic.validateForm9(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1以上999以下で入力してください。", errors[0]);
		
		dto.setStrValue1("1");
		errors = valiLogic.validateForm9(dto);
		assertNull(errors);

		dto.setStrValue1("999");
		errors = valiLogic.validateForm9(dto);
		assertNull(errors);

		dto.setIntValue1(1000);
		errors = valiLogic.validateForm9(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は-100以上999以下で入力してください。", errors[0]);
		dto.setIntValue1(999);
		
		dto.setLongValue1(-101L);
		errors = valiLogic.validateForm9(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は-100以上999以下で入力してください。", errors[0]);
		dto.setLongValue1(999L);
		
		dto.setLongObjValue1(new Long(-101L));
		errors = valiLogic.validateForm9(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は-100以上999以下で入力してください。", errors[0]);
		dto.setLongObjValue1(new Long(999L));
	}	

	/**
	 * floatRange
	 */
	public void testFloatRange() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		errors = valiLogic.validateForm10(dto);
		assertNull(errors);
		
		dto.setStrValue1("abcd"); // not int
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1はfloatに変換できませんでした。", errors[0]);
		
		dto.setStrValue1("1000"); // too big
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1.1以上999.9以下で入力してください。", errors[0]);
		
		dto.setStrValue1("-1.1"); // too small
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1.1以上999.9以下で入力してください。", errors[0]);
		
		dto.setStrValue1("1");
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1.1以上999.9以下で入力してください。", errors[0]);

		dto.setStrValue1("1.1");
		errors = valiLogic.validateForm10(dto);
		assertNull(errors);
		
		dto.setStrValue1("999.9");
		errors = valiLogic.validateForm10(dto);
		assertNull(errors);

		dto.setIntValue1(1000);
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は-100.1以上999.9以下で入力してください。", errors[0]);
		dto.setIntValue1(999);
		
		dto.setLongValue1(-101L);
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は-100.1以上999.9以下で入力してください。", errors[0]);
		dto.setLongValue1(999L);
		
		dto.setLongObjValue1(new Long(-101L));
		errors = valiLogic.validateForm10(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("LOバリュー1は-100.1以上999.9以下で入力してください。", errors[0]);
		dto.setLongObjValue1(new Long(999L));
	}	

	/**
	 * doubleRange
	 */
	public void testDoubleRange() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		errors = valiLogic.validateForm11(dto);
		assertNull(errors);
		
		dto.setStrValue1("abcd"); // not int
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1はdoubleに変換できませんでした。", errors[0]);
		
		dto.setStrValue1("1000"); // too big
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1.1以上999.9以下で入力してください。", errors[0]);
		
		dto.setStrValue1("-1.1"); // too small
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1.1以上999.9以下で入力してください。", errors[0]);
		
		dto.setStrValue1("1");
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は1.1以上999.9以下で入力してください。", errors[0]);

		dto.setStrValue1("1.1");
		errors = valiLogic.validateForm11(dto);
		assertNull(errors);
		
		dto.setStrValue1("999.9");
		errors = valiLogic.validateForm11(dto);
		assertNull(errors);

		dto.setIntValue1(1000);
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は-100.1以上999.9以下で入力してください。", errors[0]);
		dto.setIntValue1(999);
		
		dto.setLongValue1(-101L);
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("Lバリュー1は-100.1以上999.9以下で入力してください。", errors[0]);
		dto.setLongValue1(999L);
		
		dto.setFloatObjValue1(new Float(-100.2F));
		errors = valiLogic.validateForm11(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("FOバリュー1は-100.1以上999.9以下で入力してください。", errors[0]);
		
		dto.setFloatObjValue1(new Float(999.2F));
		errors = valiLogic.validateForm11(dto);
		assertNull(errors);
	}	

	/**
	 * date and email
	 */
	public void testDateAndEmail() {
		
		ValidatorTestDto dto = new ValidatorTestDto();
		String[] errors = null;
		
		dto.setIntValue1(20081107);
		errors = valiLogic.validateForm12(dto);
		assertNull(errors);
		
		dto.setStrValue1("20081107"); // bad format
		errors = valiLogic.validateForm12(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー1は日付形式(yyyy-MM-dd)で入力してください。", errors[0]);
		
		dto.setStrValue1("2008-11-07");
		errors = valiLogic.validateForm12(dto);
		assertNull(errors);
		
		dto.setIntValue1(200811); // bad format
		//dto.setIntValue1(2008117); // TODO なぜかこれだとエラーにならない！！
		errors = valiLogic.validateForm12(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("iバリュー1は日付形式(yyyyMMdd)で入力してください。", errors[0]);
		dto.setIntValue1(20081231);

		dto.setStrValue2("a@b.com");
		errors = valiLogic.validateForm12(dto);
		assertNull(errors);

		dto.setStrValue2("sato <a@b.com>"); // これはだめ
		errors = valiLogic.validateForm12(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー2に正しいメールアドレスを入力してください。", errors[0]);
		
		dto.setStrValue2("hoge@");
		errors = valiLogic.validateForm12(dto);
		assertNotNull(errors);
		assertEquals(1, errors.length);
		assertEquals("バリュー2に正しいメールアドレスを入力してください。", errors[0]);
	}	
	
}
