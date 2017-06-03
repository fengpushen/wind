package com.xl.busi.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAOImpl implements CompanyDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map> selectBs_company(Map params) {
		return sqlSession.selectList("selectBs_company", params);
	}
	
	public Map selectBs_companyByName(String c_name, String c_type){
		Map params = new HashMap();
		params.put("C_NAME", c_name);
		params.put("C_TYPE", c_type);
		return sqlSession.selectOne("selectBs_company", params);
	}
	
	public Map selectBs_companyById(String c_id){
		Map params = new HashMap();
		params.put("C_ID", c_id);
		return sqlSession.selectOne("selectBs_company", params);
	}
}
