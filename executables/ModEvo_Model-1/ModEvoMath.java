/*
	Name: Elizabeth Brooks
	File: ModEvoMath
	Modified: May 05, 2016
*/

//Class for modeling the evolutionary trajectories of a species with respect to multiple phenotypic traits
//while also accounting for underlying developmental interactions
public class ModEvoMath {

    //Class fields to store variables
	private ModelOne modEvo; //Reference variable of the ModelOne class
	private SpeciesCharacteristics speciesValues; //Reference variable of the SpeciesCharacteristics class

	//The class constructor
	public ModEvoMath(SpeciesCharacteristics speciesInputs)
	{
		//Initialize species characteristics
        speciesValues = speciesInputs;
		
		//Write species data to a txt file
        speciesValues.writeSpeciesFile();
      		
		//Initialize model one reference variable
        modEvo = new ModelOne(speciesInputs);

        //Run the model
		modEvo.runModel();                                          
	}	
}