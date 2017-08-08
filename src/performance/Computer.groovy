package performance

/**
 * Created by alex on 30.5.17.
 */
@groovy.transform.CompileStatic
class Computer {

    static int compute(String str) {
        str.length()
    }
    static String compute(int x) {
        String.valueOf(x)
    }


//    @groovy.transform.CompileStatic
    void test() {
        def computer = new Computer()
        computer.with {
            assert compute(compute('foobar')) =='6'
        }
    }
}



