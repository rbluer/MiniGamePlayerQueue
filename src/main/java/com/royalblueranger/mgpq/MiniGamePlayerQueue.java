package com.royalblueranger.mgpq;

/**
 *
 * Ideas for working with mobs:
 *    https://github.com/NerdNu/MobLimiter/blob/master/src/nu/nerd/moblimiter/EntityHelper.java
 *
 *
 * Note: metadata does not persist.  Need to use an internal mechanism to keep track of the locations
 * of the various grinders and their stats.
 *
 * YamlConfiguration.set("index", Map<List<?, ?>> and YamlConfiguration#getMapList<?, ?>
 *
 * @author RoyalBlueRanger
 *
 */

public class MiniGamePlayerQueue
	extends RBRPlugIn
{
	private static MiniGamePlayerQueue instance;

	/**
	 * <p>Warning: constructor is public only so the normal plugin manager can instantiate the class.
	 * Never ever use this, but instead use getInstance() if you need to use this class since
	 * there can only be one instance of this class in the VM.
	 */
	public MiniGamePlayerQueue()
	{
		super();

		MiniGamePlayerQueue.instance = this;
	}

	/**
	 * <p>Please note that this plugin DOES not follow the standard
	 * singleton pattern! The tooling framework that controls the plugins
	 * helps to ensure that it only gets instantiated once, so trust is placed
	 * heavily there.  Just in case the plugin was not instantiated I'm keeping
	 * the typical singleton code for getting the instance; I expect and hope
	 * it will never be used since that would imply a failure of another sort
	 * within the plugin manager.</p>
	 *
	 * @return
	 */
	public static MiniGamePlayerQueue getInstance()
	{
		if ( instance == null )
		{
			synchronized ( MiniGamePlayerQueue.class )
			{
				if ( instance == null )
				{
					instance = new MiniGamePlayerQueue();
				}
			}
		}
		return instance;
	}


    @Override
    public void onEnable()
    {
    	log( "Initializing..." );

    	initializeDb( "MiniGamePlayerQueue" );


//    	getServer().getPluginManager().registerEvents( this, this );


//    	this.getCommand("RBRTpGrinder").setExecutor(new CommandRBRTpGrinder());



        // Load all existing grinders:
        //grinders = getDb().loadAllGrinders();

    }

    /**
     *  Fired when plugin is disabled
     */
    @Override
    public void onDisable()
    {

    	// Flush any unsaved database data:

    	log( "Finished shutting down." );

    }


	public static void setInstance( MiniGamePlayerQueue instance )
	{
		MiniGamePlayerQueue.instance = instance;
	}

}
