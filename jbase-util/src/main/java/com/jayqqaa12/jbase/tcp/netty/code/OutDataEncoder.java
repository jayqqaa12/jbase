package com.jayqqaa12.jbase.tcp.netty.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.File;


public class OutDataEncoder extends MessageToByteEncoder<OutData> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OutData msg, ByteBuf out) throws Exception {

		int length = 0; // 最大值只能表示 2G以内
		for (Object o : msg.data) {
			if (o instanceof Short) length += 2;
			if (o instanceof Integer) length += 4;
			if (o instanceof Long) length += 8;
			if (o instanceof Byte) length += 1;
			if (o instanceof byte[])length +=  ((byte [])o).length;
			if (o instanceof String) length += (((String) o).getBytes()).length;
			if (o instanceof File) {
				length += ((File) o).length();  
			}
		}

		out.writeInt(length);

		for (Object obj : msg.data) {
			if (obj instanceof Short) out.writeShort((Short) obj);
			else if (obj instanceof Integer) out.writeInt((Integer) obj);
			else if (obj instanceof Long) out.writeLong((Long) obj);
			else if (obj instanceof Byte) out.writeByte((Byte) obj);
			else if (obj instanceof byte[]) out.writeBytes( (byte [])obj  );
			else if (obj instanceof String) out.writeBytes(((String) obj).getBytes());


		}

	}

	

}