package com.jayqqaa12.netty.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.io.Serializable;

public class NettySession implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private Channel session;

	public String host;// session绑定的服务器IP
	public String account;// session绑定的账号
	public String channel;// 终端设备类型

	public Long bindTime;// 登录时间
	public Long heartbeat;// 心跳时间

	public NettySession(Channel session) {
		this.session = session;
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
	/**
	 * 不阻塞 的 可能会失败 
	 * @param msg
	 * @return
	 */
	public boolean write(Object msg) {
		if (session != null && session.isActive()) {
			  session.writeAndFlush(msg);
			  return true;
		}else return false;
		
	}

	/***
	 * 阻塞 
	 * 
	 * @param msg
	 * @return
	 */
	public boolean writeAndWait(Object msg) {
		return writeAndWait(msg, 1000*5);
	}

	/**
	 * 超时断开连接
	 * 
	 * @param msg
	 * @param timeout
	 * @return
	 */
	public boolean writeAndWait(Object msg, int timeout) {
		if (session != null && session.isActive()) {
			ChannelFuture future = session.writeAndFlush(msg);
			future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			return future.awaitUninterruptibly(timeout);

		}
		return false;
	}

	public boolean isAction() {

		return session != null && session.isActive();
	}

	public void close() {
		
		if (session != null) {
			session.close();
		}
	}

	public void setSession(Channel session) {
		this.session = session;
	}

}