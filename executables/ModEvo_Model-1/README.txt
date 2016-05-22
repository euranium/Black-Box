Developer: Elizabeth Brooks 
Email: merimaat1@gmail.com 
Email: brookse@cwu.edu 
Modified: 05/05/2016

-----------------------------------------------------------------------------------------------------------
Program Instructions: 
The program may be run via the "main" method, in the "ModEvo" class.

Users will be prompted to input 23 arguments, the order in which they are entered does not matter.
Each argument has a specific string name that must be prepended to the variable value, delimited by an equal sign.
The casing of the string for the variable name does not matter.
Each argument has a specific data type, given in parentheses below.
An error message will be printed to the screen if an argument is entered in incorrectly, or an incorrect number of arguments is entered.

List of expected arguments:
1.  species (String) //Name of the organism under investigation
2.  meanTraitOne (double) //Mean of trait one
3.  meanTraitTwo (double) //Mean of trait two
4.  phenotypicVarianceTraitOne (double) //Phenotypic variance of trait one
5.  phenotypicVarianceTraitTwo (double) //Phenotypic variance of trait two
6.  heritability (double) //Heritability
7.  optimumTraitOne (double) //Optimum value of trait one
8.  optimumTraitTwo (double) //Optimum value of trait two
9.  varianceTraitOne (double) //Variance of the Gaussian function relating trait one to fitness
10. varianceTraitTwo (double) //Variance of the Gaussian function relating trait two to fitness
11. attenuationCoefficient (double) //The attenuation coefficient
12. meanInterceptReactionNorm (double) //The mean intercept of the reaction norm
13. meanSlopeReactionNorm (double) //The mean slope of the reaction norm
14. phenotypicVarianceInterceptReactionNorm (double) //The phenotypic variance of the slope of the reaction norm
15. phenotypicVarianceSlopeReactionNorm (double) //The phenotypic variance of the slope of the reaction norm
16. doseInitial (double) //The initial dose value
17. meanPreference (double) //The mean preference for UV penetration of the carapace
18. phenotypicVariancePreference (double) //The phenotypic variance of the mean preference
19. transmittance (double) //The transmittance of a non-melanized Daphnia
20. slopeConcentration (double) //The slope relating concentration of melanin to change in UVB transmittance
21. numIterations (int) //The number of iterations the user would like the program run
22. simPopSize (int) //The population size of the simulated populations
23. distributionName (String) //The nameof the distribution used to simulate populations of the given species

-----------------------------------------------------------------------------------------------------------	
Sample command line invocation:(1)
//Sample input arguments are initial testing values for the species Daphnia melanica, 
//with regards to the phenotypic raits melanin and DVM
//using the defulat gaussian distribution provided by the Random class

java ModEvo species=Daphnia meanTraitOne=2.5 meanTraitTwo=1.0 phenotypicVarianceTraitOne=0.04 phenotypicVarianceTraitTwo=0.2 heritability=0.5 optimumTraitOne=0.4 optimumTraitTwo=12.0 varianceTraitOne=100.0 varianceTraitTwo=500.0 attenuationCoefficient=0.26 meanInterceptReactionNorm=2.5 meanSlopeReactionNorm=0.0 phenotypicVarianceInterceptReactionNorm=0.2 phenotypicVarianceSlopeReactionNorm=0.2 doseInitial=27.9 meanPreference=10.0 phenotypicVariancePreference=0.5 transmittance=0.8 slopeConcentration=-0.16 numIterations=25 simPopSize=50 distributionName=defaultDistribution

-----------------------------------------------------------------------------------------------------------	
Sample command line invocation:(2)
//Sample input arguments are initial testing values for the species Daphnia melanica, 
//with regards to the phenotypic raits melanin and DVM
//using the developed gaussian distribution in the GaussianDistribution class

java ModEvo species=Daphnia meanTraitOne=2.5 meanTraitTwo=1.0 phenotypicVarianceTraitOne=0.04 phenotypicVarianceTraitTwo=0.2 heritability=0.5 optimumTraitOne=0.4 optimumTraitTwo=12.0 varianceTraitOne=100.0 varianceTraitTwo=500.0 attenuationCoefficient=0.26 meanInterceptReactionNorm=2.5 meanSlopeReactionNorm=0.0 phenotypicVarianceInterceptReactionNorm=0.2 phenotypicVarianceSlopeReactionNorm=0.2 doseInitial=27.9 meanPreference=10.0 phenotypicVariancePreference=0.5 transmittance=0.8 slopeConcentration=-0.16 numIterations=25 simPopSize=50 distributionName=gaussianDistribution

-----------------------------------------------------------------------------------------------------------	
Research Abstract: 
Quantitative genetics is the study of complex biological traits, or traits controlled by more than one gene. 
Traditional quantitative genetic models use the (co)variances of traits to predict evolution in response to selection. 
However, traits often result from nonlinear interactions between developmental factors. 
Such interactions can produce large and rapid changes to trait (co)variances. 
Because of this, traditional models may not accurately predict evolutionary dynamics. 
The goal of this project is to determine the extent to which the developmental architecture of traits affects the evolutionary response of a given species. 
As a first step, I have used an updated mathematical framework in conjunction with the Java programming language to develop a traditional model and several more advanced models. 
The models are also written in C++ to take advantage of the math libraries of the C++ programming language. 
These mathematical models will allow me to test hypotheses about how the developmental interactions among multiple traits affect their (co)variances and subsequent evolutionary trajectories.
