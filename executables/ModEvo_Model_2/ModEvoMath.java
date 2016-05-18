/*
	Name: Elizabeth Brooks
	File: ModEvoMath
	Modified: May 05, 2016
*/

//Class for modeling the evolutionary trajectories of a species with respect to multiple phenotypic traits
//while also accounting for underlying developmental interactions
public class ModEvoMath{

    //Class fields to store variables
	private ModelTwo modEvo; //Reference variable of the GeneralModel class
	private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class
   
	//Default class constructor
	public ModEvoMath()
	{

	}

	//The class constructor
	public ModEvoMath(SpeciesCharacteristics speciesInputs)
	{
		//Initialize species characteristics
        speciesValues = speciesInputs;
		
		//Write species data to a txt file
        speciesValues.writeSpeciesFile();
      		
		//Run the general model
        modEvo = new ModelTwo(speciesValues);
	modEvo.runModel();                                             
	}	
}
