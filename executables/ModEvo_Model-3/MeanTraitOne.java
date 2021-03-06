/*
	Name: Elizabeth Brooks
	File: MeanTraitOne
	Modified: October 30, 2016
*/

//Imports
import java.security.SecureRandom;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//A class to calculate the mean fitness of a species through simulation
public class MeanTraitOne {
	
	//Class fields used to calculate the mean value of trait one
	private SecureRandom randomSimulation; //For simulation of individual variable values
   private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
   private IndividualTraitOne individualTraitOneObject; //Reference variable of the IndividualTraitOne class
   //private GaussianDistribution gaussianDistObject; //Reference variable of the GaussianDistribution class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	//Variables for determining mean trait one through simulation
	private double varianceTraitOne;
	private double standardDevianceTraitOne;
	private double[] traitOneValuesArray;

	//The class constructor
	public MeanTraitOne(SpeciesCharacteristics speciesInputs)
	{
      //Initialize reference variables
      speciesValues = speciesInputs;
      individualTraitOneObject = new IndividualTraitOne(speciesValues);
		//Initialize class fields
		speciesValues = speciesInputs;
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		varianceTraitOne = speciesValues.getVarianceTraitOne();
		calculateStandardDeviationTraitOne();
	}
	
	//A method to calculate the standard deviation of trait one
	public void calculateStandardDeviationTraitOne()
	{
		standardDevianceTraitOne = Math.sqrt(Math.abs(varianceTraitOne));
	}
	
	//A method to simulate as many populations for trait one as specified
	public void calcSimulatedTraitOneValues(double meanSlopeReactionNormInput, double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
      //Set initial values
      double traitOne;
      double meanSlopeReactionNorm = meanSlopeReactionNormInput;
      double meanInterceptReactionNorm = meanInterceptReactionNormInput;
      double meanFunctionTrait = meanFunctionTraitInput;
		traitOneValuesArray = new double[simPopSize];
      double individualTraitOne = individualTraitOneObject.getIndividualTraitOne(meanSlopeReactionNorm, meanInterceptReactionNorm, meanFunctionTrait);
      if(speciesValues.getDistributionName().equals("defaultdistribution")){
			randomSimulation = new SecureRandom();
			//A for loop to fill the array with a random number for trait one
			//through the simulation of normally distributed populations
			for(int i=0; i<traitOneValuesArray.length; i++){
				//Send the appropriate values to the IndividualTraitOne class to recalculate a new mean for trait one
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
	public double getMeanTraitOneSim(double nextGenMeanSlopeReactionNormInput, double nextGenMeanInterceptReactionNormInput, double nextGenMeanFunctionTraitInput)
	{
      //Set the initial values
      double meanTraitOne = 0;
      double meanFunctionTrait = nextGenMeanFunctionTraitInput;
		double meanInterceptReactionNorm = nextGenMeanInterceptReactionNormInput;
		double meanSlopeReactionNorm = nextGenMeanSlopeReactionNormInput;
		//Depending on user selection for trait distribution, determine the current mean fitness of the population
		//Default distribution is the provided .nextGaussian function
		calcSimulatedTraitOneValues(meanSlopeReactionNorm, meanInterceptReactionNorm, meanFunctionTrait);
		//A for loop to calculate the mean fitness
		for(int i=0; i<traitOneValuesArray.length; i++){
			meanTraitOne += traitOneValuesArray[i];
		}
		meanTraitOne /= simPopSize;

      	return meanTraitOne;
	}

	//Getter methods   
   public double getVarianceTraitOneSim() {
		return varianceTraitOne;
	}
   
   public double getStandardDevianceTraitOneSim() {
		return standardDevianceTraitOne;
	}

	public int getSimPopSizeSim() {
		return simPopSize;
	}
	
	public int getNumIterationsSim() {
		return numIterations;
	}
   
   //Setter methods   
   public void setVarianceTraitOneSim(double varianceTraitOneInput) {
		varianceTraitOne = varianceTraitOneInput;
	}
   
   public void setStandardDevianceTraitOneSim(double standardDevianceTraitOneInput) {
		standardDevianceTraitOne = standardDevianceTraitOneInput;
	}
   
	public void setSimPopSizeSim(int simPopSizeInput) {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterationsSim(int numIterationsInput) {
		numIterations = numIterationsInput;
	}
}