/*
	Name: Elizabeth Brooks
	File: IndividualTraitOne
	Modified: October 30, 2016
*/

//Imports
import java.security.SecureRandom;

//A class to calculate the mean value of trait one for model two
public class IndividualTraitOne {

	//Class fields used to calculate the mean value of trait one
	private SecureRandom randomSimulation; //For simulation of individual variable values
   private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private double dose; //The UVB dose at the waters surface
	private double slopeConcentration; //The slope relating concentration of melanin to change in UVB transmittance
	private double transmittance; //The transmittance of a non-melanized Daphnia
   private double standardDeviationSlope;
   private double standardDeviationIntercept;
   private double standardDeviationFunctionTrait;
	
	//The class constructor to set the initial field values
	public IndividualTraitOne(SpeciesCharacteristics speciesInputs)
	{
      //Initialize species characteristics
      speciesValues = speciesInputs;
		//Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
      dose = speciesValues.getDoseInitial();
		transmittance = speciesValues.getTransmittance();
		slopeConcentration = speciesValues.getSlopeConcentration();
      standardDeviationSlope = calcStandardDeviationSlope();
      standardDeviationIntercept = calcStandardDeviationIntercept();
      standardDeviationFunctionTrait = calcStandardDeviationFunctionTrait();
	}
	
	//Method to calculate the portion inside the square root
	public double calcSquareRoot(double individualSlopeReactionNormInput, double individualInterceptReactionNormInput, double individualFunctionTraitInput)
	{
      //Set initial values
		double individualFunctionTrait = individualFunctionTraitInput;
		double individualSlopeReactionNorm = individualSlopeReactionNormInput;
		double individualInterceptReactionNorm = individualInterceptReactionNormInput;
		double parentheses = (-transmittance + (slopeConcentration * individualSlopeReactionNorm * dose) - (slopeConcentration * individualInterceptReactionNorm));
		double parenSquared = (Math.pow(dose, 2)) * (Math.pow(parentheses, 2));
		double firstHalfSquareRoot = ((4 * slopeConcentration * individualSlopeReactionNorm * (Math.pow(dose, 2)) * individualFunctionTrait));
		double insideSquareRoot = firstHalfSquareRoot + parenSquared;
		double squareRootPart = Math.sqrt(Math.abs(insideSquareRoot));
      return squareRootPart;
	}
	
	//Method to calculate the denominator
	public double calcDenominator(double individualSlopeReactionNormInput, double individualInterceptReactionNormInput, double individualFunctionTraitInput, 
      double squareRootPartInput)
	{
      //Set initial values
      double squareRootPart = squareRootPartInput;
      double individualFunctionTrait = individualFunctionTraitInput;
		double individualSlopeReactionNorm = individualSlopeReactionNormInput;
		double individualInterceptReactionNorm = individualInterceptReactionNormInput;
		double firstHalfDenominator = (transmittance * dose - (slopeConcentration * individualSlopeReactionNorm * (Math.pow(dose,2)))
				+ (slopeConcentration * dose * individualInterceptReactionNorm));
		double denominatorPart = firstHalfDenominator + squareRootPart;
      return denominatorPart;
	}
	
	//Method to calculate the numerator
	private double calcNumerator(double individualSlopeReactionNormInput, double individualFunctionTraitInput)
	{
      //Set initial values
		double individualFunctionTrait = individualFunctionTraitInput;
		double individualSlopeReactionNorm = individualSlopeReactionNormInput;
		double numeratorPart = (2 * individualSlopeReactionNorm * dose * individualFunctionTrait);
      return numeratorPart;
	}
	
	//Method for finishing the calculations for trait one
	public double calcTraitOne(double individualSlopeReactionNormInput, double individualInterceptReactionNormInput, double individualFunctionTraitInput)
	{
      //Set initial values
      double traitOne;
      double individualFunctionTrait = individualFunctionTraitInput;
		double individualSlopeReactionNorm = individualSlopeReactionNormInput;
		double individualInterceptReactionNorm = individualInterceptReactionNormInput;
		//Calculate the initial portions
		double squareRootPart = calcSquareRoot(individualSlopeReactionNorm, individualInterceptReactionNorm, individualFunctionTrait);
      double numeratorPart = calcNumerator(individualSlopeReactionNorm, individualFunctionTrait);
		double denominatorPart = calcDenominator(individualSlopeReactionNorm, individualInterceptReactionNorm, individualFunctionTrait, squareRootPart);
		//Complete the calculations
		double fraction = numeratorPart/denominatorPart;
      //Return individual trait one value
		return traitOne = ((-individualSlopeReactionNorm * dose) + individualInterceptReactionNorm + fraction);
	}
	
