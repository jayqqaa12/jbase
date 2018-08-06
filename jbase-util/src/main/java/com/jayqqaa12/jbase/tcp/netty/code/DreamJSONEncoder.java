/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.jayqqaa12.jbase.tcp.netty.code;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 描述:与windows行情交易服务器传输json数据的编码器，GBK编码
 *
 * @author  boyce
 * @created 2015年7月23日 下午4:41:03
 * @since   v1.0.0
 */
public class DreamJSONEncoder extends MessageToByteEncoder<String> {
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
//		System.out.println("begin encode");
		if(StringUtils.isEmpty(msg)){
			return ;
		}
		byte[] message=msg.getBytes("UTF-8");
		out.writeInt(message.length);
		out.writeBytes(message);
//		System.out.println("end encode");
	}
	public static void main(String[] args) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("a", (byte)1);
		map.put("b", 1.2);
		map.put("c", "xxxx");
		System.out.println(JSON.toJSONString(map));
	}
}
