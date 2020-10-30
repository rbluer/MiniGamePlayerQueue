package com.royalblueranger.mgpq.db;

import java.io.File;

import org.bukkit.ChatColor;


/**
 * <p>The function of this class is to provide a lot of the heavy work that would happen
 * when first starting the plugin that will use these tables.  The reason behind this class
 * is that once all is confirmed to be configured properly, then this class can be
 * dropped from memory, thus freeing up more memory with the the other classes that will
 * deal with basic behavior with the transactions.</p>
 *
 */
public class DatabaseMaint
	extends DatabaseMaintCore
{
	private int databaseVersionCurrent = 1;

	public DatabaseMaint( SQLite db )
	{
		super( db );
	}

	//TODO move this enum out to the main database class.... once it is created.

	/**
	 * <p>This contains the list of all the tables that are to be used in this plugin.
	 * None of these table names that are referenced within this enum should ever be
	 * hard-coded elsewhere.</p>
	 *
	 * <p>Generally I always use schemas with relational databases, but with SQLite a
	 * schema is actually an attached database that is created with the command
	 * <code>attach database as rbr</code>.  Therefore this is not needed, but it could
	 * be used for database backups or migrations etc..</p>
	 *
	 * <p>Simple overview:</p>
	 * <ul>
	 *   <li>rbr_mob_grinder_version: The database table version file. Used to keep track of table changes to apply updates automatically when needed.</li>
	 *   <li>rbr_mob_grinder: The primary table. Contains the stuff that is pretty static.</li>

	 *   <li>rbr_mob_grinder_costs: This table tracks the expenses and credits used to operate the grinder.</li>
	 *   <li>rbr_mob_grinder_materials: This table tracks the materials used to feed the grinder.</li>
	 *   <li>rbr_mob_grinder_mobs: This table tracks the mobs that were spawned and used.</li>
	 *
	 *   <li>rbr_mob_grinder_events: This tracks all the major events associated with the grinder.</li>
	 *
	 *   <li>rbr_mob_grinder_: </li>
	 *
	 * </ul>
	 *
	 * @author TAB6HG
	 *
	 */
	public enum DbTables
	{
		RBR_MOB_GRINDER_VERSION("rbr_mob_grinder_version"),  // Created OK - No insert

		RBR_MOB_GRINDER("rbr_mob_grinder"),

		RBR_MOB_GRINDER_COSTS("rbr_mob_grinder_costs"), // Created OK
		RBR_MOB_GRINDER_MATERIALS("rbr_mob_grinder_materials"),  // Created OK
		RBR_MOB_GRINDER_MOBS("rbr_mob_grinder_mobs"),  // Created OK

		RBR_MOB_GRINDER_EVENTS("rbr_mob_grinder_events"),
		;

		private final String dbName;
		private DbTables( String dbName )
		{
			this.dbName = dbName;
		}
		public String getDbName()
		{
			return dbName;
		}
	}

	/*
	 * <p>The purpose of this function is to perform a check on the tables to make sure
	 * they are there, if not, then create them.  Also, check the version of all the
	 * tables; if in the future we need to migrate the database to a newer format we
	 * can use the table version to control them.</p>
	 *
	 * <p>This function should only be ran once during the plugin initialization.</p>
	 *
	 */
	public void checkTables( File dbFile )
	{

		for ( DbTables table : DbTables.values() )
		{
			boolean exists = tableExists( table.getDbName() );

			// if not the create the table:
			if ( !exists )
			{
				createTable( table );
			}
		}

		// Check the version of the tables:
		int version = getTableVersion( DbTables.RBR_MOB_GRINDER_VERSION.getDbName() );

		if ( version != databaseVersionCurrent )
		{
			getDb().getPlugin().log(
					ChatColor.RED + "The database table version is out of date.  Needs to be updated." );
		}

		// Handle the different versions, may need to progressively go through different
		// migrations to get to the current version.
		if ( version == 3 )
		{
			// do something here on all tables... example add new columns to a few tables.
		}
	}

	private boolean createTable( DbTables table )
	{
		boolean success = false;
		String sql = null;

		switch ( table )
		{
			case RBR_MOB_GRINDER_VERSION:
				sql = tableDefinitionTableVersion( DbTables.RBR_MOB_GRINDER_VERSION.getDbName() );
				break;

			case RBR_MOB_GRINDER:
				sql = tableDefinitionRbrMobGrinder();
				break;


			case RBR_MOB_GRINDER_COSTS:
				sql = tableDefinitionRbrMobGrinderCosts();
				break;

			case RBR_MOB_GRINDER_MATERIALS:
				sql = tableDefinitionRbrMobGrinderMaterials();
				break;

			case RBR_MOB_GRINDER_MOBS:
				sql = tableDefinitionRbrMobGrinderMobs();
				break;


			case RBR_MOB_GRINDER_EVENTS:
				sql = tableDefinitionRbrMobGrinderEvents();
				break;

			default:
				break;
		}

		if ( sql != null )
		{
			success = createTable( sql );

			getDb().getPlugin().log(
					ChatColor.WHITE + "SQLite table " + table.getDbName() + " was " +
							(success ? "" : ChatColor.RED + "not " + ChatColor.WHITE) +
					"created. " );
			getDb().getPlugin().logDebug( ChatColor.GREEN + sql );

			if ( success && table == DbTables.RBR_MOB_GRINDER_VERSION )
			{
				success = insertVersion( DbTables.RBR_MOB_GRINDER_VERSION.dbName, databaseVersionCurrent);
			}
		}

		return success;
	}


	/**
	 * <p>The rbr_mob_grinder table is the primary record that is tied to a given player.  It contains all the information
	 * about the location of the grinder.  The intended purpose of this table is that it should never change unless
	 * the grinder is removed or rebuilt.  The ownership and location of the grinder is of the highest concern and
	 * I do not want to have frequently ran code changing this table with minor updates a few times per hour.</p>
	 *
	 * <p>The field created has the format: Date format: YYYY-MM-DD HH:MM:SS.SSS</p>
	 *
	 * @return
	 */
	private String tableDefinitionRbrMobGrinder()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( "create table if not exists " );
		sb.append( DbTables.RBR_MOB_GRINDER.getDbName() );
		sb.append( " ( " );
		sb.append(   "grinder_id integer primary key, " ); // alias to rowid
		sb.append(   "uuid varchar(36) not null unique, " );
		sb.append(   "name varchar(100) not null, " );

		sb.append(   "chunk_world varchar(32) not null, " );
		sb.append(   "chunk_x int not null, " );
		sb.append(   "chunk_z int not null, " );
		sb.append(   "chunk_y int not null, " );

		sb.append(   "created text " );  // SQLite date field.
//		sb.append(   "created text default datetime('now'), " );
//		sb.append(   "primary key (uuid asc) " );
		sb.append( "); " );

		return sb.toString();
	}

	/**
	 * <p>This costs table contains data that will be frequently updated. Such as current credits assigned to the grinder,
	 * the total mob xp levels, and any bonuses that may be applied.  It also contains slightly less frequently updated
	 * data about the grinder such as the grinder's level and the kind of mob that it is currently generating.</p>
	 *
	 * @return
	 */
	private String tableDefinitionRbrMobGrinderCosts()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( "create table if not exists " );
		sb.append( DbTables.RBR_MOB_GRINDER_COSTS.getDbName() );
		sb.append( " ( " );
		sb.append(   "grinder_id int not null, " );
