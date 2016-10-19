/*
	Name: Elizabeth Brooks
	File: MeanTraitOneSimulation
	Modified: May 11, 2016
*/

//Imports
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//A class to calculate the mean fitness of a species through simulation
public class MeanTraitOneSimulation extends IndividualTraitOne{
	
	//Class fields used to calculate the mean value of trait one
	private Random randomSimulation; //For simulation of individual variable values
    private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
   	//private GaussianDistribution gaussianDistObject; //Reference variable of the GaussianDistribution class
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulation for calc of mean fitness
	private String distributionSelection; //The distribution selected by the user
	//private int fileNum; //Current test file number
	//Variables for determining mean trait one through simulation
	private double individualTraitOne; //The individual trait one value
	private double meanTraitOne; //The mean trait one value
	private double meanTraitTwo; //The mean of trait two
	private double meanSlopeReactionNorm; //The mean slope of the reaction norm of trait one with respect to the trait relater
	private double meanInterceptReactionNorm; //The mean intercept of the reaction norm of trait one with respect to the trait relater
	private double phenotypicVarianceTraitOne; //The phenotypic variance of trait one
	private double standardDevianceTraitOne; //The standard deviation of trait one
	private double[] traitOneValuesArray; //Array to store the calulated individual trait one values

	//The class constructor
	public MeanTraitOneSimulation(SpeciesCharacteristics speciesInputs)
	{
      	//Call IndividualFitness (super) constructor
      	super(speciesInputs);
		//Initialize class fields
		speciesValues = speciesInputs;
      	//fileNum = 1;
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		distributionSelection = speciesValues.getDistributionSelection();
		meanTraitOne = speciesValues.getMeanTraitOne();
		meanTraitTwo = speciesValues.getMeanTraitTwo();
		phenotypicVarianceTraitOne = speciesValues.getPhenotypicVarianceTraitOne();
		calculateStandardDeviationTraitOne();
	}
	
	//A method to calculate the standard deviation of trait one
	public void calculateStandardDeviationTraitOne()
	{
		standardDevianceTraitOne = Math.sqrt(Math.abs(phenotypicVarianceTraitOne));
	}
	
	//A method to simulate as many populations for trait one as specified
	public void calcSimulationTraitOneValues()
	{
		traitOneValuesArray = new double[simPopSize];
      	double traitOne;
      	if(distributionSelection.equals("default")){
			randomSimulation = new Random();
			//A for loop to fill the array with a random number for trait one
			//through the simulation of normally distributed populations
			for(int i=0; i<traitOneValuesArray.length; i++){
				//Send the appropriate values to the IndividualTraitOne class to recalculate a new mean for trait one
				individualTraitOne = super.getIndividualTraitOne(meanTraitTwo, meanSlopeReactionNorm, meanInterceptReactionNorm);
				traitOne = (randomSimulation.nextGaussian() * standardDevianceTraitOne + individualTraitOne);
	         //Make sure that the trait values do not fall below zero
	         if(traitOne < 0){
	           	traitOne = 0;
	         }
	         traitOneValuesArray[i] = traitOne;
			}
		}else if(distributionSelection.equals("gaussian")){
			//gaussianDistObject.getGaussianDistribution();
			System.out.println("Distribution not yet ready for use, program exited.");
			System.exit(0);
		}else{ //Error message
			System.out.println("Incorrect distribution name entered, program exited.");
			System.exit(0);
		}
	}
	
	//Methods to return mean values
	public double getMeanTraitOneSimulation(double meanTraitTwoInput, double meanSlopeReactionNormInput, double meanInterceptReactionNormInput)
	{
		//Initialize fields with inputs
		meanTraitTwo = meanTraitTwoInput;
		meanSlopeReactionNorm = meanSlopeReactionNormInput;
		meanInterceptReactionNorm= meanInterceptReactionNormInput;
		//Generate a random number that falls within the number of model iterations entered
		//Random randomNum = new Random();
		//int randomFile = randomNum.nextInt((numIterations - 2) + 1) + 2;
		//Depending on user selection for trait distribution, determine the current mean fitness of the population
		//Default distribution is the provided .nextGaussian function
      //Create a file for the first and a random population
      /*if(fileNum == 1){
   		writeTestFileOne();
      }else if(fileNum == randomFile){
         writeTestFileOne();
      }*/
      //Calculate simulation poluations of individuals
      calcSimulationTraitOneValues();
		//A for loop to calculate the mean fitness
		for(int i=0; i<traitOneValuesArray.length; i++){
			meanTraitOne += traitOneValuesArray[i];
		}
		meanTraitOne /= simPopSize;
      //Incriment the file number
      //fileNum++;
      return meanTraitOne;
	}

