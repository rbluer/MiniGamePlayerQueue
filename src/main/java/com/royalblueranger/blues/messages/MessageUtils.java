package com.royalblueranger.blues.messages;

public class MessageUtils {
	
    public static final char COLOR_CHAR = '\u00A7';
    private static final String COLOR_ = String.valueOf(COLOR_CHAR);
    
    public static String translateAmpColorCodes(String text) {
    	String results = text;
    	
    	if ( text != null && text.contains( "&" ) ) {
    		results = text.replaceAll( "&", COLOR_ );
    	}
    	
    	return results;
    }

    public static String convertToAmpColorCodes( String textEncoded ) {
    	
    	String results = textEncoded;
    	
    	if ( textEncoded != null && textEncoded.contains( COLOR_ ) ) {
    		results = textEncoded.replaceAll( COLOR_, "&" );
    	}
    	
    	return results;
    }
}
