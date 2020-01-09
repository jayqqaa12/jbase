package com.jayqqaa12.jbase.tcp.netty.code;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * 转为json字符串
 *
 * Created by 12 on 2017/12/4.
 */
public class TextFrameCodec extends MessageToMessageCodec<TextWebSocketFrame,Object > {

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {

    out.add(new TextWebSocketFrame(JSON.toJSONString(msg)));
  }





  @Override
  protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) {
    out.add(msg.text());
  }


}
