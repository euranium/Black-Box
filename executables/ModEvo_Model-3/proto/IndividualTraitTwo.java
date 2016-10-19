/*
	Name: Elizabeth Brooks
	File: IndividualTraitTwo
	Modified: September 28, 2016 
*/

//Imports
import java.util.Random;

//A class to calculate the mean value of trait two for model three
public class IndividualTraitTwo{

	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
   private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private double traitTwo; //The mean of trait one
	private double dose; //The UVB dose at the waters surface
	private double functionTrait; //The mean functionTrait for UV penetration of the carapace
	private double interceptReactionNorm; //The intercept of the reaction norm of trait one with respect to the trait relater
	private double slopeReactionNorm; //The slope of the reaction norm of trait one with respect to the trait relater
	private double slopeConcentration; //The slope relating concentration of melanin to change in UVB transmittance
	private double transmittance; //The transmittance of a non-melanized Daphnia
	private double attenuationCoefficient; //The attenuation coefficient
	//Fields to store the values for calculation
	private double squareRootPart;
	private double denominator;
	private double numerator;
	
	//The class constructor to set the initial field values
	public IndividualTraitTwo(SpeciesCharacteristics speciesInputs)
	{
      	//Call IndividualFitness (super) constructor
      	super(speciesInputs);
      	//Initialize species characteristics
      	speciesValues = speciesInputs;
		//Set initial values
		numIterations = getNumIterationsInitial();
		simPopSize = getSimPopSizeInitial();
		functionTrait = getMeanFunctionTraitCurrent();
		interceptReactionNorm = getMeanInterceptReactionNormCurrent();
		slopeReactionNorm = getMeanSlopeReactionNormCurrent();
      	attenuationCoefficient = speciesValues.getAttenuationCoefficient();
      	dose = speciesValues.getDoseInitial();
		transmittance = speciesValues.getTransmittance();
		slopeConcentration = speciesValues.getSlopeConcentration();
	}
	
	//Method to calculate the portion inside the square root
	public void calcSquareRoot()
	{
		double parentheses = (-transmittance + (slopeConcentration * slopeReactionNorm * dose) - (slopeConcentration * interceptReactionNorm));
		double parenSquared = (Math.pow(dose, 2)) * (Math.pow(parentheses, 2));
		double firstHalfSquareRoot = ((4 * slopeConcentration * slopeReactionNorm * (Math.pow(dose, 2)) * functionTrait));
		double insideSquareRoot = firstHalfSquareRoot + parenSquared;
		squareRootPart = Math.sqrt(Math.abs(insideSquareRoot));
		calcNumerator();
	}
	
	//Method to calculate the denominator
	public void calcNumerator()
	{
		double firstHalfNumerator = (transmittance * dose - (slopeConcentration * slopeReactionNorm * (Math.pow(dose,2))) + (slopeConcentration * dose * interceptReactionNorm));
		numerator = firstHalfNumerator + squareRootPart;
		calcDenominator();
	}
	
	//Method to calculate the numerator
	private void calcDenominator()
	{
		denominator = (2 * functionTrait);
	}
	
	//Method for finishing the calculations for trait one
	public void calcTraitTwo()
	{
		//Calculate the initial portions
		calcSquareRoot();

		//Complete the calculations
		double fraction = numerator/denominator;
		traitTwo = ((1/attenuationCoefficient) * Math.log(fraction));
	}
	
