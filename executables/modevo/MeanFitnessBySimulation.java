/*
	Name: Elizabeth Brooks
	File: MeanFitnessBySimulation
	Modified: February 4, 2016
*/

//Imports
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//A class to calculate the mean fitness of a species through simulation
public class MeanFitnessBySimulation extends IndividualFitness{
	
	//Class fields for calculating mean fitness
   private SpeciesCharacteristics speciesValues;
	private IndividualTraitOne individualTraitOneObject;
	private double meanTraitOne;
	private double meanTraitTwo;
	private double varianceTraitOne;
	private double varianceTraitTwo;
	private double standardDevianceTraitOne;
	private double standardDevianceTraitTwo;
	private double meanFitness;
	private double individualTraitOne;
	private double individualTraitTwo;
	private double individualIntercept;
	private double individualSlope;
	private double[] individualFitnessArray;
	//Fields for creating and storing the values for simulation of normally distributed populations
	private Random randomSimulation;
	private int numIterations; //The number of iterations the user would like the models to be run for
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness	private double[] interceptValuesArray;
	private double[] interceptValuesArray;
	private double[] slopeValuesArray;
	private double[] traitTwoValuesArray;
	private double[] traitOneValuesArray;
    private int fileNum; //Current test file number
	//Fields for calculating mean slope and intercept of the reaction norm
	private double standardDevianceIntercept;
	private double meanIntercept;
	private double standardDevianceSlope;
	private double meanSlope;
	private double varianceIntercept;
	private double varianceSlope;
	
	//The class constructor
	public MeanFitnessBySimulation(SpeciesCharacteristics speciesInputs, double nextGenMeanTraitOneInput, double nextGenMeanTraitTwoInput, 
                                    double nextGenMeanInterceptReactionNormInput, double nextGenMeanSlopeReactionNormInput)
	{
      //Call IndividualFitness (super) constructor
      super(speciesInputs);
		//Initialize reference variables
		speciesValues = speciesInputs;
		//Set initial values
      fileNum = 1;
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		meanTraitOne = nextGenMeanTraitOneInput;
		meanTraitTwo = nextGenMeanTraitTwoInput;
		varianceTraitOne = speciesValues.getVarianceTraitOne();
		varianceTraitTwo = speciesValues.getVarianceTraitTwo();
		varianceIntercept = speciesValues.getPhenotypicVarianceInterceptReactionNorm();
		varianceSlope = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
		meanIntercept = nextGenMeanInterceptReactionNormInput;
		meanSlope = nextGenMeanSlopeReactionNormInput;
		calculateStandardDeviationIntercept();
		calculateStandardDeviationSlope();
		calculateStandardDeviationTraitOne();
		calculateStandardDeviationTraitTwo();
	}
	
	//A method to calculate the standard deviation of the intercept of the reaction norm
	public void calculateStandardDeviationIntercept()
	{
		standardDevianceIntercept = Math.sqrt(Math.abs(varianceIntercept));
	}
	
	//A method to calculate the standard deviation of the slope of the reaction norm
	public void calculateStandardDeviationSlope()
	{
		standardDevianceSlope = Math.sqrt(Math.abs(varianceSlope));
	}
	
	//A method to calculate the standard deviation of trait two
	public void calculateStandardDeviationTraitTwo()
	{
		standardDevianceTraitTwo = Math.sqrt(Math.abs(varianceTraitTwo));
	}
	
	//A method to calculate the standard deviation of trait one
	public void calculateStandardDeviationTraitOne()
	{
		standardDevianceTraitOne = Math.sqrt(Math.abs(varianceTraitOne));
	}
	
	//A method to simulate as many populations for the intercept of the reaction norm as specified
	public void calcSimulatedInterceptValues()
	{
		interceptValuesArray = new double[simPopSize];
		randomSimulation = new Random();
		//A for loop to fill the array with a random number for trait two
		//through the simulation of normally distributed populations
		for(int i=0; i<interceptValuesArray.length; i++){
			interceptValuesArray[i] = (randomSimulation.nextGaussian() * standardDevianceIntercept + meanIntercept);
		}
	}
	
