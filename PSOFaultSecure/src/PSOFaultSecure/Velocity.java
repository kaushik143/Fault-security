/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

/**
 *
 * @author SAPVI
 */

// bean class to represent velocity

public class Velocity {
	// store the Velocity in an array to accommodate multi-dimensional problem space
	private int[] vel;

	public Velocity(int[] vel) {
		super();
		this.vel = vel;
	}

	public int[] getPos() {
		return vel;
	}

	public void setPos(int[] vel) {
		this.vel = vel;
	}
	
}
  

