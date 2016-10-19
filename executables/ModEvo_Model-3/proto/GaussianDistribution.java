/*
	File: GaussianDistribution
	Modified: April 12, 2016
*/

//Class for simulating a gaussian distribution of traits
//The distribution is shaped by the input mean and standard deviation 
//for the trait in question

//Imports
import java.util.*;
import java.io.*;

//Class header
public class GaussianDistribution {

   //Class field declarations
   private String fileName ="randomNmbers.txt";//Txt file to store the random numbers generated and used
   private Double meanInit;//Initial mean value for specified trait
   private Double standDInit;//Initial standard deviation for specified trait
   private int mean; //Mean integer value for the specified trait
   private int stand;//Standard deviation integer value for the specified trait
   private int simPopSize; //Population size of the simulted population?

   //Class constructor
   public GaussianDistribution(){
      
   }

   //Method to build the gaussian distribution based on input values
   public void getGaussianDistribution(Double meanInput, Double standDInput, int simPopSizeInput) {
      //Variable initializations
      meanInit = meanInput;
      standDInit = standDInput;
      meanInit = meanInit *1000;
      standDInit = standDInit*1000;
      mean = meanInit.intValue(); //Convert from double to int
      stand = standDInit.intValue(); //Convert from double to int
      //NOTE: Is num simPopSizeInput?
      //int num = Integer.parseInt(args[2]);
      simPopSize = simPopSizeInput;
      //Build the normal distribution
      distributeValues();
   }//End class constructor
   
   //Function for sectioning the graph by the current random number
   public void distributeValues(){
      //Variable declarations for writing random numbers to file for determining the normal distribution
      int currentRandomNum = 0;//The current random number generated
      //Call the function to build the gaussian distribution based on input values
      buildGraph();
      //Separate the graph into sections to shape distribution
      try{
         FileWriter fw = new FileWriter(fileName);
         BufferedWriter bw = new BufferedWriter(fw);
         //Build a distribution for the population size specified by section 
         //depending on the generated random num
         for(int t =0; t <simPopSize; t++){
            int max = 2000;
            int min =1;
            Random rn = new Random();
            currentRandomNum = rn.nextInt(max-min+1)+min;
            if(currentRandomNum <= 2){
               max = mean - (stand*3);
               min = mean - (stand*4);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }//End inner if else
            }else if(currentRandomNum <=12){
               max = mean - (stand*2 + stand/2);
               min = mean - (stand*3);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=46){
               max = mean - (stand*2);
               min = mean - (stand*2 + stand/2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=134){
               max = mean - (stand*1 + stand/2);
               min = mean - (stand*2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=318){
               max = mean - (stand);
               min = mean - (stand*1+ stand/2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=618){
               max = mean - (stand/2);
               min = mean - (stand*1);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=1000){
               max = mean;
               min = mean - (stand/2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <= 1382){
               max = mean + (stand/2);
               min = mean;
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=1682){
               max = mean + (stand);
               min = mean + (stand/2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=1866){
               max = mean + (stand + stand/2 );
               min = mean + (stand);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=1954){
               max = mean + (stand *2);
               min = mean + (stand + stand/2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=1988){
               max = mean + (stand*2 + stand/2);
               min = mean + (stand*2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=1998){
               max = mean + (stand *3);
               min = mean + (stand*2 + stand/2);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }else if(currentRandomNum <=2000){
               max = mean + (stand *4);
               min = mean + (stand*3);
               if(negCheck (max, min) == 1){
                        currentRandomNum = rn.nextInt(max-min+1)+min;
               }
               else{
                        currentRandomNum = rn.nextInt(min-max+1)+min;
               }
            }//End outter if else if
            Double temp = (double)currentRandomNum/1000;//Convert the current individual value to Double from int
            //Build the probability density for the curent individual, x (temp)
            Double temp2 = probabilityDensity(temp);
            int anw = rn.nextInt(100000)+1;
            temp2 = (temp2/100000.0)* anw;
            //Write results to file
            String s = Double.toString(temp);
            String ss = Double.toString(temp2);
            bw.write(s);
            bw.write(" ");
            bw.write(ss);
            bw.newLine();
        }//End for
        bw.close();//Close the file
      }catch(IOException ex){
        System.out.println("Error in file writing for GaussianTraitDistribution class");
      }//End try catch
    }//End distributeValues function
    
    //Function for checking the negativity of a value
    public int negCheck(int max, int min){
      if(max > min){
         return 1;
      }
      return 0;
    }//End negCheck function

    //Function for builbing the graph of the normal, gaussian distribution
    public void buildGraph(){
      //Initialize varaibles used for building the graph
      double currentMean = (meanInit/1000);
      double currentStandD = (standDInit/1000);
      double start = currentMean - (5.0 * currentStandD);
      double end = currentMean + (5.0* currentStandD);
      double rate = currentStandD/10;
      //Initialize the variables used for building the graph
      double temp = 0; //Variable for storing the current calculated function value
      String s = ""; //String for storing the converted number values
      //Name the file for saving the graph's vlaues
      String fileName ="gaussianDistribution.txt";
      //Write to file the distributed values using the function
      try{
         FileWriter fw = new FileWriter(fileName);
         BufferedWriter bw = new BufferedWriter(fw);
         for(double i=start; i<=end; i+=rate){
            temp = probabilityDensity(i);
            //Write the current index
            s = Double.toString(i);
            bw.write(s);
            bw.write(" ");
            //Write the calculated function value
            s = Double.toString(temp);
            bw.write(s);
            bw.newLine();
         }
         bw.close();
      }
      //Catch any fite writting exceptions
      catch(IOException ex){
      }//End try, catch
    }//End buildGraph function

    //Function for building the probability densisty for the current individual, x value
    public Double probabilityDensity(Double xInput){
      //Variables declarations for calculating the proababilty distribution
      double x = xInput;
      double currentMean = (meanInit/1000);
      double currentStandD = (standDInit/1000);
      double e = Math.E; //Java Math.E provides more accuracy, to 15 decimal places
      double p = Math.PI; //Java Math.PI provides more accuracy, to 15 decimal places
      double variance = Math.pow(currentStandD,2);//Calculate variance from dtandard deviation
      //Variables for calculating the exponent of e
      double eDenominator = 2*variance;//Two times the variance
      double xMu = x-currentMean;//The difference between the input x and mean
      double eNumerator = Math.pow(xMu,2);//The squared difference of x and the mean
      double eExponent = (eNumerator/eDenominator) * -1;//The exponent of e
      //Variables for calculating the coefficient of e
      double twoPi = 2*p;//Two times pi
      double sqRootTwoPi = Math.pow(twoPi,.5);//Squareroot of two times pi
      double coeffDenominator = currentStandD * sqRootTwoPi;//The squareroot of the variance is the standard deviation, 
                                             //which is multiplied by the squareroot of two times pi
      double eCoeff = 1/coeffDenominator;//The coefficient of e
      double ePowered = Math.pow(e, eExponent);//Calculate e with the determined exponent
      double eResult = eCoeff * ePowered;//Calculate the current e with the determined coefficient
      //Return the calculated probability density     
      return eResult;
    }//End probDensity function
}//End GaussianTraitDistribution class