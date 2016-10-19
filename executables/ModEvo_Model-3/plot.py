import os
import subprocess

xmin = 9999
xmax = 0
ymin = 9999
ymax = 0

with open("meanTraitOneValues_ModelThree.txt") as f:
    next(f)
    for line in f:
        stuff = line.rstrip().split(" ")
        value = float(stuff[1])
        if value < xmin:
            xmin = value
        if value > xmax:
            xmax = value

with open("meanTraitTwoValues_ModelThree.txt") as f:
    next(f)
    for line in f:
        stuff = line.rstrip().split(" ")
        value = float(stuff[1])
        if value < ymin:
            ymin = value
        if value > ymax:
            ymax = value


if xmin > 0:
    xmin = xmin *.98
else:
    xmin = xmin * 1.02

if ymin > 0:
    ymin = ymin * .98
else:
    ymin = ymin * 1.02

xmax = xmax * 1.02
ymax = ymax * 1.02

file = open('plot.gn', 'w+')
file.write("set terminal jpeg\n")
file.write("set output \"contourPlot.png\"\n")
file.write("f(x,y)=((1.0/(abs(100.0 * 2 * 3.14))**0.5) * exp(-((x-0.4)**2)/(2*100.0))) + ((1.0/(abs(500.0 * 2 * 3.14))**0.5) * exp(-((y-12.0)**2)/(2*500)))\n")

#generate contour
file.write("set isosample 500, 500\n")
file.write("set table \"contourBase.dat\"\n")
file.write("splot [" + str(xmin) + ": " + str(xmax) + "] [" + str(ymin) + ": " + str(ymax) + "] f(x,y)\n")
file.write("unset table\n")

#create contour lines
file.write("unset surface\n")
file.write("set contour base\n")
file.write("set cntrparam level incremental 0.0,0.005,1.0\n")
file.write("set table 'contourLines.dat'\n")
file.write("splot [-20: 20] [-20: 20] f(x,y)\n")
file.write("unset table\n")

file.write("reset\n")
file.write("set xrange [" + str(xmin) + ": " + str(xmax) + "]\n")
file.write("set yrange [" + str(ymin) + ": " + str(ymax) + "]\n")
file.write("set xlabel 'Melanin'\n")
file.write("set ylabel 'DVM'\n")
file.write("set title 'fitness surface'\n")
file.write("unset key\n")
file.write("set palette rgbformulae 33,13,10\n")
file.write("plot 'contourBase.dat' with image, 'contourLines.dat' w l lt -1 lw 1, \"< paste meanTraitOneValues_ModelTwo.txt meanTraitTwoValues_ModelTwo.txt\" using 2:4 with lines lw 2 lt 4\n")
file.close();

p = subprocess.Popen("gnuplot plot.gn", shell = True)
os.waitpid(p.pid, 0)
