package com.jayqqaa12.jbase.jfinal.ext;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jayqqaa12.jbase.sdk.util.OSSKit;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.RenderingTimeHandler;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.plugin.config.ConfigPlugin;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;

/***
 * 默认开启了自动路由
 * 
 * 配置名称 基于约定
 * 
 * @author 12
 *
 */
public abstract class JbaseConfig extends JFinalConfig {
	private static boolean isDev;
	private boolean useDruid;
	private boolean useShiro;

	private Routes routes;

	static {
		String osName = System.getProperty("os.name");
		isDev = osName.indexOf("Windows") != -1;

		if (isDevMode()) System.setProperty("LOGDIR", "c:/");
		else System.setProperty("LOGDIR", "/log");// linux
	}

	public static boolean isDevMode() {
		return isDev;
	}

	public String getConfigStr(String key) {

		return ConfigKit.getStr(key);
	}

	/**
	 * 默认配置名称 基于约定
	 * 
	 * 在配置文件 xx.txt 设置如下参数即可 db.url 数据库地址 db.user 数据库名称 db.pwd 数据库密码
	 * 
	 */
	protected IDataSourceProvider addDruidPlugin(Plugins me) {

		useDruid = true;

		DruidPlugin dbPlugin = new DruidPlugin(getConfigStr("db.url"), getConfigStr("db.user"),
				getConfigStr("db.pwd"));
		// 设置 状态监听与 sql防御
		WallFilter wall = new WallFilter();
		wall.setDbType(getConfigStr("db.type"));
		dbPlugin.addFilter(wall);
		dbPlugin.addFilter(new StatFilter());
		me.add(dbPlugin);

		me.add(new SqlInXmlPlugin());

		return dbPlugin;
	}

	protected ActiveRecordPlugin addActiveRecordPlugin(Plugins me, IDataSourceProvider dataSource) {
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSource);
		me.add(arp);
		return arp;
	}

	/**
	 * 默认配置名称 基于约定
	 * 
	 * 在配置文件 xx.txt 设置如下参数即可 mongo.url 数据库地址 格式是 mongodb:// mongo.db 数据库名称
	 * 
	 */
	protected void addMongoPlugin(Plugins me) {
		// redis
		me.add(new MongodbPlugin(getConfigStr("mongo.url"), getConfigStr("mongo.db")));

	}

	protected void addEhCachePlugin(Plugins me) {
		me.add(new EhCachePlugin());

	}

	/**
	 * 默认配置文件 shiro.ini
	 * @param me
	 */
	protected void addShiroPlugin(Plugins me) {

		useShiro = true;
		me.add(new ShiroPlugin(this.routes));

	}

	/**
	 * 默认配置名称 基于约定
	 * 
	 * 在配置文件 xx.txt 设置如下参数即可 redis.db 数据库名称 redis.host 数据库地址 数据库名称 redis.pwd
	 * 数据库密码
	 * 
	 */
	protected void addRedisPlugin(Plugins me) {
		// redis
		me.add(new RedisPlugin(getConfigStr("redis.db"), getConfigStr("redis.host"), ConfigKit
				.getStr("redis.pwd")));

	}

	/**
	 * 默认配置文件 job.properties
	 * 
	 * @param me
	 */
	protected void addQuartzPlugin(Plugins me) {
		me.add(new QuartzPlugin("job.properties"));
	}

	/**
	 * 测试的时候打开 切换为开发模式
	 */
	public static void openTestMode() {
		isDev = true;
	}

	public void configConstant(Constants me) {

		ConfigKit.setDev(isDevMode());

		new ConfigPlugin(".*.txt").reload(false).start();
		me.setDevMode(isDevMode());
		SqlReporter.setLog(isDevMode());
	}

	/**
	 * 默认开启了自动路由
	 */
	public void configRoute(Routes me) {
		routes = me;
		// 自动扫描 建议用注解
		AutoBindRoutes abr = new AutoBindRoutes().autoScan(false);
		me.add(abr);
	}

	@Override
	public void configInterceptor(Interceptors me) {

		if (useShiro) me.add(new ShiroInterceptor());

	}

	@Override
	public void configHandler(Handlers me) {
		// 计算每个page 运行时间
		if (isDevMode()) me.add(new RenderingTimeHandler());

		if (isDevMode() && useDruid) me.add(new DruidStatViewHandler("/druid"));
	}

}
