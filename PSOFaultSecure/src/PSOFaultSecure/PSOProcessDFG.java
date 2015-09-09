/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

/**
 *
 * @author SAPVI
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.sql.*;
import java.util.Enumeration;

public class PSOProcessDFG implements PSOConstants {
	private Vector<Particle> swarm = new Vector<Particle>();
        private Vector<Particle> swarm2 = new Vector<Particle>();
	private double[] pBest;// = new double[gc.swarm_size];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
        private double[] fitnessValueList;// = new double[gc.swarm_size];
	public static Vector <Object> operation;
        public static String detail="";
        public static int vel_h,vel_l;
	Random generator = new Random();
	 Connection con;
         Statement st;
         ResultSet rs;
         GlobalConstants gc;
        
         ProblemSetDFG psdfg;
         int[] lastgbest;
         public PSOProcessDFG(){
             gc = GlobalConstants.getInstance();
             pBest = new double[gc.swarm_size];
             fitnessValueList = new double[gc.swarm_size];
             psdfg= new ProblemSetDFG();
             lastgbest=new int[gc.size];
         }
	public void execute() {
                //PSOProcess pp=this.pp;
		
                //readDFG();
                //read_detail();
                initializeSwarm();
		updateFitnessList();
		//vel_l=-()
		for(int i=0; i<gc.swarm_size; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int t = 0;
		double w=0.0;
		//int err = 9999;
                int count=0;
                int terminate=11;
                boolean gbflag=false,flag_vel=false;
		
		while(t < gc.no_iteration && count<terminate)
                {//&& err > ProblemSet.ERR_TOLERANCE) {
			// step 1 - update pBest
			for(int i=0; i<gc.swarm_size; i++) {
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(pBest);
			/*if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}*/
                        System.out.println("Gbest= "+gBest);
                        System.out.println("Pbest= "+pBest[bestParticleIndex]);
                        if(t == 0 || pBest[bestParticleIndex] < gBest) {
                                int[] loc1 = new int[gc.size];
                
                                    for(int j=0;j<gc.size;j++){
                                    loc1[j]=pBestLocation.get(bestParticleIndex).getLoc()[j];
                                 
                                    }
                                Location location1 = new Location(loc1);
				gBest = pBest[bestParticleIndex];
				gBestLocation = location1;
                        }
                  gc.gbestcost.add(gBest);  //add gbest cost in the list
                         
                 int r[]=new int[gc.size];
            	 for(int j=0;j<gc.size;j++){
		r[j] = gBestLocation.getLoc()[j];
                 }
                 //get location
                int gloc=0;
                for(int i=0;i<gc.size;i++){
                 int temp=1;
                 for(int j=i+1;j<gc.size;j++){
                    temp *= (gc.max[j]);
                    }
                gloc +=(r[i]-1)*temp;
                }
                if(gc.power_texe){
                gc.gbestpower.add(gc.power.get(gloc)); //add gbest power in the list
                }
                if(gc.area_texe){
                gc.gbestarea.add(gc.area.get(gloc)); //add gbest power in the list
                }
                gc.gbestexetime.add(gc.exetime.get(gloc)); //add gbest exetime in the list
                        // new gbest technique
			//w=1.0;
			w = W_UPPERBOUND - (((double) (gc.no_iteration - t)) / gc.no_iteration) * (W_UPPERBOUND - W_LOWERBOUND);
			//System.out.println("value of w= "+w);
			for(int i=0; i<gc.swarm_size; i++) {
				//double r1 = generator.nextDouble();
				//double r2 = generator.nextDouble();
                                double r1=0.5;
                                double r2=0.5;
				//System.out.println("value of r1= "+r1+"value of r2= "+r2);
				Particle p = swarm.get(i);
				
				// step 3 - update velocity
				int[] newVel = new int[gc.size];
                                for(int j=0;j<gc.size;j++){
				newVel[j] = (int) ((w*p.getVelocity().getPos()[j]) + 
                                                               (r1 * gc.c1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
                                                               (r2 * gc.c2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]));
				/*newVel[j] = (int) ((w * p.getVelocity().getPos()[j]) + 
                                                               (r1 * C1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
                                                               (r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]));
				*/
                                if(newVel[j]<(-gc.vel_range[j]))
                                    newVel[j]=(-gc.vel_range[j]);
                               // if(newVel[1]<ProblemSet.VEL_LOW)
                               //     newVel[1]=ProblemSet.VEL_LOW;
                                if(newVel[j]>gc.vel_range[j])
                                    newVel[j]=gc.vel_range[j];
                               // if(newVel[1]>ProblemSet.VEL_HIGH)
                               //     newVel[1]=ProblemSet.VEL_HIGH;
                                //System.out.println("new Velocity   "+newVel[j]+"  range= "+gc.vel_range[j]);
                                }
                                
                                Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
				// step 4 - update location
				int[] newLoc = new int[gc.size];
                                for(int j=0;j<gc.size;j++){
				newLoc[j] = p.getLocation().getLoc()[j] + newVel[j];
				
                                gc.x2=gc.x1;
                                gc.y2=gc.y1;
                                gc.x1=newLoc[0];
                                gc.y1=newLoc[1];
                                
                                //**** function for perturbation
                               // if(newLoc[j]<ProblemSet.LOC_X_LOW)
                                 //   newLoc[j]=ProblemSet.LOC_X_LOW+generator.nextInt(ProblemSet.LOC_X_HIGH-1);
                                //if(newLoc[1]<ProblemSet.LOC_Y_LOW)
                                  //  newLoc[1]=ProblemSet.LOC_Y_LOW+generator.nextInt(ProblemSet.LOC_Y_HIGH-1);;
                               // if(newLoc[j]>ProblemSet.LOC_X_HIGH)
                                //    newLoc[j]=ProblemSet.LOC_X_LOW+generator.nextInt(ProblemSet.LOC_X_HIGH-1);;
                                //if(newLoc[1]>ProblemSet.LOC_Y_HIGH)
                                 //   newLoc[1]=ProblemSet.LOC_Y_LOW+generator.nextInt(ProblemSet.LOC_Y_HIGH-1);;
                                }
                                newLoc=perturbation(newLoc);
                                    Location loc = new Location(newLoc);
				p.setLocation(loc);
                                //System.out.println("new location   "+newLoc[0]+"  "+newLoc[1]);
			}
			
			//err = (int) (ProblemSet.evaluate(gBestLocation) - 0); // minimizing the functions means it's getting closer to 0
			
			
			System.out.println("ITERATION " + t + ": ");
                        for(int j=0; j<gc.size;j++){
			System.out.println("     Best R"+j+": " + gBestLocation.getLoc()[j]);
			///System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
                        
                         }
                        
                        // check for gbest changes
                        gbflag=false;
                        for(int j=0; j<gc.size;j++){
                            if(lastgbest[j]!=gBestLocation.getLoc()[j])
                                gbflag=true;
                        }
                        if(gbflag){
                         for(int j=0; j<gc.size;j++){
                            lastgbest[j]=gBestLocation.getLoc()[j];
                         }
                         count=0;
                        }
                        else
                        {
                            count++;
                            if(count==2)
                                gc.conTime = System.currentTimeMillis();
                            //System.out.println("Count=  "+count);
                        }
                       /*
                        
                        // termination with the help of vel = 0
                        flag_vel=false;
                        
                         for(int l=0; l<gc.swarm_size; l++) {
                         Particle p = swarm.get(l);
                         for(int k=0; k<gc.size;k++){
                        if(p.getVelocity().getPos()[k]!=0){
                        flag_vel=true;
                        }
                        }
                         }
                        if(flag_vel){
                        count=0;
                        }
                        else{
                        count++;
                        }
                         if(count==2)
                                gc.conTime = System.currentTimeMillis();
                        
                        */                      
			//System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));
			
			t++;
                        // code for mutation
                        
                       /* for(int x=0;x<gc.swarm_size;x++){
                            for(int j=0; j<gc.size;j++){
                                System.out.println("  locations for particle"+x+"  "+j+" = "+pBestLocation.get(x).getLoc()[j]);
                            }
                        }*/
                       if(t < gc.no_iteration-1 && count<(terminate-2))//&& t%3==0)
                       mutation(t);
                        
			updateFitnessList();
		}
                for(int j=0; j<gc.size;j++){
                detail=detail+(t)+"  "+gBestLocation.getLoc()[j]+"   ";//+gBestLocation.getLoc()[1]+"  ";
                }
                //insert into database
              /*  try {
                     
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");      
            String url="jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:/Users/SAPVI/Documents/PSODSE.accdb;}";
            con=DriverManager.getConnection(url);
            st = con.createStatement();
             //System.out.println("Connection stablish");
             
             st.executeUpdate("INSERT INTO result (detail) VALUES('"+detail+"');");
           //System.out.println("Row is added"+detail);
            con.close();
         } catch (Exception e) {
            System.out.println("!!!Exception in extablishing DB connection. " + e.toString());
        }   */
                gc.result_comp=new <Object>Vector();
                System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
		for(int j=0; j<gc.size;j++){
			System.out.println(gc.component_name[j]+"= " + gBestLocation.getLoc()[j]);
                        gc.result_comp.add(gc.component_name[j]);
                        gc.result_comp.add(gBestLocation.getLoc()[j]);
                        gc.output=gc.output+gc.component_name[j]+"= " + gBestLocation.getLoc()[j]+"\n";
			///System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
                         }
		
		//System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
		//System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
	}
	
	public void initializeSwarm() {
		//Particle p;
                //int[] loc;
                //Location location;
                
            //gc.output=gc.output+"Initialization of particles\n";
                Particle p1 = new Particle(); 
                int[] loc1 = new int[gc.size];
                
                for(int j=0;j<gc.size;j++){
                    loc1[j]=1;
                    lastgbest[j]=1;
                }
                Location location1 = new Location(loc1);
                int[] vel1 = new int[gc.size];
			for(int j=0;j<gc.size;j++){
                        vel1[j]=0;
                        }
			Velocity velocity1 = new Velocity(vel1);
			
			p1.setLocation(location1);
			p1.setVelocity(velocity1);
			swarm.add(p1);
                        
                        // Particle 2 initialization
                        
                Particle p2 = new Particle(); 
                int[] loc2 = new int[gc.size];
                
                for(int j=0;j<gc.size;j++){
                    loc2[j]=gc.max[j];
                }
                Location location2 = new Location(loc2);
                        int[] vel2 = new int[gc.size];
			for(int j=0;j<gc.size;j++){
                        vel2[j]=0;
                        }
			Velocity velocity2 = new Velocity(vel2);
			
			p2.setLocation(location2);
			p2.setVelocity(velocity2);
			swarm.add(p2);
                     
                        Particle p3 = new Particle(); 
                int[] loc3 = new int[gc.size];
                
                for(int j=0;j<gc.size;j++){
                    loc3[j]=(1+gc.max[j])/2;
                }
                Location location3 = new Location(loc3);
                int[] vel3 = new int[gc.size];
			for(int j=0;j<gc.size;j++){
                        vel3[j]=0;
                        }
			Velocity velocity3 = new Velocity(vel3);
			
			p3.setLocation(location3);
			p3.setVelocity(velocity3);
			swarm.add(p3);
                        
                 
                        
		for(int i=0; i<gc.swarm_size-3; i++) {
		Particle p = new Particle();
			//gc.output=gc.output+"particle"+i+"\n"+"Resources are";
			// randomize location inside a space defined in Problem Set
			int[] loc = new int[gc.size];
			//loc[0] = ProblemSet.LOC_X_LOW + generator.nextInt(4) * (ProblemSet.LOC_X_HIGH - ProblemSet.LOC_X_LOW);
			//loc[1] = ProblemSet.LOC_Y_LOW + generator.nextInt(3) * (ProblemSet.LOC_Y_HIGH - ProblemSet.LOC_Y_LOW);
			
                        for(int j=0;j<gc.size;j++){
                            //System.out.println("size= "+gc.size);
                            if(i%2==0)
                        loc[j] = (1+gc.max[j])/2 + generator.nextInt((gc.max[j]/2));
                      else
                                loc[j] = (1+gc.max[j])/2 - generator.nextInt((gc.max[j]/2));
			//loc[1] = ProblemSet.LOC_Y_LOW + generator.nextInt(3);
                        //detail=detail+" "+loc[j]+" ";
                        //gc.output=gc.output+" "+loc[j]+" ";
                        if(j==0)
                        gc.x1=loc[j];
                        else
                         gc.y1=loc[j];
                        }
                          //loc[0]=1;
                          //loc[1]=1;
                          Location location = new Location(loc);
                
		
			// randomize velocity in the range defined in Problem Set
			int[] vel = new int[gc.size];
			//vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			//vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
                       //gc.output=gc.output+"\nMovement  ";
                        for(int j=0;j<gc.size;j++){
                        vel[j]=0;
                        //vel[1]=0;
                        //gc.output=gc.output+" "+vel[j]+"  ";
                        }
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);	
                        		}
	}
	
	public void updateFitnessList() {
            
                for(int i=0; i<gc.swarm_size; i++) {
                    
                    ProblemSetDFG ps2=new ProblemSetDFG(); 
			fitnessValueList[i] = ps2.evaluate(swarm.get(i).getLocation());
            }
	 }
        public void mutation(int t){
            if(t%2==0){
            for(int i=0;i<gc.swarm_size;i++){
            for(int j=0; j<gc.size-1;j++){
                        int temp=pBestLocation.get(i).getLoc()[j];
                        if(pBestLocation.get(i).getLoc()[j+1]<gc.max[j])
			pBestLocation.get(i).getLoc()[j]=pBestLocation.get(i).getLoc()[j+1];                            
                        if(pBestLocation.get(i).getLoc()[j]<gc.max[j+1])
			pBestLocation.get(i).getLoc()[j+1]=temp;
                         }   
        }
            }
            else {
         for(int i=0;i<gc.swarm_size;i++){
         for(int j=0; j<gc.size;j++){
             if(i%2==0){
                 if(pBestLocation.get(i).getLoc()[j]>1 && j%2==0){
                     //System.out.println("BM= "+pBestLocation.get(i).getLoc()[j]);
                  pBestLocation.get(i).getLoc()[j]=pBestLocation.get(i).getLoc()[j]-1;
                 // System.out.println("AM= "+pBestLocation.get(i).getLoc()[j]);
             }
                 if(pBestLocation.get(i).getLoc()[j]<gc.max[j] && j%2==1){
                    // System.out.println("BM= "+pBestLocation.get(i).getLoc()[j]);
               pBestLocation.get(i).getLoc()[j]=pBestLocation.get(i).getLoc()[j]+1;
              // System.out.println("AM= "+pBestLocation.get(i).getLoc()[j]);
             }
             }
             else {
                 if(pBestLocation.get(i).getLoc()[j]<gc.max[j] && j%2==0){
                    // System.out.println("BM= "+pBestLocation.get(i).getLoc()[j]);
                  pBestLocation.get(i).getLoc()[j]=pBestLocation.get(i).getLoc()[j]+1;
                 // System.out.println("AM= "+pBestLocation.get(i).getLoc()[j]);
             }
                 if(pBestLocation.get(i).getLoc()[j]>1 && j%2==1){
                    // System.out.println("BM= "+pBestLocation.get(i).getLoc()[j]);
               pBestLocation.get(i).getLoc()[j]=pBestLocation.get(i).getLoc()[j]-1;
               //System.out.println("AM= "+pBestLocation.get(i).getLoc()[j]);
             }
             }
         }
         }
            }
            
                for(int i=0;i<gc.swarm_size;i++){
                    pBest[i]=psdfg.evaluate(pBestLocation.get(i));
            
            }
                    /*for(int x=0;x<gc.swarm_size;x++){
                            for(int j=0; j<gc.size;j++){
                                System.out.println("  locations for particle"+x+"  "+j+" = "+pBestLocation.get(x).getLoc()[j]);
                            }
                        }*/
        
        }
        public int[] perturbation(int l[]){
            for(int j=0;j<gc.size;j++){
            if(l[j]<1)
            l[j]=1+generator.nextInt(gc.max[j]-1);
                if(l[j]>gc.max[j])
                    l[j]=1+generator.nextInt(gc.max[j]-1);
            }
            
            /*
            for(int i=0;i<l.length;i++){
                int x=generator.nextInt(5);
                for(int j=0;j<x;j++){
                    if(l[i]<1){
                        l[i]++;
                    }
                    if(l[i]>gc.max[i]){
                        l[i]--;
                        break;
                    }
                }
                for(int j=0;j<x;j++){
                    if(l[i]>gc.max[i]){
                        l[i]--;
                    }
                    if(l[i]<1){
                        l[i]++;
                        break;
                    }
                }
                
            }*/
            return l;
        }
     
     
}
  
