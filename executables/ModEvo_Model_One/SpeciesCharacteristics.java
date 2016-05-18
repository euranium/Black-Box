/*
	Name: Elizabeth Brooks
	File: SpeciesCharacteristics
   Modified: April 13, 2016
*/

//Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//The interface SpeciesCharacteristics
public class SpeciesCharacteristics {

   //Class fields to store variables
	private String species; //Name of the organism under investigation
	private double meanTraitOne; //Mean of trait one
	private double meanTraitTwo; //Mean of trait two
	private double phenotypicVarianceTraitOne; //Phernotypic variance of trait one
	private double phenotypicVarianceTraitTwo; //Phenotypic variance of trait two
	private double heritability; //Heritability
	private double optimumTraitOne; //Optimum value of trait one
	private double optimumTraitTwo; //Optimum value of trait two
	private double varianceTraitOne; //Variance of the Gaussian function relating trait one to fitness
	private double varianceTraitTwo; //Variance of the Gaussian function relating trait two to fitness
	private double meanInterceptReactionNorm; //The mean intercept of the reaction norm
	private double meanSlopeReactionNorm; //The mean slope of the reaction norm
	private double phenotypicVarianceInterceptReactionNorm; //The phenotypic variance of the slope of the reaction norm
	private double phenotypicVarianceSlopeReactionNorm; //The phenotypic variance of the slope of the reaction norm
   private int numIterations; //The number of iterations the user would like the program run
	private int simPopSize; //The population size of the simulated populations
   private double[][] heritabilityMatrix; //Heritability matrix
   //NOTE: Specific to Daphnia
   private double attenuationCoefficient; //The attenuation coefficient
   private double doseInitial; //The initial dose value
   private double meanFunctionTrait; //The mean preference for UV penetration of the carapace
   private double phenotypicVarianceFunctionTrait; //The phenotypic variance of the mean preference
   private double transmittance; //The transmittance of a non-melanized Daphnia
   private double slopeConcentration; //The slope relating concentration of melanin to change in UVB transmittance
	private String distributionName; //The user selected distribution
   //private double[] distributionArgs; //Array to store the arguments of the selected distribution
   //Class constructor to receive user input and set initial values for the model
   public SpeciesCharacteristics(String speciesInput, double meanTraitOneInput, double meanTraitTwoInput, double phenotypicVarianceTraitOneInput, 
      double phenotypicVarianceTraitTwoInput, double heritabilityInput, double optimumTraitOneInput, double optimumTraitTwoInput, double varianceTraitOneInput, 
      double varianceTraitTwoInput, double attenuationCoefficientInput, double meanInterceptReactionNormInput, double meanSlopeReactionNormInput, 
      double phenotypicVarianceInterceptReactionNormInput, double phenotypicVarianceSlopeReactionNormInput, double doseInitialInput, double meanFunctionTraitInput, 
      double phenotypicVarianceFunctionTraitInput, double transmittanceInput, double slopeConcentrationInput, int numIterationsInput, int simPopSizeInput, 
      String distributionNameInput)
	{
      //Initialize fields with input values
      species = speciesInput;
      meanTraitOne = meanTraitOneInput;
      meanTraitTwo = meanTraitTwoInput;
      phenotypicVarianceTraitOne = phenotypicVarianceTraitOneInput;
      phenotypicVarianceTraitTwo = phenotypicVarianceTraitTwoInput;
      heritability = heritabilityInput;
      optimumTraitOne = optimumTraitOneInput;
      optimumTraitTwo = optimumTraitTwoInput;
      varianceTraitOne = varianceTraitOneInput;
      varianceTraitTwo = varianceTraitTwoInput;
      attenuationCoefficient = attenuationCoefficientInput;
      meanInterceptReactionNorm = meanInterceptReactionNormInput;
      meanSlopeReactionNorm = meanSlopeReactionNormInput;
      phenotypicVarianceInterceptReactionNorm = phenotypicVarianceInterceptReactionNormInput;
      phenotypicVarianceSlopeReactionNorm = phenotypicVarianceSlopeReactionNormInput;
      doseInitial = doseInitialInput;
      meanFunctionTrait = meanFunctionTraitInput;
      phenotypicVarianceFunctionTrait = phenotypicVarianceFunctionTraitInput;
      transmittance = transmittanceInput;
      slopeConcentration = slopeConcentrationInput;
      numIterations = numIterationsInput;
      simPopSize = simPopSizeInput;
      distributionName = distributionNameInput;
      /*distributionArgs = new int[distributionArgsInput.length];
      System.arraycopy(distributionArgsInput, 0, distributionArgs, 0, distributionArgsInput.length);*/
      //Initialize the 2D array with 3 rows and 3 columns and set the matrix values
      heritabilityMatrix = new double[][]{
         {0.5, 0, 0},
	      {0, 0.5, 0},
	      {0, 0, 0.5}
      };
   }

