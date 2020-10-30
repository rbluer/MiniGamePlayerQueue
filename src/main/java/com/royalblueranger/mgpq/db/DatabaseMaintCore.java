package com.royalblueranger.mgpq.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * <p>This DatabaseMaintCore should contain the most primitive functions that are used
 * for maintenance purposes that do not have reference to any plugin specific
 * table names or enums.</p>
 *
 * <p>NOTE: To ensure all resources are properly closed, will be using try-with-resources.
 *
 */
public class DatabaseMaintCore
{
	private SQLite db;

	public DatabaseMaintCore( SQLite db )
	{
		super();

		this.db = db;
	}

	public SQLite getDb()
	{
		return db;
	}


	protected boolean tableExists( String tableName )
	{
		boolean exists = false;


		try (
				Connection conn = db.getSQLConnection();
				ResultSet rs = conn.getMetaData().getTables( null, null, tableName, null );
			)
		{
			exists = rs.next();  // Just make sure there is one or more results...
			// rs.last();
			// exists = rs.getRow() > 0;
		}
		catch ( SQLException e )
		{
			db.getPlugin().getLogger().log( Level.SEVERE,
					"Couldn't verify if a table exists: [" + tableName + "]: ", e );
		}
		return exists;
	}

	/**
	 * <p>This function, when passed the tableName, will create a generic version table
	 * so this app can track the overall version of the database tables.  The version is
	 * a simple integer and when there is a change that forces the creation of a new
	 * table or the modification of an existing table, then the version should be
	 * incremented.</p>
	 *
	 * <p>To use it, when initializing the database, you just check to see what version
	 * the tables are when starting up the app, and if it is less than the current version,
	 * then you check to see if anything should happen.</p>
	 *
	 * @param tableName
	 * @return
	 */
	protected boolean createTable( String sql )
	{
		return sqlExecute( sql, "Couldn't create table" );
	}


	protected boolean insertVersion( String tableName, int version )
	{
		String sql = "insert into " + tableName +
				" default values;";

		return sqlExecute( sql, "Couldn't insert default values" );
	}

	private boolean sqlExecute( String sql, String errorMessage )
	{
		boolean success = false;

		try (
				Connection conn = db.getSQLConnection();
				PreparedStatement ps = conn.prepareStatement( sql );
			)
		{

			ps.execute();
			success = true;

			// Note: Success is implied if an exception is not thrown for table creations.
			// Note: ps.execute() returns boolean but it does not mean success or failure.
			// Note: ps.getUpdateCount() does not indicate success on table creation.
		}
		catch ( SQLException e )
		{
			db.getPlugin().getLogger().log( Level.SEVERE, errorMessage + " [" + sql + "]: ", e );
		}
		return success;
	}

	protected int getTableVersion( String tableName )
	{
		int version = 0;
		String sql = "select count(*) as count from " + tableName;

		try (
				Connection conn = db.getSQLConnection();
				PreparedStatement ps = conn.prepareStatement( sql );
				ResultSet rs = ps.executeQuery();
			)
		{
			if ( rs.next() )
			{
				version = rs.getInt( "count" );
			}
		}
		catch ( SQLException e )
		{
			db.getPlugin().getLogger().log( Level.SEVERE, "Couldn't get database version [" + sql + "]: ", e );
		}
		return version;
	}

	/**
	 * <p>Creates the table and inserts the first record.</p>
	 * @param tableName
	 * @return
	 */
	public String tableDefinitionTableVersion( String tableName )
	{
		StringBuilder sb = new StringBuilder();

		sb.append( "create table if not exists " );
		sb.append( tableName );
		sb.append( " ( " );
		sb.append(   "version integer default 1 " );
		sb.append( "); \n" );

		return sb.toString();
	}

}