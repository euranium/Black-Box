/*
	Name: Elizabeth Brooks
	File: IndividualFitness
	Modified: February 4, 2016
*/

//A class to calculate the individual fitness 'w'
public class IndividualFitness{

	//Class fields to store variables
   private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics
	private double individualFitness; //Individual fitness
	private double meanTraitOne; //Mean value of trait one
	private double meanTraitTwo; //Mean value of trait two
	private double varianceTraitOne; //Variance of the Gaussian function relating trait one to fitness
	private double optimumTraitOne; //Optimum value of trait one
	private double varianceTraitTwo; //Variance of the Gaussian function relating trait two to fitness
	private double optimumTraitTwo; //Optimum value of trait two
	//private int numGenerations; //The number of values entered
	//Class fields to store the values of the calculated portions of the equation to find the individualFitness
	private double exponentOne;
	private double exponentTwo;
	private double fractionOne;
	private double fractionTwo;
	//Class fields to store values for determining derivatives
	private double stepSize;
	private double stepUpValue;
	private double stepDownValue;
	private double traitOnePartialDerivative;
	private double traitTwoPartialDerivative;
	//Fields for the MeanFitness subclasses
	public int numIterations;
	public double phenotypicVarianceTraitOneInitial;
	public double phenotypicVarianceTraitTwoInitial;
	public double phenotypicVarianceInterceptReactionNormInitial;
	public double phenotypicVarianceSlopeReactionNormInitial;
	public double phenotypicVariancePreferenceInitial;
	public int simPopSize;
	
	//The class constructor which will set the fields values
	public IndividualFitness(SpeciesCharacteristics speciesInputs)
	{
      //Initialize species characteristics
      speciesValues = speciesInputs;
		//Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		meanTraitOne = speciesValues.getMeanTraitOne();
		meanTraitTwo = speciesValues.getMeanTraitTwo();
		varianceTraitOne = speciesValues.getVarianceTraitOne();
		optimumTraitOne = speciesValues.getOptimumTraitOne();
		varianceTraitTwo = speciesValues.getVarianceTraitTwo();
		optimumTraitTwo = speciesValues.getOptimumTraitTwo();
	}
   
	//A method to calculate the value of the first exponent
	public void calcExponentOne()
	{
		//Calculate the top of the fraction
		double top = Math.pow((meanTraitOne-optimumTraitOne),2); //(m-wM)^2
		//Calculate the bottom of the fraction
		double bottom = 2*varianceTraitOne;
		//Calculate the total of the first exponent
		exponentOne = -(top/bottom);
	}
	
	//A method to calculate the value of the second exponent
	public void calcExponentTwo()
	{
		//Calculate the top of the fraction
		double top = Math.pow((meanTraitTwo-optimumTraitTwo),2); //(m-wD)^2
		//Calculate the bottom of the fraction
		double bottom = 2*varianceTraitTwo;
		//Calculate the total of the first exponent
		exponentTwo = -(top/bottom);
	}
	
	//A method to calculate the value of the first fraction
	public void calcFractionOne()
	{
		//Calculate the inside of the square root portion of the fraction
		double squareRootInside = (varianceTraitOne*2*Math.PI); //vM*2*pi
		//Calculate the value of the first fraction
		fractionOne = (1/Math.sqrt(Math.abs(squareRootInside)));
	}
	
	//A method to calculate the value of the second fraction
	public void calcFractionTwo()
	{
		//Calculate the inside of the square root portion of the fraction
		double squareRootInside = (varianceTraitTwo*2*Math.PI); //vD*2*pi
		//Calculate the value of the second fraction
		fractionTwo = (1/Math.sqrt(Math.abs(squareRootInside)));
	}
	
	//A method to calculate the total value of the individual fitness 'w'
	public void calcIndividualFitness(double meanTraitOneInput, double meanTraitTwoInput)
	{
		meanTraitOne = meanTraitOneInput;
		meanTraitTwo = meanTraitTwoInput;
		calcExponentOne();
		calcExponentTwo();
		calcFractionOne();
		calcFractionTwo();
		//Calculate the value of the section of the equation before the addition sign
		double sectionOne = (fractionOne*Math.pow((Math.E), exponentOne));
		//Calculate the value of the section of the equation after the addition sign
		double sectionTwo = (fractionTwo*Math.pow((Math.E), exponentTwo));
		//Calculate the value of w by adding the two sections together
		individualFitness = (sectionOne+sectionTwo);
	}
	