	//A method to simulate as many populations for the slope of the reaction norm as specified
	public void calcSimulatedSlopeValues()
	{
		slopeValuesArray = new double[simPopSize];
		randomSimulation = new Random();
		//A for loop to fill the array with a random number for trait two
		//through the simulation of normally distributed populations
		for(int i=0; i<slopeValuesArray.length; i++){
			slopeValuesArray[i] = (randomSimulation.nextGaussian() * standardDevianceSlope + meanSlope);
		}
	}
	
	//A method to simulate as many populations for trait two as specified
	public void calcSimulatedTraitTwoValues()
	{
		traitTwoValuesArray = new double[simPopSize];
      double traitTwo;
		randomSimulation = new Random();
		//A for loop to fill the array with a random number for trait two
		//through the simulation of normally distributed populations
		for(int i=0; i<traitTwoValuesArray.length; i++){
			traitTwo = (randomSimulation.nextGaussian() * standardDevianceTraitTwo + meanTraitTwo);
         //Make sure that the trait values do not fall below zero
         if(traitTwo < 0){
         	traitTwo = 0;
         }
         traitTwoValuesArray[i] = traitTwo;
		}
	}
	
	//A method to simulate as many populations for trait one as specified
	public void calcSimulatedTraitOneValues()
	{
		traitOneValuesArray = new double[simPopSize];
      double traitOne;
		randomSimulation = new Random();
		//A for loop to fill the array with a random number for trait one
		//through the simulation of normally distributed populations
		for(int i=0; i<traitOneValuesArray.length; i++){
			//Send the appropriate values to the MeanTraitOne class to recalculate a new mean for trait one
			individualSlope = slopeValuesArray[i];
			individualIntercept = interceptValuesArray[i];
			individualTraitTwo = traitTwoValuesArray[i];
			individualTraitOne = individualTraitOneObject.getTraitOne(individualSlope, individualIntercept, individualTraitTwo);
			traitOne = (randomSimulation.nextGaussian() * standardDevianceTraitOne + individualTraitOne);
         //Make sure that the trait values do not fall below zero
         if(traitOne < 0){
            traitOne = 0;
         }
         traitOneValuesArray[i] = traitOne;
		}
	}
	
	//A method to calculate the mean fitness
	public void calculateMeanFitness(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput)
	{
		simPopSize = simPopSizeInput;
		numIterations = numIterationsInput;
		randomSimulation = new Random();
		calcSimulatedInterceptValues();
		calcSimulatedSlopeValues();
		calcSimulatedTraitTwoValues();
		calcSimulatedTraitOneValues();
		individualFitnessArray = new double[simPopSize];
      
      //Create a file for the first, 10th, and 100th, simulated populations
      if(fileNum == 1){
   		writeTestFileOne();
         writeTestFileTwo();
      }else if(fileNum == 10){
         writeTestFileOne();
         writeTestFileTwo();
      }else if(fileNum == 100){
         writeTestFileOne();
         writeTestFileTwo();
      }
      
		//A for loop to fill the array with the individualFitnessValues
		for(int i=0; i<individualFitnessArray.length; i++){
			individualFitnessArray[i] = getIndividualFitness(numIterationsInput, simPopSizeInput, traitOneValuesArray[i], traitTwoValuesArray[i]);
		}

		//A for loop to calculate the mean fitness
		for(int i=0; i<individualFitnessArray.length; i++){
			meanFitness += individualFitnessArray[i];
		}
		meanFitness /= simPopSize;
      
      //Incriment the file number
      fileNum++;
	}
	
