import java.util.Random;

public class Main{
	public static void main(String[] args){
		int[] z = new int[14];
		float[] x = new float[15];
		double[][] z1 = new double[14][15];
		final float MIN = -4.0f;
		final float MAX = 14.0f;
		Random rnd = new Random();
		
		//заполнение массива z
		for (int i = 3; i < 17; i++){
			z[i-3] = i;
		}
		//заполнение массива x
		for (int i = 0; i < 15; i++){
			x[i] = MIN + rnd.nextFloat() * (MAX - MIN);
		}
		//заполнение массива z1
		for (int i = 0; i < 14; i++){
			for (int j = 0; j < 15; j++){
				z1[i][j] = newElement(z[i], x[j]);
			}
		}
		printArray(z1);	
	}
	
	//метод для вычисления элемента массива
	public static double newElement(int n, float x){
		if (n == 16){
			return Math.pow(Math.E, Math.pow(Math.E, Math.atan((x + 5)/18)));
		} else if ((n >= 10 & n <= 14) | n == 5 | n == 8){
			return Math.pow(Math.E, Math.cos(Math.asin((x + 5)/18)));
		} else {
			return Math.asin(Math.cos(Math.pow(Math.pow(x, (x - 1)/ Math.PI), Math.sin(x) - 0.5) * (2 - Math.pow(x, (double)1/9))));
		}
		
	}
	
	//метод для вывода массива
	public static void printArray(double arr[][]){
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[i].length; j++){
				System.out.printf("%10.3f ", arr[i][j]);
			}
			System.out.println();
		}
	}
}