package com.cyberpwn.cannon;

import java.util.Iterator;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CannonFix extends JavaPlugin implements Listener, Configurable
{
	private DataController dc;
	private ClusterConfig cc;
	private GMap<Chunk, Integer> chunked;
	private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
	
	public void onEnable()
	{
		dc = new DataController(this);
		cc = new ClusterConfig();
		chunked = new GMap<Chunk, Integer>();
		
		dc.start();
		dc.load(null, this);
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				Iterator<Chunk> it = chunked.k().iterator();
				
				while(it.hasNext())
				{
					Chunk i = it.next();
					chunked.put(i, chunked.get(i) - 1);
					
					if(chunked.get(i) < 0)
					{
						chunked.remove(i);
					}
				}
			}
		}, 0, 20);
	}
	
	public void onDisable()
	{
		
	}
	
	public void target(Chunk chunk)
	{
		pop(chunk);
		pop(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ()));
		pop(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ()));
		pop(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1));
		pop(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1));
		pop(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() + 1));
		pop(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() - 1));
		pop(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() - 1));
		pop(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1));
	}
	
	public void pop(Chunk chunk)
	{
		chunked.put(chunk, cc.getInt("cooldown-seconds"));
	}
	
	public boolean coolingDown(Block block)
	{
		return chunked.containsKey(block.getChunk());
	}
	
	@EventHandler
	public void onTNTExplosion(EntityExplodeEvent e)
	{
		if(e.getEntityType().equals(EntityType.PRIMED_TNT))
		{
			target(e.getLocation().getChunk());
		}
	}
	
	@EventHandler
	public void onFromTo(BlockFromToEvent e)
	{
		@SuppressWarnings("deprecation")
		int id = e.getBlock().getTypeId();
		Block b = e.getToBlock();
		
		if(generatesCobble(id, b) && coolingDown(b))
		{
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean generatesCobble(int id, Block b)
	{
		int mirrorID1 = (id == 8 || id == 9 ? 10 : 8);
		int mirrorID2 = (id == 8 || id == 9 ? 11 : 9);
		
		for(BlockFace face : faces)
		{
			Block r = b.getRelative(face, 1);
			
			if(r.getTypeId() == mirrorID1 || r.getTypeId() == mirrorID2)
			{
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void onNewConfig()
	{
		cc.set("cooldown-seconds", 10);
	}
	
	@Override
	public void onReadConfig()
	{
		
	}
	
	@Override
	public ClusterConfig getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return "config";
	}
}
