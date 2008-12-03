package jp.honestyworks.s2vali.logic;

import java.util.Map;

import org.apache.commons.validator.ValidatorResults;

import jp.honestyworks.s2vali.ErrorMessage;
import jp.honestyworks.s2vali.dto.BulkdepoCsv;
import jp.honestyworks.s2vali.dto.ValidatorTestDto;

public interface ValiLogic {
	
	public String[] validateBulkdepoCsv(BulkdepoCsv csv);
	public ValidatorResults validateBulkdepoCsv_vr(BulkdepoCsv csv);
	public ErrorMessage[] validateBulkdepoCsv_em(BulkdepoCsv csv);
	
	public String[] validateForm2(BulkdepoCsv bulkdepoCsv);

	public String[] validateForm3(BulkdepoCsv bulkdepoCsv);
	
	public String[] validateForm4(ValidatorTestDto dto);
	public String[] validateForm4(Map dto);
	
	public String[] validateForm5(ValidatorTestDto dto);
	public String[] validateForm6(ValidatorTestDto dto);
	public String[] validateForm7(ValidatorTestDto dto);
	public String[] validateForm8(ValidatorTestDto dto);
	public String[] validateForm9(ValidatorTestDto dto);
	public String[] validateForm10(ValidatorTestDto dto);
	public String[] validateForm11(ValidatorTestDto dto);
	public String[] validateForm12(ValidatorTestDto dto);

}
