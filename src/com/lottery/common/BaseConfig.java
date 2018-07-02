package com.lottery.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.lottery.common.model._MappingKit;
import com.lottery.routes.AdminRoute;
import com.lottery.routes.FrontRoute;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * API引导式配置
 */
public class BaseConfig extends JFinalConfig {
	
	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 * 
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run config 中自动生成
	 * 一条启动配置，可对该自动生成的配置再添加额外的配置项，例如 VM argument 可配置为：
	 * -XX:PermSize=64M -XX:MaxPermSize=256M
	 */
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
//		JFinal.start("WebRoot", 80, "/", 5);

		/**
		 * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		// JFinal.start("WebRoot", 80, "/");
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("dbConfig.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add(new FrontRoute());
		me.add(new AdminRoute());
	}
	
	public void configEngine(Engine me) {
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.addSharedFunction("/common/front/_layout.html");
		me.addSharedFunction("/common/member/_layout.html");
		me.addSharedFunction("/common/_layout.html");
		me.addSharedFunction("/common/_emptyLayout.html");
		me.addSharedFunction("/common/_paginate.html");
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池插件
		DruidPlugin druidPlugin = createDruidPlugin();
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setDevMode(PropKit.getBoolean("devMode", false));
		arp.setShowSql(PropKit.getBoolean("showSql", false));
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);
		
		//任务调度
		Cron4jPlugin cron=new Cron4jPlugin("cron4jConfig.txt");
		me.add(cron);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("basepath"));
	}
}
