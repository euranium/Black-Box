/*
	Name: Elizabeth Brooks
	File: ModelOne
	Modified: May 05, 2016
*/
	
//Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//Class for modeling the evolutionary trajectories of a species with respect to multiple phenotypic traits
public class ModelOne {

	//Class fields to store variables
	private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics
	private IndividualFitness individualFitnessObject; //A reference variable of the IndividualFitness class
	private MeanFitness meanFitnessObject; //A reference variable of the MeanFitnessClass
	private double meanFitness; //Mean fitness
	private double phenotypicVarianceTraitOne; //Phenotypic variance of trait one
	private double phenotypicVarianceTraitTwo; //Phenotypic variance of trait two
	private double heritability; //Heritability
	private int numIterations; //The number of generations to be calculated
	private int simPopSize; //The number of generations to be simulated for calc of mean fitness
	//Class fields to store the values of the calculated portions of the equation to find the mean melanin and dvm for each generation
	private double nextGenMeanTraitOne;
	private double nextGenMeanTraitTwo;
	//Arrays to hold each generations calculated mean trait value
	private double[] fitnessArray;
	private double[] traitOneArray;
	private double[] traitTwoArray;
	
	//The class constructor which will set the field's values
	public ModelOne(SpeciesCharacteristics speciesInputs)
	{
		//Initialize species characteristics
        speciesValues = speciesInputs;
        //Set initial values
		numIterations = speciesValues.getNumIterations();
		simPopSize = speciesValues.getSimPopSize();
		heritability = speciesValues.getHeritability();
		phenotypicVarianceTraitOne = speciesValues.getPhenotypicVarianceTraitOne();
		phenotypicVarianceTraitTwo = speciesValues.getPhenotypicVarianceTraitTwo();
		nextGenMeanTraitOne = speciesValues.getMeanTraitOne();
		nextGenMeanTraitTwo = speciesValues.getMeanTraitTwo();
	}
	
	//The methods to run the model
	public void runModel()
	{
		//Initialize objects
		individualFitnessObject = new IndividualFitness(speciesValues);
		meanFitnessObject = new MeanFitness(speciesValues, nextGenMeanTraitOne, nextGenMeanTraitTwo);
		//Begin the model looping
		setTraitArrays();
	}
	
	//Add each generations mean trait values into the trait arrays
	public void setTraitArrays()
	{
		fitnessArray = new double[numIterations];
		traitOneArray = new double[numIterations];
		traitTwoArray = new double[numIterations];
		//A for loop to save each value calculated for the mean of trait one and two
		for(int i=0;i<traitOneArray.length;i++){
			fitnessArray[i] = meanFitness;
			traitOneArray[i] = nextGenMeanTraitOne;
			traitTwoArray[i] = nextGenMeanTraitTwo;
			calcNextGenFitness();
			calcNextGenMeanTraitOne();
			calcNextGenMeanTraitTwo();
		}
      	//Write to CSV file trait values
		writeFitnessFile();
     	writeTraitTwoFile();
      	writeTraitOneFile();
	}

	//Send the new mean values of each trait to the IndividualFitness class for re calculation
	public void calcNextGenFitness()
	{
		meanFitness = meanFitnessObject.getMeanFitnessSim(nextGenMeanTraitOne, nextGenMeanTraitTwo);
	}
	
