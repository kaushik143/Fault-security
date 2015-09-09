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
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author SAPVI
 */
public class ProblemSetDFG implements PSOConstants{
 


        public static int []upperBoundaryr;
        public static int []lowerBoundaryr;
	
	Vector<Resource> resource;
	static int  complete[][];
        static String operator[];
        static int max[];//=new int[2];
        static int current[];//=new int[2];
        static String [] step=new String[500];
        static int step_indx=0;
        static int t=1;
        static int comp=1;
        static String schedule="";
       // static String [] comp_name={"mul","add","sub"};
        static GlobalConstants gc;
        public ProblemSetDFG(){
            gc = GlobalConstants.getInstance();
        }
        public double evaluate(Location location) {
            
            //System.out.println("inside evaluate");
            int r[]=new int[gc.size];
            
		double fitnessValue = 0;
                 for(int j=0;j<gc.size;j++){
		r[j] = location.getLoc()[j];
                //System.out.print("  "+r[j]+" ");
                // the "x" part of the location
		//int y = location.getLoc()[1]; // the "y" part of the location
                 }
                 //get resource location
                 int loc=0;
                for(int i=0;i<gc.size;i++){
                 int temp=1;
                 for(int j=i+1;j<gc.size;j++){
                    temp *= (gc.max[j]);
                    }
                loc +=(r[i]-1)*temp;
                }

                 //get value of cost
                 if(gc.cost.get(loc) !=0.0){
                     //System.out.println("inside");
                     //return gc.cost.get(loc);
                 }
                 
                 //System.out.println("Check");
		Vector<Object> maxComp=new <Object>Vector();
                Vector<Object> minComp=new <Object>Vector();
                 for(int j=0;j<gc.size;j++){
                maxComp.add(gc.component_name[j]);
                maxComp.add(gc.max[j]);
                minComp.add(gc.component_name[j]);
                minComp.add(1);
                 }
                 
                
               Vector <Object> component_detail=new <Object>Vector();
               for(int j=0;j<gc.size;j++){
                 component_detail.add(gc.component_name[j]);
                 component_detail.add(r[j]);
                
               }
               
               
               
               if(gc.power_texe){
                   double tpower=0.0,tmaxpower=0.0,texetime=0.0,tmaxexetime=0.0;
                   tpower=gc.power.get(loc);
                   //if(tpower ==0.0){
                     //System.out.println("inside");
                     tpower=calculatePower(component_detail);
                     gc.power.set(loc, tpower);
                 //}
                   tmaxpower=gc.power.get(gc.power.size()-1);
                   //if(tmaxpower ==0.0){
                     //System.out.println("inside");
                     tmaxpower=calculatePower(maxComp);
                     gc.power.set(gc.power.size()-1, tmaxpower);
                 //}
                   texetime=gc.exetime.get(loc);
                   //if(texetime==0.0){
                     texetime= calculateTex(component_detail);
                     gc.exetime.set(loc, texetime);
                   //}
                   tmaxexetime=gc.exetime.get(0);
                   //if(tmaxexetime ==0.0){
                     //System.out.println("inside");
                     tmaxexetime=calculateTex(minComp);
                     gc.exetime.set(0, tmaxexetime);
                 //}
                //fitnessValue= (gc.w1*((calculatePower(component_detail)-gc.PowerConstraint)/calculatePower(maxComp)) + gc.w2*((calculateTex(component_detail)-gc.TexeConstraint)/calculateTex(minComp)));
                fitnessValue= (gc.w1*((tpower-gc.PowerConstraint)/tmaxpower) + gc.w2*((texetime-gc.TexeConstraint)/tmaxexetime));
               }
                if(gc.area_texe){
                    double tarea=0.0,tmaxarea=0.0,texetime=0.0,tmaxexetime=0.0;
                   tarea=gc.area.get(loc);
                   if(tarea ==0.0){
                     //System.out.println("inside");
                     tarea=calculateArea(component_detail);
                     gc.area.set(loc, tarea);
                 }
                   tmaxarea=gc.area.get(gc.area.size()-1);
                   if(tmaxarea ==0.0){
                     //System.out.println("inside");
                     tmaxarea=calculateArea(maxComp);
                     gc.area.set(gc.area.size()-1, tmaxarea);
                 }
                   texetime=gc.exetime.get(loc);
                   if(texetime==0.0){
                     texetime= calculateTex(component_detail);
                     gc.exetime.set(loc, texetime);
                   }
                   tmaxexetime=gc.exetime.get(0);
                   if(tmaxexetime ==0.0){
                     //System.out.println("inside");
                     tmaxexetime=calculateTex(minComp);
                     gc.exetime.set(0, tmaxexetime);
                 }
                //fitnessValue= (gc.w1*((calculatePower(component_detail)-gc.PowerConstraint)/calculatePower(maxComp)) + gc.w2*((calculateTex(component_detail)-gc.TexeConstraint)/calculateTex(minComp)));
                fitnessValue= (gc.w1*((tarea-gc.AreaConstraint)/tmaxarea) + gc.w2*((texetime-gc.TexeConstraint)/tmaxexetime));
               
                    //fitnessValue= (gc.w1*((calculate_Area(component_detail)-gc.AreaConstraint)/calculate_Area(maxComp)) + gc.w2*((calculateTex(component_detail)-gc.TexeConstraint)/calculateTex(minComp)));
                }
                    //System.out.println("fitness Value=  "+fitnessValue);
               gc.cost.set(loc,fitnessValue);   //store fitness in the cost list
		return fitnessValue;
	}
        
        
           
       
        public synchronized double calculatePower(Vector component_detail){
                           
        double power=0.0;
        
        power=(calculateDynamicPower(component_detail)+calculateStaticPower(component_detail));
                System.out.println("    End calculatePower   "+power);
        //return power/1000;
        return power;
    }
    public synchronized double calculateDynamicPower(Vector component_detail){
               //System.out.println("Start calculateDynamicPower");
        double power=0.0;
        int no_mul=0,no_add=0,no_sub=0,no_com=0;
           //int size=component_detail.size();
           Object [] opr_arr=gc.operation.toArray();
        
            
      for(int j=0;j<opr_arr.length;j=j+4){
          String str=opr_arr[j]+"";
          if(str.equalsIgnoreCase("*")){
              no_mul++;
          }
          if(str.equalsIgnoreCase("+")){
              no_add++;
          }
          if(str.equalsIgnoreCase("-")){
              no_sub++;
          }
          if(str.equalsIgnoreCase("<")){
              no_com++;
          }
      }
      
      //System.out.println("MUL="+no_mul+"\n"+"ADD="+no_add+"\n"+"SUB="+no_sub+"COM="+no_com);
      //int ltcc[]=new int[2];
      double delay=0.0;
        //double lt=calculate_lt(component_detail);
       delay=calculate_cc(component_detail);
        delay =delay;
       //System.out.println("lt= "+ltcc[0]+"  cc=  "+ltcc[1]);
       // power=N*(no_mul*gc.component_dpower[1]+no_add*gc.component_dpower[0]+muxDmuxPower(component_detail))/(lt+(N-1)*cc);
         // power=gc.setofdata*(no_mul*gc.component_dpower[1]+no_add*gc.component_dpower[0]+no_sub*gc.component_dpower[0]+no_com*gc.component_dpower[0]+muxDmuxPower(component_detail))/(delay);   
            
            power=(no_mul*gc.component_dpower[1]+no_add*gc.component_dpower[0]+no_sub*gc.component_dpower[0]+no_com*gc.component_dpower[0]+muxDmuxPower(component_detail))/(delay);
//power=N*(no_mul*2504+no_add*181+no_sub*181+muxDmuxPower(component_detail))/(cc_lt.calculate_lt(component_detail)+(N-1)*cc_lt.calculate_cc(component_detail));
                System.out.println("End CalculateDynamic Power  "+power);
        return power;
    }
    public synchronized double muxDmuxPower(Vector component_detail){
                //System.out.println("Start muxDmuxPower");
        double power=0.0;
       /* int no_mul=0,no_add=0,no_sub=0,no_com=0;
       // int muxDmux[][]=mux_dmux_count(component_detail);
        String mul="mul";
        String add="add";
        String sub="sub";
        String com="com";
        int size=component_detail.size();
        Object component[]=component_detail.toArray();
        int mux=0,dmux=0;
        Object [] opr_arr=gc.operation.toArray();
        
            
      for(int j=0;j<opr_arr.length;j=j+4){
          String str=opr_arr[j]+"";
          if(str.equalsIgnoreCase("*")){
              no_mul++;
          }
          if(str.equalsIgnoreCase("+")){
              no_add++;
          }
          if(str.equalsIgnoreCase("-")){
              no_sub++;
          }
          if(str.equalsIgnoreCase("<")){
              no_com++;
          }
      }
        for(int i=0;i<size;i=i+2){
            if(mul.equals((component[i])+"")){
                if(no_mul>1)
                mux=mux+Integer.parseInt(component[i+1]+"")*2;
                dmux=dmux+Integer.parseInt(component[i+1]+"");
                //i++;
            }
               
            if(add.equals((String)component[i])){
                if(no_add>1)
               mux=mux+Integer.parseInt(component[i+1]+"")*2;
                dmux=dmux+Integer.parseInt(component[i+1]+"");
               //i++;
            }
            if(sub.equals((String)component[i])){
                if(no_sub>1)
              mux=mux+Integer.parseInt(component[i+1]+"")*2;
                dmux=dmux+Integer.parseInt(component[i+1]+"");
               //i++;
            }
            if(com.equals((String)component[i])){
              if(no_com>1)  
              mux=mux+Integer.parseInt(component[i+1]+"")*2;
               //i++;
            }
            
        }
        power=power+(mux+dmux)*0.2;
        
        */
        /*****************************/
        double muxpower = 0.1;
        for(int i=0;i<resource.size();i++){
            int muxsize=0;
           int noopr=resource.get(i).oprComplete.size();
           if(noopr==1){
             muxsize=0;  
           }else
            for(int j=-1;j<10;j++){
                if(noopr>(2<<j) && noopr<=(2<<(j+1))){
                 muxsize=2<<(j+1);  
                // x=j+2;
                 break;
                }
            }    
         //System.out.println("mux info  "+noopr+"   "+muxsize);  
         //power=power+(muxsize/2)*muxpower*3;
           power=power+muxpower*3;
         //System.out.println("powerMUX= "+power);
        }
       // System.out.println("Resource value"+resource);
        /*int size=component_detail.size();
        Object component[]=component_detail.toArray();
        for(int i=0;i<size;i=i+2){
           
                power=power+Integer.parseInt(component[i+1]+"")*2*0.2;
        }
            /*
            for(int i=0;i<2;i++){
            for (int j=0;j<5;j++){
                power=power+muxDmux[i][j]*0.1*Math.pow(2,i);
            }
            //System.out.println("");
        }*/
            //System.out.println("End muxDmuxPower");
        return power;
    }
    public synchronized double calculateStaticPower(Vector component_detail){
        //System.out.println("Start calculateStaticPower");
        double power=0.0;
        String mul="mul";
        String add="add";
        String sub="sub";
        String com="com";
        int size=component_detail.size();
        Object component[]=component_detail.toArray();
        
        for(int i=0;i<size;i=i+2){
            power += Integer.parseInt(component[i+1]+"")*gc.component_area[i/2];
           /* if(mul.equals((component[i])+"")){
                power=power+Integer.parseInt(component[i+1]+"")*2464;
                //i++;
            }
               
            if(add.equals((String)component[i])){
               power=power+Integer.parseInt(component[i+1]+"")*2032;
               //i++;
            }
            if(sub.equals((String)component[i])){
              power=power+Integer.parseInt(component[i+1]+"")*2032;
               //i++;
            }
            if(com.equals((String)component[i])){
              power=power+Integer.parseInt(component[i+1]+"")*2032;
               //i++;
            }
            */
        }
        //System.out.println("before mux Static Power  "+power);
        double muxpower = gc.mux_area;
        for(int i=0;i<resource.size();i++){
            int muxsize=0;
           int noopr=resource.get(i).oprComplete.size();
           if(noopr==1){
             muxsize=0;  
           }else
            for(int j=-1;j<10;j++){
                if(noopr>(2<<j) && noopr<=(2<<(j+1))){
                 muxsize=2<<(j+1);  
                // x=j+2;
                 break;
                }
            }    
         //System.out.println("mux info  "+noopr+"   "+muxsize);
           //if(muxsize>=2)
         //power=power+(muxsize-1)*muxpower*3;
            //System.out.println("powerMUX= "+power);
         power=power+muxpower*3;
        }
       
        //System.out.println("after mux Static Power  "+power);
         power=power*0.00002933;
         System.out.println("static power= "+power);
        return power;
    }
    
     
     public synchronized double calculateTex(Vector component_detail){
        // System.out.println("calculateTex call  ");
         gc = GlobalConstants.getInstance();
        double tex=0;
        //int ltcc[]=new int[2];
       tex=calculate_cc(component_detail);
        //tex=(ltcc[0]+(gc.setofdata-1)*ltcc[1]);
        //System.out.println("time of execution  "+tex/1000+"  "+ltcc[0]+"   "+ltcc[1]+"   "+gc.setofdata);
        return tex/1000;
    }
    
     
     public synchronized double calculate_cc(Vector compdetail){
         
        //System.out.println("calculate cc call");
         double delay=0.0;
         resource= new <Resource>Vector();
         String s="";
         String schF="";
         String stc="",sfirst="";
         int ltcc[]=new int[2];
         Operation []opera=new Operation[gc.no_operation];
         for(int i=0;i<gc.no_operation;i++){
             opera[i]=new Operation();
         }
         int size1=compdetail.size();
        // int nomul,noadd,nosub;
         int NumberOfmult=0, NumberOfadd=0, NumberOfsub=0,NumberOfcom=0, T_latency = 0,T_total;
             int Time=0,Texec=0;
             int lat=0;
        //int max[] =new int [size/2];
        Object component[]=compdetail.toArray();
        for(int i=0;i<size1;){
            //System.out.println("bbbb");
            if((component[i]+"").equalsIgnoreCase("mul"))
            NumberOfmult=Integer.parseInt(component[i+1]+"");
            if((component[i]+"").equalsIgnoreCase("add"))
            NumberOfadd=Integer.parseInt(component[i+1]+"");
            if((component[i]+"").equalsIgnoreCase("sub"))
           NumberOfsub=Integer.parseInt(component[i+1]+"");
            if((component[i]+"").equalsIgnoreCase("com"))
           NumberOfcom=Integer.parseInt(component[i+1]+"");
            //System.out.println("cccc");
            i=i+2;
        }
        
        //resouce initialization
  for(int a=0;a<size1;a=a+2){
      for(int b=0;b<Integer.parseInt(component[a+1]+"");b++){
         
          Resource temp=new Resource(component[a]+""+(b+1));
          //System.out.println(component[a]+"   "+component[a+1]+""+ temp);
         resource.add(temp); 
      }
  }
   
        //System.out.println("resource detail"+NumberOfmult+"  "+NumberOfadd+"  "+NumberOfsub+"   "+NumberOfcom);
        int adddelay,muldelay,subdelay,comdelay;
        adddelay=2;
        subdelay=2;
        comdelay=2;
        muldelay=2;
        //muldelay=gc.component_clockcycle[1]*2/gc.component_clockcycle[0];
        Object [] opr_arr=gc.operation.toArray();
         for(int i=0,j=0;i<gc.no_operation;i++,j=j+4){
          if("*".equals(opr_arr[j])){
              opera[i].delay=muldelay;
              opera[i].ltime=muldelay;
              opera[i].ttime=muldelay;
              opera[i].operator="*";
          }   
          if("+".equals(opr_arr[j])){
              opera[i].delay=adddelay;
              opera[i].ltime=adddelay;
              opera[i].ttime=adddelay;
              opera[i].operator="+";
          }
          if("-".equals(opr_arr[j])){
              opera[i].delay=subdelay;
              opera[i].ltime=subdelay;
              opera[i].ttime=subdelay;
              opera[i].operator="-";
          }
          if("<".equals(opr_arr[j])){
              opera[i].delay=comdelay;
              opera[i].ltime=comdelay;
              opera[i].ttime=comdelay;
              opera[i].operator="<";
          }
          opera[i].oper1=Integer.parseInt(opr_arr[j+1]+"");
          opera[i].oper2=Integer.parseInt(opr_arr[j+2]+"");
          opera[i].result=Integer.parseInt(opr_arr[j+3]+"");
         }
         /*
          for(int i=0,j=0;i<gc.no_operation;i++,j=j+4){
          if("*".equals(opr_arr[j])){
              opera[i].delay=gc.component_clockcycle[1];
              opera[i].ltime=gc.component_clockcycle[1];
              opera[i].ttime=gc.component_clockcycle[1];
              opera[i].operator="*";
          }   
          if("+".equals(opr_arr[j])){
              opera[i].delay=gc.component_clockcycle[0];
              opera[i].ltime=gc.component_clockcycle[0];
              opera[i].ttime=gc.component_clockcycle[0];
              opera[i].operator="+";
          }
          if("-".equals(opr_arr[j])){
              opera[i].delay=gc.component_clockcycle[0];
              opera[i].ltime=gc.component_clockcycle[0];
              opera[i].ttime=gc.component_clockcycle[0];
              opera[i].operator="-";
          }
          opera[i].oper1=Integer.parseInt(opr_arr[j+1]+"");
          opera[i].oper2=Integer.parseInt(opr_arr[j+2]+"");
          opera[i].result=Integer.parseInt(opr_arr[j+3]+"");
         }*/
        // System.out.println("test location");
             boolean exitflag = true, check_flag1 = false, check_flag2 = false,check_flag3 = false, check_flag4 = false,lat_flag=false,lat_flag2=false;
             boolean release=true,release2=true,incr=false,incr2=false,incr3=false, assign_flag=false;
            // boolean incrCount=true;
             int counter=1;
             while(exitflag)
             {
                 /*if((incr3 || incr2  ) ){   //||!(check_flag2 || check_flag4)
                     s=s+ (counter)+"   ";
                     //incrCount=false;
                     
                 }*/
                check_flag1 = false;check_flag3 = false; lat_flag=false;lat_flag2=false;
                release=true;release2=true;incr=false;incr2=false;assign_flag=false;
                //-------Latency------------------------------
                 for (int j = 0; j < gc.no_operation; j++)
                 {
                         if((opera[j].ltime < opera[j].delay)&& (opera[j].ltime > 0))
                          {   
                              //System.out.println("check3 "+j);
                              incr=true;
                              //currently it is active
                              //opera[j].ltime--;
                              opera[j].ltime=opera[j].ltime-1;
                              //s=s+j+" ";
                              //System.out.println("operation "+j+" ");
                              check_flag2 = true;
                              if(!lat_flag){
                                lat=lat+1;
                              }
                              lat_flag=true;
                              if (opera[j].ltime == 0)
                              {
                                  for(int a=0;a<resource.size();a++){
                                      
                                      Resource temp=resource.get(a);
                                      if(temp.curOpr==j){
                                         // System.out.println("check4 " +j+ temp.operator);
                                          temp.status=true;
                                          temp.oprComTime.add(lat);
                                      }
                                  }
                                  release=false;
                                  
                              }
                          }
                        
                 }
                 if(incr){
                  //s=s+"\n";
                  incr=false;
                 }
                  
                 for (int j = 0; j < gc.no_operation; j++)
                 {
                         if (opera[j].ltime > 0 && (opera[j].ltime == opera[j].delay) && release)
                         {
                                
                             //check if result is available 
                             boolean opr_a = false, opr_b = false, inputOper1 = false, inputOper2 = false;
                             for (int a = 0; a < gc.no_operation; a++)
                             {
                                 if (opera[a].result == opera[j].oper1)
                                 {
                                     inputOper1 = true; 
                                     if (opera[a].ltime == 0)
                                     {
                                         opr_a = true;
                                     }
                                 }
                                 if (opera[a].result == opera[j].oper2)
                                 {
                                     inputOper2 = true; 
                                     if (opera[a].ltime == 0)
                                     {
                                         opr_b = true;
                                     }
                                 }
                             }
                             //check if operator is available
                             if ((opr_a == true && opr_b == true) || (inputOper1 == false && inputOper2 == false)||
                                 (inputOper1 == false && opr_b == true) || (opr_a == true && inputOper2 == false))
                             {
                                // System.out.println("check1 "+j);
                                 for(int a=0;a<resource.size();a++){
                                      Resource temp=resource.get(a);
                                      //System.out.println("check5 "+j);
                                     // System.out.println(temp.operator+"   "+opera[j].operator+"   "+temp.status);
                                      if(temp.operator.equals(opera[j].operator) && temp.status){
                                          //System.out.println("check2 "+j);
                                          temp.status=false;
                                          temp.oprComplete.add(opera[j].result);
                                          
                                          //System.out.print(opera[j].result+"  ");
                                          temp.oprStartTime.add(Time);
                                          temp.curOpr=j;
                                          opera[j].ltime=opera[j].ltime-1;
                                          
                                          //print schedule
                                          s=s+opera[j].result+" ("+temp.operator+") ";
                                          s=s+temp.name+"  ";//+Time/2+"  ";
                                          //System.out.println("operation1 "+j+" ");
                                          check_flag1 = true;
                                          if(!lat_flag2 && !lat_flag){
                                           lat=lat+1;
                                            //s=s+"\n";
                                            }
                                            lat_flag2=true;
                                            incr2=true;
                                            break;
                                         }
                                  }
                                
                                 
                             }
                              
                     }
                         
                        
                 }
                 
                 //print schedule
                // if(incr2){
                 //  s=s+"\n";
                 //  incr2=false;
                // }
                
                 //System.out.println("lat "+lat);
                 //-------End Latency------------------------------
                             
                 //-------Tc------------------------------
                
                
                // if(stc.matches(sfirst) || !ok)
                 
                 //     ok=false;
                 for (int j = 0; j < gc.no_operation; j++)
                 {
                     
                     if ((opera[j].ttime < opera[j].delay) && (opera[j].ttime > 0))
                     {   //currently it is active
                         //opera[j].ttime--;
                         opera[j].ttime=opera[j].ttime-1;
                          //s=s+j+" ";
                          //System.out.println("operation2 "+j+" ");
                         check_flag4 = true;

                         if (opera[j].ttime == 0)
                         {
                             for(int a=0;a<resource.size();a++){
                                      Resource temp=resource.get(a);
                                      if(temp.curOpr==j){
                                          temp.status=true;
                                          //temp.oprComTime.add(lat);
                                      }
                                  }
                             release2=false;
                             
                         }
                     }
                 }
                 
                  
     
                 for (int j = 0; j < gc.no_operation; j++)
                 {
                     
                     if((opera[j].ttime  == opera[j].delay) && (opera[j].ltime == 0) && release2 && release)
                     {

                         //check if result is available 
                         boolean opr_a = false, opr_b = false, inputOper1 = false, inputOper2 = false;
                         for (int a = 0; a < gc.no_operation; a++)
                         {
                             if (opera[a].result == opera[j].oper1)
                             {
                                 inputOper1 = true;
                                 if (opera[a].ttime == 0)
                                 {
                                     opr_a = true;
                                 }
                             }
                             if (opera[a].result == opera[j].oper2)
                             {
                                 inputOper2 = true;
                                 if (opera[a].ttime == 0)
                                 {
                                     opr_b = true;
                                 }
                             }
                         }
                         //check if operator is available
                         if ((opr_a == true && opr_b == true) || (inputOper1 == false && inputOper2 == false) ||
                             (inputOper1 == false && opr_b == true) || (opr_a == true && inputOper2 == false))
                         {
                             // check which  FU perform this operation 
                            String FUname="";
                            int ctime=0;
                             for (Resource temp : resource) {
                                 for (int b=0;b<temp.oprComplete.size();b++){
                                     if(temp.oprComplete.get(b)==opera[j].result){
                                         FUname=temp.name;
                                         ctime=temp.oprStartTime.get(b);
                                     }
                                 }
                             }
                             
                             for(int a=0;a<resource.size();a++){
                                      Resource temp=resource.get(a);
                                      if((!temp.name.equals(FUname))){// || Time>(ctime+(gc.kc-1)*2)){
                                      if(temp.operator.equals(opera[j].operator) && temp.status ){
                                          temp.status=false;
                                          //temp.oprComplete.add(opera[j].result+gc.no_operation);
                                          //temp.oprStartTime.add(lat);
                                          temp.curOpr=j;
                                          opera[j].ttime=opera[j].ttime-1;
                                         s=s+opera[j].result+"' ("+temp.operator+") ";
                                          s=s+temp.name+"  ";
                                          //System.out.println("operation1 "+j+" ");
                                          check_flag3 = true;
                                          incr3=true;
                                          assign_flag=true;
                                          break;
                                     //s=s+"\n";
                                          
                                            }
                                        }
                                            
                                         }
                             if(!assign_flag){
                             for(int a=0;a<resource.size();a++){
                                      Resource temp=resource.get(a);
                                      if(Time>=(ctime+((gc.kc-1)*2))){// || ){
                                      if(temp.operator.equals(opera[j].operator) && temp.status ){
                                          temp.status=false;
                                          //temp.oprComplete.add(opera[j].result+gc.no_operation);
                                          //temp.oprStartTime.add(lat);
                                          temp.curOpr=j;
                                          opera[j].ttime=opera[j].ttime-1;
                                         s=s+opera[j].result+"' ("+temp.operator+") ";
                                          s=s+temp.name+"  ";
                                          //System.out.println("operation1 "+j+" ");
                                          check_flag3 = true;
                                          incr3=true;
                                         // assign_flag=true;
                                          break;
                                     //s=s+"\n";
                                          
                                            }
                                        }
                                            
                                         }
                         }
                                                              
                         }
                     }
                 }
                  
               
               // if(!(!incr3 || !incr2 || !check_flag2 || !check_flag4)){
               // Time++;
              //  }
                 exitflag = false;
                 for (int b = 0; b < gc.no_operation; b++)
                 {
                     if (opera[b].ttime > 0)
                     {
                         exitflag = true;
                     }
                 }
                if(incr3 || incr2 ){//|| !(check_flag2 || check_flag4)){ 
                   schF +=(counter)+"  "+s+"\n";
                   s="";
                   
                   counter+=1;
                  //incrCount=true;
                   incr2=false;
                   incr3=false;
                   
                 }
                else if((check_flag2 || check_flag4)){
                   check_flag4 = false; 
                   check_flag2 = false; 
                }
                else if(!(incr3 || incr2 || check_flag2 || check_flag4)){
                    if(Time%2==0){
                    schF +=(counter)+"\n";
                                     
                   counter+=1;
                    }
                }
                                
                   Time = Time+1;
                   
                   
                   //System.out.println("Schdule "+Time);
                 
                    
                 //System.out.println("T_lat= "+T_latency+"   Texec= "+Texec);
             }
              System.out.println(schF);
            int Latency = lat;
            int Tc = Time-lat ;
            ///gc.schedule=gc.schedule+"\n Latency=  "+Latency+"\n";
            //System.out.println("Time "+Time);
           //System.out.println(" Before scale Latency= "+ Latency);
           //System.out.println("Before scale CycleTime= "+ Tc);
         //End Time (T_latency, Texec)-----------------------------------------------
        //ltcc[0]=Latency*gc.component_clockcycle[0]/2;
       // ltcc[1]=Tc*gc.component_clockcycle[0]/2;

  //ltcc[0]=Latency;
// ltcc[1]=Tc;
//gc.schedule=gc.schedule+"\n Latency=  "+ltcc[0]+"\n";
 //System.out.println("Latancy="+ltcc[0]);
 //System.out.println("Cycle Time="+ltcc[1]);

  //System.out.println("\n\n\n"+sfirst);
            Checkpointing ch=new Checkpointing();
            
            delay= ch.checkpointion(schF, resource);
 return delay;
      
    }
    
