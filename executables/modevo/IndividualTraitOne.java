/*
	Name: Elizabeth Brooks
	File: IndividualTraitOne
	Modified: February 4, 2016
*/

//Imports
import java.util.Random;

//A class to calculate the mean value of trait one for model two
public class IndividualTraitOne extends GeneralModel{

	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private double traitOne; //The mean of trait one
	private double traitTwo; //The mean of trait two
	private double phenotypicVarianceTraitTwo; //The phenotypic variance of trait two
	private double interceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	private double slopeReactionNorm; //The mean slope of the reaction norm of trait one with respect to the trait relater
	private double dose; //The initial value of the UVB dose
	private double attenuationCoefficient; //The attenuation coefficient
	//Class fields to hold portions of the equation to calculate the mean value of trait one
	private double exponent;
	
	//The class constructor
	public IndividualTraitOne(int numIterationsInput, int simPopSizeInput)
	{
		//Set initial values
		numIterations = numIterationsInput;
		simPopSize = simPopSizeInput;
		numIterations = numIterationsInput;
		simPopSize = simPopSizeInput;
		traitTwo = super.getMeanTraitTwoInitial();
		phenotypicVarianceTraitTwo = super.getPhenotypicVarianceTraitTwoInitial();
		interceptReactionNorm = calcInterceptReactionNormInitial();
		slopeReactionNorm = calcSlopeReactionNormInitial();
		dose = super.getDose();
		attenuationCoefficient = super.getAttenuationCoefficientInitial();
	}
	
	//A method to calculate the exponent of base e
	public void calculateExponent(double meanTraitTwoInput)
	{
		traitTwo = meanTraitTwoInput;
		double parentheses;
		parentheses = ((-2*traitTwo)+(attenuationCoefficient*phenotypicVarianceTraitTwo));
		exponent = ((0.5*attenuationCoefficient)*parentheses);
	}
	
	//Method to calculate the individual value of trait one
	public void calculateTraitOne(double meanSlopeReactionNormInput, double meanInterceptReactionNormInput, double meanTraitTwoInput)
	{
		traitTwo = meanTraitTwoInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		calculateExponent(traitTwo);
		traitOne = (interceptReactionNorm+((-1+Math.pow((Math.E), exponent))*slopeReactionNorm*dose));
	}
	
	//A method to retrieve the mean value of trait one
	public double getTraitOne(double meanSlopeReactionNormInput, double meanInterceptReactionNormInput, double meanTraitTwoInput)
	{
		//Set initial values
		traitTwo = meanTraitTwoInput;
		interceptReactionNorm = calcInterceptReactionNormInitial();
		slopeReactionNorm = calcSlopeReactionNormInitial();
		phenotypicVarianceTraitTwo = super.getPhenotypicVarianceTraitTwoInitial();
		dose = super.getDose();
		attenuationCoefficient = super.getAttenuationCoefficientInitial();
		
		//Begin calculating the different portions of the equation
		calculateTraitOne(meanSlopeReactionNormInput, meanInterceptReactionNormInput, meanTraitTwoInput);
		
		return traitOne;
	}
	
	//A method to calculate the standard deviation of the intercept of the reaction norm
	public double calcStandardDeviationIntercept()
	{
		return Math.sqrt(Math.abs(super.getVarianceInterceptInitial()));
	}
	
	//A method to calculate the standard deviation of the slope of the reaction norm
	public double calcStandardDeviationSlope()
	{
		return Math.sqrt(Math.abs(super.getVarianceSlopeInitial()));
	}
	
	//Methods to calc the individual simulated values for trait one
	private double calcSlopeReactionNormInitial() 
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return slopeReactionNorm = (randomSimulation.nextGaussian() * calcStandardDeviationSlope() + super.getMeanSlopeReactionNormInitial());
	}
	
	private double  calcInterceptReactionNormInitial()
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return interceptReactionNorm = (randomSimulation.nextGaussian() * calcStandardDeviationIntercept() + super.getMeanInterceptReactionNormInitial());
	}
	
	//Getter methods
	public int getSimPopSizeInitial() {
		return simPopSize;
	}
	
	public int getNumIterationsInitial() {
		return numIterations;
	}
}