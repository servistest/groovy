import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * Created by alex on 23.5.17.
 */


def inputQuote = new File("/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/FXIRQuotelogicalfield.json")
def inputJSONQuote = new JsonSlurper().parseText(inputQuote.text)


Map<String, String> quoteFieleds = new HashMap();
    inputJSONQuote.each {

        for (int i = 0; i < it.value.size; i++) {
            print it.value[i].mpName
            print "       "
            println it.value[i].fields
            quoteFieleds.put((String) it.value[i].mpName, (String) it.value[i].fields)
        }

    println()
    println "    -------quoteFieleds ----------------------------------------"
    System.out.println(quoteFieleds);


    def inputChain = new File("/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/FXIRChainlogicalfield.json")

    def inputJSONChain = new JsonSlurper().parseText(inputChain.text)

//        def builder =new JsonBuilder(inputJSONChain)

    Map<String, String> chainFieleds = new HashMap();

    inputJSONChain.each {

        for (int i = 0; i < it.value.size; i++) {
            print it.value[i].mpName
            print "       "
            println it.value[i].fields
            chainFieleds.put((String) it.value[i].mpName, (String) it.value[i].fields)
        }
    }

    println()
    println " ---------------------    chainFieleds----------------------------------------"
    System.out.println(chainFieleds);



    println()
    println "    Output array of map    chainFieleds ----------------------------------------"

    for (Map.Entry<String, String> entry : chainFieleds.entrySet()) {
        System.out.println(entry.getKey() + "/" + entry.getValue());

    }

    println()
    println "    COMPARE   array of map    chainFieleds and  quoteFieleds  -- Same logical fields ----------------------------------------"
    println " ----------------Chain ---------------------------------------------Quote ----------------------------------------"

    String logicalNameKey = new String();
    String logicalNameValue = new String();
        List logicalNameToChangeList=new ArrayList()

        List tmp=new ArrayList()
    for (Map.Entry<String, String> entry : chainFieleds.entrySet()) {

        logicalNameKey = entry.getKey();
        logicalNameValue = entry.getValue();
        for (Map.Entry<String, String> entry2 : quoteFieleds.entrySet()) {
            if (entry2.getKey() == logicalNameKey) {

                if (logicalNameValue != entry2.value) {

                    print logicalNameKey.replace("[", "").replace("]", "") + " " + logicalNameValue + " "
                    println entry2.key.replace("[", "").replace("]", "") + " " + entry2.value

                    logicalNameToChangeList.add(logicalNameKey.replace("[", "").replace("]", ""))

                    // rename logical name
                    println()
                    newLogicalNameKey=logicalNameKey.replace("[", "").replace("]", "")+'11';
                    print "new logical name =" + newLogicalNameKey
                    println "------ rename logical name ------------------"
                }

            }

        }
    }
        println("---------------------------------------------------------")
        println("List logical name to change  = " + logicalNameToChangeList)



        println("---------------Output inputChain.text---------------------- ")

        String chainText=inputChain.text

        for (String logicalName :logicalNameToChangeList ){
            json=chainText.replaceAll(logicalName,logicalName+"1n1")
            println "change "+ logicalName +"--->" + logicalName+"1n1"

        }

        println(chainText)
        def newInputJSONChain = new JsonSlurper().parseText(chainText)
        def builder = new JsonBuilder(newInputJSONChain)

        println("---------------Output with new logical fields---------------------- ")

//        println(builder.toPrettyString())

        pathToSaveNewLogicalFields="/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/newFXIRChainlogicalfield.json"
        new File( pathToSaveNewLogicalFields ).write(builder.toPrettyString())



        // change logical fields in the rule.json

        pathToRuleJson="/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/ruleChain.json"

        def inputRule = new File(pathToRuleJson)


        String ruleText=inputRule.text

        for (String logicalName :logicalNameToChangeList ){
            ruleText=ruleText.replaceAll(logicalName,logicalName+"1n1")

        }

        println(ruleText)
        def newInputJSONRule = new JsonSlurper().parseText(ruleText)
        def builderRule = new JsonBuilder(newInputJSONRule)

        println("---------------Output with new logical fields in the rule---------------------- ")

//        println(builder.toPrettyString())

        pathToSaveRuleJson="/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/newRuleChain.json"
        new File( pathToSaveRuleJson ).write(builderRule.toPrettyString())

}