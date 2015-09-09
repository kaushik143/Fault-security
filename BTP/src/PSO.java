import java.util.Collections;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;




class Particle
{
	double lbcost;
	Vector<Double> curposition = new Vector<Double>();
	Vector<Double> velocity = new Vector<Double>();
	Vector<Double> lbposition = new Vector<Double>();//local best position
}



public class PSO 
{
	static Vector<Double> gbposition = new Vector<Double>();//global best position
	static Vector<Double> minposition=new Vector<Double>();
	static Vector<Double> maxposition=new Vector<Double>();
	static Vector<Double> position1,position2,position3;
	static Vector<Integer> ufList=new Vector<Integer>();
	
	static Boolean noimprovement=false,equilibriumreached=false;
	static double gbcost=Double.MAX_VALUE,gwcost=Double.MIN_VALUE;
	static Vector<Particle> particles;
	static int dim,I,numUF;	//I is the user specified value
	static double consfactor=0.729;
	static Particle gwcandidate;
	
	
	 public static double getCauchy() 
	 {
		 return 0.5+ + Math.atan( (new Random()).nextDouble()) / Math.PI;
	 }
	 
	 public static double getGaussian()
	 {
		 return  (new Random()).nextGaussian(); //mean 0 variance 1
	 }

	public static void main(String[] args) 
	{

		PSOProcess();

	}
	
	static void PSOProcess()
	{
		initialize();
		process();
		for(int i=0;i<dim;i++)
		{
			if(i==dim-1)
				System.out.print(ufList.get((int)Math.floor(gbposition.get(i)))  + "  ");
			else 
				System.out.print(gbposition.get(i)+"  ");
		
		}
		
		System.out.println("Cost is "+ gbcost);
	}
	
	static void initialize()//sets initial velocity, positions, lbposition, gbposition
	{
		String inp;
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Prakhar Sharma\\workspace1\\BTP\\input3.txt"));
			
			inp=br.readLine();//no. of particles
			int numParticles=Integer.parseInt(inp);
			particles = new Vector<Particle>();
			
			inp=br.readLine();//dim of particles
			dim=Integer.parseInt(inp);
			
			I=Integer.parseInt(br.readLine());	//Iterations like 36 in paper			
			fillUF();
			
			  

			inp=br.readLine();// minimum configuration
			String units[]=inp.split(" ");
			for(int i=0;i<dim;i++)
				minposition.add(Double.parseDouble(units[i]));
			
			
			inp=br.readLine();// maximum configuration
			units=inp.split(" ");
			for(int i=0;i<dim;i++)
				maxposition.add(Double.parseDouble(units[i]));
			
			//for including UF range
			dim++;	
			minposition.add(0.0);
			maxposition.add((double)numUF-1);
			
			for(int i=0;i<dim;i++)gbposition.add(0.0);
			position1=new Vector<Double>(dim);position2=new Vector<Double>(dim);position3=new Vector<Double>(dim);
			for(int i=0;i<dim;i++)		//initialization of positions 
			{
				position1.add(0.0);position2.add(0.0);position3.add(0.0);
			}
			
			
			
			for(int j=0;j<numParticles;j++)
			{
				Random rand= new Random();
				Particle candidate=new Particle();
				for(int i=0;i<dim;i++)candidate.lbposition.add(0.0);
				
				for(int i=0;i<dim;i++)
				{
					if(j==0)//first particle
					{	
						candidate.curposition.add(minposition.get(i));			
					}
					else if(j==1)
					{
						candidate.curposition.add(maxposition.get(i));
					}
					else if(j==2)
					{
						candidate.curposition.add(Math.floor((maxposition.get(i)+minposition.get(i))/2));
					}
					else
					{
						candidate.curposition.add(rand.nextInt((int)(maxposition.get(i)-minposition.get(i))+1) +minposition.get(i));
					}

					candidate.velocity.add(0.0);//initial velocity
				}
				
				//calculate fitness
				candidate.lbcost=evaluate(candidate.curposition);
				for(int i=0;i<dim;i++)
				candidate.lbposition.set(i,candidate.curposition.get(i));
				
				if(gwcost<candidate.lbcost)
				{
					gwcost=candidate.lbcost;
					gwcandidate=candidate;
				}
				if(gbcost>candidate.lbcost)
				{
					System.out.println("GB CHANGED");
					gbcost=candidate.lbcost;
					for(int i=0;i<dim;i++)
					gbposition.set(i, candidate.curposition.get(i));
					System.out.println("1GBCOST "+ gbcost);
					System.out.println("1GBPOS "+ gbposition);
				}
				
				particles.add(candidate);
				//System.out.println(candidate.curposition);
				
			}
			
			
			
			br.close();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	
	}
	
