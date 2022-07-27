import java.util.*; 

public class TsaiMatrixMultiplication {
    public int[][] classicMatrixMultiplication(int[][] matrixOne, int[][] matrixTwo) {
        int[][] matrixThree = new int[matrixOne.length][matrixOne.length]; 
        for (int i = 0; i < matrixOne.length; i++) {
            for (int j = 0; j < matrixTwo.length; j++) {
                for (int k = 0; k < matrixOne[0].length; k++) {
                    matrixThree[i][j] += (matrixOne[i][k]*matrixTwo[k][j]); 
                }
            }
        }
        return matrixThree; 
    }

    public int[][] recursiveMatrixMultiplication(int[][] matrixOne, int[][] matrixTwo) {
        int size = matrixOne.length;            //recursion reference 
        int productResult[][] = new int[size][size];
        if (size == 1) {
            productResult[0][0] = matrixOne[0][0]*matrixTwo[0][0];
        }
        else {
            int half = size/2;
            int[][] varA11 = new int[half][half], varA12 = new int[half][half];
            int[][] varA21 = new int[half][half], varA22 = new int[half][half]; //split first matrix into four parts
            int[][] varB11 = new int[half][half], varB12 = new int[half][half];
            int[][] varB21 = new int[half][half], varB22 = new int[half][half]; //split second matrix into four parts

            fill(matrixOne, varA11, 0, 0); fill(matrixOne, varA12, 0, half); 
            fill(matrixOne, varA21, half, 0); fill(matrixOne, varA22, half, half);  //filling quadrants for matrix one
            
            fill(matrixTwo, varB11, 0, 0); fill(matrixTwo, varB12, 0, half);
            fill(matrixTwo, varB21, half, 0); fill(matrixTwo, varB22, half, half);  //filling quadrants for matrix two 

            //formula to improve run time by reducing multiplication counts
            int[][] C11 = straussAdd(recursiveMatrixMultiplication(varA11, varB11), recursiveMatrixMultiplication(varA12, varB21)); 
            int[][] C12 = straussAdd(recursiveMatrixMultiplication(varA11, varB12), recursiveMatrixMultiplication(varA12, varB22));
            int[][] C21 = straussAdd(recursiveMatrixMultiplication(varA21, varB11), recursiveMatrixMultiplication(varA22, varB21));
            int[][] C22 = straussAdd(recursiveMatrixMultiplication(varA21, varB12), recursiveMatrixMultiplication(varA22, varB22));
            straussMerge(C11, productResult, 0, 0); straussMerge(C12, productResult, 0, half); //merge results to final array 
            straussMerge(C21, productResult, half, 0); straussMerge(C22, productResult, half, half); 

        }
        return productResult; 

    }

    public int[][] straussMultiplication(int[][] matrixOne, int[][] matrixTwo) {
        int size = matrixOne.length; 
        int productResult[][] = new int[size][size]; 
        if (size == 1) {
            productResult[0][0] = matrixOne[0][0]*matrixTwo[0][0];
        }
        else {
            int half = size/2; 
            int[][] varA11 = new int[half][half], varA12 = new int[half][half];
            int[][] varA21 = new int[half][half], varA22 = new int[half][half];
            int[][] varB11 = new int[half][half], varB12 = new int[half][half];
            int[][] varB21 = new int[half][half], varB22 = new int[half][half]; 

            fill(matrixOne, varA11, 0, 0); fill(matrixOne, varA12, 0, half); 
            fill(matrixOne, varA21, half, 0); fill (matrixOne, varA22, half, half);

            fill(matrixTwo, varB11, 0, 0); fill(matrixTwo, varB12, 0, half); 
            fill(matrixTwo, varB12, half, 0); fill(matrixTwo, varB22, half, half); 

            //following Strauss equations
            int[][] P = straussMultiplication(straussAdd(varA11, varA22), straussAdd(varB11, varB22)); 
            int[][] Q = straussMultiplication(straussAdd(varA21, varA22), varB11); 
            int[][] R = straussMultiplication(varA11, straussSubtract(varB12, varB22)); 
            int[][] S = straussMultiplication(varA22, straussSubtract(varB21, varB11)); 
            int[][] T = straussMultiplication(straussAdd(varA11, varA12), varB22); 
            int[][] U = straussMultiplication(straussSubtract(varA21, varA11), straussAdd(varB11, varB12));
            int[][] V = straussMultiplication(straussSubtract(varA12, varA22), straussAdd(varB21, varB22));
            int[][] C11 = straussAdd(straussSubtract(straussAdd(P, S), T), V);
            int[][] C12 = straussAdd(R,T);
            int[][] C21 = straussAdd(Q, S);
            int[][] C22 = straussAdd(straussSubtract(straussAdd(P, R), Q), U);

            //merge results to final array
            straussMerge(C11, productResult, 0, 0); straussMerge(C12, productResult, 0, half);
            straussMerge(C21, productResult, half, 0); straussMerge(C22, productResult, half, half); 
        }
        return productResult; 

    }

    private int[][] straussSubtract(int[][] matrixA, int[][] matrixB) {
        int[][] difference = new int[matrixA.length][matrixA.length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                difference[i][j] = matrixA[i][j] - matrixB[i][j]; 
            }
        }
        return difference; 
    }

    private void straussMerge(int[][] matrixOne, int[][] result, int partOne, int partTwo) {
        for (int i = 0, position1 = partOne; i < matrixOne.length; i++, position1++) {
            for (int j = 0, position2 = partTwo; j < matrixOne.length; j++, position2++) {
                result[position1][position2] = matrixOne[i][j]; 
            }
        }
    }

    private int[][] straussAdd(int[][] matrixA, int[][] matrixB) {
        int[][] sum = new int[matrixA.length][matrixA.length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                sum[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return sum; 

    }

    private void fill(int[][] matrixOne, int[][] matrixFill, int pos1, int pos2) {
        for (int i = 0, position1 = pos1; i <matrixFill.length; i++, position1++) {
            for (int j = 0, position2 = pos2; j < matrixFill.length; j++, position2++) {
                matrixFill[i][j] = matrixOne[position1][position2];
            }
        }
    }

    public static void main(String[] args) {
        int[][] array1 = new int[2][2];
        array1[0][0] = 10;
        array1[0][1] = 20;
        array1[1][0] = 30;
        array1[1][1] = 40;

        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array1[i].length; j++) {
                array1[i][j] = j; 
                System.out.println(array1[i][j]); 
            }
        }

        int[][] array2 = new int[2][2]; 
        array2[0][0] = 50; 
        array2[0][1] = 60;
        array2[1][0] = 70;
        array2[1][1] = 80; 

        for (int i = 0; i < array2.length; i++) {
            for (int j = 0; j < array2[i].length; j++) {
                array2[i][j] = j; 
                System.out.println(array2[i][j]); 
            }
        }

        long start = System.nanoTime();

        TsaiMatrixMultiplication tsai = new TsaiMatrixMultiplication();
        int[][] x = tsai.classicMatrixMultiplication(array1, array2); 
        

        long end = System.nanoTime();
       
        int[][] array3 = new int[2][2];
        for (int i = 0; i < array3.length; i++) {
            for (int j = 0; j < array3[i].length; j++) {
                System.out.println(array1[i][j]); 
            }
        }

        long total = end - start; 
        System.out.println("The run time is: "); 
        System.out.println(total); 


        /*int rows = 2;
        int columns = 2; 

        int[][] array = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                array[i][j] = 0; 
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(array[i][j]); 
            }
            System.out.println(); 
        }
       
   
    }
    */
}
}