	//A method to calculate the mean fitness
	public void calculateMeanTraitOne(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput)
	{
		simPopSize = simPopSizeInput;
		numIterations = numIterationsInput;
		randomSimulation = new Random();
		calcSimulatedInterceptValues();
		calcSimulatedSlopeValues();
		calcSimulatedTraitTwoValues();
		calcSimulatedTraitOneValues();
		individualFitnessArray = new double[simPopSize];
      		
		//A for loop to fill the array with the individualFitnessValues
		for(int i=0; i<individualFitnessArray.length; i++){
			individualFitnessArray[i] = getIndividualFitness(numIterationsInput, simPopSizeInput, traitOneValuesArray[i], traitTwoValuesArray[i]);
		}
		
		//A for loop to calculate the mean of trait one
		for(int i=0; i<traitOneValuesArray.length; i++){
			meanTraitOne += traitOneValuesArray[i];
		}
		meanTraitOne /= simPopSize;
	}
	
	//A method to calculate the mean fitness
	public void calculateMeanTraitTwo(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput)
	{
		simPopSize = simPopSizeInput;
		numIterations = numIterationsInput;
		randomSimulation = new Random();
		meanTraitOne = meanTraitOneInput;
		meanTraitTwo = meanTraitTwoInput;
		calcSimulatedInterceptValues();
		calcSimulatedSlopeValues();
		calcSimulatedTraitTwoValues();
		calcSimulatedTraitOneValues();
		individualFitnessArray = new double[simPopSize];
      
		//A for loop to fill the array with the individualFitnessValues
		for(int i=0; i<individualFitnessArray.length; i++){
			individualFitnessArray[i] = getIndividualFitness(numIterationsInput, simPopSizeInput, traitOneValuesArray[i], traitTwoValuesArray[i]);
		}
		
		//A for loop to calculate the mean of trait two
		for(int i=0; i<traitTwoValuesArray.length; i++){
			meanTraitTwo += traitTwoValuesArray[i];
		}
		meanTraitTwo /= simPopSize;
	}
	
	//Methods to return mean values
	public double getMeanFitnessSim(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput, 
			double meanGenInterceptReactionNormInput, double nextGenMeanSlopeReactionNormInput)
	{
		//Set initial values
		individualTraitOneObject = new IndividualTraitOne(numIterations, simPopSize);
		numIterations = numIterationsInput;
		simPopSize = simPopSizeInput;
		meanTraitOne = meanTraitOneInput;
		meanTraitTwo = meanTraitTwoInput;
		meanIntercept = meanGenInterceptReactionNormInput;
		meanSlope = nextGenMeanSlopeReactionNormInput;
		varianceTraitOne = super.getPhenotypicVarianceTraitOneInitial();
		varianceTraitTwo = super.getPhenotypicVarianceTraitTwoInitial();
		varianceIntercept = super.getPhenotypicVarianceInterceptReactionNormInitial();
		varianceSlope = super.getPhenotypicVarianceSlopeReactionNormInitial();
		calculateStandardDeviationIntercept();
		calculateStandardDeviationSlope();
		calculateStandardDeviationTraitOne();
		calculateStandardDeviationTraitTwo();
		
      //Calc mean fitness
		calculateMeanFitness(numIterationsInput, simPopSizeInput, meanTraitOneInput, meanTraitTwoInput);
		
      //Return the calculated mean fitness
		return meanFitness;
	}
	
	public double getMeanTraitOneSim(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput, 
			double meanGenInterceptReactionNormInput, double nextGenMeanSlopeReactionNormInput)
	{
		//Set initial values
		individualTraitOneObject = new IndividualTraitOne(numIterations, simPopSize);
		numIterations = numIterationsInput;
		simPopSize = simPopSizeInput;
		meanTraitOne = meanTraitOneInput;
		meanTraitTwo = meanTraitTwoInput;
		meanIntercept = meanGenInterceptReactionNormInput;
		meanSlope = nextGenMeanSlopeReactionNormInput;
		varianceTraitOne = super.getPhenotypicVarianceTraitOneInitial();
		varianceTraitTwo = super.getPhenotypicVarianceTraitTwoInitial();
		varianceIntercept = super.getPhenotypicVarianceInterceptReactionNormInitial();
		varianceSlope = super.getPhenotypicVarianceSlopeReactionNormInitial();
		calculateStandardDeviationIntercept();
		calculateStandardDeviationSlope();
		calculateStandardDeviationTraitOne();
		calculateStandardDeviationTraitTwo();
		
		calculateMeanTraitOne(numIterationsInput, simPopSizeInput, meanTraitOneInput, meanTraitTwoInput);
		
		return meanTraitOne;
	}
	
