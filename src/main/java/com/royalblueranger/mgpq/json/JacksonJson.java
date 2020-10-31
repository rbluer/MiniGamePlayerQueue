package com.royalblueranger.mgpq.json;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonJson
{

	public static ObjectMapper getJsonObjectMapper()
	{
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
		om.enable(SerializationFeature.INDENT_OUTPUT);
		return om;
	}
	
	public static String createJsonString(Object obj)
	{
		ObjectMapper om = getJsonObjectMapper();
		String json = null;
		try
		{
			json = om.writeValueAsString( obj );
		}
		catch ( JsonProcessingException e )
		{
			e.printStackTrace();
		}
		return json;
	}

	
	public static <E> E readJsonString(String jsonString, Class<E> klass)
	{
		E results = null;
		
		if ( jsonString != null && jsonString.trim().length() > 0 )
		{
			ObjectMapper mapper = getJsonObjectMapper();
			
			try
			{
				results = mapper.readValue( jsonString, klass );
			}
			catch ( IOException e )
			{
				e.printStackTrace();
				
				System.err.println( "JacksonJson.readJsonString: ## Failed to process jsonString value, IOException. " + 
						e.getMessage() );
			}
			
		}
		else
		{
			System.err.println( "JacksonJson.readJsonString: ## Failed to process json String value since it is empty." );
		}
		
		return results;
	}

	
	/**
	 * <p>This function requires the File and a generated String (YAML or JSON), and then it will save the
	 * the contents to that file.</p>
	 * 
	 * <p>This function will initially save the contents to a .tmp file, and after it was successful in 
	 * saving to the file system, then it will delete the original and rename the new file.  This will 
	 * allow the original to remain if there was a failure on the writing of the file.</p>
	 * 
	 * @param file
	 * @param yaml
	 */
	public static void writeString(File file, String contents)
	{
		String tempFilename = file.getPath() + ".tmp";
		File tempFile = new File(tempFilename);
	
		if ( contents == null || "null".equalsIgnoreCase( contents ) )
		{
			Logger.getGlobal().log(Level.WARNING, "Failure: Contents is null. Tring to write to file: " + 
					file.getAbsolutePath() );
			
			return;
		}
		
		boolean success = false;
		try(
				PrintWriter out = new PrintWriter(tempFile);
			)
		{
			out.println(contents);
			
			success = true;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if ( success && tempFile.exists() )
		{
			if ( file.exists() )
			{
				file.delete();
			}
			tempFile.renameTo( file );
		}
	}
	
	
	public static <E> E readJsonFile(Path inputFile, Class<E> klass)
	{
		E results = null;
		
		if ( Files.exists( inputFile ) )
		{
			ObjectMapper mapper = getJsonObjectMapper();
			
			try
			{
				File file = inputFile.toFile();

			// NOTE: The use of utf-8 stream really does not change anything.  I'll keep this commented
			//       out for a while to make sure its not needed...
			
//			Charset utf8 = Charset.forName("UTF-8");
//			try (
//					InputStreamReader isr = new InputStreamReader( new FileInputStream( file ), utf8);
//				)
//			{
//				
//				results = mapper.readValue( isr, klass );
				results = mapper.readValue( file, klass );

				System.err.println( "JacksonJson.readJsonFile: Loaded file. " + inputFile.getFileName() );
			}
			catch ( IOException e )
			{
				e.printStackTrace();
				
				System.err.println( "JacksonJson.readJsonFile: ## Failed to load file, IOException. " + inputFile.getFileName() + "   " +
						e.getMessage() );

			}
		}
		else
		{
			System.err.println( "JacksonJson.readJsonFile: ## Failed to load file, it does not exist. " + inputFile.getFileName() );
		}
		
		return results;
	}
	
	/**
	 * <p>This function will take a Path target and write the provided object to a 
	 * temp file.  Then if it is successful, then it will delete the original file and 
	 * rename the temp file.
	 * </p>
	 * 
	 * @param file
	 * @param obj
	 */
	public static void writeJsonFile(Path outputFile, Object obj)
	{
		ObjectMapper mapper = getJsonObjectMapper();
		
		File file = outputFile.toFile();
		File tempFile = new File( file.getParentFile(), file.getName() + ".tmp");
		
		try
		{
			mapper.writeValue( tempFile, obj );
			
			if ( file.exists() )
			{
				long lastModified = file.lastModified();
				Date lmDate = new Date(lastModified);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String suffix = "_" + sdf.format( lmDate ) + ".json";
				String fileName = file.getName().replace( ".json", suffix );
				File renamedFile = new File( file.getParentFile(), fileName);
				file.renameTo( renamedFile );
				
				System.err.println( "JacksonJson.writeJsonFile: Existing file named to: " + renamedFile.getAbsolutePath() );
			}
			
			Files.deleteIfExists( outputFile );

			tempFile.renameTo( file );
			
			System.err.println( "JacksonJson.writeJsonFile: Saved file: " + file.getAbsolutePath() );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			System.err.println( "JacksonJson.writeJsonFile: ## Failed to saved file: " + 
					tempFile.getAbsolutePath() + " (exists = " + tempFile.exists() + ")   " +
					file.getAbsolutePath() + "  (exists = " + file.exists() + ")   " + 
						e.getMessage());
		}
		
	}
	
}