	static Vector<Double> process() //return global best position //optimal configuration
	{
		int iter=0,numParticle=0;
		int gwnumber=0;
		double newcost=0;
		int icount=0;
		noimprovement=equilibriumreached=false;
		Boolean isbetter=false;
		while(iter<100 && !noimprovement && !equilibriumreached)
		{
			equilibriumreached=true;
			numParticle=0;
			
			gwcost=Double.MIN_VALUE;
			gwcandidate=particles.get(0);
			
			gwnumber=0;
			for(Particle candidate : particles)
			{
				numParticle++;
				for(int i=0;i<dim;i++)
				{
					candidate.velocity.set(i,
											consfactor*(candidate.velocity.get(i)+
											2.05*Math.random()*(candidate.lbposition.get(i)-candidate.curposition.get(i))+
											2.05*Math.random()*(gbposition.get(i)-candidate.curposition.get(i)))
										   );

					
					// Velocity Clamping										

					if(candidate.velocity.get(i)>(minposition.get(i)+maxposition.get(i))/2)
						candidate.velocity.set(i,(minposition.get(i)+maxposition.get(i))/2);
					
					if(candidate.velocity.get(i)<-(minposition.get(i)+maxposition.get(i))/2)
						candidate.velocity.set(i,-(minposition.get(i)+maxposition.get(i))/2);
					
				
					candidate.velocity.set(i, Math.floor(candidate.velocity.get(i)));/// so that position has integral coordinates
					candidate.curposition.set(i, candidate.curposition.get(i)+candidate.velocity.get(i));

					//End terminal perturbation		
					
					if(candidate.curposition.get(i)>maxposition.get(i))
						candidate.curposition.set(i, maxposition.get(i));
					else if(candidate.curposition.get(i)<minposition.get(i))
						candidate.curposition.set(i, minposition.get(i));
			
					
					if(Math.abs(candidate.velocity.get(i))>0)equilibriumreached=false;

				}
				
				//System.out.println("Velocity "+candidate.velocity);
				
		//		System.out.println("Position "+candidate.curposition);
			
				
				
				
				newcost=evaluate(candidate.curposition);
				
				if(gwcost<newcost)
				{
					gwcost=newcost;
					gwcandidate=candidate;
					gwnumber=numParticle;
				}
				
				//update local best position and local best cost
				if(newcost<=candidate.lbcost)
				{
					candidate.lbcost=newcost;			//candidate's best cost calculated till now
					for(int i=0;i<dim;i++)
					candidate.lbposition.set(i,candidate.curposition.get(i));	
					
					
					//update global best
					if(gbcost>candidate.lbcost)
					{
						icount=0;
						gbcost=candidate.lbcost;
						for(int i=0;i<dim;i++)
						gbposition.set(i,candidate.curposition.get(i));
						//System.out.println("GBCOST "+ gbcost);
						//System.out.println("GBPOS "+ gbposition);
					}
				}	
				
				
				
			}	
			
			
			if(iter%2==0)		//mutation probability 0.5
			{
				double tempcost=evaluate(gwcandidate.curposition);
				//System.out.println("Position before " +gwcandidate.curposition);
				//System.out.println("trying mutation");
				isbetter=mutate(gwcandidate,gwcost,gwnumber);
				
				if(isbetter)
				{
					gwcost=evaluate(gwcandidate.curposition);
//					System.out.println("Mutation applied ");
//					System.out.println(tempcost +"  to   "+ gwcost);
//					System.out.println("Position after " +gwcandidate.curposition);
					
				}
				
			}
			
			icount++;
			if(icount>=100)noimprovement=true;
			iter++;
		}
		
//		for(int i=0;i<dim;i++)
//			System.out.println(gbposition.get(i) + " ");
		System.out.println("Iteration count "+iter);
		return gbposition;
	}
	
