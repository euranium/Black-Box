/*
	Name: Elizabeth Brooks
	File: ModEvo
	Modified: July 28, 2016
*/

//Imports
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Class to set up and run the models then graph the results
public class ModEvo{

   //The main method used to run the models
   public static void main(String[]args){
      //Initialize variables for modeling evolution
      String species = ""; //Name of the organism under investigation
	    //NOTE: specific to Daphnia?
      double meanTraitOne = 0; //Mean of trait one
      double meanTraitTwo = 0; //Mean of trait two
      double phenotypicVarianceTraitOne = 0; //Phernotypic variance of trait one
      double phenotypicVarianceTraitTwo = 0; //Phenotypic variance of trait two
      double heritability = 0; //Heritability
      double optimumTraitOne = 0; //Optimum value of trait one
      double optimumTraitTwo = 0; //Optimum value of trait two
      double varianceTraitOne = 0; //Variance of the Gaussian function relating trait one to fitness
      double varianceTraitTwo = 0; //Variance of the Gaussian function relating trait two to fitness
      double attenuationCoefficient = 0; //The attenuation coefficient
      double meanInterceptReactionNorm = 0; //The mean intercept of the reaction norm
      double meanSlopeReactionNorm = 0; //The mean slope of the reaction norm
      double phenotypicVarianceInterceptReactionNorm = 0; //The phenotypic variance of the slope of the reaction norm
      double phenotypicVarianceSlopeReactionNorm = 0; //The phenotypic variance of the slope of the reaction norm
      double doseInitial = 0; //The initial dose value
      double meanPreference = 0; //The mean preference for UV penetration of the carapace
      double phenotypicVariancePreference = 0; //The phenotypic variance of the mean preference
      double transmittance = 0; //The transmittance of a non-melanized 
      double slopeConcentration = 0; //The slope relating concentration of melanin to change in UVB transmittance
      int numIterations = 0; //The number of iterations the user would like the program run
      int simPopSize = 0; //The population size of the simulated populations
      double distributionShift = 0; //The shift of the sigmoidal distribution function
      double distributionSlope = 0; //The slope of the sigmoidal distribution function
      double distributionMax = 0; //The max of the sigmoidal distribution function    
      //Initialize object reference variables
      SpeciesCharacteristics speciesInputs; //SpeciesCharacteristics class reference variable
      ModEvoMath modEvo; //ModEvoMath class reference variable     
      //Verify the correct number of arguments have been input     
      if(args.length == 23){
         //Report if there is an error in recieving argument inputs
         try {
            //Parse the args input as "varName=value" to ensure the correct value is entered for each variable
            //Variables for reading in user input
            String argStr;
            String argVal;
            for(int i=0;i<args.length;i++){
               //Create a substrig of each input argument delimited by the equal sign
               String[] argSS = args[i].split("="); //Array to store input substrings
               argStr = argSS[0]; //Store the first arg in a string variable
               argVal = argSS[1]; //Store the second arg in a temp string variable
               //Retrieve each argument from its specified substring, order of input does not matter
               //Convert the input arguments from string to the appropriate data type (int or double)
               if((argStr.toLowerCase()).equals("species")){
                     species = argVal.toUpperCase();
               }else if((argStr.toLowerCase()).equals("meantraitone")){
   	               meanTraitOne = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("meantraittwo")){
                     meanTraitTwo = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("phenotypicvariancetraitone")){
                     phenotypicVarianceTraitOne = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("phenotypicvariancetraittwo")){
                     phenotypicVarianceTraitTwo = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("heritability")){
                     heritability = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("optimumtraitone")){
                     optimumTraitOne = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("optimumtraittwo")){
                     optimumTraitTwo = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("variancetraitone")){
                     varianceTraitOne = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("variancetraittwo")){
                     varianceTraitTwo = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("attenuationcoefficient")){
                     attenuationCoefficient = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("meaninterceptreactionnorm")){
                     meanInterceptReactionNorm = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("meanslopereactionnorm")){
                     meanSlopeReactionNorm = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("phenotypicvarianceinterceptreactionnorm")){
                     phenotypicVarianceInterceptReactionNorm = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("phenotypicvarianceslopereactionnorm")){
                     phenotypicVarianceSlopeReactionNorm = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("doseinitial")){
                     doseInitial = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("meanpreference")){
                     meanPreference = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("phenotypicvariancepreference")){
                     phenotypicVariancePreference = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("transmittance")){
                     transmittance = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("slopeconcentration")){
                     slopeConcentration = Double.parseDouble(argVal);
               }else if((argStr.toLowerCase()).equals("numiterations")){
                     numIterations = Integer.parseInt(argVal);
               }else if((argStr.toLowerCase()).equals("simpopsize")){
                     simPopSize = Integer.parseInt(argVal);
               }else if((argStr.toLowerCase()).equals("distributionmax")||
                  (argStr.toLowerCase()).equals("distributionslope")||
                  (argStr.toLowerCase()).equals("distributionshift")){
                     if((argStr.toLowerCase()).equals("distributionmax"))
                        distributionMax = argVal.toLowerCase();
                        if((argSS[2].toLowerCase()).equals("distributionslope"))
                           distributionSlope = argSS[3].toLowerCase();
                           if((argSS[4].toLowerCase()).equals("distributionshift"))
                              distributionShift = argSS[5].toLowerCase();
                           }else{ //Error handling
                              System.err.println("Invalid input recieved for distribution parameters... program exited.");
                              System.exit(0);
                           } //End else
                        }else if((argSS[2].toLowerCase()).equals("distributionshift"))
                           distributionShift = argSS[3].toLowerCase();
                           if((argSS[4].toLowerCase()).equals("distributionslope"))
                              distributionSlope = argSS[5].toLowerCase();
                           }else{ //Error handling
                              System.err.println("Invalid input recieved for distribution parameters... program exited.");
                              System.exit(0);
                           } //End else
                        }else{ //Error handling
                           System.err.println("Invalid input recieved for distribution parameters... program exited.");
                           System.exit(0);
                        } //End else, if
                     }else if((argStr.toLowerCase()).equals("distributionslope"))
                        distributionSlope = argVal.toLowerCase();
                        if((argSS[2].toLowerCase()).equals("distributionmax"))
                           distributionMax = argSS[3].toLowerCase();
                           if((argSS[4].toLowerCase()).equals("distributionshift"))
                              distributionShift = argSS[5].toLowerCase();
                           }else{ //Error handling
                              System.err.println("Invalid input recieved for distribution parameters... program exited.");
                              System.exit(0);
                           } //End else
                        }else if((argSS[2].toLowerCase()).equals("distributionshift"))
                           distributionShift = argSS[3].toLowerCase();
                           if((argSS[4].toLowerCase()).equals("distributionmax"))
                              distributionMax = argSS[5].toLowerCase();
                           }else{ //Error handling
                              System.err.println("Invalid input recieved for distribution parameters... program exited.");
                              System.exit(0);
                           } //End else
                        }else{ //Error handling
                           System.err.println("Invalid input recieved for distribution parameters... program exited.");
                           System.exit(0);
                        } //End else, if
                     }else if((argStr.toLowerCase()).equals("distributionshift")){
                        distributionShift = argVal.toLowerCase();
                        if((argSS[2].toLowerCase()).equals("distributionmax"))
                           distributionMax = argSS[3].toLowerCase();
                           if((argSS[4].toLowerCase()).equals("distributionslope"))
                              distributionSlope = argSS[5].toLowerCase();
                           }else{ //Error handling
                              System.err.println("Invalid input recieved for distribution parameters... program exited.");
                              System.exit(0);
                           } //End else
                        }else if((argSS[2].toLowerCase()).equals("distributionslope"))
                           distributionSlope = argSS[3].toLowerCase();
                           if((argSS[4].toLowerCase()).equals("distributionmax"))
                              distributionMax = argSS[5].toLowerCase();
                           }else{ //Error handling
                              System.err.println("Invalid input recieved for distribution parameters... program exited.");
                              System.exit(0);
                           } //End else
                        }else{ //Error handling
                           System.err.println("Invalid input recieved for distribution parameters... program exited.");
                           System.exit(0);
                        } //End second inner else, if
                     } //End first inner else, if
               }else{
                     System.err.println("Incorrect argument string entered for arg[" + i + "]... program exited.");
                     System.exit(0); //Do not run the models if incorrect input is recieved
               }//End if else if
            }//End for
         }catch(NumberFormatException e){
              System.err.println("Invalid argument data type... program exited.");
              System.exit(0);
         }//End try, catch
         //Send the input values to the  SpeciesCharacteristics class
         speciesInputs = new SpeciesCharacteristics(species, meanTraitOne, meanTraitTwo, phenotypicVarianceTraitOne, phenotypicVarianceTraitTwo, heritability, 
                                                      optimumTraitOne, optimumTraitTwo, varianceTraitOne, varianceTraitTwo, attenuationCoefficient, meanInterceptReactionNorm, 
                                                      meanSlopeReactionNorm, phenotypicVarianceInterceptReactionNorm, phenotypicVarianceSlopeReactionNorm, doseInitial, 
                                                      meanPreference, phenotypicVariancePreference, transmittance, slopeConcentration, numIterations, simPopSize, 
                                                      distributionSlope, distributionShift, distributionMax);
         //Run the general model
         modEvo = new ModEvoMath(speciesInputs);
         modEvo.runModEvo();                                             
         //Program complete
   		System.out.println("Evolutionary trajectories modeled... program complete.");
      }else{
         System.err.println("Invalid number of input arguments... program exited."); 
         System.exit(0);
      }//End first if 
	}//End main method
}//End ModEvo class
