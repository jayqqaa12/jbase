package com.jayqqaa12.jbase.jfinal.ext.model;


import com.alibaba.fastjson.JSONObject;
import com.jayqqaa12.jbase.jfinal.ext.model.Model;
import com.jayqqaa12.jbase.util.IpUtil;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;

/**
 *需要 创建 ip 数据库
 *
 *CREATE TABLE `ip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` int(10) unsigned DEFAULT NULL,
  `end` int(10) unsigned DEFAULT NULL,
  `nation` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5759 DEFAULT CHARSET=utf8
 *
 * @author 12
 *
 */
@TableBind(tableName = "ip")
public class Ip extends Model<Ip>
{
	private static final long serialVersionUID = -3901846514758691907L;

	public static Ip dao = new Ip();

	public Ip findIp(String ipStr)
	{
		String sql = "select nation,province from ip where inet_aton(?) between start and end";
		Ip ip = findFirst(sql, ipStr);
		if (ip == null)
		{
			JSONObject json = IpUtil.getIp(ipStr);
			if (json!=null&& json.getString("country") != null) save(json,ipStr);
			
			return findFirst(sql, ipStr);
		}
		else return ip;

		
	}

	public void save(JSONObject json,String ip)
	{
		if(json==null) return ;
		
		if(json.getString("start").equals("-1")){
			
			json.put("start", ip);
			json.put("end", ip);
		}
			
		Db.update("insert into ip(start,end,nation,province,city) values(inet_aton(?),inet_aton(?),?,?,?) ", json.getString("start"),
				json.getString("end"), json.getString("country"), json.getString("province"),json.getString("city"));
	}

}