   //Method to write species data to TXT file
   public void writeSpeciesFile()
   {
      //Write to file inputs
      //Catch exceptions and write to file in TXT format
      try {
         //Determine which test number is being run for file naming
         int fileCount = 1;
         String speciesPath = "speciesInputs_GeneralModel.txt";
         File speciesFile = new File(speciesPath);
         if (speciesFile.exists()){
            /*//Loop through the existing files
            while(speciesFile.exists()){
               fileCount++;
               speciesPath = "speciesInputs_GeneralModel.txt";
               speciesFile = new File(speciesPath);
            }*/            
            //Create meanTraitOneFile and file writer
            speciesFile.createNewFile();
            FileWriter fw = new FileWriter(speciesFile.getAbsoluteFile()); 
            //Write to file the header
            fw.write("VariableName,VariableValue\n");
            String b;
            //Write to file VariableName,VariableValue
            fw.append("Species");
            fw.append(" ");
            b = species;
            fw.append(b);
            fw.append("\n");

            fw.append("MeanTraitOne");
            fw.append(" ");
            b = Double.toString(meanTraitOne);
            fw.append(b);
            fw.append("\n");
                     
            fw.append("MeanTraitTwo");
            fw.append(" ");
            b = Double.toString(meanTraitTwo);
            fw.append(b);
            fw.append("\n");
         
            fw.append("PhenotypicVarianceTraitOne");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceTraitOne);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceTraitTwo");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("Heritability");
            fw.append(" ");
            b = Double.toString(heritability);
            fw.append(b);
            fw.append("\n");

            fw.append("OptimumTraitOne");
            fw.append(" ");
            b = Double.toString(optimumTraitOne);
            fw.append(b);
            fw.append("\n");

            fw.append("OptimumTraitTwo");
            fw.append(" ");
            b = Double.toString(optimumTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("VarianceTraitOne");
            fw.append(" ");
            b = Double.toString(varianceTraitOne);
            fw.append(b);
            fw.append("\n");

            fw.append("VarianceTraitTwo");
            fw.append(" ");
            b = Double.toString(varianceTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("AttenuationCoefficient");
            fw.append(" ");
            b = Double.toString(attenuationCoefficient);
            fw.append(b);
            fw.append("\n");

            fw.append("MeanInterceptReactionNorm");
            fw.append(" ");
            b = Double.toString(meanInterceptReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("MeanSlopeReactionNorm");
            fw.append(" ");
            b = Double.toString(meanSlopeReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceInterceptReactionNorm");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceInterceptReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceSlopeReactionNorm");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceSlopeReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("NumberIterations");
            fw.append(" ");
            b = Double.toString(numIterations);
            fw.append(b);
            fw.append("\n");
         
            fw.append("SimulatedPopulationSize");
            fw.append(" ");
            b = Double.toString(simPopSize);
            fw.append(b);
            fw.append("\n");  

            fw.append("DoesInitial");
            fw.append(" ");
            b = Double.toString(doseInitial);
            fw.append(b);
            fw.append("\n");

            fw.append("MeanFunctionTrait");
            fw.append(" ");
            b = Double.toString(meanFunctionTrait);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceFunctionTrait");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceFunctionTrait);
            fw.append(b);
            fw.append("\n");

            fw.append("Transmittance");
            fw.append(" ");
            b = Double.toString(transmittance);
            fw.append(b);
            fw.append("\n");

            fw.append("SlopeConcentration");
            fw.append(" ");
            b = Double.toString(slopeConcentration);
            fw.append(b);
            fw.append("\n");

            fw.append("Distribution");
            fw.append(" ");
            b = distributionName;
            fw.append(b);
            fw.append("\n");              
            //Close the file
            fw.close();
         }else if(!speciesFile.exists()){
            //Create meanTraitOneFile and file writer
            speciesFile.createNewFile();
            FileWriter fw = new FileWriter(speciesFile.getAbsoluteFile()); 
            //Write to file the header
            fw.write("VariableName,VariableValue\n");
            String b;
            //Write to file VariableName,VariableValue
            fw.append("Species");
            fw.append(" ");
            b = species;
            fw.append(b);
            fw.append("\n");

            fw.append("MeanTraitOne");
            fw.append(" ");
            b = Double.toString(meanTraitOne);
            fw.append(b);
            fw.append("\n");
                     
            fw.append("MeanTraitTwo");
            fw.append(" ");
            b = Double.toString(meanTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceTraitOne");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceTraitOne);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceTraitTwo");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("Heritability");
            fw.append(" ");
            b = Double.toString(heritability);
            fw.append(b);
            fw.append("\n");

            fw.append("OptimumTraitOne");
            fw.append(" ");
            b = Double.toString(optimumTraitOne);
            fw.append(b);
            fw.append("\n");

            fw.append("OptimumTraitTwo");
            fw.append(" ");
            b = Double.toString(optimumTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("VarianceTraitOne");
            fw.append(" ");
            b = Double.toString(varianceTraitOne);
            fw.append(b);
            fw.append("\n");

            fw.append("VarianceTraitTwo");
            fw.append(" ");
            b = Double.toString(varianceTraitTwo);
            fw.append(b);
            fw.append("\n");

            fw.append("AttenuationCoefficient");
            fw.append(" ");
            b = Double.toString(attenuationCoefficient);
            fw.append(b);
            fw.append("\n");

            fw.append("MeanInterceptReactionNorm");
            fw.append(" ");
            b = Double.toString(meanInterceptReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("MeanSlopeReactionNorm");
            fw.append(" ");
            b = Double.toString(meanSlopeReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceInterceptReactionNorm");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceInterceptReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceSlopeReactionNorm");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceSlopeReactionNorm);
            fw.append(b);
            fw.append("\n");

            fw.append("NumberIterations");
            fw.append(" ");
            b = Double.toString(numIterations);
            fw.append(b);
            fw.append("\n");
         
            fw.append("SimulatedPopulationSize");
            fw.append(" ");
            b = Double.toString(simPopSize);
            fw.append(b);
            fw.append("\n");  

            fw.append("DoesInitial");
            fw.append(" ");
            b = Double.toString(doseInitial);
            fw.append(b);
            fw.append("\n");

            fw.append("MeanFunctionTrait");
            fw.append(" ");
            b = Double.toString(meanFunctionTrait);
            fw.append(b);
            fw.append("\n");

            fw.append("PhenotypicVarianceFunctionTrait");
            fw.append(" ");
            b = Double.toString(phenotypicVarianceFunctionTrait);
            fw.append(b);
            fw.append("\n");

            fw.append("Transmittance");
            fw.append(" ");
            b = Double.toString(transmittance);
            fw.append(b);
            fw.append("\n");

            fw.append("SlopeConcentration");
            fw.append(" ");
            b = Double.toString(slopeConcentration);
            fw.append(b);
            fw.append("\n");

            fw.append("Distribution");
            fw.append(" ");
            b = distributionName;
            fw.append(b);
            fw.append("\n");
            //Close the file
            fw.close();
         }else{
            //Display error message
            System.out.println("Error in file naming, SpeciesCharacteristics");
            System.exit(0);
         }        
      } catch (IOException e) {
         System.err.format("IOException: %s%n", e);
      }

   }


	//Getter methods to retrieve the values stored in the class fields
	public double getMeanTraitOne(){
      return meanTraitOne;
   }

	public double getMeanTraitTwo(){
      return meanTraitTwo;
   }

	public double getPhenotypicVarianceTraitOne(){
      return phenotypicVarianceTraitOne;
   }

	public double getPhenotypicVarianceTraitTwo(){
      return phenotypicVarianceTraitTwo;
   }

	public double getHeritability(){
      return heritability;
   }

	public double getOptimumTraitOne(){
      return optimumTraitOne;
   }

	public double getOptimumTraitTwo(){
      return optimumTraitTwo;
   }

	public double getVarianceTraitOne(){
      return varianceTraitOne;
   }

	public double getVarianceTraitTwo(){
      return varianceTraitTwo;
   }

	public double getPhenotypicVarianceSlopeReactionNorm(){
      return phenotypicVarianceSlopeReactionNorm;
   }

	public double getPhenotypicVarianceInterceptReactionNorm(){
      return phenotypicVarianceInterceptReactionNorm;
   }

	public double getMeanInterceptReactionNorm(){
      return meanInterceptReactionNorm;
   }

	public double getMeanSlopeReactionNorm(){
      return meanSlopeReactionNorm;
   }
   
   public double[][] getHeritabilityMatrix(){
      return heritabilityMatrix;
   }

	public int getNumIterations(){
      return numIterations;
   }

	public int getSimPopSize(){
      return simPopSize;
   }

   public String getDistributionName(){
      return distributionName;
   }

   /*public double[] getDistributionArgs(){
      return distributionArgs;
   }*/

	//NOTE: Specific to Daphnia
   public  double getDoseInitial(){
      return doseInitial;
   }

	public  double getAttenuationCoefficient(){
      return attenuationCoefficient;
   }

	public  double getMeanFunctionTrait(){
      return meanFunctionTrait;
   }
   
	public  double getPhenotypicVarianceFunctionTrait(){
      return phenotypicVarianceFunctionTrait;
   }

	public  double getTransmittance(){
      return transmittance;
   }
   
	public  double getSlopeConcentration(){
      return slopeConcentration;
   }
   
   //Setter methods to retrieve the values stored in the class fields
	public void setMeanTraitOne(double meanTraitOneInput){
      meanTraitOne = meanTraitOneInput;
   }

	public void setMeanTraitTwo(double meanTraitTwoInput){
      meanTraitTwo = meanTraitTwoInput;
   }

	public void setPhenotypicVarianceTraitOne(double phenotypicVarianceTraitOneInput){
      phenotypicVarianceTraitOne = phenotypicVarianceTraitOneInput;
   }

	public void setPhenotypicVarianceTraitTwo(double phenotypicVarianceTraitTwoInput){
      phenotypicVarianceTraitTwo = phenotypicVarianceTraitTwoInput;
   }

	public void setHeritability(double heritabilityInput){
      heritability = heritabilityInput;
   }

	public void setOptimumTraitOne(double optimumTraitOneInput){
      optimumTraitOne = optimumTraitOneInput;
   }

	public void setOptimumTraitTwo(double optimumTraitTwoInput){
      optimumTraitTwo = optimumTraitTwoInput;
   }

	public void setVarianceTraitOne(double varianceTraitOneInput){
      varianceTraitOne = varianceTraitOneInput;
   }

	public void setVarianceTraitTwo(double varianceTraitTwoInput){
      varianceTraitTwo = varianceTraitTwoInput;
   }

	public void setPhenotypicVarianceSlopeReactionNorm(double phenotypicVarianceSlopeReactionNormInput){
      phenotypicVarianceSlopeReactionNorm = phenotypicVarianceSlopeReactionNormInput;
   }

	public void setPhenotypicVarianceInterceptReactionNorm(double phenotypicVarianceInterceptReactionNormInput){
      phenotypicVarianceInterceptReactionNorm = phenotypicVarianceInterceptReactionNormInput;
   }

	public void setMeanInterceptReactionNorm(double meanInterceptReactionNormInput){
      meanInterceptReactionNorm = meanInterceptReactionNormInput;
   }

	public void setMeanSlopeReactionNorm(double meanSlopeReactionNormInput){
      meanSlopeReactionNorm = meanSlopeReactionNormInput;
   }
   
   public void setHeritabilityMatrix(double[][] heritabilityMatrixInput){
      heritabilityMatrix = heritabilityMatrixInput;
   }

	public void setNumIterations(int numIterationsInput){
      numIterations = numIterationsInput;
   }
	
	public void setSimPopSize(int simPopSizeInput){
      simPopSize = simPopSizeInput;
   }

   public void setDistributionName(String distributionNameInput){
      distributionName = distributionNameInput;
   }

   /*public void setDistributionArgs(double[] distributionArgsInput){
      distributionArgs = new int[distributionArgsInput.length];
      System.arraycopy(distributionArgsInput, 0, distributionArgs, 0, distributionArgsInput.length);
   }*/
   
	//NOTE: Specific to Daphnia
   public void setDoseInitial(double doseInitialInput){
      doseInitial = doseInitialInput;
   }

	public void setAttenuationCoefficient(double attenuationCoefficientInput){
      attenuationCoefficient = attenuationCoefficientInput;
   }

	public void setMeanFunctionTrait(double meanFunctionTraitInput){
      meanFunctionTrait = meanFunctionTraitInput;
   }

	public void setPhenotypicVarianceFunctionTrait(double phenotypicVarianceFunctionTraitInput){
      phenotypicVarianceFunctionTrait = phenotypicVarianceFunctionTraitInput;
   }

	public void setTransmittance(double transmittanceInput){
      transmittance = transmittanceInput;
   }

	public void setSlopeConcentration(double slopeConcentrationInput){
      slopeConcentration = slopeConcentrationInput;
   }
}
