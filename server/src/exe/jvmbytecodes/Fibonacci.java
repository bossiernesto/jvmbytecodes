public class Fibonacci {

	public static void main(String args[]) {
		int firstNum = 0;
		int secondNum = 1;
		int fibonacci = 0;
		int temp = 0;

		int i = 0;
		while (i < 10) {
			temp = secondNum;
			secondNum = firstNum + secondNum;
			firstNum = temp;

			i++;
		}

		fibonacci = secondNum; // the twelfth fib num

	}

}
