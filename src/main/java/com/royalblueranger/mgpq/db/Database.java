package com.royalblueranger.mgpq.db;

import java.sql.Connection;

import com.royalblueranger.mgpq.RBRPlugIn;

/**
 * <h1>Keep</h1>
 *
 * <p>This is the "core" functionality of database tools.  It should be generic and not
 * locked in to SQLite or MYSql.</p>
 *
 * <p>Roughly based upon the example found at https://www.spigotmc.org/threads/how-to-sqlite.56847/
 * to provide direction and hints on how to setup and deal with SQLite databases.  Basically how things
 * have changed is that spigot now includes the drivers for MySQL and SQLite so you don't have to
 * setup anything with the jars anymore.  Made extensive use of refactoring to make the database
 * code more stable and extensible; based on past experiences with java and relational databases.</p>
 *
 */
public abstract class Database
{
	private RBRPlugIn plugin;
	private String dbname;

	public Database( RBRPlugIn plugin, String databaseName )
	{
		super();
		this.plugin = plugin;

		this.dbname = plugin.getConfig().getString( "SQLite.filename", databaseName );

		registerDatabaseDriver();

		// Check to make sure the database has been initialized before and all tables exist:
		configureDatabase();
	}



	/**
	 * <p>This function gets called first. It creates the empty file in the file system if
	 * it does not exist and registers the db drivers.</p>
	 */
	protected abstract void registerDatabaseDriver();

	/**
	 * <p>This function is supposed to check to see if the tables exist, and if not, create
	 * them.  It should also apply any database conversion needed to ensure the
	 * database tables are at the current version.</p>
	 */
	protected abstract void configureDatabase();


	public abstract Connection getSQLConnection();


	public RBRPlugIn getPlugin()
	{
		return plugin;
	}

	public String getDbname()
	{
		return dbname;
	}
}
