/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;



import java.util.Vector;

/**
 *
 * @author SAPVI
 */
public class GlobalConstants {
    final int type=5;
    public Vector<Object> operation;
     
   // public Vector<Object> resource;
    public Vector<Object> max_component_detail;
    public double component_area[];
    //public double component_spower[];
    public double component_dpower[];
    public int component_clockcycle[];
    public String component_name[];
    public int max[];
    public int vel_range[];
    public double PowerConstraint=0.0;
    public double AreaConstraint=0.0;
    public double TexeConstraint=0.0;
    boolean area_texe=false;
    boolean power_texe=false;
    public String operator[];
    public String output="";
    public String output2="";    
    public int size=0;              //dimension
    public double w1=0.5,w2=0.5;
    public double c1=2,c2=2;
    public int setofdata=1000;
    public int x1=1,y1=1,x2=1,y2=1;
    public int no_operation; 
    public int no_iteration=15;
    public int swarm_size=3;
    public double mux_dpower=0.0;
    public double mux_area=0;
    public int mux_delay=0;
    public String schedule="";
    public Vector<Object> result_comp;
    public int[] lastgbest;
    public long conTime=0;
    Vector<Integer> UFList;
    public boolean CDFG=false;
   public int predictcount=0;
   public int kc=1;
    //public double power[][];
   // public double exetime[][];
    public Vector<Double> cost; //list of cost for all combination of resources
    public Vector<Double> gbestcost;  // list of all gbest particle cost
    public Vector<Double> exetime; //list of exetime for all combination of resource
    public Vector<Double> power;   //list of power for all combination of resources
    public Vector<Double> area;
    public Vector<Double> gbestexetime; //list of exetime for all combination of resource
    public Vector<Double> gbestpower;   //list of power for all combination of resources
    public Vector<Double> gbestarea;
    public Vector<Location> gblocation;
    public Vector<Integer>controlstep;
    public int pmul=0,padd=0,psub=0;
    static private GlobalConstants _instance;
    
    // ********** End of object declaration **********
    
    /* This method initializes the GlobalConstants object, if it is not yet
     * initialized. Otherwise it returns a copy of an already created instance
     * of this class. This method helps in sharing single copy of this class.
     */
    static public GlobalConstants getInstance() {
        if (_instance == null) {
            _instance = new GlobalConstants();
        }
        return _instance;
    }

    /* Class constructor, initializes all objects and is private. It is invoked
     * by getInstance() method. All the classes that declares an object of
     * GlobalConstants class actually shares only a single copy of it's instance.
     */
    private GlobalConstants() {
      operation=new <Object>Vector();
      //resource=new <Object>Vector();
      max_component_detail=new <Object>Vector();
       result_comp=new <Object>Vector();  
      component_area=new double[type];
      component_name=new String[type];
      component_clockcycle=new int[type];
      component_dpower=new double[type];
      max=new int[type];
      vel_range=new int[type];
      operator=new String[type];
      lastgbest=new int[type];
      cost=new <Double>Vector(); 
      gbestcost=new <Double>Vector();
      power=new <Double>Vector(); 
       area=new <Double>Vector(); 
      exetime=new <Double>Vector(); 
      gbestpower=new <Double>Vector(); 
      gbestexetime=new <Double>Vector();
      gbestarea=new <Double>Vector();
      UFList=new <Integer>Vector();
      gblocation=new <Location>Vector();
      controlstep= new <Integer>Vector();
     
    }
    
}
