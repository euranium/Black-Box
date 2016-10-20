/*
	Name: Elizabeth Brooks
	File: IndividualTraitOne
	Modified: September 28, 2016
*/

//Imports
import java.util.Random;

//A class to calculate the mean value of trait one for model three
public class IndividualTraitOne{

	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
   	private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private double traitOne; //The mean of trait one
	private double dose; //The UVB dose at the waters surface
	private double functionTrait; //The mean functionTrait for UV penetration of the carapace
	private double interceptReactionNorm; //The intercept of the reaction norm of trait one with respect to the trait relater
	private double slopeReactionNorm; //The slope of the reaction norm of trait one with respect to the trait relater
	private double slopeConcentration; //The slope relating concentration of melanin to change in UVB transmittance
	private double transmittance; //The transmittance of a non-melanized Daphnia
	//Fields to store the values for calculation
	private double squareRootPart;
	private double numeratorPart;
	private double denominatorPart;
	
	//The class constructor to set the initial field values
	public IndividualTraitOne(SpeciesCharacteristics speciesInputs)
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
		calcDenominator();
	}
	
	//Method to calculate the denominator
	public void calcDenominator()
	{
		double firstHalfDenominator = (transmittance * dose - (slopeConcentration * slopeReactionNorm * (Math.pow(dose,2)))
				+ (slopeConcentration * dose * interceptReactionNorm));
		denominatorPart = firstHalfDenominator + squareRootPart;
		calcNumerator();
	}
	
	//Method to calculate the numerator
	private void calcNumerator()
	{
		numeratorPart = (2 * slopeReactionNorm * dose * functionTrait);
	}
	
	//Method for finishing the calculations for trait one
	public void calcTraitOne()
	{
		//Calculate the initial portions
		calcSquareRoot();

		//Complete the calculations
		double fraction = numeratorPart/denominatorPart;
		traitOne = ((-slopeReactionNorm * dose) + interceptReactionNorm + fraction);
	}
	
	//Method to numerically calculate the derivation of the fitness function for the intercept of the reaction norm
	public double numericallyCalcInterceptPartialDerivative(double nextGenMeanTraitOneInput, double meanSlopeReactionNormInput, 
			double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
		//Set the initial values
		functionTrait = meanFunctionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitOne = nextGenMeanTraitOneInput;
		double stepSize = (meanSlopeReactionNormInput * 0.001);
		//Calculate a small step up
		interceptReactionNorm += stepSize;
		calcTraitOne();
		double stepUpValue = traitOne;

		//Re-initialize
		functionTrait = meanFunctionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitOne = nextGenMeanTraitOneInput;
		//Calculate a small step down
		interceptReactionNorm -= stepSize;
		calcTraitOne();
		double stepDownValue = traitOne;
		
		//Calculate the partial derivative of the intercept
		double interceptPartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return interceptPartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for the slope of the reaction norm
	public double numericallyCalcSlopePartialDerivative(double nextGenMeanTraitOneInput, double meanSlopeReactionNormInput, 
			double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
		//Set the initial values
		functionTrait = meanFunctionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitOne = nextGenMeanTraitOneInput;
		double stepSize = (meanSlopeReactionNormInput * 0.001);
		//Calculate a small step up
		slopeReactionNorm += stepSize;
		calcTraitOne();
		double stepUpValue = traitOne;

		//Re-initialize
		functionTrait = meanFunctionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitOne = nextGenMeanTraitOneInput;
		//Calculate a small step down
		slopeReactionNorm -= stepSize;
		calcTraitOne();
		double stepDownValue = traitOne;
		
		//Calculate the partial derivative of the slope
		double slopePartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return slopePartialDerivative;
	}
	
	//Method to numerically calculate the derivation of the fitness function for functionTrait of UV penetration
	public double numericallyCalcFunctionTraitPartialDerivative(double nextGenMeanTraitOneInput, double meanSlopeReactionNormInput, 
			double meanInterceptReactionNormInput, double meanFunctionTraitInput)
	{
		//Set the initial values
		functionTrait = meanFunctionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitOne = nextGenMeanTraitOneInput;
		double stepSize = (meanFunctionTraitInput * 0.001);
		//Calculate a small step up
		functionTrait += stepSize;
		calcTraitOne();
		double stepUpValue = traitOne;

		//Re-initialize
		functionTrait = meanFunctionTraitInput;
		interceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = meanSlopeReactionNormInput;
		traitOne = nextGenMeanTraitOneInput;
		//Calculate a small step down
		functionTrait -= stepSize;
		calcTraitOne();
		double stepDownValue = traitOne;
		
		//Calculate the partial derivative of trait one
		double functionTraitPartialDerivative = ((stepUpValue - stepDownValue)/(stepSize+stepSize));
		return functionTraitPartialDerivative;
	}
	
	//Method to calculate the individual value of functionTrait
	public double getIndividualTraitOne()
	{
		//Set the initial values
		functionTrait = calcFunctionTraitInitial();
		interceptReactionNorm = calcInterceptReactionNormInitial();
		slopeReactionNorm = calcSlopeReactionNormInitial();
		//Begin calculating the different portions of the equation
		calcTraitOne();		
		//Return the calculated value for trait one
		return traitOne;
	}
	
	//A method to calculate the standard deviation of the intercept of the reaction norm
	public double calcStandardDeviationIntercept()
	{
		return Math.sqrt(Math.abs(getVarianceInterceptInitial()));
	}
	
	//A method to calculate the standard deviation of the slope of the reaction norm
	public double calcStandardDeviationSlope()
	{
		return Math.sqrt(Math.abs(getVarianceSlopeInitial()));
	}
	
	//A method to calculate the standard deviation of the UV functionTrait
	public double calcStandardDeviationFunctionTrait(){
		return Math.sqrt(Math.abs(getVarianceFunctionTraitInitial()));
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
   
   public double getTraitOneInitial() {
		return traitOne;
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
   
   public void setTraitOneInitial(double traitOneInitialInput) {
		traitOne = traitOneInitialInput;
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
