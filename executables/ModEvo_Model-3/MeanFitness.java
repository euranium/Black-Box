/*
	Name: Elizabeth Brooks
	File: MeanFitnessBySimulation
	Modified: June 30, 2016
*/

//Imports
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//A class to calculate the mean fitness of a species through simulation
public class MeanFitness extends IndividualFitness{
	
	//Class fields for calculating mean fitness
    private SpeciesCharacteristics speciesValues;
	private IndividualTraitOne individualTraitOneObject;
    private IndividualTraitTwo individualTraitTwoObject;
	private double meanTraitOne;
	private double meanTraitTwo;
	private double varianceTraitOne;
	private double varianceTraitTwo;
	private double standardDevianceTraitOne;
	private double standardDevianceTraitTwo;
	private double meanFitness;
	private double individualTraitOne;
	private double individualTraitTwo;
	private double[] individualFitnessArray;
	//Fields for creating and storing the values for simulation of normally distributed populations
	private Random randomSimulation;
	private int numIterations; //The number of iterations the user would like the models to be run for
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness	private double[] interceptValuesArray;
	private double[] traitTwoValuesArray;
	private double[] traitOneValuesArray;
	private double distributionSlope; //The user selected distribution slope
   	private double distributionShift; //The user selected distribution shift
   	private double distributionMax; //The user selected distribution max	

	//The class constructor
	public MeanFitness(SpeciesCharacteristics speciesInputs, double nextGenMeanTraitOneInput, double nextGenMeanTraitTwoInput)
	{
      	//Call IndividualFitness (super) constructor
      	super(speciesInputs);
		//Initialize reference variables
		speciesValues = speciesInputs;
      	individualTraitOneObject = new IndividualTraitOne(speciesValues);
      	individualTraitTwoObject = new IndividualTraitTwo(speciesValues);
		//Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		distributionSlope = speciesValues.getDistributionSlope();
      	distributionShift = speciesValues.getDistributionShift();
      	distributionMax = speciesValues.getDistributionMax();
      	meanTraitOne = nextGenMeanTraitOneInput;
		meanTraitTwo = nextGenMeanTraitTwoInput;
		varianceTraitOne = speciesValues.getVarianceTraitOne();
		varianceTraitTwo = speciesValues.getVarianceTraitTwo();
		calculateStandardDeviationTraitOne();
		calculateStandardDeviationTraitTwo();
	} //End MeanFitness constructor
	
	//A method to calculate the standard deviation of trait two
	public void calculateStandardDeviationTraitTwo()
	{
		standardDevianceTraitTwo = Math.sqrt(Math.abs(varianceTraitTwo));
	} //End calculateStandardDeviationTraitTwo
	
	//A method to calculate the standard deviation of trait one
	public void calculateStandardDeviationTraitOne()
	{
		standardDevianceTraitOne = Math.sqrt(Math.abs(varianceTraitOne));
	} //End calculateStandardDeviationTraitOne
	
	//A method to simulate as many populations for trait one as specified
	public void calcSimulatedTraitOneValues()
	{
		traitOneValuesArray = new double[simPopSize];
        double traitOne;
      	//Determine the current mean fitness of the population
		randomSimulation = new Random();
		//A for loop to fill the array with a random number for trait one
		//through the simulation of normally distributed populations
		for(int i=0; i<traitOneValuesArray.length; i++){
			//Send the appropriate values to the IndividualTraitOne class to recalculate a new mean for trait one
			individualTraitOne = individualTraitOneObject.getIndividualTraitOne();
			traitOne = (randomSimulation.nextGaussian() * standardDevianceTraitOne + individualTraitOne);
		    //Make sure that the trait values do not fall below zero
		    if(traitOne < 0){
		     	traitOne = 0;
		    } //End if
		    traitOneValuesArray[i] = traitOne;
		} //End for
	} //End calcSimulatedTraitOneValues

	//A method to simulate as many populations for trait two as specified
	public void calcSimulatedTraitTwoValues()
	{
		traitTwoValuesArray = new double[simPopSize];
        double traitTwo;
      	//Determine the current mean fitness of the population
		randomSimulation = new Random();
		//A for loop to fill the array with a random number for trait one
		//through the simulation of normally distributed populations
		for(int i=0; i<traitTwoValuesArray.length; i++){
			//Send the appropriate values to the IndividualTraitTwo class to recalculate a new mean for trait two
			individualTraitTwo = individualTraitTwoObject.getIndividualTraitTwo();
			traitTwo = (randomSimulation.nextGaussian() * standardDevianceTraitTwo + individualTraitTwo);
	     	//Make sure that the trait values do not fall below zero
	        if(traitTwo < 0){
	           traitTwo = 0;
	        } //End if
	        traitTwoValuesArray[i] = traitTwo;
		} //End for
	} //End calcSimulatedTraitTwoValues
	