//		sb.append(   "uuid varchar(36), " );

		sb.append(   "grinder_level int not null, " );
		sb.append(   "mob_type varchar(30) not null, " );

		// Credit related fields:
		sb.append(   "credits decimal(15,5) default 0.0, " );
		sb.append(   "credits_bonus decimal(15,2) default 0.0, " );

		// XP related fields:
		sb.append(   "xp int default 0, " );
		sb.append(   "xp_bonus int default 0, " );

		sb.append(   " " );
		sb.append(   "primary key (grinder_id asc), " );
		sb.append(   "foreign key (grinder_id) references " );
		sb.append(       DbTables.RBR_MOB_GRINDER.getDbName() );
		sb.append(       " (grinder_id) ON DELETE CASCADE ON UPDATE NO ACTION " );
		sb.append( "); " );

		return sb.toString();
	}

	private String tableDefinitionRbrMobGrinderMaterials()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( "create table if not exists " );
		sb.append( DbTables.RBR_MOB_GRINDER_MATERIALS.getDbName() );
		sb.append( " ( " );
		sb.append(   "grinder_id int not null, " );
		sb.append(   "material_type varchar(30), " );

		sb.append(   "count int default 0, " );
		sb.append(   "credits decimal(15,5) default 0.0, " );

		sb.append(   " " );
		sb.append(   "primary key (grinder_id asc, material_type asc), " );
		sb.append(   "foreign key (grinder_id) references " );
		sb.append(       DbTables.RBR_MOB_GRINDER.getDbName() );
		sb.append(       " (grinder_id) ON DELETE CASCADE ON UPDATE NO ACTION " );
		sb.append( "); " );

		return sb.toString();
	}

	private String tableDefinitionRbrMobGrinderMobs()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( "create table if not exists " );
		sb.append( DbTables.RBR_MOB_GRINDER_MOBS.getDbName() );
		sb.append( " ( " );
		sb.append(   "grinder_id int not null, " );
		sb.append(   "mob_type varchar(30), " );

		sb.append(   "count int default 0, " );
		sb.append(   "credits decimal(15,5) default 0, " );

		sb.append(   " " );
		sb.append(   "primary key (grinder_id asc, mob_type asc), " );
		sb.append(   "foreign key (grinder_id) references " );
		sb.append(       DbTables.RBR_MOB_GRINDER.getDbName() );
		sb.append(       " (grinder_id) ON DELETE CASCADE ON UPDATE NO ACTION " );
		sb.append( "); " );

		return sb.toString();
	}

	/**
	 *
	 * Date format: YYYY-MM-DD HH:MM:SS.SSS
	 *
	 * @return
	 */
	private String tableDefinitionRbrMobGrinderEvents()
	{
		StringBuilder sb = new StringBuilder();

		sb.append( "create table if not exists " );
		sb.append( DbTables.RBR_MOB_GRINDER_EVENTS.getDbName() );
		sb.append( " ( " );
		sb.append(   "grinder_id int not null, " );
		sb.append(   "created text, " );
		sb.append(   "event_type varchar(30) not null, " );

		sb.append(   "event varchar(200), " );
		sb.append(   "misc varchar(20), " );

		sb.append(   " " );
		sb.append(   "primary key (grinder_id asc, created asc), " );
		sb.append(   "foreign key (grinder_id) references " );
		sb.append(       DbTables.RBR_MOB_GRINDER.getDbName() );
		sb.append(       " (grinder_id) ON DELETE CASCADE ON UPDATE NO ACTION " );
		sb.append( "); " );

		return sb.toString();
	}
}
