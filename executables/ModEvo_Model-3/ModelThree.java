/*
	Name: Elizabeth Brooks
	File: ModelThree
	Modified: June 24, 2016
*/

//Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Class for modeling the evolutionary trajectories of a species with respect to multiple phenotypic traits
//while also accounting for underlying developmental interactions
public class ModelThree {

    //Class fields to store variables
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
    private IndividualFitness individualFitnessObject; //Reference variable of the IndividualFitness class
	private MeanFitness meanFitnessObject; //Reference variable of the MeanFitness class
	private MeanTraitOne meanTraitOneObject; //Reference variable of the MeanTraitOne class
	private MeanTraitTwo meanTraitTwoObject; //Reference variable of the MeanTraitTwo class
	private IndividualTraitOne individualTraitOneObject; //Reference variable of the IndividualTraitOne class
	private IndividualTraitTwo individualTraitTwoObject; //Reference variable of the IndividualTraitTwo class
	private int numIterations; //The number of iterations the user would like the models to be run for
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
    private double phenotypicVarianceTraitOne; //The phenotypic variance of trait two
	private double phenotypicVarianceTraitTwo; //The phenotypic variance of trait two
	private double phenotypicVarianceInterceptReactionNorm; //The phenotypic variance of the intercept of the reaction norm
	private double phenotypicVarianceSlopeReactionNorm; //The phenotypic variance of the slope of the reaction norm
    private double phenotypicVarianceFunctionTrait; //The phenotypic variance of the phenotypic trait acting as a function of traits one and two
	private double[][] heritabilityMatrix; //Matrix of heritability values
	private double meanFitness; //Mean fitness
	//Fields to hold the values for calculating each generation's values
	private double nextGenMeanTraitOne; //The mean of trait one
	private double nextGenMeanTraitTwo; //The mean of trait two
	private double nextGenMeanInterceptReactionNorm; //The next generation's mean intercept of the reaction norm
	private double nextGenMeanSlopeReactionNorm; //The next gerneration's mean value for the slope of the reaction norm
    private double nextGenMeanFunctionTrait; //The next generation's mean value for the function of traits one and two
  	//Arrays to hold each generations calculated mean trait value
	private double[] traitOneArray; //Array of mean values for the first trait
	private double[] traitTwoArray; //Array of mean values for the second trait
	private double[] slopeArray; //Array of mean slope values of the reaction norm
	private double[] interceptArray; //Array of mean intercept values of the reaction norm
	private double[] preferenceArray; //Array of mean preference values
   