	//A method to calculate the mean fitness
	public void calculateMeanFitness(double meanTraitOneInput, double meanTraitTwoInput)
	{
		//Array for storing the calculated individual fitness values
		individualFitnessArray = new double[simPopSize];
		//Calculate individual trait values
		calcSimulatedTraitOneValues();
		calcSimulatedTraitTwoValues();
		//A for loop to fill the array with the individualFitnessValues
		for(int i=0; i<individualFitnessArray.length; i++){
			individualFitnessArray[i] = super.getIndividualFitness(traitOneValuesArray[i], traitTwoValuesArray[i]);
		} //End for
		//A for loop to calculate the mean fitness
		for(int i=0; i<individualFitnessArray.length; i++){
			meanFitness += individualFitnessArray[i];
		} //End for
		meanFitness /= simPopSize;
	} //End calculateMeanFitness
	
	//Methods to return mean values
	public double getMeanFitnessSim(double meanTraitOneInput, double meanTraitTwoInput,
			double nextGenMeanInterceptReactionNormInput, double nextGenMeanSlopeReactionNormInput, double nextGenMeanFunctionTraitInput)
	{
		//Set initial values
		individualTraitOneObject = new IndividualTraitOne(speciesValues);
		individualTraitTwoObject = new IndividualTraitTwo(speciesValues);
		meanTraitOne = meanTraitOneInput;
		meanTraitTwo = meanTraitTwoInput;
		varianceTraitOne = speciesValues.getPhenotypicVarianceTraitOne();
		varianceTraitTwo = speciesValues.getPhenotypicVarianceTraitTwo();
		calculateStandardDeviationTraitOne();
		calculateStandardDeviationTraitTwo();
      	//Calc mean fitness
		calculateMeanFitness(meanTraitOneInput, meanTraitTwoInput);
      	//Return the calculated mean fitness
		return meanFitness;
	} //End getMeanFitnessSim
	
	//Getter methods
   	public double getMeanTraitOne() {
		return meanTraitOne;
	}
   
   	public double getMeanTraitTwo() {
		return meanTraitTwo;
	}
   
   	public double getVarianceTraitOne() {
		return varianceTraitOne;
	}
   
   	public double getVarianceTraitTwo() {
		return varianceTraitTwo;
	}
   
   	public double getStandardDevianceTraitOne() {
		return standardDevianceTraitOne;
	}
   
   	public double getStandardDevianceTraitTwo() {
		return standardDevianceTraitTwo;
	}
   
   	public double getMeanFitness() {
		return meanFitness;
	}
   
   	public double getIndividualTraitOne() {
		return individualTraitOne;
	}
   
   	public double getIndividualTraitTwo() {
		return individualTraitTwo;
	}
   
	public int getSimPopSize() {
		return simPopSize;
	}
	
	public int getNumIterations() {
		return numIterations;
	}

   	//Setter methods
   	public void setMeanTraitOne(double meanTraitOneInput) {
		meanTraitOne = meanTraitOneInput;
	}
   
   	public void setMeanTraitTwo(double meanTraitTwoInput) {
		meanTraitTwo = meanTraitTwoInput;
	}
   
   	public void setVarianceTraitOne(double varianceTraitOneInput) {
		varianceTraitOne = varianceTraitOneInput;
	}
   
   	public void setVarianceTraitTwo(double varianceTraitTwoInput) {
		varianceTraitTwo = varianceTraitTwoInput;
	}
   
   	public void setStandardDevianceTraitOne(double standardDevianceTraitOneInput) {
		standardDevianceTraitOne = standardDevianceTraitOneInput;
	}
   
   	public void setStandardDevianceTraitTwo(double standardDevianceTraitTwoInput) {
		standardDevianceTraitTwo = standardDevianceTraitTwoInput;
	}
   
   	public void setMeanFitness(double meanFitnessInput) {
		meanFitness = meanFitnessInput;
	}
   
   	public void setIndividualTraitOne(double individualTraitOneInput) {
		individualTraitOne = individualTraitOneInput;
	}
   
   	public void setIndividualTraitTwo(double individualTraitTwoInput) {
		individualTraitTwo = individualTraitTwoInput;
	}
   
	public void setSimPopSize(int simPopSizeInput) {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterations(int numIterationsInput) {
		numIterations = numIterationsInput;
	}
}