	//Method to numerically calculate the derivation of the fitness function for trait one
	public double numericallyCalcTraitOnePartialDerivative(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput)
	{
		//Set initial values
		meanTraitOne = meanTraitOneInput;
		stepSize = (meanTraitOneInput * 0.001);
		//Calculate a small step up
		meanTraitOne += stepSize;
		getIndividualFitness(numIterationsInput, simPopSizeInput, meanTraitOne, meanTraitTwoInput);
		stepUpValue = individualFitness;		
		
		//Re-initialize
		meanTraitOne = meanTraitOneInput;
		//Calculate a small step down
		meanTraitOne -= stepSize;
		getIndividualFitness(numIterationsInput, simPopSizeInput, meanTraitOne, meanTraitTwoInput);
		stepDownValue = individualFitness;
		
		//Calculate the partial derivative of trait one
		traitOnePartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return traitOnePartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for trait two
	public double numericallyCalcTraitTwoPartialDerivative(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput)
	{
		//Set initial values
		meanTraitTwo = meanTraitTwoInput;
		stepSize = (meanTraitTwoInput * 0.001);
		//Calculate a small step up
		meanTraitTwo += stepSize;
		getIndividualFitness(numIterationsInput, simPopSizeInput, meanTraitOneInput, meanTraitTwo);
		stepUpValue = individualFitness;
		
		//Re-initialize
		meanTraitTwo = meanTraitTwoInput;
		//Calculate a small step down
		meanTraitTwo -= stepSize;
		getIndividualFitness(numIterationsInput, simPopSizeInput, meanTraitOneInput, meanTraitTwo);
		stepDownValue = individualFitness;

		//Calculate the partial derivative of trait two
		traitTwoPartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return traitTwoPartialDerivative;
	}
	
	//Methods to set the updated mean values of trait one and two
	public void setMeanTraitOne(double meanTraitOneInput)
	{
		meanTraitOne = meanTraitOneInput;
	}
	
	public void setMeanTraitTwo(double meanTraitTwoInput)
	{
		meanTraitTwo = meanTraitTwoInput;
	}
	
	//The following are methods to retrieve the value of specific variables
	public double getIndividualFitness(int numIterationsInput, int simPopSizeInput, double meanTraitOneInput, double meanTraitTwoInput)
	{
		//Set initial values
		numIterations = numIterationsInput;
		simPopSize = simPopSizeInput;
		varianceTraitOne = speciesValues.getVarianceTraitOne();
		optimumTraitOne = speciesValues.getOptimumTraitOne();
		varianceTraitTwo = speciesValues.getVarianceTraitTwo();
		optimumTraitTwo = speciesValues.getOptimumTraitTwo();
		
      //Calculate the individual fitness based on the given mean trait values
		calcIndividualFitness(meanTraitOneInput, meanTraitTwoInput);

		return individualFitness;
	}
	
	public double getMeanTraitOne()
	{
		return meanTraitOne;
	}
	
	
	public double getVarianceTraitOne()
	{
		return varianceTraitOne;
	}
	
	public double getOptimumTraitOne()
	{
		return optimumTraitOne;
	}
	
	public double getMeanTraitTwo()
	{
		return meanTraitTwo;
	}
	
	public double getVarianceTraitTwo()
	{
		return varianceTraitTwo;
	}
	
	public double getOptimumTraitTwo()
	{
		return optimumTraitTwo;
	}
	
	public double getFractionOne()
	{
		return fractionOne;
	}
	
	public double getFractionTwo()
	{
		return fractionTwo;
	}
	
	public double getExponentOne()
	{
		return exponentOne;
	}
	
	public double getExponentTwo()
	{
		return exponentTwo;
	}
	
	//Getter methods for the MeanFitness subclasses
	public int getIterationNum()
	{
		return numIterations;
	}
	
	public int getSimPopSizeInitial()
	{
		return simPopSize;
	}
	
	public double getPhenotypicVarianceTraitOneInitial()
	{
		return phenotypicVarianceTraitOneInitial;
	}
	
	public double getPhenotypicVarianceTraitTwoInitial()
	{
		return phenotypicVarianceTraitTwoInitial;
	}

	
	public double getPhenotypicVarianceInterceptReactionNormInitial()
	{
		return phenotypicVarianceInterceptReactionNormInitial;
	}
	
	public double getPhenotypicVarianceSlopeReactionNormInitial()
	{
		return phenotypicVarianceSlopeReactionNormInitial;
	}
	
	public double getPhenotypicVariancePreferenceInitial()
	{
		return phenotypicVariancePreferenceInitial;
	}
}