	public double getMeanTraitTwoSim(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput, 
			double meanInterceptReactionNormInput, double nextGenMeanSlopeReactionNormInput)
	{
		//Set initial values
		individualTraitOneObject = new IndividualTraitOne(numIterations, simPopSize);
		numIterations = numIterationsInput;
		simPopSize = simPopSizeInput;
		meanTraitOne = meanTraitOneInput;
		meanTraitTwo = meanTraitTwoInput;
		meanIntercept = meanInterceptReactionNormInput;
		meanSlope = nextGenMeanSlopeReactionNormInput;
		varianceTraitOne = super.getPhenotypicVarianceTraitOneInitial();
		varianceTraitTwo = super.getPhenotypicVarianceTraitTwoInitial();
		varianceIntercept = super.getPhenotypicVarianceInterceptReactionNormInitial();
		varianceSlope = super.getPhenotypicVarianceSlopeReactionNormInitial();
		calculateStandardDeviationIntercept();
		calculateStandardDeviationSlope();
		calculateStandardDeviationTraitOne();
		calculateStandardDeviationTraitTwo();
		
		calculateMeanTraitTwo(numIterationsInput, simPopSizeInput, meanTraitOneInput, meanTraitTwoInput);
      
		return meanTraitTwo;
	}
   
   //Method for writing test results to a TXT file
   public void writeTestFileOne(){
		//Catch exceptions and write to file in TXT format
		try {
			//Write to the file simulatedPopTraitOne TXT file
         //Determine which test number is being run for file naming
         int fileCount = 1;
         String simulatedTraitOnePath = "traitOneSimulatedPop_" + fileNum + "_ModelTwo_" + fileCount + ".txt";
         File simulatedTraitOneFile = new File(simulatedTraitOnePath);
			if (simulatedTraitOneFile.exists()){
            //Loop through the existing files
			   while(simulatedTraitOneFile.exists()){
				   fileCount++;
               simulatedTraitOnePath = "traitOneSimulatedPop_" + fileNum + "_ModelTwo_" + fileCount + ".txt";
               simulatedTraitOneFile = new File(simulatedTraitOnePath);
			   }
            //Create simulatedTraitOneFile and file writer
            simulatedTraitOneFile.createNewFile();
            FileWriter fwOne = new FileWriter(simulatedTraitOneFile.getAbsoluteFile()); 
            //Write to file the header
			   fwOne.write("IndividualNumber,IndividualTraitOne\n");
			   String a;
			   String b;
				//A for loop to write the first individual trait values
			   for(int i=0, k=1; i<traitOneValuesArray.length; i++, k++){
			   	a = Integer.toString(k);
			   	fwOne.append(a);
			   	fwOne.append(" ");
			   	b = Double.toString(traitOneValuesArray[i]);
			   	fwOne.append(b);
			   	fwOne.append("\n");
			   }
	    		//Close the file
			   fwOne.close();
         }else if(!simulatedTraitOneFile.exists()){
            //Create simulatedTraitOneFile and file writer
            simulatedTraitOneFile.createNewFile();
            FileWriter fwOne = new FileWriter(simulatedTraitOneFile.getAbsoluteFile()); 
            //Write to file the header
			   fwOne.write("IndividualNumber,IndividualTraitOne\n");
			   String a;
			   String b;
				//A for loop to write the first individual trait values
			   for(int i=0, k=1; i<traitOneValuesArray.length; i++, k++){
			   	a = Integer.toString(k);
			   	fwOne.append(a);
			   	fwOne.append(" ");
			   	b = Double.toString(traitOneValuesArray[i]);
			   	fwOne.append(b);
			   	fwOne.append("\n");
			   }
	    		//Close the file
			   fwOne.close();
			}else{
            //Display error message
            System.out.println("Error in file naming, MeanFitnessBySimulation");
            System.exit(0);
         }			
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}   
   }
   
