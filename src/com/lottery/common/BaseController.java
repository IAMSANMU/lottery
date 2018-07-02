package com.lottery.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.jfinal.core.Controller;
import com.lottery.common.model.LotUser;import com.lottery.common.utils.Constant;

public class BaseController extends Controller {
	
	public <T> List<T> getModelList(Class<T> modelClass,String modelName){
		Set<String> modelPrefix = getPrefixName(modelName);
		List<T> resultList = new ArrayList<T>();
		for (String modelName2 : modelPrefix) {
			resultList.add(getModel(modelClass,modelName2));
		}
		return resultList;
	}
	private Set<String> getPrefixName(String modelName){
		Pattern p = Pattern.compile(modelName + "\\[\\d\\].[a-zA-z0-9]+");
		Map<String, String[]> parasMap = getParaMap();
		String paraKey;
		Set<String> modelPrefix = new HashSet<String>();
		for (Entry<String, String[]> e : parasMap.entrySet()) {
			paraKey = e.getKey();
			if(p.matcher(paraKey).find()){
				modelPrefix.add(paraKey.split("\\.")[0]);
			}
		}
		return modelPrefix;
	}
	
	public <T> List<T> getBeanList(Class<T> modelClass,String modelName){
		Set<String> modelPrefix = getPrefixName(modelName);
		List<T> resultList = new ArrayList<T>();
		for (String modelName2 : modelPrefix) {
			resultList.add(getBean(modelClass,modelName2));
		}
		return resultList;
	}
	
	public LotUser loginUser(){
		return (LotUser)getSessionAttr(Constant.SELLER_SESSION);
	}

}
