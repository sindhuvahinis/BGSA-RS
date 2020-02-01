package gsa;

import Rough.RoughSetTheory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 * @author sindhuvahinis
 * @date 03 March 2016
 *
 * Binary Gravitation Search Algorithm implementation.
 * This class, incorporates the BGSA implementation with rough set theory,
 * to give the featured results.
 */
class BinaryGSA {
    private Scanner in;
    private Random rand;


    public int giveMeBinary() {
        Random rg = new Random();
        return rg.nextInt(2);
    }

    public String binNumber(int n) {
        StringBuilder storage = new StringBuilder();
        int i = 0;

        while (i < n) {
            if (i != n - 1) {
                int binny = this.giveMeBinary();
                storage.append(binny);
                i++;
            } else {
                int binny = 0;
                storage.append(binny);
                i++;
            }
        }
        return storage.toString();
    }


    String[] getXPrev(int n) {
        BinaryGSA gsa = new BinaryGSA();
        rand = new Random();

        String[] xSend = new String[n];

        for (int i = 0; i < n; i++) {
            xSend[i] = (gsa.binNumber(n));

        }
        return xSend;
    }

    public double[] calculateFitness(int n, String[] xsend) throws FileNotFoundException {
        RoughSetTheory rf = new RoughSetTheory();
        double[] fit = new double[n];
        for (int i = 0; i < n; i++) {
            fit[i] = rf.cmon(xsend[i], 13, 25);
        }
        return fit;
    }

    private void printFitnessFunction(int n, double[] fit, int t) {
        System.out.println("At the iteration: " + t);
        System.out.println("postion\tvelocity");
        System.out.println(" the fitness function for iteration " + t);
        for (int i = 0; i < n; i++) {
            System.out.println(fit[i]);
        }
    }

    double calculateBest(double[] fit, int n) {
        double best = -1;
        for (int i = 0; i < n; i++) {
            if (fit[i] > best) {
                best = fit[i];
            }
        }
        System.out.println("The best: " + best);
        return best;
    }

    public double calculateWorst(double[] fit, int n) {
        double worst = 1000;
        for (int i = 0; i < n; i++) {
            if (fit[i] < worst) {
                worst = fit[i];
            }
        }
        System.out.println("The worst: " + worst);
        return worst;
    }

    public int calculateIndexWorst(double[] fit, int n) {
        int index = 0;
        double worst = 1000;
        for (int i = 0; i < n; i++) {
            if (fit[i] < worst) {
                worst = fit[i];
                index = i;
            }
        }
        System.out.println("The index of the Worst: " + index);
        return index;
    }

    public int calculateIndexBest(double[] fit, int n) {
        int index = 0;
        double best = -1;
        for (int i = 0; i < n; i++) {
            if (fit[i] > best) {
                best = fit[i];
                index = i;
            }
        }
        System.out.println("The index of the best: " + index);
        return index;
    }

    public double[] calculateM(double best, double worst, double[] fit, int n) {
		// mass
    	double[] mass = new double[n];
        for (int i = 0; i < n; i++) {
            mass[i] = (fit[i] - worst) / best - worst;
		}
        return mass;
    }

    public double calculateTotal(double[] m, int n) {
        double total = 0;
        for (int i = 0; i < n; i++) {
            total += m[i];
        }
        return total;
    }

    public long convertDecimal(long x) {
        int decimal = 0, power = 0;
        while (true) {
            if (x == 0)
                break;
            else {
                long temp = x % 10;
                decimal += temp * Math.pow(2, power);
                power++;
                x = x / 10;
            }
        }
        return decimal;
    }

