package com.lottery.sp;

import java.util.List;

import com.lottery.common.model.DcSpfSp;

public class SpService {
	private static final DcSpfSp dao = new DcSpfSp().dao();

	public List<DcSpfSp> getSpList(String term) {
		String sql = "select sp.* from dc_spf_sp sp where sp.id in(select MAX(id) from dc_spf_sp where term=? group by matchId)";
		return dao.find(sql, term);
	}
}
