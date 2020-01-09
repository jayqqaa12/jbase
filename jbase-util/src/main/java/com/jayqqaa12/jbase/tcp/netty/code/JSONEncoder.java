package com.jayqqaa12.jbase.tcp.netty.code;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class JSONEncoder extends MessageToByteEncoder<Object> {
  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    if (msg == null) return;

    byte[] message= JSON.toJSONString(msg).getBytes("UTF-8");

    out.writeInt(message.length);
    out.writeBytes(message);
  }

}
