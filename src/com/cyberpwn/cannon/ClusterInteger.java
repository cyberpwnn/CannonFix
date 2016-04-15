package com.cyberpwn.cannon;

import com.cyberpwn.cannon.ClusterConfig.ClusterDataType;

public class ClusterInteger extends Cluster
{
	public ClusterInteger(String key, Integer value)
	{
		super(ClusterDataType.INTEGER, key, value.doubleValue());
	}
	
	public int get()
	{
		return value.intValue();
	}
	
	public void set(int i)
	{
		value = (double) i;
	}
}
