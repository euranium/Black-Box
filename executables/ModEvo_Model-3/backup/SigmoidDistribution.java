/*
	Name: Elizabeth Brooks
	File: SigmoidDistribution
	Modified: August 4, 2016
*/

//Imports
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Class to form a distribution using the sigmoid function f(x,max,slope,shift) = max/(1+e^(-(x-shift)*slope))
public class SigmoidDistribution {

	//Class fields
	private double x; //Sigmoid function input x
	private double max; //Sigmoid function input max
	private double slope; //Sigmoid function input slope
	private double shift; //Sigmoid function input shift
	private double functionCurrentValue; //Current value of the function using input parameters

	//Class constructor
	public SigmoidDistribution() {

	}

	//Method to generate and store a population of individuals that fall under a sigmoid distribution
	public getSigmoidDistribution() {
		//Calculate the given sigmoid distribution
		double innerParenOne = (x-shift);
		double innerParenTwo = -(innerParenOne*slope);
		double outterParen = (1+Math.e^innerParenTwo);
		functionCurrentValue = max/outterParen;

	}

	//Method to retrieve an individual from the generated sigmoid distribution
	public getRandomIndividual() {
		//Generate a random number using secure random
		//The following will create SUN SHA1PRNG, if the highest priority CSP is SUN
		SecureRandom secureRandomObject = SecureRandom.getInstance("SHA1PRNG");
		double randomNum = secureRandomObject.nextDouble();
		//Check if the random number falls under the specified sigmoid distribution

	}
}
