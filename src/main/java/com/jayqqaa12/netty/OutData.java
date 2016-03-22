package com.jayqqaa12.netty;

import java.util.List;

import com.google.common.collect.Lists;

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
