package com.royalblueranger.mgpq.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.royalblueranger.mgpq.RBRPlugIn;

/**
 * <h1>Trash</h1>
 *
 * Note this is a basic JUNK class... I don't want to totally delete these function yet,
 * but I don't think I will use them either.
 *
 * @author RBR
 *
 */
public class DatabaseMisc
		extends SQLite
{
	private String table = "table_name_goes_here";

	public DatabaseMisc( RBRPlugIn plugin, String databaseName )
	{
		super( plugin, databaseName );
	}


	/*
	 * Came from the Database.java example.
	 */
	public void initialize()
	{
		try (
				Connection connection = getSQLConnection();
				PreparedStatement ps = connection.prepareStatement( "SELECT * FROM " + table + " WHERE player = ?" );
				ResultSet rs = ps.executeQuery();
			)
		{

		}
		catch ( SQLException ex )
		{
			getPlugin().getLogger().log( Level.SEVERE, "Unable to retreive connection", ex );
		}
	}



	// These are the methods you can use to get things out of your database. You
	// of course can make new ones to return different things in the database.
	// This returns the number of people the player killed.
	public Integer getTokens( String player )
	{
		int kills = 0;
		try (
				Connection conn = getSQLConnection();
				PreparedStatement ps = conn.prepareStatement( "SELECT * FROM " + table + " WHERE player = ?;" );
			)
		{
			ps.setString( 1, player );

			try (
					ResultSet rs = ps.executeQuery();
				)
			{
				
				while ( rs.next() )
				{
					if ( rs.getString( "player" ).equalsIgnoreCase( player.toLowerCase() ) )
					{
						
						kills = rs.getInt( "kills" );
					}
				}
			}
		}
		catch ( SQLException ex )
		{
			getPlugin().getLogger().log( Level.SEVERE, "Couldn't execute db statement: ", ex );
		}
		return kills;
	}

	// Exact same method here, Except as mentioned above i am looking for total!
	public Integer getTotal( String player )
	{
		int total = 0;
		try (
				Connection conn = getSQLConnection();
				PreparedStatement ps = conn.prepareStatement( "SELECT * FROM " + table + " WHERE player = ?;" );
			)
		{
			ps.setString( 1, player );

			try (
					ResultSet rs = ps.executeQuery();
				)
			{
				while ( rs.next() )
				{
					if ( rs.getString( "player" ).equalsIgnoreCase( player.toLowerCase() ) )
					{
						total = rs.getInt( "total" );
					}
				}
			}
		}
		catch ( SQLException ex )
		{
			getPlugin().getLogger().log( Level.SEVERE, "Couldn't execute db statement: ", ex );
		}

		return total;
	}

	// Now we need methods to save things to the database
	public void setTokens( Player player, Integer tokens, Integer total )
	{
		try (
				Connection conn = getSQLConnection();
				PreparedStatement ps = conn.prepareStatement( "REPLACE INTO " + table + " (player,kills,total) VALUES(?,?,?)" );
			)
		{
			ps.setString( 1, player.getName().toLowerCase() );

			ps.setInt( 2, tokens );
			ps.setInt( 3, total );
			ps.executeUpdate();
			return;
		}
		catch ( SQLException ex )
		{
			getPlugin().getLogger().log( Level.SEVERE, "Couldn't execute db statement: ", ex );
		}
		return;
	}

}
