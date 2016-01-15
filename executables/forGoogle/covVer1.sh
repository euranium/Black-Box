#echo "Type in value A"
A=$1

#echo "Type in value K"
K=$2

#echo "Type in value B"
B=$3

#echo "Type in value v"
v=$4

#echo "Type in value Q"
Q=$5

#echo "Type in value C"
C=$6

#echo "Type in value M"
M=$7

#echo "Type in start value of the x axis "
X=$8

#echo "Type in end value of x axis"
E=$9

#echo "rate of taking samples "
R=$10

javac covVer1.java
java covVer1 $A $K $B $v $Q $C $M $X $E $R > covVer1Data.txt
echo "set terminal png" > covVer1Gnuplot.gp
echo "set output 'covVer1Plot.png'">> covVer1Gnuplot.gp
echo "plot 'covVer1Data.txt' using 1:2 with lines" >> covVer1Gnuplot.gp
gnuplot covVer1Gnuplot.gp
