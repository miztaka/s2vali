package jp.honestyworks.s2vali;

import jp.honestyworks.s2vali.dto.BulkdepoCsv;
import jp.honestyworks.s2vali.logic.ValiLogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorResults;
import org.seasar.extension.unit.S2TestCase;

public class S2ValiTest extends S2TestCase {
	
	private final static Log log = LogFactory.getLog(S2ValiTest.class);
	
	private ValiLogic valiLogic;
	
	public void setUp() {
		include("app_test.dicon");
	}
	
	public void testNormal() {
		
		String[] errors = valiLogic.validateBulkdepoCsv(new BulkdepoCsv());
		
		for (int i=0; i<errors.length; i++) {
			log.debug(errors[i]);
		}
		
		ValidatorResults vr = valiLogic.validateBulkdepoCsv_vr(new BulkdepoCsv());
		assertNotNull(vr);
		assertEquals(2, vr.getPropertyNames().size());
	}
	
	/**
	 * リソースファイルからメッセージを取得する
	 * プレースホルダーに値を置換する
	 * <arg>を読込む。
	 */
	public void testMessageHandle() {
		
		BulkdepoCsv csv = new BulkdepoCsv();
		csv.setCard_id("111"); // too short
		String[] errors = valiLogic.validateBulkdepoCsv(csv);
		
		assertEquals(2, errors.length);
		assertEquals("連番を入力してください。", errors[0]);
		assertEquals("カード番号は12文字以上16文字以下で入力してください。", errors[1]);
		
		ErrorMessage[] messages = valiLogic.validateBulkdepoCsv_em(csv);
		assertEquals(2, messages.length);
		assertEquals("{0}を入力してください。", messages[0].getMessage());
		assertEquals(1, messages[0].getArgs().length);
		assertEquals("連番", (messages[0].getArgs())[0]);
		assertEquals("{0}は{1}文字以上{2}文字以下で入力してください。", messages[1].getMessage());
		assertEquals(3, messages[1].getArgs().length);
		assertEquals("カード番号", (messages[1].getArgs())[0]);
		assertEquals("12", (messages[1].getArgs())[1]);
		assertEquals("16", (messages[1].getArgs())[2]);
	}

	/**
	 * 引数が与えられなかった場合はプレースホルダーが残る。
	 */
	public void testMessageHundle2() {

		String[] errors = valiLogic.validateForm2(new BulkdepoCsv());
		
		assertEquals(1, errors.length);
		assertEquals("{0}を入力してください。", errors[0]);
	}

	/**
	 * Fieldで指定したメッセージが優先される
	 */
	public void testMessageHundle3() {

		String[] errors = valiLogic.validateForm3(new BulkdepoCsv());
		
		assertEquals(1, errors.length);
		assertEquals("シーケンス番号は絶対に入力しないとだめだぞ。", errors[0]);
	}
	
	
	//------------- 以下 setter ---------------------//
	
	public void setValiLogic(ValiLogic valiLogic) {
		this.valiLogic = valiLogic;
	}

}