	static Boolean mutate(Particle candidate,double cost,int numParticle)//	returns true and changes particles configuration else returns false
	{
		double cost1=rotate(candidate,cost,numParticle);	//returns Double.MAX_VALUE if not useful else returns the reduced cost and sets the position1
		
		if(cost1<cost)
		{	
			for(int i=0;i<dim;i++)
				candidate.curposition.set(i, position1.get(i));
			return true;
		}
		else return false; 	
/*		
		double cost2=cauchy(candidate,cost);	//returns Double.MAX_VALUE if not useful else returns the reduced cost and sets the position2
		double cost3=gaussian(candidate,cost);	//returns Double.MAX_VALUE if not useful else returns the reduced cost and sets the position3
		
		//System.out.println("Costs "+ cost1+"  "+cost2 +"  "+ cost3);
		if(cost1<=cost2&&cost1<=cost3&&cost1<cost)
		{
			for(int i=0;i<dim;i++)
			candidate.curposition.set(i, position1.get(i));
			return true;
		}
			
		else if(cost2<=cost1&&cost2<=cost3&&cost2<cost)
		{
			for(int i=0;i<dim;i++)
				candidate.curposition.set(i, position2.get(i));
				
			return true;
		}
			
		else if(cost3<=cost1&&cost3<=cost2&&cost3<cost)
		{
			for(int i=0;i<dim;i++)
				candidate.curposition.set(i, position3.get(i));
				
			return true;
		}
			
		return false;
*/
	}
	
	static double rotate(Particle candidate, double cost,int numParticle)
	{
		Vector<Double> position4,position5;
		position4=new Vector<Double>(dim);
		position5=new Vector<Double>(dim);
		
		for(int i=0;i<dim;i++)
		{
			position4.add(0.0);position5.add(0.0);
		}
		for(int i=0;i<dim;i++)
		{
			position4.set(i,candidate.curposition.get(i));
			position5.set(i,candidate.curposition.get(i));
		
		}
		
		
		if(numParticle%2==3)//left rotation
		{
			position4.set(0,position4.get(dim-1));	
			for(int i=1;i<dim;i++)			
			{
				position4.set(i,position4.get(i-1));
			}
			
			//===========================================================================================================
			//End terminal perturbation	
			for(int i=0;i<dim;i++)
			{
				if(position4.get(i)>maxposition.get(i))
					position4.set(i, maxposition.get(i));
				else if(position4.get(i)<minposition.get(i))
					position4.set(i, minposition.get(i));
			}
			//===========================================================================================================
			double cost1=evaluate(position4);
			
			if(cost1<cost)
			{
				for(int i=0;i<dim;i++)
					position1.set(i,position4.get(i));
				return cost1;
			}
			else return Double.MAX_VALUE;
		}		
		
		else   		//increment decrement
		{
		
			for(int i=0;i<dim;i++)
			{
				if(i%2!=0)
					position5.set(i, position5.get(i)+4);
				else
					position5.set(i, position5.get(i)-4);				
			}
			
			//===========================================================================================================
			//End terminal perturbation	
			for(int i=0;i<dim;i++)
			{
				if(position5.get(i)>maxposition.get(i))
					position5.set(i, maxposition.get(i));
				else if(position5.get(i)<minposition.get(i))
					position5.set(i, minposition.get(i));
			}
			//===========================================================================================================
					
			double cost2=evaluate(position5);
			
			if(cost2<cost)
			{
				for(int i=0;i<dim;i++)
					position1.set(i,position5.get(i));					
				return cost2;
			}
			else return Double.MAX_VALUE;
		}
		
	}
	
	
	
