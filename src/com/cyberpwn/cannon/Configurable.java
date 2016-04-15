package com.cyberpwn.cannon;

public interface Configurable
{
	void onNewConfig();
	void onReadConfig();
	ClusterConfig getConfiguration();
	String getCodeName();
}
