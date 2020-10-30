package com.royalblueranger.mgpq.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.sqlite.SQLiteConfig;

import com.royalblueranger.mgpq.RBRPlugIn;

/**
 * <h1>Keep</h1>
 *
 * <p>This </p>
 * @author RBR
 *
 */
public class SQLite
		extends Database
{
	private File dbFile;

	public SQLite( RBRPlugIn plugin, String databaseName )
	{
		super( plugin, databaseName );
	}


	public Connection getSQLConnection()
	{
		String connStr = "jdbc:sqlite:" + dbFile.getPath();

		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection( connStr );
		}
		catch ( SQLException ex )
		{
			getPlugin().getLogger().log( Level.SEVERE, "SQLite exception on initialize: [" + connStr + "]", ex );
		}
//		catch ( ClassNotFoundException ex )
//		{
//			plugin.getLogger().log( Level.SEVERE, "Missing the SQLite JBDC library; place jar in classpath." );
//		}
		return conn;
	}


	/**
	 * This function will create the SQLite database file if it does not exist, including any
	 * missing directories.  It will then register the SQLite JDBC driver for use later on.
	 */
	@Override
	protected void registerDatabaseDriver()
	{
		File dbPath = getPlugin().getDataFolder();
		this.dbFile = new File( dbPath, getDbname() + ".db" );
		if ( !dbFile.exists() )
		{
			try
			{
				if ( !dbPath.exists() )
				{
					dbPath.mkdirs();
				}
				dbFile.createNewFile();
				getPlugin().log( ChatColor.WHITE + "SQLite database was just created: " + dbFile.getPath() );
			}
			catch ( IOException e )
			{
				getPlugin().getLogger().log( Level.SEVERE,
						"File write error: " + getDbname() +
						".db  Could not create the initial database." );
			}
		}
		getPlugin().log( ChatColor.WHITE + "SQLite database file location: " + dbFile.getPath() + "  size: " + dbFile.length() );


		if ( getPlugin().getConfig().getBoolean( "SQLite.enforceForeignKeys", false ) )
		{
			SQLiteConfig conf = new SQLiteConfig();
			conf.enforceForeignKeys(true);
		}


		try
		{
			Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
			DriverManager.registerDriver(driver);
			getPlugin().log( "Registered the SQLite database driver. " );
		}
		catch(Exception e)
		{
			getPlugin().getLogger().log( Level.SEVERE, "Failed to Registered the SQLite database driver. " +
						e.getMessage(), e );
		}
	}

	/**
	 * <p>The simplicity of this function prevents a lot of one-time-use functions from remaining loaded
	 * in memory while the plugin runs.  Once the tables are checked all of the related clases will be
	 * GC'd.</p>
	 *
	 */
	@Override
	protected void configureDatabase()
	{
		DatabaseMaint dbMaint = new DatabaseMaint( this );

		dbMaint.checkTables( dbFile );
	}


//	public TreeMap<Chunk,GrinderData> loadAllGrinders()
//	{
//		TreeMap<Chunk,GrinderData> results = new TreeMap<>();
//
//		return results;
//	}

}
