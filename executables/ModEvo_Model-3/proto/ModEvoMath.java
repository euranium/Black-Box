/*
	Name: Elizabeth Brooks
	File: ModEvoMath
	Modified: October 11, 2016
*/

//Class for modeling the evolutionary trajectories of a species with respect to multiple phenotypic traits
//while also accounting for underlying developmental interactions
public class ModEvoMath{

    //Class fields to store variables
	private ModelThree modEvo; //Reference variable of the ModelThree class
	private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
   
	//The class constructor
	public ModEvoMath(SpeciesCharacteristics speciesInputs)
	{
		//Initialize species characteristics
        speciesValues = speciesInputs;
		//Write species data to a txt file
        speciesValues.writeSpeciesFile(); 
        //Send input values to ModEvo
        modEvo = new ModelThree(speciesValues); 
	}

	//Method to run ModEvo and verify successful program completion
	public int runModEvo()
	{
        //Run the model
		modEvo.runModel();
		//Return 1 for successful program completion
		return 1;
	}	
}