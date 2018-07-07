package com.lottery.buy;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jfinal.aop.Before;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.lottery.admin.news.NewsService;
import com.lottery.common.model.LotUser;
import com.lottery.common.model.NewsContext;
import com.lottery.common.model.UserBuy;
import com.lottery.common.model.UserBuy.TypeEnum;
import com.lottery.wallet.WalletService;

public class BuyService {
	private static Logger log=Logger.getLogger(BuyService.class);
	private NewsService newsService=new NewsService();
	private WalletService walletService=Duang.duang(WalletService.class);
	
	public boolean HasbuyNews(int newsId,int userId){
		String sql="select count(1) from user_buy where outId=? and userId=? and type=?";
		return Db.queryInt(sql,newsId,userId,TypeEnum.News.ordinal())>0;
	}
	
	/**
	 * 购买文章 自动判断是否已购买
	 * @param newsId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Before(Tx.class)
	public boolean BuyNews(int newsId, LotUser user) throws Exception{
		boolean hasBuy=HasbuyNews(newsId,user.getId());
		if(hasBuy){
			log.info("[购买文章"+user.getAccount()+"]该用户已经购买文章id="+newsId+",无需重复购买");
		}else{
			NewsContext  news=newsService.get(newsId);
			if(news==null){
				throw new Exception("文章不存在");
			}
			if(news.getIsFree() || news.getMoney().compareTo(BigDecimal.ZERO)==0){//免费
				hasBuy=true;
			}else{
				walletService.BuyNews(user, news);
				String remark="购买文章,文章id"+newsId;
				//增加UserBuy日志
				UserBuy  entity=new UserBuy();
				entity.setUserId(user.getId());
				entity.setAccount(user.getAccount());
				entity.setCreateTime(new Date());
				entity.setOutId(newsId);
				entity.setType(TypeEnum.News.ordinal());
				entity.setRemark(remark);
				entity.save();
				hasBuy=true;
				log.info("[购买文章"+user.getAccount()+"]成功:"+remark);
			}
		}
		return hasBuy;
	}
	
}
