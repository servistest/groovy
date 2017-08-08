import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

/**
 * Created by alex on 23.5.17.
 */
@Canonical
@EqualsAndHashCode
class Person {
    String name
    int age;

    @Override
    boolean equals(Object obj) {
        return true
    }


//    boolean asBoolean(){
//        return age<100
//    }
}
