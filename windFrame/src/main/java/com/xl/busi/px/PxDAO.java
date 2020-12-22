package com.xl.busi.px;

import java.util.List;
import java.util.Map;

public interface PxDAO {

	Map selectBs_jcptpx_cp(Map params);

	List selectBs_jcptpx_xm(Map params);

	List selectBs_jcptpxcp_xm_att(Map params);

	void insertBx_jcptcp_xm(Map params);

	Map selectBs_jcptpxcp_xm(Map params);

	List selectV_bx_jcptcp(Map params);

}