    public synchronized double calculateArea(Vector component_detail)
    {
        //System.out.println("strat calculate Area");
        double area=0.0;
        String mul="mul";
        String add="add";
        String sub="sub";
        String com="com";
        double delay=0.0;
         //int ltcc[]=new int[2];
        //double lt=calculate_lt(component_detail);
        delay=calculate_cc(component_detail);
        int size=component_detail.size();
        Object component[]=component_detail.toArray();
       /* for(int i=0;i<size;i=i+2){
            if(mul.equals(component[i]+"")){
                area=area+Integer.parseInt(component[i+1]+"")*2464;
                //i++;
            }
               
            if(add.equals(component[i]+"")){
               area=area+Integer.parseInt(component[i+1]+"")*2030;
               //i++;
            }
           if("sub".equals(component[i]+"")){
               area=area+Integer.parseInt(component[i+1]+"")*2030;
               i++;
            }
            if("com".equals(component[i]+"")){
               area=area+Integer.parseInt(component[i+1]+"")*2030;
               i++;
            }
            
        }
        * */
       
        
        for(int i=0;i<size;i=i+2){
            area += Integer.parseInt(component[i+1]+"")*gc.component_area[i/2];
           /* if(mul.equals((component[i])+"")){
                power=power+Integer.parseInt(component[i+1]+"")*2464;
                //i++;
            }
               
            if(add.equals((String)component[i])){
               power=power+Integer.parseInt(component[i+1]+"")*2032;
               //i++;
            }
            if(sub.equals((String)component[i])){
              power=power+Integer.parseInt(component[i+1]+"")*2032;
               //i++;
            }
            if(com.equals((String)component[i])){
              power=power+Integer.parseInt(component[i+1]+"")*2032;
               //i++;
            }
            */
        }
        //System.out.println("before mux Area  "+area);
        double muxarea = gc.mux_area;
        for(int i=0;i<resource.size();i++){
            int muxsize=0;
           int noopr=resource.get(i).oprComplete.size();
           if(noopr==1){
             muxsize=0;  
           }else
            for(int j=-1;j<10;j++){
                if(noopr>(2<<j) && noopr<=(2<<(j+1))){
                 muxsize=2<<(j+1);  
                // x=j+2;
                 break;
                }
            }    
         //System.out.println("mux info  "+noopr+"   "+muxsize);
           //if(muxsize>=2)
         //area=area+(muxsize-1)*muxarea*3;
        area=area+muxarea*3; 
        //System.out.println("powerMUX= "+power);
        }
        
        //Enumeration e=component_detail.elements();
         //while(e.hasMoreElements()){
        //System.out.println("area is = "+area);
        //System.out.println("end calculate Area");
            return area;   
         }
   
   }

   

