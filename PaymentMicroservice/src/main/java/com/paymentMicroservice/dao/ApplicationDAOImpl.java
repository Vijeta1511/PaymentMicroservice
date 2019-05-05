package com.paymentMicroservice.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.paymentMicroservice.domain.Application;
import com.paymentMicroservice.rm.ApplicationRowMapper;

@Repository
/**
 * Application DAO Implementation class
 * It extends BaseDAO and Implements the ApplicationDAO methods
 * @author vijetaagrawal
 *
 */
public class ApplicationDAOImpl extends BaseDAO implements ApplicationDAO {

	@Override //Method to register application
	public Application save(Application a) {
		
		String name = a.getName();
		Integer userId = a.getUserId();
		String imageLocation = a.getImageLocation();
		String appDescription = a.getAppDesc();
		
		String sql = "INSERT INTO application(name, userId, imageLocation, appDescription)"
                + " VALUES(:name, :userId, :imageLocation, :appDescription)";
		
		Map m = new HashMap();
		m.put("name", name);
		m.put("userId", userId);
		m.put("imageLocation", imageLocation);
		m.put("appDescription", appDescription);
			
		 	KeyHolder kh = new GeneratedKeyHolder();
	        SqlParameterSource ps = new MapSqlParameterSource(m);
	        super.getNamedParameterJdbcTemplate().update(sql, ps, kh);
	        Integer app_id = kh.getKey().intValue();
	        a.setApp_id(app_id);
	        
	    return a;
	}



	@Override //Method to find an application by its ID
	public Application findById(Integer app_id) {
		String sql = "SELECT app_id, userId, name FROM application WHERE app_id=?";
		Application a = getJdbcTemplate().queryForObject(sql, new ApplicationRowMapper(), app_id);
		return a;
	}

	@Override //Method to find an application by its property
	public Application findByProperty(String propName, String propValue) {
		String sql = "SELECT app_id, userId, name FROM application WHERE "+propName+"=?";
		return getJdbcTemplate().queryForObject(sql, new ApplicationRowMapper(), propValue);// TODO Auto-generated method stub
	}

	

}
