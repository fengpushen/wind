package com.xl.busi.common;

import java.util.List;
import java.util.Map;

public interface CommonDAO {

	List<Map> selectCom_area();

	List<Map> selectV_com_area(Map params);
}
