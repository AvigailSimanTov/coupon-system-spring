package couponsystem.utilities;

import couponsystem.entities.Client;

public class CouponSystemUtils {
	
	public static boolean between(int numberToCheck, int smallerNumber, int greaterNumber) {
		if (numberToCheck >= smallerNumber && numberToCheck <= greaterNumber) {
			return true;
		}
		
		return false;
	}
	
	public static void classifyPassword(Client client) {
		client.setPassword("Classified");
	}
	
}