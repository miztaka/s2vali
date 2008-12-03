package jp.honestyworks.s2vali.dto;

import java.io.Serializable;

/**
 * 一括入金のアップロードCSVデータです。
 * @author miztaka
 *
 */
public class BulkdepoCsv implements Serializable {
	
	private static final long serialVersionUID = 3619683628623377115L;

	public static final String[] NAME_MAPPING = new String[] {
		"seq_no","card_id","shop_id","volume","value_group","comment"
	};
	
	private String seq_no;
	private String card_id;
	private String shop_id;
	private String volume;
	private String value_group;
	private String comment;
	public String getSeq_no() {
		return seq_no;
	}
	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getValue_group() {
		return value_group;
	}
	public void setValue_group(String value_group) {
		this.value_group = value_group;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
