package com.jayqqaa12.jbase.tcp.netty.code;

import com.google.common.collect.Lists;

import java.util.List;

public class OutData {

	public List<Object> data = Lists.newArrayList();
	
	
	public OutData() {
	}
	
	
	public OutData(short code) {
		data.add(code);
	}
	
	

	public OutData add(Object data) {
		this.data.add(data);
		
		return this;
	}

 
}
