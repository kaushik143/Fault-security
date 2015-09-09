/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

/**
 *
 * @author SAPVI
 */



// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 3;
	int MAX_ITERATION = 30;
	int PROBLEM_DIMENSION = 2;
	int C1 = 2;
	int C2 = 2;
	double W_UPPERBOUND = 0.4;
	double W_LOWERBOUND = 0.9;
}
  

