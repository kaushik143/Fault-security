/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

/**
 *
 * @author SAPVI
 */
public class Particle {
  	private double fitnessValue;
	private Velocity velocity;
	private Location location;
	
	public Particle() {
		super();
	}

	public Particle(int fitnessValue, Velocity velocity, Location location) {
		super();
		this.fitnessValue = fitnessValue;
		this.velocity = velocity;
		this.location = location;
	}

	public Velocity getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getFitnessValue() {
                ProblemSetDFG psf=new ProblemSetDFG();
		fitnessValue = psf.evaluate(location);
		return fitnessValue;
	}
}
  

