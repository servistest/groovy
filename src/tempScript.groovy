import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def content = """
{
   "app":{ },
   "at":2,
   "badv":[ ],
   "bcat":[ ],
   "device":{
      "carrier":"310-410",
      "connectiontype":3,
      "devicetype":1,
      "dnt":0,
      "dpidmd5":"268d403db34e32c45869bb1401247af9",
      "dpidsha1":"1234" 
   }
}"""

def inputChain = new File("/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/FXIRChainlogicalfield.json")
def inputJSONChain = new JsonSlurper().parseText(inputChain.text)


//def builder = new JsonBuilder(inputJSONChain)
//println builder.content.Relationship[0].mpName
// def s=builder.content.Relationship[1].mpName

//List tempList=new ArrayList()
//tempList.add("new name")
//s="[HasContributedChains1110]"
//builder.content.Relationship[0].mpName=tempList
//print builder.toString()
//println(builder.toPrettyString())

List newMpNameList=new ArrayList()
newMpNameList.add("HasInstruments0000")

List tempList=new ArrayList()
tempList.add("HasInstruments0")
inputJSONChain.each {

    for (int i = 0; i < it.value.size; i++) {
//        print it.value[i].mpName
        if (it.value[i].mpName==tempList){
            println "search"
            it.value[i].mpName=newMpNameList
//            println(it.value[i].mpName)
        }

    }
}

//println(inputJSONChain)

//def builder = new JsonBuilder(inputJSONChain)
//println(builder.toPrettyString())


//println builder.content.Relationship[0].mpName


//--------------------------------------------
String json=inputChain.text
println("---------------Output inputChain.text---------------------- ")
String newjson=json.replaceAll("HasInstruments0","HasInstruments0000")
//println(newjson)
def newInputJSONChain = new JsonSlurper().parseText(newjson)



def builder = new JsonBuilder(newInputJSONChain)

println(builder.toPrettyString())

pathToSave="/home/alex/java/TR/coyote/src/main/scripts/merge/groovy/FXIR/testFXIRChainlogicalfield.json"
new File( pathToSave ).write(builder.toPrettyString())



println("--------------------------------------")
String testString = '"sds"     sds'
print testString.replaceAll('"' + "sds" + '"','"sds010"')


//print builder.toString().replaceAll('HasInstruments000','HasInstruments')
//println "-------------------------------new builder---------------------"
//println(builder.toPrettyString())

