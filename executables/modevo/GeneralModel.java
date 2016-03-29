/*
	Name: Elizabeth Brooks
	File: GeneralModel
	Modified: February 4, 2016
*/

//Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneralModel{

        //Class fields to store variables
	private IndividualFitness individualFitnessObject; //Reference variable of the IndividualFitness class
	private MeanFitnessBySimulation meanFitnessObject; //Reference variable of the MeanFitnessClass
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics
	private int numIterations; //The number of iterations the user would like the models to be run for
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private double phenotypicVarianceTraitTwo; //The phenotypic variance of trait two
	private double phenotypicVarianceInterceptReactionNorm; //The phenotypic variance of the intercept of the reaction norm with respect to the trait relater
	private double phenotypicVarianceSlopeReactionNorm; //The phenotypic variance of the slope of the reaction norm with respect to the trait relater
	private double attenuationCoefficient; //The attenuation coefficient
	private double[][] heritabilityMatrix; //Matrix of heritability values
	private double meanFitness; //Mean fitness
	//Fields to hold the values for calculating each generation's values
	private double nextGenMeanTraitOne; //The mean of trait one
	private double nextGenMeanTraitTwo; //The mean of trait two
	private double nextGenMeanInterceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	private double nextGenMeanSlopeReactionNorm; //The phenotypic variance of the slope of the reaction norm with respect to the trait relater
    private double doseInitial; //The initial dose value
	private double meanPreference; //The mean preference for UV penetration of the carapace
	private double phenotypicVariancePreference; //The phenotypic variance of the mean preference
	private double transmittance; //The transmittance of a non-melanized Daphnia
	private double slopeConcentration; //The slope relating concentration of melanin to change in UVB transmittance
	//Arrays to hold each generations calculated mean trait value
	private double[] traitOneArray; //Array of mean values for the first trait
	private double[] traitTwoArray; //Array of mean values for the second trait
	private double[] slopeArray; //Array of slope values of the reaction norm
	private double[] interceptArray; //Array of intercept values of the reaction norm
	
	//The class constructor
	public GeneralModel(SpeciesCharacteristics speciesInputs)
	{
		//Initialize species characteristics
                speciesValues = speciesInputs;
		//Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		phenotypicVarianceSlopeReactionNorm = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
		nextGenMeanTraitOne = speciesValues.getMeanTraitOne();
		nextGenMeanTraitTwo = speciesValues.getMeanTraitTwo();
		phenotypicVarianceTraitTwo = speciesValues.getPhenotypicVarianceTraitTwo();
		nextGenMeanInterceptReactionNorm = speciesValues.getMeanInterceptReactionNorm();
		nextGenMeanSlopeReactionNorm = speciesValues.getMeanSlopeReactionNorm();
	        phenotypicVarianceInterceptReactionNorm = speciesValues.getPhenotypicVarianceInterceptReactionNorm();
	        phenotypicVarianceSlopeReactionNorm = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
		attenuationCoefficient = speciesValues.getAttenuationCoefficient();
                doseInitial = speciesValues.getDoseInitial();
	        meanPreference = speciesValues.getMeanPreference();
	        phenotypicVariancePreference = speciesValues.getPhenotypicVariancePreference();
	        transmittance = speciesValues.getTransmittance();
	        slopeConcentration = speciesValues.getSlopeConcentration();
		
		//Write species data to TXT file
                speciesValues.writeSpeciesFile();
      		
		//Run the model
		runModel(numIterations, simPopSize);
	}
	
	//The default class constructor
	public GeneralModel()
	{

	}
	
	//Methods to run the model
	public void runModel(int numIterationsInput, int simPopSizeInput)
	{
		//Initialize objects
		individualFitnessObject = new IndividualFitness(speciesValues);
		meanFitnessObject = new MeanFitnessBySimulation(speciesValues, nextGenMeanTraitOne, 
				nextGenMeanTraitTwo, nextGenMeanInterceptReactionNorm, nextGenMeanSlopeReactionNorm);
		
		//Begin the model looping
		setTraitArrays(numIterationsInput, simPopSizeInput);
	}
	
	//Add each generations mean trait values into the trait arrays
	public void setTraitArrays(int numIterationsInput, int simPopSizeInput)
	{
		traitOneArray = new double[numIterations];
		traitTwoArray = new double[numIterations];
		slopeArray = new double[numIterations];
		interceptArray = new double[numIterations];
		//A for loop to save each value calculated for the mean of trait one and two
		for(int i=0;i<traitOneArray.length;i++){
			traitOneArray[i] = nextGenMeanTraitOne;
			traitTwoArray[i] = nextGenMeanTraitTwo;
			slopeArray[i] = nextGenMeanSlopeReactionNorm;
			interceptArray[i] = nextGenMeanInterceptReactionNorm;
			calcNextGenFitnessAndTraitOne(numIterationsInput, simPopSizeInput);
			calcNextGenMeanTraitTwo(numIterationsInput, simPopSizeInput);
			calcNextGenMeanInterceptReactionNorm(numIterationsInput, simPopSizeInput);
			calcNextGenMeanSlopeReactionNorm(numIterationsInput, simPopSizeInput);
		}
      
      //Write to TXT file trait values
      writeTraitTwoFile();
      writeTraitOneFile();
	}
	
	//Send the new mean values of each trait to the IndividualFitness class for re calculation
	public void calcNextGenFitnessAndTraitOne(int numIterationsInput, int simPopSizeInput)
	{
		meanFitness = meanFitnessObject.getMeanFitnessSim(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, 
				nextGenMeanTraitTwo, nextGenMeanInterceptReactionNorm, nextGenMeanSlopeReactionNorm);
		nextGenMeanTraitOne = meanFitnessObject.getMeanTraitOneSim(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, 
				nextGenMeanTraitTwo, nextGenMeanInterceptReactionNorm, nextGenMeanSlopeReactionNorm);
	}
	
	//A method to calculate the mean intercept of the reaction norm
	public void calcNextGenMeanInterceptReactionNorm(int numIterationsInput, int simPopSizeInput)
	{
		//Numerically differentiate
		double fitnessInverse = (1/meanFitness);
		nextGenMeanInterceptReactionNorm += (calcHeritabilityMatrixValue("intercept")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, nextGenMeanTraitTwo)*0) + 
				(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, nextGenMeanTraitTwo)*
						phenotypicVarianceInterceptReactionNorm);
	}
	
	//A method to calculate the mean slope of the reaction norm
	public void calcNextGenMeanSlopeReactionNorm(int numIterationsInput, int simPopSizeInput)
	{
		//Numerically differentiate
		double fitnessInverse = (1/meanFitness);
		nextGenMeanSlopeReactionNorm += (calcHeritabilityMatrixValue("slope")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, nextGenMeanTraitTwo)*0) +
					(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, nextGenMeanTraitTwo)*
							calcLastPortionForSlope());	
	}
	
	//A method to calculate the mean value of trait two
	public void calcNextGenMeanTraitTwo(int numIterationsInput, int simPopSizeInput)
	{
		//Numerically differentiate
		double fitnessInverse = (1/meanFitness);
		nextGenMeanTraitTwo += (calcHeritabilityMatrixValue("trait two")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, nextGenMeanTraitTwo)) +
				 (fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(numIterationsInput, simPopSizeInput, nextGenMeanTraitOne, nextGenMeanTraitTwo)*
						 calcLastPortionForTraitTwo());
	}
	
	//A method to determine which value to return from the heritability matrix
	public double calcHeritabilityMatrixValue(String variable)
	{
		//Initialize the 2D array with 3 rows and 3 columns and set the matrix values
		heritabilityMatrix = speciesValues.getHeritabilityMatrix();		
		//Determine which matrix value to return based on String argument
		if(variable.equals("intercept")){
			return ((nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][0]) + 
					(nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][1]) +
					(nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][2]));
		}else if(variable.equals("slope")){
			return ((nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][0]) + 
					(nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][1]) +
					(nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][2]));
		}else{
			return ((nextGenMeanTraitTwo * heritabilityMatrix[2][0]) + 
					(nextGenMeanTraitTwo * heritabilityMatrix[2][1]) +
					(nextGenMeanTraitTwo * heritabilityMatrix[2][2]));
		}
	}
	
	//A method to calculate the partial derivative of trait one with respect to individual fitness
	//For the mean of the slope of the reaction norm
	public double calcLastPortionForSlope()
	{
		double exponent = (-nextGenMeanTraitTwo*attenuationCoefficient);
		double lastPortionForSlope = (((doseInitial*(Math.pow(Math.E, exponent)))-doseInitial)*phenotypicVarianceSlopeReactionNorm);
		return lastPortionForSlope;
	}
	
	
	//A method to calculate the partial derivative of trait one with respect to individual fitness
	//For the mean of trait two
	public double calcLastPortionForTraitTwo()
	{
		double exponent = (-nextGenMeanTraitTwo*attenuationCoefficient);
		double lastPortionForTraitTwo = (attenuationCoefficient*nextGenMeanSlopeReactionNorm*doseInitial*(Math.pow(Math.E, exponent))*phenotypicVarianceTraitTwo);
		return lastPortionForTraitTwo;
	}
   
   //Method to write trait one values to a TXT file
   public void writeTraitOneFile()
   {
   	//Write to file the first traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         int fileCount = 1;
         String meanTraitOnePath = "meanTraitOneValues_GeneralModel_" + fileCount + ".txt";
         File meanTraitOneFile = new File(meanTraitOnePath);         
         if (meanTraitOneFile.exists()){
            //Loop through the existing files
			   while(meanTraitOneFile.exists()){
		   	   fileCount++;
               meanTraitOnePath = "meanTraitOneValues_GeneralModel_" + fileCount + ".txt";
               meanTraitOneFile = new File(meanTraitOnePath);
		   	}
            //Create meanTraitOneFile and file writer
            FileWriter fwOne = new FileWriter(meanTraitOneFile.getAbsoluteFile()); 
				meanTraitOneFile.createNewFile();
            //Write to file the header
			   fwOne.write("Generation,MeanTraitOne\n");
			   String aOne;
			   String bOne;			
			   for(int i=0, k=1;i<traitOneArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aOne = Integer.toString(k);
			   	fwOne.append(aOne);
			   	fwOne.append(" ");
			   	bOne = Double.toString(traitOneArray[i]);
			   	fwOne.append(bOne);
			   	fwOne.append("\n");
			   }			
			   //Close the file
			   fwOne.close();
         }else if(!meanTraitOneFile.exists()){
            //Create meanTraitOneFile and file writer
            FileWriter fwOne = new FileWriter(meanTraitOneFile.getAbsoluteFile()); 
				meanTraitOneFile.createNewFile();
            //Write to file the header
			   fwOne.write("Generation,MeanTraitOne\n");
			   String aOne;
			   String bOne;			
			   for(int i=0, k=1;i<traitOneArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aOne = Integer.toString(k);
			   	fwOne.append(aOne);
			   	fwOne.append(" ");
			   	bOne = Double.toString(traitOneArray[i]);
			   	fwOne.append(bOne);
			   	fwOne.append("\n");
			   }			
			   //Close the file
			   fwOne.close();
			}else{
            //Display error message
            System.out.println("Error in file naming, GeneralModel");
            System.exit(0);
         }		
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }
		
   //Method to write trait one values to a TXT file
   public void writeTraitTwoFile()
   {
		//Write to file the second traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         int fileCount = 1;
         String meanTraitTwoPath = "meanTraitTwoValues_GeneralModel_" + fileCount + ".txt";
         File meanTraitTwoFile = new File(meanTraitTwoPath);
			if (meanTraitTwoFile.exists()){
            //Loop through the existing files
			   while(meanTraitTwoFile.exists()){
				   fileCount++;
               meanTraitTwoPath = "meanTraitTwoValues_GeneralModel_" + fileCount + ".txt";
               meanTraitTwoFile = new File(meanTraitTwoPath);
			   }
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanTraitTwoFile.getAbsoluteFile()); 
				meanTraitTwoFile.createNewFile();
            //Write to file the header
			   fwTwo.write("Generation,MeanTraitTwo\n");
			   String aTwo;
			   String bTwo;			
			   for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aTwo = Integer.toString(k);
			   	fwTwo.append(aTwo);
			   	fwTwo.append(" ");
			   	bTwo = Double.toString(traitTwoArray[i]);
			   	fwTwo.append(bTwo);
			   	fwTwo.append("\n");	
			   }			
			   //Close the file
			   fwTwo.close();
         }else if(!meanTraitTwoFile.exists()){
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanTraitTwoFile.getAbsoluteFile()); 
				meanTraitTwoFile.createNewFile();
            //Write to file the header
			   fwTwo.write("Generation,MeanTraitTwo\n");
			   String aTwo;
			   String bTwo;			
			   for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aTwo = Integer.toString(k);
			   	fwTwo.append(aTwo);
			   	fwTwo.append(" ");
			   	bTwo = Double.toString(traitTwoArray[i]);
			   	fwTwo.append(bTwo);
			   	fwTwo.append("\n");	
			   }			
			   //Close the file
			   fwTwo.close();	
			}else{
            //Display error message
            System.out.println("Error in file naming, GeneralModel");
            System.exit(0);
         }		
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	}
	
	//Methods to retrieve the mean values of each trait as well as the slope and intercept of the reaction norm
	public double getNextGenMeanInterceptReactionNorm()
	{
		return nextGenMeanInterceptReactionNorm;
	}
	
	public double getNextGenMeanSlopeReactionNorm()
	{
		return nextGenMeanSlopeReactionNorm;
	}
	
	public double getNextGenMeanTraitTwo()
	{
		return nextGenMeanTraitTwo;
	}
	
	public double getNextGenMeanTraitOne()
	{
		return nextGenMeanTraitOne;
	}
	
	public double getVarianceIntercept()
	{
		return phenotypicVarianceInterceptReactionNorm;
	}
	
	public double getVarianceSlope()
	{
		return phenotypicVarianceSlopeReactionNorm;
	}
	
	public int getSimPopSize() {
		return simPopSize;
	}
	
	//Getter methods for IndividualTraitOne and Two
	public int getSimPopSizeInitial() {
		return simPopSize;
	}
	
	public int getNumIterationsInitial() {
		return numIterations;
	}
	
	public double getDose()
	{
		return doseInitial;
	}
	
	public double getInterceptReactionNormInitial()
	{
		return nextGenMeanInterceptReactionNorm;
	}
	
	public double getSlopeReactionNormInitial()
	{
		return nextGenMeanSlopeReactionNorm;
	}
	
	public double getAttenuationCoefficientInitial()
	{
		return attenuationCoefficient;
	}
	
	public double getMeanTraitTwoInitial()
	{
		return nextGenMeanTraitTwo;
	}
	
	public double getPhenotypicVarianceTraitTwoInitial()
	{
		return phenotypicVarianceTraitTwo;
	}

	public double getPreferenceInitial() {
		return meanPreference;
	}
	
	public double getMeanInterceptReactionNormInitial() {
		return nextGenMeanInterceptReactionNorm;
	}
	
	public double getMeanSlopeReactionNormInitial() {
		return nextGenMeanSlopeReactionNorm;
	}
	
	public double getVarianceInterceptInitial() {
		return phenotypicVarianceInterceptReactionNorm;
	}

	public double getVarianceSlopeInitial() {
		return phenotypicVarianceSlopeReactionNorm;
	}
}