    public double[][] calculateR(long[] xprev, int n) {
        double r[][] = new double[n][n];            // distance
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    long xi = convertDecimal(xprev[i]);
                    long xj = convertDecimal(xprev[j]);
                    r[i][j] = Math.abs(xi - xj);
                }
            }
        }
        return r;
    }

    public double[] calculateMass(int n, double[] m, double total) {
        double mass[] = new double[n];            // mass normalized
        for (int i = 0; i < n; i++) {
            mass[i] = m[i] / total;
        }
        return mass;
    }

    private double calculateHamming(String xi, String xj) {
        double result = 0;

        for (int i = 0; i < xi.length(); i++) {
            int xxi = Integer.parseInt(xi.substring(i, i + 1));
            int xxj = Integer.parseInt(xj.substring(i, i + 1));
            result += (xxj - xxi);
        }
        return result;
    }


    public double[][] calculateF(int n, double gt, double[] m, double[][] r, String[] xsend, double[][] f, int e) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    double distance = calculateHamming(xsend[i], xsend[j]);
                    f[i][j] = gt * ((m[i] * m[j]) / (r[i][j] + e)) * distance;
                }
            }
        }
        return f;
    }

    public double[] calculateForce(ArrayList<String> kbest, double[][] f, int n) {
        double[] force = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && kbest.contains(String.valueOf(i + 1)) && kbest.contains(String.valueOf(j + 1))) {
                    double random = (rand.nextDouble()) % 1000;
                    random = random / 1000;

                    force[i] = force[i] + (random * f[i][j]);
                }
            }
        }
        return force;
    }

    public double[] calculateAcc(double[] force, double[] mass, int n) {
        double a[] = new double[n];    // acceleration

        for (int i = 0; i < n; i++) {
            a[i] = force[i] / mass[i];
        }
        return a;
    }

    public double[] calculateVnext(double[] a, int n, double[] vprev) {
        double vnext[] = new double[n];

        for (int i = 0; i < n; i++) {
            double random = (rand.nextInt()) % 1000;
            random = random / 1000;
            vnext[i] = (random * vprev[i]) + a[i];
        }
        return vnext;
    }

    public double[] calculateVprev(double[] vnext, int n) {
        double vprev[] = new double[n];         // velocities
        for (int i = 0; i < n; i++) {
            vprev[i] = vnext[i];
        }
        return vprev;
    }

    public String[] calculateXnext(double[] vnext, String[] xsend, int n) {
        rand = new Random();
        double sv[] = new double[n];
        //	int xnext[]=new int[n];      // position
        //	String xsend[]=new String[n];

        for (int i = 0; i < n; i++) {
            double temp = (rand.nextInt()) % 1000;
            temp = temp / 1000;

            sv[i] = Math.tanh(vnext[i]);

            if (temp < sv[i]) {
                String binary = xsend[i];
                binary = binary.replaceAll("1", "X");
                binary = binary.replaceAll("0", "1");
                binary = binary.replaceAll("X", "0");

                String t = binary.substring(n - 1, n);
                //	if(t=="1")
                //	{
                binary = binary.substring(0, n - 1) + "0";
                //		}

                xsend[i] = binary;
            }
        }
        return xsend;
    }

    long[] updateXprev(long[] xnext, long[] xprev, int n) {
        for (int i = 0; i < n; i++) {
            xprev[i] = xnext[i];
        }
        return xprev;
    }

    public static void main(String args[]) throws FileNotFoundException {

//		String names[]={"Availability","Security","Response","Accessibility","Price","Speed","Storage_Space","Features","Ease_of_Use","Technical_Support","Customer_Service","Level_of_Expertise"};  

        BinaryGSA gsa = new BinaryGSA();

        int n = gsa.getNAgents();
        int T = n;

        //	int g0=gsa.getGO();
        int g0 = 100;

        //	int e=gsa.getE();
        int e = 2;

        ArrayList<String> kbest = gsa.initialKbest(n);

        String xsend[] = gsa.getXPrev(n);

        long xprev[] = new long[n];

        for (int i = 0; i < n; i++) {
            xprev[i] = Long.parseLong(xsend[i]);
        }

//		gsa.printKbest(kbest);

        double vprev[] = new double[n];
        double vnext[] = new double[n];
        double r[][] = new double[n][n];

        double best = 0;
        int indexbest = 0;

        double fit[] = new double[n];

        int temp = -1;
        vprev[0] = 0;
        vnext[0] = 0;
        r[0][0] = 0;

        for (int t = 0; temp < 80; t++)
        //	for(int t=0;t<10;t++)
        //	for(int t=0;best!=0.8;t++)
        {

            System.out.println("At the iteration: " + t);

            System.out.println("X Position");

            for (int i = 0; i < n; i++) {
                System.out.println(xsend[i]);
            }

            int index = 0;
            double gt = g0 * (Math.exp((-20) * t) / T);                       // calculate G(t)
            double f[][] = gsa.initialzeForceO(n);                       // initialize all force =0
            double force[] = gsa.initializeForce(n);

            fit = gsa.calculateFitness(n, xsend);

            //	gsa.printFitnessFunction(n,fit,t);

            best = gsa.calculateBest(fit, n);


            temp = (int) (best * 100);                                       // calculate best

            double worst = gsa.calculateWorst(fit, n);
            index = gsa.calculateIndexWorst(fit, n);                      // calculate the worst function
            indexbest = gsa.calculateIndexBest(fit, n);

            double m[] = gsa.calculateM(best, worst, fit, n);

            double total = gsa.calculateTotal(m, n);

            r = gsa.calculateR(xprev, n);

            double mass[] = gsa.calculateMass(n, m, total);

            f = gsa.calculateF(n, gt, m, r, xsend, f, e);

            force = gsa.calculateForce(kbest, f, n);

            double a[] = gsa.calculateAcc(force, mass, n);

            vnext = gsa.calculateVnext(a, n, vprev);

            vprev = gsa.calculateVprev(vnext, n);

            long xnext[] = new long[n];

            xsend = gsa.calculateXnext(vnext, xsend, n);

            for (int i = 0; i < n; i++) {
                xnext[i] = Long.parseLong(xsend[i]);
            }

            xprev = gsa.updateXprev(xnext, xprev, n);

            kbest.remove(String.valueOf(index + 1));

            //		gsa.printFitnessFunction(n,fit,t);

//			gsa.printKbest(kbest);
        }
		
	/*	int data[][] =new int[n][n];
		
		int result[]=new int[n];
		
		for(int i=0;i<n;i++)
		{
			if(fit[i] == best)
			{
				for(int j=0;j<n;j++)
				{
					
						data[i][j]=Integer.parseInt(xsend[i].substring(j,j+1));
					
				}
		
			}
		}
		
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				System.out.print(data[i][j]+" ");
			}
			
			System.out.print("\n");
		}
		
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				result[i]+=data[j][i];
			}
			
		}
		
		for(int i=0;i<n;i++)
		{
			System.out.print(result[i]+" ");
		}
		
		String nivi;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				
			}
		}
		*/
		
/*		String stemp=xsend[indexbest];	
		
		for(int i=0;i<(stemp.length());i++)
		{
			String tem=stemp.substring(i,i+1);
			if(tem.equals("1"))
			{
				System.out.println(names[i]);
			}
		}
		
		*/
    }

    public int getNAgents() {
        in = new Scanner(System.in);
        System.out.println("Enter the number of agents:");
        int n = in.nextInt();

        return n;
    }

    private int getGO() {
        in = new Scanner(System.in);
        System.out.println("Enter G0:");
        int g0 = in.nextInt();

        return g0;
    }

    private int getE() {
        in = new Scanner(System.in);
        System.out.println("Enter Epsilon(e):");
        int e = in.nextInt();

        return e;
    }

    ArrayList<String> initialKbest(int n) {
        ArrayList<String> kbest = new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            kbest.add(String.valueOf(i + 1));
        }

        return kbest;
    }

    private void printKbest(ArrayList<String> kbest) {
        System.out.println("Kbest");

        for (String value : kbest) {
            System.out.println(kbest);
        }
    }

    public double[][] initialzeForceO(int n) {
        double f[][] = new double[n][n];  // force
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                f[i][j] = 0;
            }
        }
        return f;
    }

    public double[] initializeForce(int n) {
        double force[] = new double[n];           // final force
        for (int i = 0; i < n; i++) {
            force[i] = 0;
        }
        return force;
    }
}