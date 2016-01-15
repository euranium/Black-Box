import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
public class covVer1 extends JPanel {
    static double[] data = new double[11];
    static void filda (double i, double da){
	if(i == 0){
	    //System.out.println("x y");
	}
	System.out.println(i + " " + da);
    }
    static void doMath(double A, double K, double B, double v, double Q, double C, double M, double X , double E, double R){
	double e = 2.71828;
	double start = X;
	double end = E;
	double i = X;
	double cal = 0.0;
	double ka = K - A;
	double vv = 1/v;
      
	for(double starts = start; starts < end; starts = starts + R){
	    double mm = starts - M;
	    double bs = -1 * B * mm;
	    double es = Math.pow( Math.E, bs);
	    double qe = Q * es;
	    double cqe = C + qe;
	    double cqee = Math.pow (cqe, vv);
      
	    cal = A + (ka / cqee) ;
	    filda(i, cal);
	    i= i + R;
	}
    } 

 
    public static void main(String[] args) {
	// String s = null 
        double A = Double.parseDouble(args[0].trim());
        double K= Double.parseDouble(args[1].trim());
        double B= Double.parseDouble(args[2].trim());
        double v= Double.parseDouble(args[3].trim());
        double Q= Double.parseDouble(args[4].trim());
        double C= Double.parseDouble(args[5].trim());
        double M = Double.parseDouble(args[6].trim());
        double X= Double.parseDouble(args[7].trim());
        double E= Double.parseDouble(args[8].trim());
        double R = Double.parseDouble(args[9].trim());
        doMath(A, K, B, v, Q, C, M, X , E, R);
    }
}
