/*
	Name: Elizabeth Brooks
	File: GeneralModel
	Modified: May 11, 2016
*/

//Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Class for modeling the evolutionary trajectories of a species with respect to multiple phenotypic traits
//while also accounting for underlying developmental interactions
public class ModelTwo {

    //Class fields to store variables
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
    private MeanTraitOne meanTraitOneObject; //Reference variable of the MeanTraitOne class
	private IndividualFitness individualFitnessObject; //A reference variable of the IndividualFitness class
	private MeanFitness meanFitnessObject; //A reference variable of the MeanFitness class
	private int numIterations; //The number of iterations the user would like the models to be run for
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private String meanSelection; //The user selection of mean trait one analyzation method
	private double phenotypicVarianceTraitOne; //The phenotypic variance of trait one
	private double phenotypicVarianceTraitTwo; //The phenotypic variance of trait two
	private double phenotypicVarianceInterceptReactionNorm; //The phenotypic variance of the intercept of the reaction norm with respect to the trait relater
	private double phenotypicVarianceSlopeReactionNorm; //The phenotypic variance of the slope of the reaction norm with respect to the trait relater
	private double doseInitial; //The initial value of the trait relater
	private double attenuationCoefficient; //The attenuation coefficient
	private double[][] heritabilityMatrix;
	private double meanFitness; //Mean fitness
	//Fields to hold the values for calculating each generation's values
	private double nextGenMeanTraitOne; //The mean of trait one
	private double nextGenMeanTraitTwo; //The mean of trait two
	private double nextGenMeanInterceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	private double nextGenMeanSlopeReactionNorm; //The phenotypic variance of the slope of the reaction norm with respect to the trait relater
	//Arrays to hold each generations calculated mean model values
	private double[] fitnessArray;
	private double[] traitOneArray;
	private double[] traitTwoArray;
	private double[] slopeArray;
	private double[] interceptArray;
   
	//The class constructor
	public ModelTwo(SpeciesCharacteristics speciesInputs)
	{
		//Initialize species characteristics
        speciesValues = speciesInputs;
		//Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		meanSelection = speciesValues.getMeanSelection();
		phenotypicVarianceSlopeReactionNorm = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
		nextGenMeanTraitOne = speciesValues.getMeanTraitOne();
		nextGenMeanTraitTwo = speciesValues.getMeanTraitTwo();
		phenotypicVarianceTraitOne = speciesValues.getPhenotypicVarianceTraitOne();
		phenotypicVarianceTraitTwo = speciesValues.getPhenotypicVarianceTraitTwo();
		nextGenMeanInterceptReactionNorm = speciesValues.getMeanInterceptReactionNorm();
		nextGenMeanSlopeReactionNorm = speciesValues.getMeanSlopeReactionNorm();
	    phenotypicVarianceInterceptReactionNorm = speciesValues.getPhenotypicVarianceInterceptReactionNorm();
	    phenotypicVarianceSlopeReactionNorm = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
	}
	
	//Methods to run the model
	public void runModel()
	{
		//Initialize objects
		meanTraitOneObject = new MeanTraitOne(speciesValues);
		individualFitnessObject = new IndividualFitness(speciesValues);
		meanFitnessObject = new MeanFitness(speciesValues);
		//Begin the model looping
		setTraitArrays();
	}

	//Add each generations mean trait values into the trait arrays
	public void setTraitArrays()
	{
		fitnessArray = new double [numIterations];
		traitOneArray = new double[numIterations];
		traitTwoArray = new double[numIterations];
		slopeArray = new double[numIterations];
		interceptArray = new double[numIterations];
		//A for loop to save each value calculated for the mean model values
		for(int i=0;i<numIterations;i++){
			fitnessArray[i] = meanFitness;
			traitOneArray[i] = nextGenMeanTraitOne;
			traitTwoArray[i] = nextGenMeanTraitTwo;
			slopeArray[i] = nextGenMeanSlopeReactionNorm;
			interceptArray[i] = nextGenMeanInterceptReactionNorm;
			calcNextGenFitness();
			calcNextGenMeanTraitOne();
			calcNextGenMeanTraitTwo();
			calcNextGenMeanInterceptReactionNorm();
			calcNextGenMeanSlopeReactionNorm();
		}
      	//Write to CSV file trait values
      	writeFitnessFile();
      	writeTraitOneFile();
      	writeTraitTwoFile();
      	writeSlopeFile();
      	writeInterceptFile();
   }
	
	//Send the new mean values of each trait to the MeanFitness class for re calculation
	public void calcNextGenFitness()
	{
		meanFitness = meanFitnessObject.getMeanFitnessSim(nextGenMeanTraitOne, nextGenMeanTraitTwo, 
			nextGenMeanInterceptReactionNorm, nextGenMeanSlopeReactionNorm);
	}

	//Send the new mean values of each trait to the MeanFitness class for re calculation
	public void calcNextGenMeanTraitOne()
	{
		nextGenMeanTraitOne = meanTraitOneObject.getMeanTraitOne(nextGenMeanTraitTwo, 
			nextGenMeanInterceptReactionNorm, nextGenMeanSlopeReactionNorm, meanSelection);
	}