	//Method to numerically calculate the derivation of the fitness function for the intercept of the reaction norm
	public double numericallyCalcInterceptPartialDerivative(double nextGenMeanTraitTwoInput, double meanSlopeReactionNormInput, 
			double meanInterceptReactionNormInput, double functionTraitInput)
	{
		//Set initial values
		functionTrait = functionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitTwo = nextGenMeanTraitTwoInput;
		double stepSize = (interceptReactionNorm * 0.001);
		//Calculate a small step up
		interceptReactionNorm += stepSize;
		calcTraitTwo();
		double stepUpValue = traitTwo;
		
		//Re-initialize
		functionTrait = functionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitTwo = nextGenMeanTraitTwoInput;
		//Calculate a small step down
		interceptReactionNorm += stepSize;
		calcTraitTwo();
		double stepDownValue = traitTwo;
		
		//Calculate the partial derivative of trait one
		double interceptPartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return interceptPartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for the slope of the reaction norm
	public double numericallyCalcSlopePartialDerivative(double nextGenMeanTraitTwoInput, double meanSlopeReactionNormInput, 
			double meanInterceptReactionNormInput, double functionTraitInput)
	{
		//Set initial values
		functionTrait = functionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitTwo = nextGenMeanTraitTwoInput;
		double stepSize = (slopeReactionNorm * 0.001);		
		//Calculate a small step up
		slopeReactionNorm += stepSize;
		calcTraitTwo();
		double stepUpValue = traitTwo;
		
		//Re-initialize
		functionTrait = functionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitTwo = nextGenMeanTraitTwoInput;
		//Calculate a small step down
		slopeReactionNorm += stepSize;
		calcTraitTwo();
		double stepDownValue = traitTwo;
		
		//Calculate the partial derivative of trait one
		double slopePartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return slopePartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for functionTrait of UV penetration
	public double numericallyCalcFunctionTraitPartialDerivative(double nextGenMeanTraitTwoInput, double meanSlopeReactionNormInput,
			double meanInterceptReactionNormInput, double functionTraitInput)
	{
		//Set initial values
		functionTrait = functionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitTwo = nextGenMeanTraitTwoInput;
		double stepSize = (functionTrait * 0.001);
		//Calculate a small step up
		functionTrait += stepSize;
		calcTraitTwo();
		double stepUpValue = traitTwo;
		
		//Re-initialize
		functionTrait = functionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitTwo = nextGenMeanTraitTwoInput;
		//Calculate a small step down
		functionTrait += stepSize;
		calcTraitTwo();
		double stepDownValue = traitTwo;
		
		//Calculate the partial derivative of trait one
		double functionTraitPartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return functionTraitPartialDerivative;
	}

	//Method to calculate the individual value of trait one
	public double getIndividualTraitTwo()
	{
		//Set the initial values
		functionTrait = calcFunctionTraitInitial();
		interceptReactionNorm = calcInterceptReactionNormInitial();
		slopeReactionNorm = calcSlopeReactionNormInitial();
		
		//Begin calculating the different portions of the equation
		calcTraitTwo();	
		
		//Return the calculated value for trait one
		return traitTwo;
	}
	
	//A method to calculate the standard deviation of the intercept of the reaction norm
	public double calcStandardDeviationIntercept()
	{
		return Math.sqrt(getVarianceInterceptInitial());
	}
	
	//A method to calculate the standard deviation of the slope of the reaction norm
	public double calcStandardDeviationSlope()
	{
		return Math.sqrt(getVarianceSlopeInitial());
	}
	
	//A method to calculate the standard deviation of the UV functionTrait
	public double calcStandardDeviationFunctionTrait(){
		return Math.sqrt(getVarianceFunctionTraitInitial());
	}
	
	//Methods to calc the individual simulated values for trait one
	private double calcSlopeReactionNormInitial() 
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return slopeReactionNorm = (randomSimulation.nextGaussian() * calcStandardDeviationSlope() + getMeanSlopeReactionNormCurrent());
	}
	
	private double  calcInterceptReactionNormInitial()
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return interceptReactionNorm = (randomSimulation.nextGaussian() * calcStandardDeviationIntercept() + getMeanInterceptReactionNormCurrent());
	}

	private double calcFunctionTraitInitial()
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return functionTrait = (randomSimulation.nextGaussian() * calcStandardDeviationFunctionTrait() + getMeanFunctionTraitCurrent());
	}
	
	//Getter methods
	public int getNumIterations() {
		return numIterations;
	}
   
   public int getSimPopSize() {
		return simPopSize;
	}
   
   public double getTraitTwoInitial() {
		return traitTwo;
	}
   
   public double getDose() {
		return dose;
	}
   
   public double getFunctionTrait() {
		return functionTrait;
	}
   
   public double getInterceptReactionNorm() {
		return interceptReactionNorm;
	}
   
   public double getSlopeReactionNorm() {
		return slopeReactionNorm;
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
   
   public void setTraitTwoInitial(double traitTwoInitialInput) {
		traitTwo = traitTwoInitialInput;
	}
   
   public void setDose(double doseInput) {
		dose = doseInput;
	}
   
   public void setFunctionTrait(double functionTraitInput) {
		functionTrait = functionTraitInput;
	}
   
   public void setInterceptReactionNorm(double interceptReactionNormInput) {
		interceptReactionNorm = interceptReactionNormInput;
	}
   
   public void setSlopeReactionNorm(double slopeReactionNormInput) {
		slopeReactionNorm = slopeReactionNormInput;
	}
   
   public void setSlopeConcentration(double slopeConcentrationInput) {
		slopeConcentration = slopeConcentrationInput;
	}
   
   public void setTransmittance(double transmittanceInput) {
		transmittance = transmittanceInput;
	}
}
