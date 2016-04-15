package com.cyberpwn.cannon;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataController
{
	private File dataFolder;
	
	public DataController(CannonFix pl)
	{
		this.dataFolder = pl.getDataFolder();
	}
	
	public void start()
	{
		verify(dataFolder);
	}
	
	/**
	 * Saves the configuration from the configurable object.
	 * If the config does not exist, it will be created.
	 * @param category The "folder" it is within (use null for root plugin folder)
	 * @param c The class (usually this for the same object)
	 */
	public void save(String category, Configurable c)
	{
		File file = null;
		
		if(category == null)
		{
			file = new File(dataFolder, c.getCodeName() + ".yml");
		}
		
		else
		{
			file = new File(new File(dataFolder, category), c.getCodeName() + ".yml");
		}
		
		if(!file.exists() && file.isDirectory())
		{
			file.delete();
		}
		
		if(!file.exists())
		{
			c.onNewConfig();
			verifyFile(file);
		}
		
		saveFileConfig(file, c.getConfiguration().toYaml());
	}
	
	/**
	 * Loads the configuration from the configurable object.
	 * If the config does not exist, it will be created.
	 * @param category The "folder" it is within (use null for root plugin folder)
	 * @param c The class (usually this for the same object)
	 */
	public void load(String category, Configurable c)
	{
		File file = null;
		
		if(category == null)
		{
			file = new File(dataFolder, c.getCodeName() + ".yml");
		}
		
		else
		{
			file = new File(new File(dataFolder, category), c.getCodeName() + ".yml");
		}
		
		if(!file.exists() && file.isDirectory())
		{
			file.delete();
		}
		
		if(!file.exists())
		{
			c.onNewConfig();
			verifyFile(file);
			saveFileConfig(file, c.getConfiguration().toYaml());
		}
		
		loadConfigurableSettings(file, c);
		c.onReadConfig();
	}
	
	private void loadConfigurableSettings(File file, Configurable c)
	{
		c.onNewConfig();
		FileConfiguration fc = loadFileConfig(file);
		
		for(String i : fc.getKeys(true))
		{
			if(fc.isBoolean(i))
			{
				c.getConfiguration().set(i, fc.getBoolean(i));
			}
			
			if(fc.isDouble(i))
			{
				c.getConfiguration().set(i, fc.getDouble(i));
			}
			
			if(fc.isInt(i))
			{
				c.getConfiguration().set(i, fc.getInt(i));
			}
			
			if(fc.isString(i))
			{
				c.getConfiguration().set(i, fc.getString(i));
			}
			
			if(fc.isList(i))
			{
				c.getConfiguration().set(i, new GList<String>(fc.getStringList(i)));
			}
		}
		
		for(String i : c.getConfiguration().getData().keySet())
		{
			fc.set(i, c.getConfiguration().getAbstract(i));
		}
		
		saveFileConfig(file, fc);
	}
	
	private FileConfiguration loadFileConfig(File file)
	{
		FileConfiguration fc = new YamlConfiguration();
		
		try
		{
			fc.load(file);
		}
		
		catch(IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
		
		return fc;
	}
	
	private void saveFileConfig(File file, FileConfiguration fc)
	{
		try
		{
			fc.save(file);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void verifyFile(File file)
	{
		if(!file.exists())
		{
			verify(file.getParentFile());
			
			try
			{
				file.createNewFile();
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void verify(File file)
	{
		if(!file.exists())
		{
			file.mkdirs();
		}
	}
}