	//A method to calculate the mean value of trait two
	public void calcNextGenMeanTraitTwo()
	{
		//Numerically differentiate
		double fitnessInverse = (1/meanFitness);
		nextGenMeanTraitTwo += (calcHeritabilityMatrixValue("trait two")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)) +
				 (fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)
				 *calcLastPortionForTraitTwo());
	}
	
	//A method to calculate the mean intercept of the reaction norm
	public void calcNextGenMeanInterceptReactionNorm()
	{
		//Numerically differentiate
		double fitnessInverse = (1/meanFitness);
		nextGenMeanInterceptReactionNorm += (calcHeritabilityMatrixValue("intercept")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*0) + 
				(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)
					*phenotypicVarianceInterceptReactionNorm);
	}
	
	//A method to calculate the mean slope of the reaction norm
	public void calcNextGenMeanSlopeReactionNorm()
	{
		//Numerically differentiate
		double fitnessInverse = (1/meanFitness);
		nextGenMeanSlopeReactionNorm += (calcHeritabilityMatrixValue("slope")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*0) +
					(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)
						*calcLastPortionForSlope());	
	}
	
	//A method to determine which value to return from the heritability matrix
	public double calcHeritabilityMatrixValue(String variableSelection)
	{
		//Initialize the 2D array with 3 rows and 3 columns and set the matrix values
		heritabilityMatrix = speciesValues.getHeritabilityMatrix();		
		//Determine which matrix value to return based on String argument
		if(variableSelection.equals("intercept")){
			return ((nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][0]) + 
					(nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][1]) +
					(nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][2]));
		}else if(variableSelection.equals("slope")){
			return ((nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][0]) + 
					(nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][1]) +
					(nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][2]));
		}else{
			return ((nextGenMeanTraitTwo * heritabilityMatrix[2][0]) + 
					(nextGenMeanTraitTwo * heritabilityMatrix[2][1]) +
					(nextGenMeanTraitTwo * heritabilityMatrix[2][2]));
		}
	}
	
	//A method to calculate the partial derivative of trait one with respect to fitness
	//For the mean of the slope of the reaction norm
	public double calcLastPortionForSlope()
	{
		double exponent = (-nextGenMeanTraitTwo*attenuationCoefficient);
		double lastPortionForSlope = (((doseInitial*(Math.pow(Math.E, exponent)))-doseInitial)*phenotypicVarianceSlopeReactionNorm);
		return lastPortionForSlope;
	}
	
	
	//A method to calculate the partial derivative of trait one with respect to fitness
	//For the mean of trait two
	public double calcLastPortionForTraitTwo()
	{
		double exponent = (-nextGenMeanTraitTwo*attenuationCoefficient);
		double lastPortionForTraitTwo = (attenuationCoefficient*nextGenMeanSlopeReactionNorm*doseInitial*(Math.pow(Math.E, exponent))*phenotypicVarianceTraitTwo);
		return lastPortionForTraitTwo;
	}

	//Method to write trait one values to a TXT file
    public void writeFitnessFile()
    {
   	//Write to file the first traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanFitnessPath = "meanFitnessValues_ModelTwo.txt";
         File meanFitnessFile = new File(meanFitnessPath);         
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanFitnessFile.getAbsoluteFile()); 
			meanFitnessFile.createNewFile();
            //Write to file the header
			fwTwo.write("Generation MeanFitness\n");
			String aOne;
			String bOne;			
			for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
				//Write to file generationNumber, traitOne
			   	aOne = Integer.toString(k);
			   	fwTwo.append(aOne);
			   	fwTwo.append(" ");
			   	bOne = Double.toString(fitnessArray[i]);
			   	fwTwo.append(bOne);
			   	fwTwo.append("\n");
			}			
			//Close the file
			fwTwo.close();
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }

	//Method to write trait one values to a TXT file
    public void writeTraitOneFile()
    {
   	//Write to file the first traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanTraitOnePath = "meanTraitOneValues_ModelTwo.txt";
         File meanTraitOneFile = new File(meanTraitOnePath);         
         /*if (meanTraitOneFile.exists()){
            //Loop through the existing files
			   while(meanTraitOneFile.exists()){
		   	   fileCount++;
               meanTraitOnePath = "meanTraitOneValues_GeneralModel.txt";
               meanTraitOneFile = new File(meanTraitOnePath);
		   	}*/
            //Create meanTraitOneFile and file writer
            FileWriter fwOne = new FileWriter(meanTraitOneFile.getAbsoluteFile()); 
				meanTraitOneFile.createNewFile();
            //Write to file the header
			   fwOne.write("Generation MeanTraitOne\n");
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
         /*}else if(!meanTraitOneFile.exists()){
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
         }*/		
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }

   //Method to write trait one values to a TXT file
    public void writeTraitTwoFile()
    {
   	//Write to file the first traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanTraitTwoPath = "meanTraitTwoValues_ModelTwo.txt";
         File meanTraitTwoFile = new File(meanTraitTwoPath);         
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanTraitTwoFile.getAbsoluteFile()); 
			meanTraitTwoFile.createNewFile();
            //Write to file the header
			fwTwo.write("Generation MeanTraitTwo\n");
			String aOne;
			String bOne;			
			for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
				//Write to file generationNumber, traitOne
			   	aOne = Integer.toString(k);
			   	fwTwo.append(aOne);
			   	fwTwo.append(" ");
			   	bOne = Double.toString(traitTwoArray[i]);
			   	fwTwo.append(bOne);
			   	fwTwo.append("\n");
			}			
			//Close the file
			fwTwo.close();
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }
		
   //Method to write trait one values to a TXT file
   public void writeSlopeFile()
   {
		//Write to file the second traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanSlopePath = "meanSlopeValues_ModelTwo.txt";
         File meanSlopeFile = new File(meanSlopePath);
            //Create meanTraitOneFile and file writer
            FileWriter fwS = new FileWriter(meanSlopeFile.getAbsoluteFile()); 
			meanSlopeFile.createNewFile();
            //Write to file the header
			fwS.write("Generation MeanSlope\n");
			String aTwo;
			String bTwo;			
			for(int i=0, k=1;i<slopeArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aTwo = Integer.toString(k);
			   	fwS.append(aTwo);
			   	fwS.append(" ");
			   	bTwo = Double.toString(slopeArray[i]);
			   	fwS.append(bTwo);
			   	fwS.append("\n");	
			}			
			//Close the file
			fwS.close();
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	}

	//Method to write trait one values to a TXT file
   public void writeInterceptFile()
   {
		//Write to file the second traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanInterceptPath = "meanInterceptValues_ModelTwo.txt";
         File meanInterceptFile = new File(meanInterceptPath);
            //Create meanTraitOneFile and file writer
            FileWriter fwI = new FileWriter(meanInterceptFile.getAbsoluteFile()); 
			meanInterceptFile.createNewFile();
            //Write to file the header
			fwI.write("Generation MeanIntercept\n");
			String aTwo;
			String bTwo;			
			for(int i=0, k=1;i<interceptArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aTwo = Integer.toString(k);
			   	fwI.append(aTwo);
			   	fwI.append(" ");
			   	bTwo = Double.toString(interceptArray[i]);
			   	fwI.append(bTwo);
			   	fwI.append("\n");	
			}			
			//Close the file
			fwI.close();
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	}
	
	//Getter methods 
   public int getSimPopSizeInitial() 
   {
		return simPopSize;
	}
	
	public int getNumIterationsInitial() 
   {
		return numIterations;
	}	
	
   public double getMeanTraitOneCurrent()
	{
		return nextGenMeanTraitOne;
	}
	
	public double getMeanTraitTwoCurrent()
	{
		return nextGenMeanTraitTwo;
	}
	
	public double getMeanInterceptReactionNormCurrent() 
   {
		return nextGenMeanInterceptReactionNorm;
	}
	
	public double getMeanSlopeReactionNormCurrent() 
   {
		return nextGenMeanSlopeReactionNorm;
	}

   public double getVarianceTraitOneInitial()
	{
		return phenotypicVarianceTraitOne;
	}
   
   public double getVarianceTraitTwoInitial()
	{
		return phenotypicVarianceTraitTwo;
	}
	
	public double getVarianceInterceptInitial() 
   {
		return phenotypicVarianceInterceptReactionNorm;
	}

	public double getVarianceSlopeInitial() 
   {
		return phenotypicVarianceSlopeReactionNorm;
	}
   
   //Setter methods 
   public void setSimPopSizeInitial(int simPopSizeInput) 
   {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterationsInitial(int numIterationsInput) 
   {
		numIterations = numIterationsInput;
	}	
	
	public void setMeanTraitOneCurrent(double nextGenMeanTraitOneInput)
	{
		nextGenMeanTraitOne = nextGenMeanTraitOneInput;
	}
	
	public void setMeanTraitTwoCurrent(double nextGenMeanTraitTwoInput)
	{
		nextGenMeanTraitTwo = nextGenMeanTraitTwoInput;
	}
	
	public void setMeanInterceptReactionNormCurrent(double nextGenMeanInterceptReactionNormInput) 
   {
		nextGenMeanInterceptReactionNorm = nextGenMeanInterceptReactionNormInput;
	}
	
	public void setMeanSlopeReactionNormCurrent(double nextGenMeanSlopeReactionNormInput) 
   {
		nextGenMeanSlopeReactionNorm = nextGenMeanSlopeReactionNormInput;
	}
   	
	public void setVarianceInterceptInitial(double phenotypicVarianceInterceptReactionNormInput) 
   {
		phenotypicVarianceInterceptReactionNorm = phenotypicVarianceInterceptReactionNormInput;
	}

	public void setVarianceSlopeInitial(double phenotypicVarianceSlopeReactionNormInput) 
   {
		phenotypicVarianceSlopeReactionNorm = phenotypicVarianceSlopeReactionNormInput;
	}
}
