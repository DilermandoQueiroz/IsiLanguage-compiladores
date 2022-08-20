import java.util.Scanner;
public class MainClass {
	public static void main(String args[]) {
		Scanner _key = new Scanner(System.in);
		double  a;
		double  b;
		double  c;
		double  d;
		System.out.println("Programa Teste");
		System.out.println("Digite A");
		a= _key.nextDouble();
		System.out.println("Digite B");
		b= _key.nextDouble();
		while (a<b) {
System.out.println("A = ");System.out.println(a);a = a+1;}
		System.out.println("A = ");
		System.out.println(a);
		System.out.println("B = ");
		System.out.println(b);
	}
}