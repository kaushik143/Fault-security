/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PSOFaultSecure;

/**
 *
 * @author SAPVI
 */
public class ExastiveSearch {
GlobalConstants gc;
Location location;
ProblemSetDFG psd;

int rc[];
int best=0;
   public ExastiveSearch(){
       gc = GlobalConstants.getInstance();
       psd= new ProblemSetDFG();
       
       
   }
    
    public synchronized void createList(){
        System.out.println("Start create List");
        int size;
        int lenth=gc.max_component_detail.size();
        size=lenth/2;
        System.out.println("max lenth= "+lenth);
        int max[]=new int[lenth/2];
        
        
        //gc.power_texe=true;
        //Object component[]=dfact_component_detail.toArray();
        //int temp[]=new int[size/2];
        //Vector<Object> design_space= new <Object>Vector();
        /* for(int i=0;i<size;i++){
          String  name=gc.component_name[i];
          max[i]=0;
          //temp[i]=1;
         */ 
       
            for(int j=0;j<(gc.size);j++)
            max[j]=gc.max[j];
        
        //find no of combination
        
        int combination=1,temp=1;
        for(int x=0;x<size;x++)
            combination=combination*max[x];
           int design_space[][]=new int[combination][size];
        
           int itr=1,k=1;
        for( int i =0; i<size ;i++){
        for( int j=0;j<combination;j++){
            //System.out.println(i+"      "+j);
        design_space[j][i]=temp;
        k++;
        if(i!=0){
            if(k>itr){
               temp++;
                k=1;
        }}
        if(i==0)
        temp++;
        //k++;
        //System.out.println(design_space[j][i]);
        if(temp==(max[i]+1)) {
                temp=1;
            }
        }
        itr=itr*max[i];
        temp=1;
        k=1;
    } 
        for(int i=0;i<design_space.length;i++){
            for(int j=0;j<design_space[i].length;j++){
                System.out.print("DS  "+design_space[i][j]+"  ");
            }
            System.out.println("");
        }
               
        gc.cost.clear();
        gc.power.clear();
        gc.exetime.clear();
        gc.area.clear();
        int total=1;
        {
            for(int j=0;j<(gc.size);j++)
            total=total*gc.max[j];
        }
        System.out.println("Total= "+total +" gc.size= "+gc.size);
        for(int i=0;i<total;i++){
            gc.cost.add(0.0);
            gc.power.add(0.0);
            gc.area.add(0.0);
            gc.exetime.add(0.0);
            gc.controlstep.add(0);
        }
        
       rc=new int[size];
       for(int i=0;i<design_space.length;i++){
            for(int j=0;j<design_space[i].length;j++){
                rc[j]=design_space[i][j];
                System.out.print(rc[j]+"  ");
                
            }
            location= new Location(rc);
            System.out.println("call evaluation");
            {
              Double d = psd.evaluate(location);   
             }
             }
       
       
       double bestcost=0.0,bestpower=0.0,bestexetime=0.0,bestarea=0.0;
        for(int i=0;i<gc.cost.size();i++){
          if(gc.cost.elementAt(best)>gc.cost.elementAt(i)){
              best=i;
              bestcost=gc.cost.elementAt(i);
              bestpower=gc.power.elementAt(i);
              bestexetime=gc.exetime.elementAt(i);
              bestarea=gc.area.elementAt(i);
            }
        }
        int temp2=best;
        
        
        int loc[]=new int[gc.size];
       /* if(!gc.CDFG)
        { 
        for(int m=0;m<gc.size;m++){
        temp=1;
        for(int j=m+1;j<gc.size;j++){
            
                      temp *= (gc.max[j]);
          }
        loc[m]=temp2/temp;
        //System.out.println("no  "+no);
        temp2 = temp2%temp;
        //System.out.println("no  "+no);
    }
        
           for(int m=0;m<gc.size;m++){
               System.out.print(gc.component_name[m]+"  ");
               gc.output2=gc.output2+gc.component_name[m]+"=  ";
               System.out.println((loc[m]+1)+"  ");
               gc.output2=gc.output2+(loc[m]+1)+"\n";
           }
         System.out.println("power= "+bestpower);
         gc.output2=gc.output2+"power= "+bestpower+"\n";
         System.out.println("Execution time= "+bestexetime);
         gc.output2=gc.output2+"Execution time= "+bestexetime+"\n";
         System.out.println("Cost= "+bestcost);
         gc.output2=gc.output2+"Cost= "+bestcost+"\n";
    }
     */      
           
        temp=1;
        System.out.println("ALL");
        {
        System.out.println("GC_COST size= "+gc.cost.size());
        for(int i=0;i<gc.cost.size();i++){
         //if(gc.cost.elementAt(i)!=0.0)
            {
             int no=i;
             for(int m=0;m<gc.size;m++){
        temp=1;
        for(int j=m+1;j<gc.size;j++){
            
                      temp *= (gc.max[j]);
          }
        loc[m]=no/temp;
        //System.out.println("no  "+no);
        no = no%temp;
        //System.out.println("no  "+no);
    }
           for(int m=0;m<gc.size;m++){
               if(i==best){
                gc.output2=gc.output2+gc.component_name[m]+"=  ";
                gc.output2=gc.output2+(loc[m]+1)+"\n";   
               }
               System.out.print((loc[m]+1)+"  ");
           } 
            System.out.print("   "+gc.power.elementAt(i)+"   "+gc.area.elementAt(i)+"    "+ gc.exetime.elementAt(i)+"   "+gc.cost.elementAt(i)+"\n");
             }
         
       } 
        }
            
       System.out.println("best solution is=  "); 
       
         gc.output2=gc.output2+"power= "+bestpower+"\n";
         gc.output2=gc.output2+"Area= "+bestarea+"\n";
         
         gc.output2=gc.output2+"Execution time= "+bestexetime+"\n";
         
         gc.output2=gc.output2+"Cost= "+bestcost+"\n";
         System.out.println(gc.output2);
         System.out.println("Power\tExecutiontime");
         for(int i=0;i<gc.cost.size();i++){
           if(gc.power.get(i)<=gc.PowerConstraint && gc.exetime.get(i)<= gc.TexeConstraint){
               System.out.println(gc.power.get(i)+"\t"+gc.exetime.get(i));
           }  
         }
       }
     }
       