/*
	Name: Elizabeth Brooks
	File: MeanTraitTwo
	Modified: June 24, 2016
*/

//Imports
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//A class to calculate the mean trait two through simulation
public class MeanTraitTwo extends IndividualTraitTwo{
	
	//Class fields used to calculate the mean value of trait Two
	private Random randomSimulation; //For simulation of individual variable values
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
    //private GaussianDistribution gaussianDistObject; //Reference variable of the GaussianDistribution class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	//Variables for determining mean trait Two through simulation
	private double individualTraitTwo;
	private double meanTraitTwo;
	private double varianceTraitTwo;
	private double standardDevianceTraitTwo;
	private double[] traitTwoValuesArray;

	//The class constructor
	public MeanTraitTwo(SpeciesCharacteristics speciesInputs, double nextGenMeanTraitTwoInput)
	{
      	//Call IndividualFitness (super) constructor
      	super(speciesInputs);
		//Initialize class fields
		speciesValues = speciesInputs;
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		meanTraitTwo = nextGenMeanTraitTwoInput;
		varianceTraitTwo = speciesValues.getVarianceTraitTwo();
		calculateStandardDeviationTraitTwo();
	}
	
	//A method to calculate the standard deviation of trait Two
	public void calculateStandardDeviationTraitTwo()
	{
		standardDevianceTraitTwo = Math.sqrt(Math.abs(varianceTraitTwo));
	}
	
	//A method to simulate as many populations for trait Two as specified
	public void calcSimulatedTraitTwoValues()
	{
		traitTwoValuesArray = new double[simPopSize];
      	double traitTwo;
      	if(speciesValues.getDistributionName().equals("defaultdistribution")){
			randomSimulation = new Random();
			//A for loop to fill the array with a random number for trait Two
			//through the simulation of normally distributed populations
			for(int i=0; i<traitTwoValuesArray.length; i++){
				//Send the appropriate values to the IndividualTraitTwo class to recalculate a new mean for trait Two
				individualTraitTwo = getIndividualTraitTwo();
				traitTwo = (randomSimulation.nextGaussian() * standardDevianceTraitTwo + individualTraitTwo);
	         //Make sure that the trait values do not fall below zero
	         if(traitTwo < 0){
	           	traitTwo = 0;
	         }
	         traitTwoValuesArray[i] = traitTwo;
			}
		}else if(speciesValues.getDistributionName().equals("gaussiandistribution")){
			//gaussianDistObject.getGaussianDistribution();
			System.out.println("Distribution not yet ready for use, program exited");
			System.exit(0);
		}
	}
	
	//Methods to return mean values
	public double getMeanTraitTwoSim(double meanTraitTwoInput)
	{
		//Depending on user selection for trait distribution, determine the current mean fitness of the population
		//Default distribution is the provided .nextGaussian function
      
		//A for loop to calculate the mean fitness
		for(int i=0; i<traitTwoValuesArray.length; i++){
			meanTraitTwo += traitTwoValuesArray[i];
		}
		meanTraitTwo /= simPopSize;

      	return meanTraitTwo;
	}

	//Getter methods
   public double getMeanTraitTwoSim() {
		return meanTraitTwo;
	}
   
   public double getVarianceTraitTwoSim() {
		return varianceTraitTwo;
	}
   
   public double getStandardDevianceTraitTwoSim() {
		return standardDevianceTraitTwo;
	}
   
   public double getIndividualTraitTwoSim() {
		return individualTraitTwo;
	}
   
	public int getSimPopSizeSim() {
		return simPopSize;
	}
	
	public int getNumIterationsSim() {
		return numIterations;
	}
   
   //Setter methods   
   public void setMeanTraitTwoSim(double meanTraitTwoInput) {
		meanTraitTwo = meanTraitTwoInput;
	}
   
   public void setVarianceTraitTwoSim(double varianceTraitTwoInput) {
		varianceTraitTwo = varianceTraitTwoInput;
	}
   public void setStandardDevianceTraitTwoSim(double standardDevianceTraitTwoInput) {
		standardDevianceTraitTwo = standardDevianceTraitTwoInput;
	}

   public void setIndividualTraitTwoSim(double individualTraitTwoInput) {
		individualTraitTwo = individualTraitTwoInput;
	}
   
	public void setSimPopSizeSim(int simPopSizeInput) {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterationsSim(int numIterationsInput) {
		numIterations = numIterationsInput;
	}
}