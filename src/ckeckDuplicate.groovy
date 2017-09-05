
//
//  File: mergeLF.groovy
//
//  The script for merging two json with logical fields.
//
import groovy.json.JsonBuilder
import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.transform.Field

/**
 * Created  3.8.17.
 */

@Field File lfFile
@Field File ruleFile
@Field File baseLfFile
@Field File outDir



@Field Map baseLFMaps = new HashMap<>()
@Field Map lfMaps = new HashMap<>()
@Field Map lfToReplaceMaps = new HashMap<>()

//temp
@Field Map lfMyMaps = new HashMap<>()
@Field Map myLFToReplaceMaps = new HashMap<>()

@Field List logicalNameToChangeList = new ArrayList()

@Field String postfixForNewLogicalName = "1n1"

@Field String newLFFileName = "/newLF.json"
@Field String newRuleFileName = "/newRule.json"
@Field String mergeLfFileName = "/merge_lf.json"

@Field Integer countArguments = 4



def checkFileAndParse(File fileName) {

    def json

    try {
        json = new JsonSlurper().parseText(fileName.text)
    } catch (JsonException e) {
        println fileName + " - File is not valid"
        throw e
    }
    return json
}

void extractFieldsAndMpNameFromLF() {

    Map lfMap = new HashMap()
    File fileName = new File("/home/alex/IdeaProjects/ihg/groovy3/src/merge3domens/output/FXIRREFlogicalfield.json")

    def json = checkFileAndParse(fileName)

    json.each {

        for (int i = 0; i < it.value.size; i++) {

            if (!lfMap[(String) it.value[i].mpName]) {

                //Check for the same logical names in one file
                lfMap.put((String) it.value[i].mpName, (String) it.value[i].fields)

            } else {

                println("The same logical fields !!!  - " + it.value[i].mpName + " in the " + fileName + " Please correct!!!")
//                System.exit(0)

            }
        }
    }
}

extractFieldsAndMpNameFromLF()