	//Method to numerically calculate the derivation of the fitness function for the intercept of the reaction norm
	public double numericallyCalcInterceptPartialDerivative(double meanSlopeReactionNormInput, double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
		//Set the initial values
		double meanFunctionTrait = meanFunctionTraitInput;
		double meanInterceptReactionNorm = meanInterceptReactionNormInput;
		double meanSlopeReactionNorm = meanSlopeReactionNormInput;
		/*double tempMeanInterceptReactionNorm = meanInterceptReactionNorm;
      while(tempMeanInterceptReactionNorm == 0){ //Will cause h = 0
         tempMeanInterceptReactionNorm = calcIndividualSlopeReactionNorm(tempMeanInterceptReactionNorm);
      }
		double stepSize = (tempMeanInterceptReactionNorm * 0.01); //Small time step?*/
      double stepSize = 0.1;
		//Calculate a small step up
		double stepUpInterceptReactionNorm = meanInterceptReactionNorm;
      stepUpInterceptReactionNorm += stepSize;
		double stepUpValue = getIndividualTraitOne(meanSlopeReactionNorm, stepUpInterceptReactionNorm, meanFunctionTrait);
		//Calculate a small step down
		double stepDownInterceptReactionNorm = meanInterceptReactionNorm;
      stepDownInterceptReactionNorm -= stepSize;
      /*double h = (meanInterceptReactionNorm - stepDownInterceptReactionNorm);*/
		double stepDownValue = getIndividualTraitOne(meanSlopeReactionNorm, stepDownInterceptReactionNorm, meanFunctionTrait);
		//Calculate the partial derivative of the intercept
		double interceptPartialDerivative = ((stepUpValue - stepDownValue)/(2*stepSize));
		return interceptPartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for the slope of the reaction norm
	public double numericallyCalcSlopePartialDerivative(double meanSlopeReactionNormInput, double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
		//Set the initial values
		double meanFunctionTrait = meanFunctionTraitInput;
		double meanInterceptReactionNorm = meanInterceptReactionNormInput;
		double meanSlopeReactionNorm = meanSlopeReactionNormInput;
	   /*double tempMeanSlope = meanSlopeReactionNorm;
      while(tempMeanSlope == 0){ //Will cause h = 0
         tempMeanSlope = calcIndividualSlopeReactionNorm(tempMeanSlope);
      }
      double stepSize = (tempMeanSlope * 0.01); //Small time step?*/
      double stepSize = 0.1;
		//Calculate a small step up
		double stepUpSlopeReactionNorm = meanSlopeReactionNorm;
      stepUpSlopeReactionNorm += stepSize;
		double stepUpValue = getIndividualTraitOne(stepUpSlopeReactionNorm, meanInterceptReactionNorm, meanFunctionTrait);
		//Calculate a small step down
		double stepDownSlopeReactionNorm = meanSlopeReactionNorm;
      stepDownSlopeReactionNorm -= stepSize;
      /*double h = (meanSlopeReactionNorm - stepDownSlopeReactionNorm);*/
		double stepDownValue = getIndividualTraitOne(stepDownSlopeReactionNorm, meanInterceptReactionNorm, meanFunctionTrait);
		//Calculate the partial derivative of the slope
		double slopePartialDerivative = ((stepUpValue - stepDownValue)/(2*stepSize));
		return slopePartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for functionTrait of UV penetration
	public double numericallyCalcFunctionTraitPartialDerivative(double meanSlopeReactionNormInput, double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
		//Set the initial values
		double meanFunctionTrait = meanFunctionTraitInput;
		double meanInterceptReactionNorm = meanInterceptReactionNormInput;
		double meanSlopeReactionNorm = meanSlopeReactionNormInput;
		/*double tempMeanFunctionTrait = meanFunctionTrait;
      while(tempMeanFunctionTrait == 0){ //Will cause h = 0
         tempMeanFunctionTrait = calcIndividualSlopeReactionNorm(tempMeanFunctionTrait);
      }
		double stepSize = (tempMeanFunctionTrait * 0.01); //Small time step?*/
      double stepSize = 0.1;
		//Calculate a small step up
		double stepUpFunctionTrait = meanFunctionTrait;
      stepUpFunctionTrait += stepSize;
		double stepUpValue = getIndividualTraitOne(meanSlopeReactionNorm, meanInterceptReactionNorm, stepUpFunctionTrait);
		//Calculate a small step down
		double stepDownFunctionTrait = meanFunctionTrait;
      stepDownFunctionTrait -= stepSize;
      /*double h = (meanFunctionTrait - stepDownFunctionTrait);*/
		double stepDownValue = getIndividualTraitOne(meanSlopeReactionNorm, meanInterceptReactionNorm, stepDownFunctionTrait);
		//Calculate the partial derivative of trait one
		double functionTraitPartialDerivative = ((stepUpValue - stepDownValue)/(2*stepSize));
		return functionTraitPartialDerivative;
	}
	
	//Method to calculate the individual value of functionTrait
	public double getIndividualTraitOne(double nextGenMeanSlopeReactionNormInput, double nextGenMeanInterceptReactionNormInput, double nextGenMeanFunctionTraitInput)
	{
		//Set the initial values
      double meanFunctionTrait = nextGenMeanFunctionTraitInput;
		double meanInterceptReactionNorm = nextGenMeanInterceptReactionNormInput;
		double meanSlopeReactionNorm = nextGenMeanSlopeReactionNormInput;
      double individualFunctionTrait;
		double individualSlopeReactionNorm;
		double individualInterceptReactionNorm;
      //Calc individual values
		individualFunctionTrait = calcIndividualFunctionTrait(meanFunctionTrait);
		individualInterceptReactionNorm = calcIndividualInterceptReactionNorm(meanInterceptReactionNorm);
		individualSlopeReactionNorm = calcIndividualSlopeReactionNorm(meanSlopeReactionNorm);
		//Return the calculated value for trait one
		return calcTraitOne(individualSlopeReactionNorm, individualInterceptReactionNorm, individualFunctionTrait);		
	}
	
	//A method to calculate the standard deviation of the intercept of the reaction norm
	public double calcStandardDeviationIntercept()
	{
		return Math.sqrt(Math.abs(speciesValues.getPhenotypicVarianceInterceptReactionNorm()));
	}
	
	//A method to calculate the standard deviation of the slope of the reaction norm
	public double calcStandardDeviationSlope()
	{
		return Math.sqrt(Math.abs(speciesValues.getPhenotypicVarianceSlopeReactionNorm()));
	}
	
	//A method to calculate the standard deviation of the UV functionTrait
	public double calcStandardDeviationFunctionTrait(){
		return Math.sqrt(Math.abs(speciesValues.getPhenotypicVarianceFunctionTrait()));
	}
	
	//Methods to calc the individual simulated values for trait one
	private double calcIndividualSlopeReactionNorm(double nextGenMeanSlopeReactionNormInput) 
	{
      //Set the initial values
		double meanSlopeReactionNorm = nextGenMeanSlopeReactionNormInput;
		randomSimulation = new SecureRandom();
		//simulation of normally distributed populations
		return (randomSimulation.nextGaussian() * standardDeviationSlope + meanSlopeReactionNorm);
	}
	
	private double  calcIndividualInterceptReactionNorm(double nextGenMeanInterceptReactionNormInput)
	{
      //Set the initial values
		double meanInterceptReactionNorm = nextGenMeanInterceptReactionNormInput;
		randomSimulation = new SecureRandom();
		//simulation of normally distributed populations
		return (randomSimulation.nextGaussian() * standardDeviationIntercept + meanInterceptReactionNorm);
	}

	private double calcIndividualFunctionTrait(double nextGenMeanFunctionTraitInput)
	{
      //Set the initial values
      double meanFunctionTrait = nextGenMeanFunctionTraitInput;
		randomSimulation = new SecureRandom();
		//simulation of normally distributed populations
		return (randomSimulation.nextGaussian() * standardDeviationFunctionTrait + meanFunctionTrait);
	}

	//Getter methods
	public int getNumIterations() {
		return numIterations;
	}
   
   public int getSimPopSize() {
		return simPopSize;
	}
      
   public double getDose() {
		return dose;
	}
   
   public double getSlopeConcentration() {
		return slopeConcentration;
	}
   
   public double getTransmittance() {
		return transmittance;
	}
   
   //Setter methods
	public void setNumIterations(int numIterationsInput) {
		numIterations = numIterationsInput;
	}
   
   public void setSimPopSize(int simPopSizeInput) {
		simPopSize = simPopSizeInput;
	}
   
   public void setDose(double doseInput) {
		dose = doseInput;
	}
      
   public void setSlopeConcentration(double slopeConcentrationInput) {
		slopeConcentration = slopeConcentrationInput;
	}
   
   public void setTransmittance(double transmittanceInput) {
		transmittance = transmittanceInput;
	}
}
