package com.jayqqaa12.jbase.util.upload;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;

import com.jayqqaa12.jbase.util.Fs;
import com.jayqqaa12.jbase.util.L;

public class FlashUpload
{
	
	public static final  String PATH="/upload/image/";
	
	/***
	 * 配合 flash upload 插件 使用
	 * 
	 * @param req
	 * @return
	 */
	public static String flashUpload(HttpServletRequest req)
	{

		String path = req.getRealPath(PATH);

		long savePicName = System.currentTimeMillis();

		// String file_src = _savePath + savePicName + "_src.jpg"; //保存原图
		// String filename162 = _savePath + savePicName + "_162.jpg"; //保存162
		// String filename20 = _savePath + savePicName + "_20.jpg"; //保存20

		// String pic=request.getParameter("pic");
		// String pic1=request.getParameter("pic1");
		// String pic3=request.getParameter("pic3");

		String pic2 = req.getParameter("pic2");
		
		// 图2
		try
		{
			
			Fs.writeFile(new File(path +File.separator+ savePicName + "_48.jpg"), new BASE64Decoder().decodeBuffer(pic2));
		} catch (IOException e)
		{
			return "{\"status\":0}";
		}

		String picUrl = PATH + savePicName;

		return "{\"status\":1,\"picUrl\":\"" + picUrl + "\"}";

		// if(!pic.equals("")&&pic!=null){
		// //原图
		// File file = new File(file_src);
		// FileOutputStream fout = null;
		// fout = new FileOutputStream(file);
		// fout.write(new BASE64Decoder().decodeBuffer(pic));
		// fout.close();
		// }

		// 图1
		// File file1 = new File(filename162);
		// FileOutputStream fout1 = null;
		// fout1 = new FileOutputStream(file1);
		// fout1.write(new BASE64Decoder().decodeBuffer(pic1));
		// fout1.close();

	}

}
