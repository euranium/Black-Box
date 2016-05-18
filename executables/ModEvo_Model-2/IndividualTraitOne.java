/*
	Name: Elizabeth Brooks
	File: IndividualTraitOne
	Modified: May 11, 2016
*/

//Imports
import java.util.Random;

//A class to calculate the mean value of trait one for model two
public class IndividualTraitOne {

	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	private double traitOne; //The mean of trait one
	private double traitTwo; //The mean of trait two
	private double phenotypicVarianceTraitTwo; //The phenotypic vacriance of trait two
	private double dose; //The UVB dose at the waters surface
	private double meanSlopeReactionNorm; //The mean slope of the reaction norm of trait one with respect to the trait relater
	private double meanInterceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	private double slopeReactionNorm; //The individual slope of the reaction norm of trait one with respect to the trait relater
	private double interceptReactionNorm; //The individual intercept of the reaction norm of trait one with respect to the trait relater
	private double phenotypicVarianceSlope; //The phenotypic variance of the slope of the reaction norm
	private double phenotypicVarianceIntercept; //The phenotypic variance of the intercept of the reaction norm
	private double standardDevianceSlope; //The standard deviance of the slope of the reaction norm
	private double standardDevianceIntercept; //The standard deviance of the intercept of the reaction norm
	private double slopeConcentration; //The slope relating concentration of melanin to change in UVB transmittance
	private double transmittance; //The transmittance of a non-melanized Daphnia
	private double attenuationCoefficient; //The attenuation coefficient
	//Fields to store the values for calculation
	private double exponent;
	private double squareRootPart;
	private double numeratorPart;
	private double denominatorPart;
	
	//The class constructor to set the initial field values
	public IndividualTraitOne(SpeciesCharacteristics speciesInputs)
	{
        //Initialize species characteristics
        speciesValues = speciesInputs;
		//Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		traitTwo = speciesValues.getMeanTraitTwo();
		phenotypicVarianceTraitTwo = speciesValues.getPhenotypicVarianceTraitTwo();
		phenotypicVarianceSlope = speciesValues.getPhenotypicVarianceSlopeReactionNorm();
		phenotypicVarianceIntercept = speciesValues.getPhenotypicVarianceInterceptReactionNorm();
		interceptReactionNorm = speciesValues.getMeanInterceptReactionNorm();
		slopeReactionNorm = speciesValues.getMeanSlopeReactionNorm();
		dose = speciesValues.getDoseInitial();
		attenuationCoefficient = speciesValues.getAttenuationCoefficient();
		calcStandardDeviationSlope();
		calcStandardDeviationIntercept();
	}

	//A method to calculate the standard deviation of the slope of the reaction norm
	public void calcStandardDeviationSlope()
	{
		standardDevianceSlope = Math.sqrt(Math.abs(phenotypicVarianceSlope));
	}
	
	//A method to calculate the standard deviation of the intercept of the reaction norm
	public void calcStandardDeviationIntercept()
	{
		standardDevianceIntercept = Math.sqrt(Math.abs(phenotypicVarianceIntercept));
	}
	
	//A method to calculate the exponent of base e
	public void calculateExponent()
	{
		exponent = (-(traitTwo)*attenuationCoefficient);
	}
	
	//Method to calculate the individual value of trait one
	public void calculateTraitOne()
	{
		double parenthases;
		calculateExponent();
		parenthases = (dose*(Math.pow((Math.E), exponent))*(-(dose)));
		traitOne = (interceptReactionNorm+slopeReactionNorm*(parenthases));
	}
	
	//A method to retrieve the mean value of trait one
	public double getIndividualTraitOne(double meanTraitTwoInput, double meanSlopeReactionNormInput, double meanInterceptReactionNormInput)
	{
		//Set initial values
		traitTwo = meanTraitTwoInput;
		meanSlopeReactionNorm = meanSlopeReactionNormInput;
		meanInterceptReactionNorm = meanInterceptReactionNormInput;
		slopeReactionNorm = calcIndividualSlopeReactionNorm();
		interceptReactionNorm = calcIndividualInterceptReactionNorm();
		//Begin calculating the different portions of the equation
		calculateTraitOne();
		return traitOne;
	}
	
	//Methods to calc the individual simulated values for trait one
	private double calcIndividualSlopeReactionNorm() 
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return slopeReactionNorm = (randomSimulation.nextGaussian() * standardDevianceSlope + meanSlopeReactionNorm);
	}
	
	private double  calcIndividualInterceptReactionNorm()
	{
		randomSimulation = new Random();
		//simulation of normally distributed populations
		return interceptReactionNorm = (randomSimulation.nextGaussian() * standardDevianceIntercept + meanInterceptReactionNorm);
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

	public double getStandardDevianceSlope() {
		return standardDevianceSlope;
	}

	public double getStandardDevianceIntercept() {
		return standardDevianceIntercept;
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

	public void getStandardDevianceSlope(double standardDevianceSlopeInput) {
		standardDevianceSlope = standardDevianceSlopeInput;
	}

	public void getStandardDevianceIntercept(double standardDevianceInterceptInput) {
		standardDevianceIntercept = standardDevianceInterceptInput;
	}
}