	//The class constructor
	public ModelThree(SpeciesCharacteristics speciesInputs)
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
        nextGenMeanFunctionTrait = speciesValues.getMeanFunctionTrait();
	    phenotypicVarianceInterceptReactionNorm = speciesValues.getPhenotypicVarianceInterceptReactionNorm();
	    phenotypicVarianceSlopeReactionNorm = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
	}
	
	//Methods to run the model
	public void runModel()
	{
		//Initialize objects
		individualFitnessObject = new IndividualFitness(speciesValues);		
		individualTraitOneObject = new IndividualTraitOne(speciesValues);
		individualTraitTwoObject = new IndividualTraitTwo(speciesValues);
		meanFitnessObject = new MeanFitness(speciesValues, nextGenMeanTraitOne, nextGenMeanTraitTwo);
		meanTraitOneObject = new MeanTraitOne(speciesValues, nextGenMeanTraitOne);
		meanTraitTwoObject = new MeanTraitTwo(speciesValues, nextGenMeanTraitTwo);
		//Begin the model looping
		setTraitArrays();
	}

	//Add each generations mean trait values into the trait arrays
	public void setTraitArrays()
	{
		traitOneArray = new double[numIterations];
		traitTwoArray = new double[numIterations];
		slopeArray = new double[numIterations];
		interceptArray = new double[numIterations];
		preferenceArray = new double[numIterations];
		//A for loop to save each value calculated for the mean of trait one and two
		for(int i=0;i<numIterations;i++){
			traitOneArray[i] = nextGenMeanTraitOne;
			traitTwoArray[i] = nextGenMeanTraitTwo;
			slopeArray[i] = nextGenMeanSlopeReactionNorm;
			interceptArray[i] = nextGenMeanInterceptReactionNorm;
			preferenceArray[i] = nextGenMeanFunctionTrait;
			calcNextGenFitnessTraitOneTraitTwo();
			calcNextGenMeanInterceptReactionNorm();
			calcNextGenMeanSlopeReactionNorm();
			calcNextGenMeanFunctionTrait();
		}
      	//Write to CSV file trait values
      	writeTraitTwoFile();
      	writeTraitOneFile();
      	writeSlopeFile();
      	writeInterceptFile();
      	writePreferenceFile();
   }
	
	//Send the new mean values of each trait to the IndividualFitness class for re calculation
	public void calcNextGenFitnessTraitOneTraitTwo()
	{
		meanFitness = meanFitnessObject.getMeanFitnessSim(nextGenMeanTraitOne, 
				nextGenMeanTraitTwo, nextGenMeanInterceptReactionNorm, nextGenMeanSlopeReactionNorm, nextGenMeanFunctionTrait);
		nextGenMeanTraitOne = meanTraitOneObject.getMeanTraitOneSim(nextGenMeanTraitOne);
		nextGenMeanTraitTwo = meanTraitTwoObject.getMeanTraitTwoSim(nextGenMeanTraitOne);
	}

	//A method to calculate the mean intercept of the reaction norm 
	public void calcNextGenMeanInterceptReactionNorm()
	{
		calcNextGenFitnessTraitOneTraitTwo();
		double traitTwoDerivative = individualTraitTwoObject.numericallyCalcInterceptPartialDerivative(nextGenMeanTraitTwo, nextGenMeanSlopeReactionNorm, 
				nextGenMeanInterceptReactionNorm, nextGenMeanFunctionTrait);
		double traitOneDerivative = individualTraitOneObject.numericallyCalcInterceptPartialDerivative(nextGenMeanTraitOne, nextGenMeanSlopeReactionNorm, 
				nextGenMeanInterceptReactionNorm, nextGenMeanFunctionTrait);
		double fitnessInverse = (1/meanFitness);
		nextGenMeanInterceptReactionNorm += (calcHeritabilityMatrixValue("intercept")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*
				traitTwoDerivative*phenotypicVarianceInterceptReactionNorm)+
				(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*
				traitOneDerivative*phenotypicVarianceInterceptReactionNorm);
	}

	//A method to calculate the mean slope of the reaction norm
	public void calcNextGenMeanSlopeReactionNorm()
	{
		calcNextGenFitnessTraitOneTraitTwo();
		double traitOneDerivative = individualTraitOneObject.numericallyCalcSlopePartialDerivative(nextGenMeanTraitOne, nextGenMeanSlopeReactionNorm, 
				nextGenMeanInterceptReactionNorm, nextGenMeanFunctionTrait);
		double traitTwoDerivative = individualTraitTwoObject.numericallyCalcSlopePartialDerivative(nextGenMeanTraitTwo, nextGenMeanSlopeReactionNorm,
				nextGenMeanInterceptReactionNorm, nextGenMeanFunctionTrait);
		double fitnessInverse = (1/meanFitness);
		nextGenMeanInterceptReactionNorm += (calcHeritabilityMatrixValue("slope")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*
				traitTwoDerivative*phenotypicVarianceSlopeReactionNorm)+
				(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*
				traitOneDerivative*phenotypicVarianceSlopeReactionNorm);
	}

	//A method to calculate the mean value of trait two
	public void calcNextGenMeanFunctionTrait()
	{
		calcNextGenFitnessTraitOneTraitTwo();
		double traitOneDerivative = individualTraitOneObject.numericallyCalcFunctionTraitPartialDerivative(nextGenMeanTraitOne, nextGenMeanSlopeReactionNorm, 
				nextGenMeanInterceptReactionNorm, nextGenMeanFunctionTrait);
		double traitTwoDerivative = individualTraitTwoObject.numericallyCalcFunctionTraitPartialDerivative(nextGenMeanTraitTwo, nextGenMeanSlopeReactionNorm, 
				nextGenMeanInterceptReactionNorm, nextGenMeanFunctionTrait);
		double fitnessInverse = (1/meanFitness);
		nextGenMeanInterceptReactionNorm += (calcHeritabilityMatrixValue("function trait")*fitnessInverse*
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*
				traitTwoDerivative*phenotypicVarianceFunctionTrait)+
				(fitnessInverse*individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo)*
				traitOneDerivative*phenotypicVarianceFunctionTrait);
	}

	//A method to determine which value to return from the heritability matrix
	public double calcHeritabilityMatrixValue(String variable)
	{
		//Initialize the 2D array with 3 rows and 3 columns and set the matrix values
		//Copy the matrix from the SpecieCharacteristics class
		int tempLength = speciesValues.getHeritabilityMatrix().length;
		heritabilityMatrix = new double[tempLength][];
		for(int m=0; m<tempLength; m++)
		{
		  double[] tempMatrix = speciesValues.getHeritabilityMatrix()[m];
		  tempLength = speciesValues.getHeritabilityMatrix().length;
		  heritabilityMatrix[m] = new double[tempLength];
		  System.arraycopy(tempMatrix, 0, heritabilityMatrix[m], 0, tempLength);
		}
		//Determine which matrix value to return based on String argument
		if(variable.equals("intercept")){
			return ((nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][0]) + 
					(nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][1]) +
					(nextGenMeanInterceptReactionNorm * heritabilityMatrix[0][2]));
		}else if(variable.equals("slope")){
			return ((nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][0]) + 
					(nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][1]) +
					(nextGenMeanSlopeReactionNorm * heritabilityMatrix[1][2]));
		}else{//function trait
			return ((nextGenMeanFunctionTrait * heritabilityMatrix[2][0]) + 
					(nextGenMeanFunctionTrait * heritabilityMatrix[2][1]) +
					(nextGenMeanFunctionTrait * heritabilityMatrix[2][2]));
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
         String meanTraitOnePath = "meanTraitOneValues_ModelThree.txt";
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
         String meanTraitTwoPath = "meanTraitTwoValues_ModelThree.txt";
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
         String meanSlopePath = "meanSlopeValues_ModelThree.txt";
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
         String meanInterceptPath = "meanInterceptValues_ModelThree.txt";
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

	//Method to write trait one values to a TXT file
   public void writePreferenceFile()
   {
		//Write to file the second traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         String meanCoefficientPath = "meanPreferenceValues_ModelThree.txt";
         File meanCoefficientFile = new File(meanCoefficientPath);
            //Create meanTraitOneFile and file writer
            FileWriter fwI = new FileWriter(meanCoefficientFile.getAbsoluteFile()); 
			meanCoefficientFile.createNewFile();
            //Write to file the header
			fwI.write("Generation MeanPreference\n");
			String aTwo;
			String bTwo;			
			for(int i=0, k=1;i<preferenceArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	aTwo = Integer.toString(k);
			   	fwI.append(aTwo);
			   	fwI.append(" ");
			   	bTwo = Double.toString(preferenceArray[i]);
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
   
   public double getMeanFunctionTraitCurrent() 
   {
		return nextGenMeanFunctionTrait;
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
   
   public double getVarianceFunctionTraitInitial() 
   {
		return phenotypicVarianceFunctionTrait;
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
   
   public void setMeanFunctionTraitCurrent(double nextGenMeanFunctionTraitInput) 
   {
		nextGenMeanFunctionTrait = nextGenMeanFunctionTraitInput;
	}
   	
	public void setVarianceInterceptInitial(double phenotypicVarianceInterceptReactionNormInput) 
   {
		phenotypicVarianceInterceptReactionNorm = phenotypicVarianceInterceptReactionNormInput;
	}

	public void setVarianceSlopeInitial(double phenotypicVarianceSlopeReactionNormInput) 
   {
		phenotypicVarianceSlopeReactionNorm = phenotypicVarianceSlopeReactionNormInput;
	}
   
   public void setVarianceFunctionTraitInitial(double phenotypicVarianceFunctionTraitInput) 
   {
		phenotypicVarianceFunctionTrait = phenotypicVarianceFunctionTraitInput;
	}
}
