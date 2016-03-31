set terminal png
set output 'covVer1Plot.png'
plot 'covVer1Data.txt' using 1:2 with lines
