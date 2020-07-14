package ml.govnoed.BlockShuffle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ml.govnoed.BlockShuffle.Data.DataManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public DataManager data;
	
	public boolean gameActive = false;
	
	List<Material> blocks = new ArrayList<Material>();
	
	List<String> activePlayers = new ArrayList<String>();
	Map<String, String> playersBlocks = new HashMap<String, String>();
	Map<String, Boolean> playerDone = new HashMap<String, Boolean>();
	
	
	public int taskId;
	
	
	@Override
	public void onEnable() {
		this.data = new DataManager(this);
		this.getServer().getPluginManager().registerEvents(this, this);

		setblocks();
	}

	@Override
	public void onDisable() {

		gameActive = false;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(label.equalsIgnoreCase("start")) {
			
			BukkitRunnable timer = new BukkitRunnable() {

				@Override
				public void run() {					
					gameActive = false;
					activePlayers.clear();
					playerDone.clear();
					
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "Times's up! Send "+ ChatColor.WHITE + "/start " + ChatColor.RED + "to play one more round!");
				}
				
			};
			
			timer.runTaskLater(this, 20L * 300);
			taskId = timer.getTaskId();
			
			Bukkit.getServer().broadcastMessage("=======================");
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "The game has started! You have " + ChatColor.WHITE + "5 minutes" + ChatColor.GREEN + " to complete the task!");
						

			for (Player player : Bukkit.getOnlinePlayers()) {
				Random random = new Random();
				Material material = null;
				while (material == null) {
					material = blocks.get(random.nextInt(blocks.size()));
					if (!(material.isBlock())) {
						material = null;
					}
				}
				activePlayers.add(player.getName());
				playersBlocks.put(player.getName(), material.name());
				playerDone.put(player.getName(), false);

				player.sendMessage(ChatColor.GREEN + "Your block is " + ChatColor.WHITE + material.name());
				player.sendMessage("=======================");
			}
		}
		gameActive = true;
		data.getConfig().set("game.active", 1);
		data.saveConfig();
		return false;

	}
	
	public void prepareScoreboard() {
		Bukkit.getServer().getScoreboardManager().getMainScoreboard().registerNewObjective("BlockShuffle", "dummy", "BlockShuffle");
	}
	
	public void deleteScoreboard() {
		Bukkit.getServer().getScoreboardManager().getMainScoreboard().getObjective("BlockShuffle").unregister();
	}
	
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Location loc = event.getPlayer().getLocation().clone().subtract(0, 1, 0);
		Block b = loc.getBlock();
		Player player = (Player) event.getPlayer();
		if (gameActive == true) {
			if (b.getType().toString().equalsIgnoreCase(playersBlocks.get(player.getName()))) {

				if ( (activePlayers.contains(player.getName())) & (!(playerDone.get(player.getName()))) ) {
					

					
					Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN +" is done! Their block was " + ChatColor.WHITE + playersBlocks.get(player.getName()));
					
					playerDone.put(player.getName(), true);
					

					
					if ((allPlayersDone(event)) == true) {
						gameActive = false;

						
						activePlayers.clear();
						playerDone.clear();
						Bukkit.getScheduler().cancelTask(taskId);

						Bukkit.getServer().broadcastMessage(" ");
						Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "All players are done! Good job!");
						Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Type " + ChatColor.WHITE + "/start " + ChatColor.GOLD + "if you want to play again.");
						return;
					}
				}
				
				
			}

		}
	}
	
	public boolean allPlayersDone(PlayerMoveEvent event) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (activePlayers.contains(p.getName())) {
				if (!playerDone.get(p.getName())) {
					return false;
				}
			}
		}
		return true;
		
	}
	
	
	
	
	
	
	public void setblocks() {
		blocks.add(Material.STONE);
		blocks.add(Material.STONE_STAIRS);
		blocks.add(Material.STONE_BRICKS);
		
		blocks.add(Material.GRANITE);
		blocks.add(Material.POLISHED_GRANITE);
		blocks.add(Material.POLISHED_GRANITE_STAIRS);
		
		blocks.add(Material.DIORITE);
		blocks.add(Material.POLISHED_DIORITE);
		
		blocks.add(Material.ANDESITE);
		blocks.add(Material.POLISHED_ANDESITE);
		blocks.add(Material.POLISHED_ANDESITE_STAIRS);
		
		blocks.add(Material.GRASS_BLOCK);
		blocks.add(Material.DIRT);
		
		blocks.add(Material.COBBLESTONE);
		blocks.add(Material.COBBLESTONE_SLAB);
		blocks.add(Material.COBBLESTONE_STAIRS);
		blocks.add(Material.COBBLESTONE_WALL);
		
		blocks.add(Material.OAK_LEAVES);
		blocks.add(Material.OAK_LOG);
		blocks.add(Material.OAK_PLANKS);
		blocks.add(Material.OAK_STAIRS);
		blocks.add(Material.OAK_TRAPDOOR);
		
		blocks.add(Material.SPRUCE_LEAVES);
		blocks.add(Material.SPRUCE_LOG);
		blocks.add(Material.SPRUCE_PLANKS);
		blocks.add(Material.SPRUCE_STAIRS);
		blocks.add(Material.SPRUCE_TRAPDOOR);
		
		blocks.add(Material.BIRCH_LEAVES);
		blocks.add(Material.BIRCH_LOG);
		blocks.add(Material.BIRCH_PLANKS);
		blocks.add(Material.BIRCH_STAIRS);
		blocks.add(Material.BIRCH_TRAPDOOR);
		
		blocks.add(Material.DARK_OAK_LEAVES);
		blocks.add(Material.DARK_OAK_LOG);
		blocks.add(Material.DARK_OAK_PLANKS);
		blocks.add(Material.DARK_OAK_STAIRS);
		blocks.add(Material.DARK_OAK_TRAPDOOR);
		
		blocks.add(Material.BEDROCK);
		blocks.add(Material.SAND);
		blocks.add(Material.GRAVEL);
		
		blocks.add(Material.GOLD_ORE);
		blocks.add(Material.IRON_ORE);
		blocks.add(Material.COAL_ORE);
		blocks.add(Material.DIAMOND_ORE);
		blocks.add(Material.LAPIS_ORE);
		
		blocks.add(Material.GLASS);
		blocks.add(Material.OBSIDIAN);
		blocks.add(Material.BRICKS);
		blocks.add(Material.BRICK_STAIRS);
		blocks.add(Material.PUMPKIN);
		blocks.add(Material.MELON);
		blocks.add(Material.HAY_BLOCK);
		blocks.add(Material.TERRACOTTA);
		blocks.add(Material.COAL_BLOCK);
		
		
		blocks.add(Material.NETHERRACK);
		blocks.add(Material.SOUL_SAND);
		blocks.add(Material.SOUL_SOIL);
		
		
		blocks.add(Material.WHITE_WOOL);
		blocks.add(Material.ORANGE_WOOL);
		blocks.add(Material.MAGENTA_WOOL);
		blocks.add(Material.LIGHT_BLUE_WOOL);
		blocks.add(Material.YELLOW_WOOL);
		blocks.add(Material.PINK_WOOL);
		blocks.add(Material.GRAY_WOOL);
		blocks.add(Material.LIGHT_GRAY_WOOL);
		blocks.add(Material.CYAN_WOOL);
		blocks.add(Material.PURPLE_WOOL);
		blocks.add(Material.BLUE_WOOL);
		blocks.add(Material.BROWN_WOOL);
		blocks.add(Material.RED_WOOL);
		blocks.add(Material.BLACK_WOOL);
		
		blocks.add(Material.WHITE_STAINED_GLASS);
		blocks.add(Material.ORANGE_STAINED_GLASS);
		blocks.add(Material.MAGENTA_STAINED_GLASS);
		blocks.add(Material.LIGHT_BLUE_STAINED_GLASS);
		blocks.add(Material.YELLOW_STAINED_GLASS);
		blocks.add(Material.PINK_STAINED_GLASS);
		blocks.add(Material.GRAY_STAINED_GLASS);
		blocks.add(Material.LIGHT_GRAY_STAINED_GLASS);
		blocks.add(Material.CYAN_STAINED_GLASS);
		blocks.add(Material.PURPLE_STAINED_GLASS);
		blocks.add(Material.BLUE_STAINED_GLASS);
		blocks.add(Material.BROWN_STAINED_GLASS);
		blocks.add(Material.RED_STAINED_GLASS);
		blocks.add(Material.BLACK_STAINED_GLASS);
		
		blocks.add(Material.WHITE_CONCRETE);
		blocks.add(Material.ORANGE_CONCRETE);
		blocks.add(Material.MAGENTA_CONCRETE);
		blocks.add(Material.LIGHT_BLUE_CONCRETE);
		blocks.add(Material.YELLOW_CONCRETE);
		blocks.add(Material.PINK_CONCRETE);
		blocks.add(Material.GRAY_CONCRETE);
		blocks.add(Material.LIGHT_GRAY_CONCRETE);
		blocks.add(Material.CYAN_CONCRETE);
		blocks.add(Material.PURPLE_CONCRETE);
		blocks.add(Material.BLUE_CONCRETE);
		blocks.add(Material.BROWN_CONCRETE);
		blocks.add(Material.RED_CONCRETE);
		blocks.add(Material.BLACK_CONCRETE);
		
		blocks.add(Material.WHITE_CONCRETE_POWDER);
		blocks.add(Material.ORANGE_CONCRETE_POWDER);
		blocks.add(Material.MAGENTA_CONCRETE_POWDER);
		blocks.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
		blocks.add(Material.YELLOW_CONCRETE_POWDER);
		blocks.add(Material.PINK_CONCRETE_POWDER);
		blocks.add(Material.GRAY_CONCRETE_POWDER);
		blocks.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
		blocks.add(Material.CYAN_CONCRETE_POWDER);
		blocks.add(Material.PURPLE_CONCRETE_POWDER);
		blocks.add(Material.BLUE_CONCRETE_POWDER);
		blocks.add(Material.BROWN_CONCRETE_POWDER);
		blocks.add(Material.RED_CONCRETE_POWDER);
		blocks.add(Material.BLACK_CONCRETE_POWDER);
		
		blocks.add(Material.WHITE_TERRACOTTA);
		blocks.add(Material.ORANGE_TERRACOTTA);
		blocks.add(Material.MAGENTA_TERRACOTTA);
		blocks.add(Material.LIGHT_BLUE_TERRACOTTA);
		blocks.add(Material.YELLOW_TERRACOTTA);
		blocks.add(Material.PINK_TERRACOTTA);
		blocks.add(Material.GRAY_TERRACOTTA);
		blocks.add(Material.LIGHT_GRAY_TERRACOTTA);
		blocks.add(Material.CYAN_TERRACOTTA);
		blocks.add(Material.PURPLE_TERRACOTTA);
		blocks.add(Material.BLUE_TERRACOTTA);
		blocks.add(Material.BROWN_TERRACOTTA);
		blocks.add(Material.RED_TERRACOTTA);
		blocks.add(Material.BLACK_TERRACOTTA);
		
		blocks.add(Material.WHITE_GLAZED_TERRACOTTA);
		blocks.add(Material.ORANGE_GLAZED_TERRACOTTA);
		blocks.add(Material.MAGENTA_GLAZED_TERRACOTTA);
		blocks.add(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
		blocks.add(Material.YELLOW_GLAZED_TERRACOTTA);
		blocks.add(Material.PINK_GLAZED_TERRACOTTA);
		blocks.add(Material.GRAY_GLAZED_TERRACOTTA);
		blocks.add(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
		blocks.add(Material.CYAN_GLAZED_TERRACOTTA);
		blocks.add(Material.PURPLE_GLAZED_TERRACOTTA);
		blocks.add(Material.BLUE_GLAZED_TERRACOTTA);
		blocks.add(Material.BROWN_GLAZED_TERRACOTTA);
		blocks.add(Material.RED_GLAZED_TERRACOTTA);
		blocks.add(Material.BLACK_GLAZED_TERRACOTTA);
		
		blocks.add(Material.RED_BED);
		blocks.add(Material.BLACK_BED);
		blocks.add(Material.ORANGE_BED);
		blocks.add(Material.MAGENTA_BED);
		
		blocks.add(Material.REDSTONE_LAMP);
		blocks.add(Material.NOTE_BLOCK);
		blocks.add(Material.TNT);
		blocks.add(Material.PISTON);
		
		blocks.add(Material.HOPPER);
		blocks.add(Material.RAIL);
		
		blocks.add(Material.LOOM);
		blocks.add(Material.COMPOSTER);
		blocks.add(Material.BARREL);
		blocks.add(Material.SMOKER);
		blocks.add(Material.BLAST_FURNACE);
		blocks.add(Material.CARTOGRAPHY_TABLE);
		blocks.add(Material.FLETCHING_TABLE);
		blocks.add(Material.GRINDSTONE);
		blocks.add(Material.SMITHING_TABLE);
		blocks.add(Material.STONECUTTER);
		blocks.add(Material.BELL);
		blocks.add(Material.CAMPFIRE);
		
	}
	

}