	//Determine the mean value of trait one for each generation
	public void calcNextGenMeanTraitOne()
	{
		calcNextGenFitness();
		//Numerically differentiate
		//Calculate the second portion of the equation
		double fitnessInverse = (1/meanFitness);
		double secondPortion = ((Math.pow(heritability,2)) * (fitnessInverse) * 
				individualFitnessObject.numericallyCalcTraitOnePartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo) *
				phenotypicVarianceTraitOne);
		nextGenMeanTraitOne += secondPortion;
	}
	
	//Determine the mean value of trait two for each generation
	public void calcNextGenMeanTraitTwo()
	{
		calcNextGenFitness();
		//Numerically differentiate
		//Calculate the second portion of the equation
		double fitnessInverse = (1/meanFitness);
		double secondPortion = ((Math.pow(heritability,2)) * (fitnessInverse) * 
				individualFitnessObject.numericallyCalcTraitTwoPartialDerivative(nextGenMeanTraitOne, nextGenMeanTraitTwo) *
				phenotypicVarianceTraitTwo);
		nextGenMeanTraitTwo += secondPortion;
	}
   
	//Method to write trait one values to a TXT file
    public void writeFitnessFile()
    {
   	//Write to file the first traits values
		//Catch exceptions and write to file in TXT format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanTraitTwoPath = "meanFitnessValues_ModelOne.txt";
         File meanTraitTwoFile = new File(meanTraitTwoPath);         
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanTraitTwoFile.getAbsoluteFile()); 
			meanTraitTwoFile.createNewFile();
            //Write to file the header
			fwTwo.write("Generation MeanFitness\n");
			String aOne;
			String bOne;			
			for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
				//Write to file generationNumber, traitOne
			   	aOne = Integer.toString(k);
			   	fwTwo.append(aOne);
			   	fwTwo.append(" ");
			   	bOne = Double.toString(fitnessArray[i]);
			   	fwTwo.append(bOne);
			   	fwTwo.append("\n");
			}			
			//Close the file
			fwTwo.close();
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }

   //Method to write trait one values to a CSV file
   public void writeTraitOneFile()
   {
   	//Write to file the first traits values
		//Catch exceptions and write to file in CSV format
		try {
         //Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanTraitOnePath = "meanTraitOneValues_ModelOne" + ".txt";
			File meanTraitOneFile = new File(meanTraitOnePath);        
			if (meanTraitOneFile.exists()){
            //Loop through the existing files
			   while(meanTraitOneFile.exists()){
				   //fileCount++;
               meanTraitOnePath = "meanTraitOneValues_ModelOne" + ".txt";
               meanTraitOneFile = new File(meanTraitOnePath);
			   }
            //Create meanTraitOneFile and file writer
            meanTraitOneFile.createNewFile();
            FileWriter fwOne = new FileWriter(meanTraitOneFile.getAbsoluteFile()); 
            //Write to file the header
			   fwOne.write("Generation MeanTraitOne\n");
			   String a;
			   String b;			
			   //Write to file generationNumber, traitOne
			   for(int i=0, k=1;i<traitOneArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	a = Integer.toString(k);
			   	fwOne.append(a);
			   	fwOne.append(" ");
			   	b = Double.toString(traitOneArray[i]);
			   	fwOne.append(b);
			   	fwOne.append("\n");
			   }			
			   //Close the file
			   fwOne.close();
         }else if(!meanTraitOneFile.exists()){
            //Create meanTraitOneFile and file writer
            meanTraitOneFile.createNewFile();
            FileWriter fwOne = new FileWriter(meanTraitOneFile.getAbsoluteFile()); 
            //Write to file the header
			   fwOne.write("Generation MeanTraitOne\n");
			   String a;
			   String b;			
			   //Write to file generationNumber, traitOne
			   for(int i=0, k=1;i<traitOneArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	a = Integer.toString(k);
			   	fwOne.append(a);
			   	fwOne.append(" ");
			   	b = Double.toString(traitOneArray[i]);
			   	fwOne.append(b);
			   	fwOne.append("\n");
			   }			
			   //Close the file
			   fwOne.close();
			}else{
            //Display error message
            System.out.println("Error in file naming, ModelOne");
            System.exit(0);
         }			
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	}	
      
   //Method to write trait one values to a CSV file
   public void writeTraitTwoFile()
   {
		//Write to file the second traits values
		//Catch exceptions and write to file in CSV format
		try {
		//Determine which test number is being run for file naming
         //int fileCount = 1;
         String meanTraitTwoPath = "meanTraitTwoValues_ModelOne" + ".txt";
         File meanTraitTwoFile = new File(meanTraitTwoPath);         
			if (meanTraitTwoFile.exists()){
            //Loop through the existing files
			   while(meanTraitTwoFile.exists()){
			   	//fileCount++;
              meanTraitTwoPath = "meanTraitTwoValues_ModelOne" + ".txt";
              meanTraitTwoFile = new File(meanTraitTwoPath);
			   }
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanTraitTwoFile.getAbsoluteFile()); 
				meanTraitTwoFile.createNewFile();
            //Write to file the header
			   fwTwo.write("Generation MeanTraitTwo\n");
			   String a;
			   String b;			
			   for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	a = Integer.toString(k);
			   	fwTwo.append(a);
			   	fwTwo.append(" ");
			   	b = Double.toString(traitTwoArray[i]);
			   	fwTwo.append(b);
			   	fwTwo.append("\n");
			   }			
			   //Close the file
			   fwTwo.close();
         }else if(!meanTraitTwoFile.exists()){
            //Create meanTraitOneFile and file writer
            FileWriter fwTwo = new FileWriter(meanTraitTwoFile.getAbsoluteFile()); 
				meanTraitTwoFile.createNewFile();
            //Write to file the header
			   fwTwo.write("Generation MeanTraitTwo\n");
			   String a;
			   String b;			
			   for(int i=0, k=1;i<traitTwoArray.length;i++, k++){
			   	//Write to file generationNumber, traitOne
			   	a = Integer.toString(k);
			   	fwTwo.append(a);
			   	fwTwo.append(" ");
			   	b = Double.toString(traitTwoArray[i]);
			   	fwTwo.append(b);
			   	fwTwo.append("\n");
			   }			
			   //Close the file
			   fwTwo.close();		
			}else{
            //Display error message
            System.out.println("Error in file naming, ModelOne");
            System.exit(0);
         }	
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
   }
	
	//Getter methods 
   public int getSimPopSizeInitial() 
   {
		return simPopSize;
	}
	
	public int getNumIterationsInitial() 
   {
		return numIterations;
	}	
	
   public double getMeanTraitOneCurrent()
	{
		return nextGenMeanTraitOne;
	}
	
	public double getMeanTraitTwoCurrent()
	{
		return nextGenMeanTraitTwo;
	}

	//Setter methods 
   public void setSimPopSizeInitial(int simPopSizeInput) 
   {
		simPopSize = simPopSizeInput;
	}
	
	public void setNumIterationsInitial(int numIterationsInput) 
   {
		numIterations = numIterationsInput;
	}	
	
	public void setMeanTraitOneCurrent(double nextGenMeanTraitOneInput)
	{
		nextGenMeanTraitOne = nextGenMeanTraitOneInput;
	}
	
	public void setMeanTraitTwoCurrent(double nextGenMeanTraitTwoInput)
	{
		nextGenMeanTraitTwo = nextGenMeanTraitTwoInput;
	}
}