   //Method for writing test results to a TXT file
   public void writeTestFileTwo(){
		//Catch exceptions and write to file in TXT format
		try {
			//Write to the file simulatedPopTraitTwo TXT file
         //Determine which test number is being run for file naming
         int fileCount = 1;
         String simulatedTraitTwoPath = "traitTwoSimulatedPop_" + fileNum + "_ModelTwo_" + fileCount + ".txt";
         File simulatedTraitTwoFile = new File(simulatedTraitTwoPath);         
			if (simulatedTraitTwoFile.exists()){
            //Loop through the existing files
			   while(simulatedTraitTwoFile.exists()){
				   fileCount++;
               simulatedTraitTwoPath = "traitTwoSimulatedPop_" + fileNum + "_ModelTwo_" + fileCount + ".txt";
               simulatedTraitTwoFile = new File(simulatedTraitTwoPath);
			   }
            //Create simulatedTraitTwoFile and file writer
            simulatedTraitTwoFile.createNewFile();
            FileWriter fwTwo = new FileWriter(simulatedTraitTwoFile.getAbsoluteFile()); 
            //Write to file the header
			   fwTwo.write("IndividualNumber,IndividualTraitTwo\n");
			   String a;
			   String b;			
			   //A for loop to write the first individual trait values
			   for(int i=0, k=1; i<traitTwoValuesArray.length; i++, k++){
				   a = Integer.toString(k);
				   fwTwo.append(a);
				   fwTwo.append(" ");
				   b = Double.toString(traitTwoValuesArray[i]);
				   fwTwo.append(b);
				   fwTwo.append("\n");
			   }	
			   //Close the file
			   fwTwo.close();
         }else if(!simulatedTraitTwoFile.exists()){
            //Create simulatedTraitTwoFile and file writer
            simulatedTraitTwoFile.createNewFile();
            FileWriter fwTwo = new FileWriter(simulatedTraitTwoFile.getAbsoluteFile()); 
            //Write to file the header
			   fwTwo.write("IndividualNumber,IndividualTraitTwo\n");
			   String a;
			   String b;			
			   //A for loop to write the first individual trait values
			   for(int i=0, k=1; i<traitTwoValuesArray.length; i++, k++){
				   a = Integer.toString(k);
				   fwTwo.append(a);
				   fwTwo.append(" ");
				   b = Double.toString(traitTwoValuesArray[i]);
				   fwTwo.append(b);
				   fwTwo.append("\n");
			   }	
			   //Close the file
			   fwTwo.close();
			}else{
            //Display error message
            System.out.println("Error in file naming, MeanFitnessBySimulation");
            System.exit(0);
         }			
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }
	
	//Methods to set mean values
	public void setMeanFitnessSim(double meanFitnessInput)
	{
		meanFitness = meanFitnessInput;
	}
	
	public void setMeanTraitOneSim(double meanTraitOneInput)
	{
		meanTraitOne = meanTraitOneInput;
	}
	
	public void setMeanTraitTwoSim(double meanTraitTwoInput)
	{
		meanTraitTwo = meanTraitTwoInput;
	}
	
	public void setMeanSlopeSim(double meanSlopeInput)
	{
		meanSlope = meanSlopeInput;
	}
	
	public void setMeanInterceptSim(double meanInterceptInput)
	{
		meanSlope = meanInterceptInput;
	}

	//Getter methods
	public int getSimPopSizeInitial() {
		return simPopSize;
	}
	
	public int getNumIterationsInitial() {
		return numIterations;
	}
}
