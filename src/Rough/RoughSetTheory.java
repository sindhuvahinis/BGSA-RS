package Rough;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class provides the implementation of RoughSet Theory.
 *
 * @author sindhuvahinis
 * @date 03 March 2016
 */
public class RoughSetTheory {

	double[][] getData(int nAttributes, int nSamples) throws FileNotFoundException {
        double data[][] = new double[nAttributes][nSamples];
        int i = 0;
        Scanner sc = new Scanner(new File("ids.txt"));
        // 	System.out.println("Fetching Data...");

        while (sc.hasNextLine() && i < nAttributes) {
            int j = 0;
            while (sc.hasNextInt() && j < nSamples) {
                data[i][j] = sc.nextInt();
                j++;
            }
            i++;
        }
        return data;
    }


    String[][] findClusters(int nAttributes, int nSamples, double data[][]) {
        String clusters[][] = new String[nAttributes][nSamples];

        for (int i = 0; i < nAttributes; i++) {
            String checker = " ";
            int clusterinc = 0;

            for (int j = 0; j < nSamples && clusterinc < nSamples; j++) {
                if (!(checker.contains(String.valueOf(data[i][j])))) {
                    checker = checker + String.valueOf(data[i][j]);
                    clusters[i][clusterinc] = String.valueOf(j + 1);
                } else {
                    continue;
                }

                for (int k = 0; k < nSamples; k++) {
                    if (j != k && data[i][j] == data[i][k]) {
                        clusters[i][clusterinc] = clusters[i][clusterinc] + "-" + String.valueOf(k + 1);
                    }
                }

                clusterinc++;
            }

        }

        return clusters;
    }


    double[][] calculateClusters(int nAttributes, int nSamples, String clusters[][]) {

//		System.out.println("Calculate Clusters");
        double resultValue[][] = new double[nAttributes][nAttributes];

        resultValue[0][0] = 0;

        int m = 0, count;

        for (int l = 0; l < (nAttributes - 1); l++) {
            for (int k = 0; k < nAttributes; k++) {
                for (int j = 0; j < nSamples; j++) {
                    for (int i = 0; i < nSamples; i++) {
                        if ((clusters[l][j] != null) && (clusters[k][i] != null)) {
                            count = 0;
							
			/*				for(int z=0;(z<clusters[l][j].length());z++)
							{
								String temp=(clusters[l][j]).substring(z,z+1);
								if(clusters[k][i].contains(temp))
										count++;
							} */

                            int n = clusters[l][j].length(), z = 0, flag = 0;
                            while (z < n) {
                                String temp = "";
                                while (z < n) {
                                    if (!(clusters[l][j].substring(z, z + 1).equals("-"))) {
                                        temp = temp + clusters[l][j].substring(z, z + 1);
                                        z++;
                                    } else {
                                        break;
                                    }
                                }
                                //	System.out.println(temp);
                                if (!(clusters[k][i].contains(temp)))
                                    flag = 1;
                                count++;
                                z++;
                            }

                            //		System.out.println("The numbers: "+count);


                            if (flag == 0) {
                                resultValue[l][m] = resultValue[l][m] + count;
                            }
                        }
                    }
                }

                resultValue[l][m] = resultValue[l][m] / nSamples;
                m = (m + 1) % nAttributes;
            }
        }
        return resultValue;
    }


    double[] calculateTotal(int nAttributes, double[][] resultValue) {
        double total[] = new double[nAttributes];

        for (int i = 0; i < nAttributes; i++) {
            for (int j = 0; j < nAttributes; j++) {
                total[i] = total[i] + resultValue[i][j];
            }
            total[i] = total[i] / nAttributes;

        }
        return total; // total - average
    }

    double calculateMax(int nAttributes, double[] total) {
        double maximum = -1;

        for (int i = 0; i < nAttributes; i++) {
            if (maximum < total[i]) {
                maximum = total[i];
            }
        }

        return maximum;
    }

    String getFromGsa() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the input Strings");
        String input = in.next();