	//Method for writing test results to a TXT file
    /*public void writeTestFileOne(){
		//Catch exceptions and write to file in TXT format
		try {
			//Write to the file simulationPopTraitOne TXT file
         //Determine which test number is being run for file naming
         int fileCount = 1;
         String simulationTraitOnePath = "traitOneSimulationPop_" + fileNum + "_ModelTwo.txt";
         File simulationTraitOneFile = new File(simulationTraitOnePath);
			/*if (simulationTraitOneFile.exists()){
            //Loop through the existing files
			   while(simulationTraitOneFile.exists()){
				   fileCount++;
               simulationTraitOnePath = "traitOneSimulationPop_" + fileNum + "_GeneralModel.txt";
               simulationTraitOneFile = new File(simulationTraitOnePath);
			   }*/
            //Create simulationTraitOneFile and file writer
            /*simulationTraitOneFile.createNewFile();
            FileWriter fwOne = new FileWriter(simulationTraitOneFile.getAbsoluteFile()); 
            //Write to file the header
			   fwOne.write("IndividualNumber IndividualTraitOne\n");
			   String a;
			   String b;
				//A for loop to write the first individual trait values
			   for(int i=0, k=1; i<traitOneValuesArray.length; i++, k++){
			   	a = Integer.toString(k);
			   	fwOne.append(a);
			   	fwOne.append(" ");
			   	b = Double.toString(traitOneValuesArray[i]);
			   	fwOne.append(b);
			   	fwOne.append("\n");
			   }
	    		//Close the file
			   fwOne.close();
        /*}else if(!simulationTraitOneFile.exists()){
            //Create simulationTraitOneFile and file writer
            simulationTraitOneFile.createNewFile();
            FileWriter fwOne = new FileWriter(simulationTraitOneFile.getAbsoluteFile()); 
            //Write to file the header
			   fwOne.write("IndividualNumber,IndividualTraitOne\n");
			   String a;
			   String b;
				//A for loop to write the first individual trait values
			   for(int i=0, k=1; i<traitOneValuesArray.length; i++, k++){
			   	a = Integer.toString(k);
			   	fwOne.append(a);
			   	fwOne.append(" ");
			   	b = Double.toString(traitOneValuesArray[i]);
			   	fwOne.append(b);
			   	fwOne.append("\n");
			   }
	    		//Close the file
			   fwOne.close();
			}else{
            //Display error message
            System.out.println("Error in file naming, MeanFitnessBySimulation");
            System.exit(0);
         }*/			
		/*} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}   
    }*/
	
	//Getter methods
	public double getMeanTraitOneSimulation() {
		return meanTraitOne;
	}

   public double getMeanTraitTwoSimulation() {
		return meanTraitTwo;
	}
   
   public double getphenotypicVarianceTraitOneSimulation() {
		return phenotypicVarianceTraitOne;
	}
   
   public double getStandardDevianceTraitOneSimulation() {
		return standardDevianceTraitOne;
	}
   
   public double getIndividualTraitOneSimulation() {
		return individualTraitOne;
	}
   
	public int getSimPopSizeSimulation() {
		return simPopSize;
	}
	
	public int getNumIterationsSimulation() {
		return numIterations;
	}
   
   /*public int getFileNumSim() {
		return fileNum;
	}*/
   
   //Setter methods
	public void setMeanTraitOneSimulation(double meanTraitOneInput) {
		meanTraitOne = meanTraitOneInput;
	}

   public void setMeanTraitTwoSimulation(double meanTraitTwoInput) {
		meanTraitTwo = meanTraitTwoInput;
	}
   
   public void setphenotypicVarianceTraitOneSimulation(double phenotypicVarianceTraitOneInput) {
		phenotypicVarianceTraitOne = phenotypicVarianceTraitOneInput;
	}
   
   public void setStandardDevianceTraitOneSimulation(double standardDevianceTraitOneInput) {
		standardDevianceTraitOne = standardDevianceTraitOneInput;
	}
   
   public void setIndividualTraitOneSimulation(double individualTraitOneInput) {
		individualTraitOne = individualTraitOneInput;
	}
   
	public void setSimPopSizeSimulation(int simPopSizeInput) {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterationsSimulation(int numIterationsInput) {
		numIterations = numIterationsInput;
	}
   
   /*public void setFileNumSimulation(int fileNumInput) {
		fileNum = fileNumInput;
	}*/
}