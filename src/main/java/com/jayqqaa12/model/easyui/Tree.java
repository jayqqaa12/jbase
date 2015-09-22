package com.jayqqaa12.model.easyui;

import java.util.ArrayList;
import java.util.List;

import com.jayqqaa12.jbase.jfinal.ext.model.Model;

/**
 * EasyUI tree模型
 * 
 * 
 */
public class Tree  implements java.io.Serializable
{
	public static final long serialVersionUID = 2592596759941016872L;

	public Integer id;
	public String text;
	public String state = "closed";// open,closed
	public boolean checked = false;
	public Object attributes;
	public List<Tree> children =new ArrayList<Tree>();
	public String iconCls;
	public Integer pid;
	
	
	public Tree(Integer id, Integer pid ,String text,String icon ,Object obj ,boolean haveChild){
		
		this.id=id;
		this.pid=pid;
		this.text=text;
		this.iconCls=icon;
		this.attributes=obj;

		
		
		if(!haveChild) changeState();
		
	}

	public void changeState()
	{
		this.state = "open";
	}


	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public Object getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Object attributes)
	{
		this.attributes = attributes;
	}

	public List<Tree> getChildren()
	{
		return children;
	}

	public void setChildren(List<Tree> children)
	{
		this.children = children;
	}

	public String getIconCls()
	{
		return iconCls;
	}

	public void setIconCls(String iconCls)
	{
		this.iconCls = iconCls;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Integer getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}


}