	static double cauchy(Particle candidate,double cost)
	{
		
		Vector<Double> velocity1 =new Vector<Double>(dim);
		for(int i=0;i<dim;i++)
		{
			velocity1.add(0.0);
		}
		for(int i=0;i<dim;i++)velocity1.set(i,candidate.velocity.get(i));
		
		
		Vector<Double> position6=new Vector<Double>(dim);
		
		for(int i=0;i<dim;i++)
		{
			position6.add(0.0);
		}
		for(int i=0;i<dim;i++)position6.set(i,candidate.curposition.get(i));
		
		for(int i=0;i<dim;i++)
		{
			velocity1.set(i,velocity1.get(i)*Math.exp(PSO.getCauchy()));
		}
		//===============================================================================================================
		// Velocity Clamping
		
		for(int i=0;i<dim;i++)
		{
			if(velocity1.get(i)>(minposition.get(i)+maxposition.get(i))/2)
				velocity1.set(i,(minposition.get(i)+maxposition.get(i))/2);
		
			if(velocity1.get(i)<-(minposition.get(i)+maxposition.get(i))/2)
				velocity1.set(i,-(minposition.get(i)+maxposition.get(i))/2);
		}
		//===============================================================================================================
		
		for(int i=0;i<dim;i++)	//position update
		{
			position6.set(i,position6.get(i)+ velocity1.get(i)*Math.exp(PSO.getCauchy()));
		}
		
		//===========================================================================================================
		//End terminal perturbation	
		for(int i=0;i<dim;i++)
		{
			if(position6.get(i)>maxposition.get(i))
				position6.set(i, maxposition.get(i));
			else if(position6.get(i)<minposition.get(i))
				position6.set(i, minposition.get(i));
		}
		//===========================================================================================================

		double cost1=evaluate(position6);
		if(cost>cost1)
		{
			for(int i=0;i<dim;i++)
			position2.set(i,position6.get(i));
			return cost1;
			
		}
		return Double.MAX_VALUE;
		
	}

	static double gaussian(Particle candidate,double cost)
	{
		Vector<Double> velocity1 =new Vector<Double>(dim);
		for(int i=0;i<dim;i++)
		{
			velocity1.add(0.0);
		}
		for(int i=0;i<dim;i++)velocity1.set(i,candidate.velocity.get(i));
		
		
		Vector<Double> position6=new Vector<Double>(dim);
		for(int i=0;i<dim;i++)
		{
			position6.add(0.0);
		}
		for(int i=0;i<dim;i++)position6.set(i,candidate.curposition.get(i));
		
		for(int i=0;i<dim;i++)
		{
			velocity1.set(i,velocity1.get(i)*Math.exp(PSO.getGaussian()));
		}
		
		//===============================================================================================================
		// Velocity Clamping
		
		for(int i=0;i<dim;i++)
		{
			if(velocity1.get(i)>(minposition.get(i)+maxposition.get(i))/2)
				velocity1.set(i,(minposition.get(i)+maxposition.get(i))/2);
		
			if(velocity1.get(i)<-(minposition.get(i)+maxposition.get(i))/2)
				velocity1.set(i,-(minposition.get(i)+maxposition.get(i))/2);
		}
		//===============================================================================================================
				
		for(int i=0;i<dim;i++)
		{
			position6.set(i,position6.get(i)+ velocity1.get(i)*Math.exp(PSO.getGaussian()));
		}

		//===========================================================================================================
		//End terminal perturbation	
		for(int i=0;i<dim;i++)
		{
			if(position6.get(i)>maxposition.get(i))
				position6.set(i, maxposition.get(i));
			else if(position6.get(i)<minposition.get(i))
				position6.set(i, minposition.get(i));
		}
		//===========================================================================================================
		
		double cost1=evaluate(position6);
		if(cost>cost1)
		{
			for(int i=0;i<dim;i++)
			position3.set(i,position6.get(i));
			return cost1;
		}
		return Double.MAX_VALUE;
	}

	
	static double evaluate(Vector<Double> position)
	{
		
	/*	costfunction1
		double prod=1.0;
		for(int i=0;i<dim;i++)
		{
			if(i==dim-1)
				prod*=ufList.get((int) (Math.floor(position.get(i))));
			else 
				prod*=position.get(i);
		}
		return prod;
	*/
		
		//costfunction2
		int value=1;
		for(int i=0;i<dim;i++)
		{
			if(i==dim-1)
				value=lcm(value,ufList.get((int) (Math.floor(position.get(i)))));
				
			else 
				value=lcm(value,(int) (Math.floor(position.get(i))));
		}
		return (double)value;
	
		
		
		
	}
	

	static void fillUF()
	{
		numUF=0;
		for( int uf =2 ;uf<=I/2;uf++)
		{
			if(I%uf<uf/2)
			{
				ufList.add(uf);numUF++;
			}
		}
		
		for(int uf=2;uf<=I;uf++)
		{
			if(I%uf<uf/2)break;			
			else
			{
				ufList.add(uf);numUF++;			
			}
				
		}
		
		Collections.sort(ufList);
		
		//System.out.println(ufList);
	}
	
	static int lcm(int a, int b)
	{
		return a*b/gcd(a,b);
	}
	static int gcd(int a , int b)
	{
		if(b==0)return a;
		else 
		return gcd(b,a%b); 
	}
	
}



