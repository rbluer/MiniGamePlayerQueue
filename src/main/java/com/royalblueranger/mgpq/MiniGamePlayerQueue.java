package com.royalblueranger.mgpq;

import com.royalblueranger.blues.bstats.InitializeBstats;
import com.royalblueranger.mgpq.command.CommandsMgpqCore;

/**
 * 
 * @author RoyalBlueRanger
 *
 */

public class MiniGamePlayerQueue
	extends RBRPlugIn
{
	public static final int BSTATS_ID__MINIGAMEPLAYERQUEUE = 15645;
	
	private static MiniGamePlayerQueue instance;
	
	@SuppressWarnings("unused")
	private InitializeBstats bstats;
	

	/**
	 * <p>Warning: constructor is public only so the normal plugin manager can instantiate the class.
	 * Never ever use this, but instead use getInstance() if you need to use this class since
	 * there can only be one instance of this class in the VM.
	 */
	public MiniGamePlayerQueue()
	{
		super();

		if ( MiniGamePlayerQueue.instance == null ) {
			
			MiniGamePlayerQueue.instance = this;
		}
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
    	super.onEnable();
    	
    	// Register all classes that contains commands. They can always be accessed 
    	// through getCommandHandler().getRegisteredCommands() with the cmdGroup.
    	getCommandHandler().registerCommands( new CommandsMgpqCore() );
    	
    	


//    	getServer().getPluginManager().registerEvents( this, this );

    	
		bstats = new InitializeBstats( this, BSTATS_ID__MINIGAMEPLAYERQUEUE );
    }

    /**
     *  Fired when plugin is disabled
     */
    @Override
    public void onDisable()
    {
    	


    	// Shut down the core components after items registered in this class
    	super.onDisable();
    }


	public static void setInstance( MiniGamePlayerQueue instance )
	{
		MiniGamePlayerQueue.instance = instance;
	}

}
