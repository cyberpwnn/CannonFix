package com.cyberpwn.cannon;

import com.cyberpwn.cannon.ClusterConfig.ClusterDataType;

public class Cluster
{
	protected final ClusterDataType type;
	protected final String key;
	protected Double value;
	
	public Cluster(ClusterDataType type, String key, Double value)
	{
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public ClusterDataType getType()
	{
		return type;
	}

	public String getKey()
	{
		return key;
	}
}
