package com.jayqqaa12.jbase.tcp.netty.code;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * Created by 12 on 2017/12/4.
 */
public class TextFrameCodec extends MessageToMessageCodec<TextWebSocketFrame,String > {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out)  {
        out.add(new TextWebSocketFrame(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) {
        out.add(msg.text());
    }


}
