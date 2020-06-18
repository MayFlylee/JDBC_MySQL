public class Factorial {


    public void fact(int n) {
        int r=1;
        for (int i = 1; i <= n; i++) {
            r = r * i;
        }
        System.out.println(r);
    }
}
