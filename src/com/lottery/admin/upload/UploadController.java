package com.lottery.admin.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.lottery.common.BaseController;
import com.lottery.common.utils.JsonResult;

public class UploadController extends BaseController {

	public void index() {
		String type = getPara(0);
		String basePath = "upload";

		String date = DateKit.toStr(new Date());
		String path = File.separator+basePath+File.separator + type +  File.separator + date +  File.separator;

		List<String> pathList = new ArrayList<String>();

		JsonResult json = new JsonResult();
		List<UploadFile> datas;
		try {
			datas = getFiles();
			for (UploadFile file : datas) {
				String newName = UUID.randomUUID().toString() + file.getFileName();
				String rootPath= PathKit.getWebRootPath()  +path;
				File folder=new File(rootPath);
				folder.mkdirs();
				
				rootPath += newName;
				path+=newName;
				pathList.add(path);
				file.getFile().renameTo(new File(rootPath));
			}
			json.setSuccess(true);
			json.setData(pathList);
		} catch (Exception e) {
			json.setSuccess(false);
			json.setMessage("系统错误:" + e.getMessage());
		}
		renderJson(json.toJsonString());
	}
	public static void main(String[] args) {
		File a =new File("D:\\tomcat\\apache-tomcat-8.5.15\\webapps\\Lottery\\upload\\context\\a.jpg");
		a.mkdirs();
		System.out.println(a);
	}
}
