package com.royalblueranger.blues.bstats;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Prison bstats ID = 657
 * HyperLoreUtils bstats ID = 15642
 * MiniGamePlayerQueue bstats ID = 15645
 * Quoteological bstats ID = 15646
 * 
 * 
 * @author Blue
 *
 */
public class InitializeBstats {
	
	private JavaPlugin plugin;
	private int pluginId;
	
	private Metrics bStatsMetrics = null;
	
	
	public InitializeBstats( JavaPlugin plugin, int pluginId ) {
		super();
		
		this.plugin = plugin;
		this.pluginId = pluginId;
		
		initialize();
	}
	
	
	
	private void initialize() {
		
    	bStatsMetrics = new Metrics( getPlugin(), getPluginId() );
		
    	setupCustomCharts();
	}

	
	/**
	 * This contains all of the custom charts to be used.
	 * 
	 * The examples are from Prison and have been commented out and are only here as references.
	 * 
	 */
	private void setupCustomCharts() {
		
//        // Report the modules being used
//        SimpleBarChart sbcModulesUsed = new SimpleBarChart("modules_used", () -> {
//            Map<String, Integer> valueMap = new HashMap<>();
//            for (Module m : PrisonAPI.getModuleManager().getModules()) {
//                valueMap.put(m.getName(), 1);
//            }
//            return valueMap;
//        });
//        bStatsMetrics.addCustomChart( sbcModulesUsed );
//
//        // Report the API level
//        SimplePie spApiLevel = 
//                new SimplePie("api_level", () -> 
//                	"API Level " + Prison.API_LEVEL + "." + Prison.API_LEVEL_MINOR );
//        bStatsMetrics.addCustomChart( spApiLevel );
//        
//        
//        Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
//        Optional<Module> prisonRanksOpt = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
//        
//        int mineCount = prisonMinesOpt.map(module -> ((PrisonMines) module).getMineManager().getMines().size()).orElse(0);
//        int rankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getRankCount()).orElse(0);
//        
//        int defaultRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getDefaultLadderRankCount()).orElse(0);
//        int prestigesRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getPrestigesLadderRankCount()).orElse(0);
//        int otherRankCount = rankCount - defaultRankCount - prestigesRankCount;
//        
//        int ladderCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getladderCount()).orElse(0);
//        int playerCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getPlayersCount()).orElse(0);
//        
//        MultiLineChart mlcMinesRanksAndLadders = 
//        		new MultiLineChart("mines_ranks_and_ladders", new Callable<Map<String, Integer>>() {
//            @Override
//            public Map<String, Integer> call() throws Exception {
//                Map<String, Integer> valueMap = new HashMap<>();
//                valueMap.put("mines", mineCount);
//                valueMap.put("ranks", rankCount);
//                valueMap.put("ladders", ladderCount);
//                valueMap.put("players", playerCount);
//                return valueMap;
//            }
//        });
//        bStatsMetrics.addCustomChart( mlcMinesRanksAndLadders );
//        
//        MultiLineChart mlcPrisonRanks = new MultiLineChart("prison_ranks", new Callable<Map<String, Integer>>() {
//        	@Override
//        	public Map<String, Integer> call() throws Exception {
//        		Map<String, Integer> valueMap = new HashMap<>();
//        		valueMap.put("ranks", rankCount);
//        		valueMap.put("defaultRanks", defaultRankCount);
//        		valueMap.put("prestigesRanks", prestigesRankCount);
//        		valueMap.put("otherRanks", otherRankCount);
//        		return valueMap;
//        	}
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonRanks );
//        
//        
//        MultiLineChart mlcPrisonladders = new MultiLineChart("prison_ladders", new Callable<Map<String, Integer>>() {
//        	@Override
//        	public Map<String, Integer> call() throws Exception {
//        		Map<String, Integer> valueMap = new HashMap<>();
//        		
//        		PrisonRanks pRanks = (PrisonRanks) prisonRanksOpt.orElseGet( null );
//        		for ( RankLadder ladder : pRanks.getLadderManager().getLadders() ) {
//        	
//        			valueMap.put( ladder.getName(), ladder.getRanks().size() );
//        		}
//        		
//        		return valueMap;
//        	}
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonladders );
//
//        
//        DrilldownPie mlcPrisonPlugins = new DrilldownPie("plugins", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	TreeMap<String, RegisteredPluginsData> plugins = Prison.get().getPrisonCommands().getRegisteredPluginData();
//        	
//        	for (String pluginName : plugins.keySet() ) {
//        		RegisteredPluginsData pluginData = plugins.get( pluginName );
//				
//        		Map<String, Integer> entry = new HashMap<>();
//        		entry.put( pluginData.getPluginVersion(), 1 );
//        		
//        		map.put( pluginData.getPluginName(), entry );
//			}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPlugins );

	}


	public JavaPlugin getPlugin() {
		return plugin;
	}
	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public Metrics getbStatsMetrics() {
		return bStatsMetrics;
	}
	public void setbStatsMetrics(Metrics bStatsMetrics) {
		this.bStatsMetrics = bStatsMetrics;
	}

	public int getPluginId() {
		return pluginId;
	}
	public void setPluginId(int pluginId) {
		this.pluginId = pluginId;
	}
	
}

