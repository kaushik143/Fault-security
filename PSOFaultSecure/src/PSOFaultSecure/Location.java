/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

/**
 *
 * @author SAPVI
 */
public class Location {
 
	// store the Location in an array to accommodate multi-dimensional problem space
	private int[] loc;

	public Location(int[] loc) {
		super();
		this.loc = loc;
	}

	public int[] getLoc() {
		return loc;
	}

	public void setLoc(int[] loc) {
		this.loc = loc;
	}
	
}
   

