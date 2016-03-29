/*
	Name: Elizabeth Brooks
	File: ModEvo
	Modified: February 24, 2016
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
      
      //NOTE: Allow input selection of simulated population distribution
      
      //Initialize object reference variables
      SpeciesCharacteristics speciesInputs; //SpeciesCharacteristics class object reference variable
      GeneralModel modEvo; //GeneralModel class object reference variable
            
      //Verify the correct number of arguments have been input     
      if(args.length == 22){
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
                     species = argSS[1];
               }else if((argStr.toLowerCase()).equals("meantraitone")){
   	               meanTraitOne = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("meantraittwo")){
                     meanTraitTwo = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("phenotypicvariancetraitone")){
                     phenotypicVarianceTraitOne = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("phenotypicvariancetraittwo")){
                     phenotypicVarianceTraitTwo = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("heritability")){
                     heritability = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("optimumtraitone")){
                     optimumTraitOne = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("optimumtraittwo")){
                     optimumTraitTwo = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("variancetraitone")){
                     varianceTraitOne = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("variancetraittwo")){
                     varianceTraitTwo = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("attenuationcoefficient")){
                     attenuationCoefficient = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("meaninterceptreactionnorm")){
                     meanInterceptReactionNorm = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("meanslopereactionnorm")){
                     meanSlopeReactionNorm = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("phenotypicvarianceinterceptreactionnorm")){
                     phenotypicVarianceInterceptReactionNorm = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("phenotypicvarianceslopereactionnorm")){
                     phenotypicVarianceSlopeReactionNorm = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("doseinitial")){
                     doseInitial = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("meanpreference")){
                     meanPreference = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("phenotypicvariancepreference")){
                     phenotypicVariancePreference = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("transmittance")){
                     transmittance = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("slopeconcentration")){
                     slopeConcentration = Double.parseDouble(argSS[1]);
               }else if((argStr.toLowerCase()).equals("numiterations")){
                     numIterations = Integer.parseInt(argSS[1]);
               }else if((argStr.toLowerCase()).equals("simpopsize")){
                     simPopSize = Integer.parseInt(argSS[1]);
               }else{
                     System.err.println("Incorrect argument string entered for arg[" + i + "], program exited.");
                     System.exit(0); //Do not run the models if incorrect input is recieved
               }//End if else if
            }//End for
         }catch(NumberFormatException e){
              System.err.println("Argument must be entered in the correct data type.");
              System.exit(1);
         }//End try, catch
         //Send the input values to the  SpeciesCharacteristics class
         speciesInputs = new SpeciesCharacteristics(species, meanTraitOne, meanTraitTwo, phenotypicVarianceTraitOne, phenotypicVarianceTraitTwo, heritability, 
                                                      optimumTraitOne, optimumTraitTwo, varianceTraitOne, varianceTraitTwo, attenuationCoefficient, meanInterceptReactionNorm, 
                                                      meanSlopeReactionNorm, phenotypicVarianceInterceptReactionNorm, phenotypicVarianceSlopeReactionNorm, doseInitial, 
                                                      meanPreference, phenotypicVariancePreference, transmittance, slopeConcentration, numIterations, simPopSize);
         //Run the general model
         modEvo = new GeneralModel(speciesInputs);                                             
         //Program complete
   		System.out.println("Evolutionary trajectories modeled.");
      }else{
         System.err.println("22 arguments are expected."); 
      }//End first if 
	}//End main method
}//End ModEvo class
