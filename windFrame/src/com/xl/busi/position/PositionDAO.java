package com.xl.busi.position;

import java.util.List;
import java.util.Map;

public interface PositionDAO {

	List<Map> selectBs_position(Map params);

	Map selectBs_positionById(String id);

	Map selectBs_positionByName(String c_id, String p_name);

	List<Map> selectBs_position_req(Map params);

	Map selectBs_position_reqById(String id);

	List<Map> selectV_syn_hire();

}
