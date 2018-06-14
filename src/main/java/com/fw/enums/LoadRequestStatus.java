package com.fw.enums;
/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum LoadRequestStatus {
	// 
	DELIVERED("DELIVERED"),CANCELLED("CANCELLED"),BLOCKED_REQUEST("BLOCKED_REQUEST"),BIDDING_IN_PROGRESS("BIDDING_IN_PROGRESS"),BOOKED("BOOKED"),
	REQUESTED("REQUESTED"),AWARDED("AWARDED"),REQUEST_ON_HOLD("REQUEST_ON_HOLD"),EXPIRED("EXPIRED"),RESUME("RESUME");
	
    private final String dbString;

    private LoadRequestStatus(String dbString ) {
        this.dbString = dbString;
    }

    public String getValue () {
        return this.dbString;
    }

    public static LoadRequestStatus fromString(String str ) {
        if( "DELIVERED".equalsIgnoreCase(str) ) {
            return( DELIVERED );
        }
        else if( "CANCELLED".equalsIgnoreCase(str) ) {
            return(CANCELLED );
        }
        else if( "BLOCKED_REQUEST".equalsIgnoreCase(str) ) {
            return( BLOCKED_REQUEST );
        }
        else if( "BIDDING_IN_PROGRESS".equalsIgnoreCase(str) ) {
            return( BIDDING_IN_PROGRESS );
        }
        else if( "BOOKED".equalsIgnoreCase(str) ) {
            return( BOOKED );
        } 
        else if( "REQUESTED".equalsIgnoreCase(str) ) {
            return( REQUESTED );
        } 
        else if( "AWARDED".equalsIgnoreCase(str) ) {
            return( AWARDED );
        } 
        else if( "EXPIRED".equalsIgnoreCase(str) ) {
            return( EXPIRED );
        } 
        else if( "REQUEST_ON_HOLD".equalsIgnoreCase(str) ) {
            return( REQUEST_ON_HOLD );
        } 
        else if( "RESUME".equalsIgnoreCase(str) ) {
            return( RESUME );
        } 
        else {
            throw new RuntimeException("No such Status type:" + str );
        }
    }
    
}
