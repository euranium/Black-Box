/*
	Name: Elizabeth Brooks
	File: MeanTraitOne
	Modified: May 11, 2016
*/

//Imports
import java.util.Random;

//A class to calculate the mean value of trait one for model two
public class MeanTraitOne {

	//Class fields used to calculate the mean value of trait one
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
    private MeanTraitOneAnalytical analyticalMeanObject; //Reference variable of the MeanTraitOneAnalytical class
    private MeanTraitOneSimulation simulationMeanObject; //Reference variable of the MeanTraitOneSimulation class
	private String meanSelection; //The user selection of mean trait one analyzation method
	private double meanTraitOne; //The mean of trait one
	private double meanTraitTwo; //The mean of trait two
	private double meanSlopeReactionNorm; //The mean slope of the reaction norm of trait one with respect to the trait relater
	private double meanInterceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	
	//The class constructor to set the initial field values
	public MeanTraitOne(SpeciesCharacteristics speciesInputs)
	{
        //Initialize reference variables
        speciesValues = speciesInputs;
        analyticalMeanObject = new MeanTraitOneAnalytical(speciesValues);
        simulationMeanObject = new MeanTraitOneSimulation(speciesValues);
		//Set initial values
		meanSelection = speciesValues.getMeanSelection();
		meanTraitOne = speciesValues.getMeanTraitOne();
		meanTraitTwo = speciesValues.getMeanTraitTwo();
		meanInterceptReactionNorm = speciesValues.getMeanInterceptReactionNorm();
		meanSlopeReactionNorm = speciesValues.getMeanSlopeReactionNorm();
	}
	
	//A method to retrieve the mean value of trait one
	public double getMeanTraitOne(double meanTraitTwoInput, double meanSlopeReactionNormInput, 
									double meanInterceptReactionNormInput, String meanSelectionInput)
	{
		//Set initial values
		meanTraitTwo = meanTraitTwoInput;
		meanSlopeReactionNorm = meanSlopeReactionNormInput;
		meanInterceptReactionNorm = meanInterceptReactionNormInput;
		meanSelection = meanSelectionInput;
		//Determine mean trait one using the user selected method
		if(meanSelection.equals("analytical")){
			meanTraitOne = analyticalMeanObject.getMeanTraitOneAnalytical(meanTraitTwo, meanSlopeReactionNorm, meanInterceptReactionNorm);
		}else if(meanSelection.equals("simulation")){
			meanTraitOne = simulationMeanObject.getMeanTraitOneSimulation(meanTraitTwo, meanSlopeReactionNorm, meanInterceptReactionNorm);
		}else{ //Error message
			System.out.println("Incorrect distribution name entered, program exited.");
			System.exit(0);
		}
		//Return the calculated mean trait one
		return meanTraitOne;
	}

	//Getter methods
	public double getMeanTraitOne() {
		return meanTraitOne;
	}
   
   public double getMeanTraitTwo() {
		return meanTraitTwo;
	}

   public double getMeanInterceptReactionNorm() {
		return meanInterceptReactionNorm;
	}
   
   public double getMeanSlopeReactionNorm() {
		return meanSlopeReactionNorm;
	}

	public String getMeanSelectionTraitOne() {
		return meanSelection;
	}
   
   //Setter methods
	public void setMeanTraitOne(double meanTraitOneInput) {
		meanTraitOne = meanTraitOneInput;
	}

	public void setMeanTraitTwo(double meanTraitTwoInput) {
		meanTraitTwo = meanTraitTwoInput;
	}
   
   public void setMeanInterceptReactionNorm(double meanInterceptReactionNormInput) {
		meanInterceptReactionNorm = meanInterceptReactionNormInput;
	}
   
   public void setMeanSlopeReactionNorm(double meanSlopeReactionNormInput) {
		meanSlopeReactionNorm = meanSlopeReactionNormInput;
	}

	public void setMeanSelectionTraitOne(String meanSelectionInput) {
		meanSelection = meanSelectionInput;
	}
}
