/*
	Name: Elizabeth Brooks
	File: MeanTraitOne
	Modified: June 24, 2016
*/

//Imports
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//A class to calculate the mean value of trait one through simulation
public class MeanTraitOne extends IndividualTraitOne{
	
	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
   	//private GaussianDistribution gaussianDistObject; //Reference variable of the GaussianDistribution class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	//Variables for determining mean trait one through simulation
	private double individualTraitOne;
	private double meanTraitOne;
	private double varianceTraitOne;
	private double standardDevianceTraitOne;
	private double[] traitOneValuesArray;

	//The class constructor
	public MeanTraitOne(SpeciesCharacteristics speciesInputs, double nextGenMeanTraitOneInput)
	{
      	//Call IndividualFitness (super) constructor
      	super(speciesInputs);
		//Initialize class fields
		speciesValues = speciesInputs;
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		meanTraitOne = nextGenMeanTraitOneInput;
		varianceTraitOne = speciesValues.getVarianceTraitOne();
		calculateStandardDeviationTraitOne();
	}
	
	//A method to calculate the standard deviation of trait one
	public void calculateStandardDeviationTraitOne()
	{
		standardDevianceTraitOne = Math.sqrt(Math.abs(varianceTraitOne));
	}
	
	//A method to simulate as many populations for trait one as specified
	public void calcSimulatedTraitOneValues()
	{
		traitOneValuesArray = new double[simPopSize];
      	double traitOne;
      	if(speciesValues.getDistributionName().equals("defaultdistribution")){
			randomSimulation = new Random();
			//A for loop to fill the array with a random number for trait one
			//through the simulation of normally distributed populations
			for(int i=0; i<traitOneValuesArray.length; i++){
				//Send the appropriate values to the IndividualTraitOne class to recalculate a new mean for trait one
				individualTraitOne = super.getIndividualTraitOne();
				traitOne = (randomSimulation.nextGaussian() * standardDevianceTraitOne + individualTraitOne);
	         //Make sure that the trait values do not fall below zero
	         if(traitOne < 0){
	           	traitOne = 0;
	         }
	         traitOneValuesArray[i] = traitOne;
			}
		}else if(speciesValues.getDistributionName().equals("gaussiandistribution")){
			//gaussianDistObject.getGaussianDistribution();
			System.out.println("Distribution not yet ready for use, program exited");
			System.exit(0);
		}
	}
	
	//Methods to return mean values
	public double getMeanTraitOneSim(double meanTraitOneInput)
	{
		//Depending on user selection for trait distribution, determine the current mean fitness of the population
		//Default distribution is the provided .nextGaussian function
		
		//A for loop to calculate the mean fitness
		for(int i=0; i<traitOneValuesArray.length; i++){
			meanTraitOne += traitOneValuesArray[i];
		}
		meanTraitOne /= simPopSize;

      	return meanTraitOne;
	}

	//Getter methods
   public double getMeanTraitOneSim() {
		return meanTraitOne;
	}
   
   public double getVarianceTraitOneSim() {
		return varianceTraitOne;
	}
   
   public double getStandardDevianceTraitOneSim() {
		return standardDevianceTraitOne;
	}
   
   public double getIndividualTraitOneSim() {
		return individualTraitOne;
	}
   
	public int getSimPopSizeSim() {
		return simPopSize;
	}
	
	public int getNumIterationsSim() {
		return numIterations;
	}
   
   //Setter methods
   public void setMeanTraitOneSim(double meanTraitOneInput) {
		meanTraitOne = meanTraitOneInput;
	}
   
   public void setVarianceTraitOneSim(double varianceTraitOneInput) {
		varianceTraitOne = varianceTraitOneInput;
	}
   
   public void setStandardDevianceTraitOneSim(double standardDevianceTraitOneInput) {
		standardDevianceTraitOne = standardDevianceTraitOneInput;
	}
   
   public void setIndividualTraitOneSim(double individualTraitOneInput) {
		individualTraitOne = individualTraitOneInput;
	}
   
	public void setSimPopSizeSim(int simPopSizeInput) {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterationsSim(int numIterationsInput) {
		numIterations = numIterationsInput;
	}
}