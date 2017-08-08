package epam

import com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile
import groovy.json.JsonSlurper
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import jdk.internal.dynalink.beans.StaticClass

/**
 * Created by alex on 24.5.17.
 */
@Canonical
@CompileStatic
 class Money {
     Integer amount
    String currency

    @groovy.transform.TypeChecked(TypeCheckingMode.SKIP)
     Money plus(Money obj1){

        if (this.currency==obj1.currency){
            return new Money(this.amount+obj1.amount,this.currency);
        }
        else {
            URL rateUrl =new URL("http://api.fixer.io/latest?symbols=USD,GBP");
             def rate =new JsonSlurper().parseText(rateUrl.text)
            def rateUSD=rate.rates."USD" as Integer
            return new Money(this.amount+obj1.amount*rateUSD,this.currency,)
        }

    }


     Money plus(int amount ){
        return new Money(this.amount+amount,this.currency)
    }



}
