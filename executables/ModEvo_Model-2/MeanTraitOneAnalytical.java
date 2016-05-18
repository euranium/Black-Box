/*
	Name: Elizabeth Brooks
	File: MeanTraitOneAnalytical
	Modified: May 11, 2016
*/

//Imports
import java.util.Random;

//A class to calculate the mean value of trait one for model two
public class MeanTraitOneAnalytical{

	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
	private double meanTraitOne; //The mean trait one value
	private double meanTraitTwo; //The mean of trait two
	private double phenotypicVarianceTraitTwo; //The phenotypic vacriance of trait two
	private double dose; //The UVB dose at the waters surface
	private double meanSlopeReactionNorm; //The mean slope of the reaction norm of trait one with respect to the trait relater
	private double meanInterceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	private double transmittance; //The transmittance of a non-melanized Daphnia
	private double attenuationCoefficient; //The attenuation coefficient
	//Fields to store the values for calculation
	private double exponent;
	private double squareRootPart;
	private double numeratorPart;
	private double denominatorPart;
	
	//The class constructor to set the initial field values
	public MeanTraitOneAnalytical(SpeciesCharacteristics speciesInputs)
	{
        //Initialize species characteristics
        speciesValues = speciesInputs;
		//Set initial values
		meanTraitOne = speciesValues.getMeanTraitOne();
		meanTraitTwo = speciesValues.getMeanTraitTwo();
		phenotypicVarianceTraitTwo = speciesValues.getPhenotypicVarianceTraitTwo();
		meanInterceptReactionNorm = speciesValues.getMeanInterceptReactionNorm();
		meanSlopeReactionNorm = speciesValues.getMeanSlopeReactionNorm();
		dose = speciesValues.getDoseInitial();
		attenuationCoefficient = speciesValues.getAttenuationCoefficient();
	}
	
	//A method to calculate the exponent of base e
	public void calculateExponent()
	{
		double parentheses;
		parentheses = ((-2*meanTraitTwo)+(attenuationCoefficient*phenotypicVarianceTraitTwo));
		exponent = ((0.5*attenuationCoefficient)*parentheses);
	}
	
	//Method to calculate the individual value of trait one
	public void calculateTraitOne()
	{
		calculateExponent();
		meanTraitOne = (meanInterceptReactionNorm+((-1+Math.pow((Math.E), exponent))*meanSlopeReactionNorm*dose));
	}
	
	//A method to retrieve the mean value of trait one
	public double getMeanTraitOneAnalytical(double meanTraitTwoInput, double meanSlopeReactionNormInput, double meanInterceptReactionNormInput)
	{
		//Set initial values
		meanTraitTwo = meanTraitTwoInput;
		meanSlopeReactionNorm = meanSlopeReactionNormInput;
		meanInterceptReactionNorm = meanInterceptReactionNormInput;
		//Begin calculating the different portions of the equation
		calculateTraitOne();
		return meanTraitOne;
	}

	//Getter methods

	public double getMeanTraitTwoAnalytical() {
		return meanTraitTwo;
	}

	public double getPhenotypicVarianceTraitTwo() {
		return phenotypicVarianceTraitTwo;
	}
   
   public double getMeanTraitOneAnalytical() {
		return meanTraitOne;
	}
   
   public double getDose() {
		return dose;
	}
   
   public double getMeanInterceptReactionNorm() {
		return meanInterceptReactionNorm;
	}
   
   public double getMeanSlopeReactionNorm() {
		return meanSlopeReactionNorm;
	}
   
   public double getTransmittance() {
		return transmittance;
	}

	public double getAttenuationCoefficient(){
		return attenuationCoefficient;
	}

   //Setter methods
	public void setMeanTraitTwoAnalytical(double meanTraitTwoInput) {
		meanTraitTwo = meanTraitTwoInput;
	}

	public void setPhenotypicVarianceTraitTwo(double phenotypicVarianceTraitTwoInput) {
		phenotypicVarianceTraitTwo = phenotypicVarianceTraitTwoInput;
	}
   
   public void setMeanTraitOneAnalytical(double meanTraitOneInput) {
		meanTraitOne = meanTraitOneInput;
	}
   
   public void setDose(double doseInput) {
		dose = doseInput;
	}
   
   public void setMeanInterceptReactionNorm(double meanInterceptReactionNormInput) {
		meanInterceptReactionNorm = meanInterceptReactionNormInput;
	}
   
   public void setMeanSlopeReactionNorm(double meanSlopeReactionNormInput) {
		meanSlopeReactionNorm = meanSlopeReactionNormInput;
	}

   public void setTransmittance(double transmittanceInput) {
		transmittance = transmittanceInput;
	}

	public void setAttenuationCoefficient(double attenuationCoefficientInput) {
		attenuationCoefficient = attenuationCoefficientInput;
	}
}