        return input;
    }

    String[] findInputCluster(String input, String[][] clusters, int nAttributes, int nSamples) {
        String inputcluster[] = new String[(nAttributes - 1) * nSamples];
        int length = input.length(), index = 0;
        String tinput = "";
        for (int i = 0; i < length; i++) {
            String temp = input.substring(i, i + 1);
            if (temp.equals("1")) {
                tinput += String.valueOf(i);
            }
        }
        //	System.out.println("Tinput is: "+tinput);
        int tlength = tinput.length(), brek = 0;
        for (int i = 0; i < tlength; i++) {
            int temp = Integer.parseInt(tinput.substring(i, i + 1));
            for (int j = 0; j < nSamples; j++) {
                if (clusters[temp][j] != null) {
                    inputcluster[index++] = clusters[temp][j];
                }
            }
        }
        return inputcluster;
    }


    double[] getMergeCluster(int nAttributes, int nSamples, String[] inputcluster, String[][] clusters) {
        double mergeclusters[] = new double[nAttributes];
        mergeclusters[0] = 0;
        int count;
        String temp1 = "";
        for (int j = 0; j < nAttributes; j++) {
            for (int k = 0; k < ((nAttributes - 1) * nSamples); k++) {
                for (int l = 0; l < nSamples; l++) {
                    // -----
                    if (inputcluster[k] != null && clusters[j][l] != null) {
                        int n = inputcluster[k].length(), z = 0, counter = 0;
                        count = 0;
                        while (z < n) {
                            String temp = "";
                            while (z < n) {
                                if (!(inputcluster[k].substring(z, z + 1).equals("-"))) {
                                    temp = temp + inputcluster[k].substring(z, z + 1);
                                    z++;
                                } else {
                                    break;
                                }
                            }

                            if ((clusters[j][l].contains(temp))) {
                                //		System.out.println("cluster "+clusters[j][l]+" Temp : "+temp);
                                count++;
                            }
                            counter++;
                            z++;
                        }


                        if (count == counter && mergeclusters[j] < nSamples) {
                            String[] a = check(temp1, inputcluster[k], count);
                            mergeclusters[j] = mergeclusters[j] + (Double.parseDouble(a[1]));
                            temp1 += a[0];
                            //		System.out.println("MergeCluster: "+mergeclusters[j]+" Count: "+count);
                        }
                    }
                    // ------
                }

            }
            temp1 = "";
            mergeclusters[j] = mergeclusters[j] / nSamples;
        }
        return mergeclusters;
    }

    String[] check(String temp1, String inputcluster, int count) {
        String a[] = new String[2];
        int z = 0;
        int n = inputcluster.length();
        List<String> pls = new ArrayList<String>();
        while (z < n) {
            String temp = "";
            while (z < n) {
                if (!(inputcluster.substring(z, z + 1).equals("-"))) {
                    temp = temp + inputcluster.substring(z, z + 1);
                    z++;
                } else
                    break;
            }

            pls.add("-" + temp + "-");
            z++;
        }

        //	System.out.println("Pls before :"+pls);

        for (int i = 0; i < pls.size(); ) {
            if (temp1.contains(pls.get(i))) {
                pls.remove(pls.get(i));
            } else
                i++;
        }

//		System.out.println("Pls after :"+pls);

        String fin = "";

        for (String item : pls) {
            fin = fin + item;
        }


        int count1 = pls.size();

        //	System.out.println("a[0]"+fin+"a[1]"+count1);
        a[0] = fin;
        a[1] = String.valueOf(count1);

        return a;

    }


    // Main Function

    public double cmon(String input, int nAttributes, int nSamples) throws FileNotFoundException {

        RoughSetTheory rf = new RoughSetTheory();

        Scanner in = new Scanner(System.in);

//		int nAttributes = rf.getnAttributes();

//		int nSamples = rf.getnSamples();

        double data[][] = rf.getData(nAttributes, nSamples);

        String clusters[][] = rf.findClusters(nAttributes, nSamples, data);

        double resultValue[][] = rf.calculateClusters(nAttributes, nSamples, clusters);

        double total[] = rf.calculateTotal(nAttributes, resultValue);

        double maximum = rf.calculateMax(nAttributes, total);

        //	String input=rf.getFromGsa();

        String inputcluster[] = rf.findInputCluster(input, clusters, nAttributes, nSamples);

        double mergeclusters[] = rf.getMergeCluster(nAttributes, nSamples, inputcluster, clusters);

        //	rf.print(nAttributes,nSamples,clusters,resultValue,total,maximum,inputcluster,mergeclusters);

        double temp = 0;

        for (int i = 0; i < (nAttributes - 1); i++) {
            temp += mergeclusters[i];
        }
        temp = temp / (nAttributes - 1);
        System.out.println("Mean: " + temp + " for input position: " + input);

        return temp;

    }

    int getnAttributes() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of attributes in the database: ");
        int n = in.nextInt();
        return n;
    }

    int getnSamples() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of Samples in the database: ");
        int n = in.nextInt();
        return n;
    }

    void print(int nAttributes, int nSamples, String[][] clusters, double[][] resultValue, double[] total, double maximum, String[] inputcluster, double[] mergeclusters) {

        System.out.println("Clusters of individual attributes:");

        for (int i = 0; i < nAttributes; i++) {
            for (int j = 0; j < nSamples; j++) {
                System.out.print(clusters[i][j] + " ");
            }

            System.out.print("\n");
        }

        System.out.println("\nResult values of individuals:");

        for (int i = 0; i < nAttributes; i++) {
            for (int j = 0; j < nAttributes; j++) {
                System.out.print((Math.floor(resultValue[i][j] * 10000) / 10000) + " ");
            }

            System.out.print("\n");
        }

        System.out.print("Final Values:");

        for (int i = 0; i < nAttributes; i++) {
            System.out.println((Math.floor(total[i] * 10000) / 10000) + " ");
        }

        System.out.print("Maximum Value: ");

        System.out.println(Math.floor(maximum * 10000) / 10000);

        System.out.print("Input Clusters: ");
        for (int i = 0; i < ((nAttributes - 1) * nSamples); i++) {
            if (inputcluster[i] != null) {
                System.out.print(inputcluster[i] + " ");
            }

        }

        System.out.print("Merge Clusters: ");
        for (int i = 0; i < nAttributes; i++) {
            System.out.print((Math.floor(mergeclusters[i] * 10000) / 10000) + " ");
        }
        System.out.print("\n");
    }
}