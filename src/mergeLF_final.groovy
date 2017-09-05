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

@Field List logicalNameToChangeList = new ArrayList()

@Field String postfixForNewLogicalName = "1n1"

@Field String newLFFileName = "/newLF.json"
@Field String newRuleFileName = "/newRule.json"
@Field String mergeLfFileName = "/merge_lf.json"

@Field Integer countArguments = 4


void outInformationScript() {

    println("Usage: groovy mergeLF.groovy [lf] [rule] [baself] [outdir]")
    println("lf:  input logic field json ")
    println("rule: path to ruledir for lf json ")
    println("base: the logic fields as base, not change json")
    println("outdir: the merged result")
    println("eg : groovy ./mergeLF.groovy  FXIR/DIRChainlogicalfield.json   FXIR/ruleChain.json FXIR/DIRQuotelogicalfield.json  FXIR/output")
    System.exit(0)

}

void outError(String nameFile) {

    println(nameFile + " -  file/dir  not found ")
    outInformationScript()
    System.exit(0)

}

void printList(List list, String information) {
    println()
    println(information)
    println("Amount of elements =  " + list.size())
    println()
    list.each {
        println(it + " -->" + it + postfixForNewLogicalName)
    }
}

void printMap(Map map, String information) {

    println()
    println(information)
    println("Amount of elements =  " + map.size())
    println()
    map.each {
        println(it.key + " --> " + it.value)
    }
}

void checkExistsInputFiles() {

    lfFile = new File(args[0])
    assert lfFile.exists(): outError(lfFile.name)

    ruleFile = new File(args[1])
    assert ruleFile.exists(): outError(ruleFile.name)

    baseLfFile = new File(args[2])
    assert baseLfFile.exists(): outError(baseLfFile.name)

    outDir = new File(args[3])
    assert outDir.exists(): outError(outDir.name)

}

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

void extractFieldsAndMpNameFromLF(File fileName, Map lfMap) {

    def json = checkFileAndParse(fileName)

    json.each {

        for (int i = 0; i < it.value.size; i++) {

            if (!lfMap[(String) it.value[i].mpName]) {

                //Check for the same logical names in one file
                lfMap.put((String) it.value[i].mpName, (String) it.value[i].fields)

            } else {

                println("The same logical fields !!!  - " + it.value[i].mpName + " in the " + fileName + " Please correct!!!")
                System.exit(0)

            }
        }
    }
}


void сollisionsDeleting(File inFile, String pathToOutFile, List replaceList) {

    String ruleText = inFile.text

    for (String logicalName : replaceList) {
        ruleText = ruleText.replaceAll('"' + logicalName + '"', '"' + logicalName + postfixForNewLogicalName + '"')
        ruleText = ruleText.replaceAll('/' + logicalName + '/', '/' + logicalName + postfixForNewLogicalName + '/')

    }

    def newJSONRule = new JsonSlurper().parseText(ruleText)
    def builderRule = new JsonBuilder(newJSONRule)
    new File(pathToOutFile).write(builderRule.toPrettyString())

}


void findCollisions(Map lfMaps, Map baseLFMaps) {
    println()
    println " SEARCH COLLISIONS .."
    println "                                   LF                                                                            Base LF                                       "

    for (Map.Entry lfEntry : lfMaps.entrySet()) {
        for (Map.Entry baseLFEntry : baseLFMaps.entrySet()) {
            if ((baseLFEntry.getKey() == lfEntry.getKey()) && (lfEntry.getValue() != baseLFEntry.value)) {

                logicalNameToChangeList.add(lfEntry.getKey().replace("[", "").replace("]", ""))

                printf('%-115s', lfEntry.getKey().replace("[", "").replace("]", "") + " " + lfEntry.getValue() + "      ")
                println baseLFEntry.key.replace("[", "").replace("]", "") + " " + baseLFEntry.value

            }

        }
    }

}

void replacementFieldsInFile(String pathToInFile, String pathToOutFile, Map replaceMaps) {

    File newRule = new File(pathToInFile)
    String ruleText = newRule.text

    for (def logicalName : replaceMaps) {

        ruleText = ruleText.replaceAll('"' + logicalName.key + '"', '"' + logicalName.value + '"')
        ruleText = ruleText.replaceAll('/' + logicalName.key + '/', '/' + logicalName.value + '/')

    }

    def newJSONRule = new JsonSlurper().parseText(ruleText)
    def builderRule = new JsonBuilder(newJSONRule)
    new File(pathToOutFile).write(builderRule.toPrettyString())
}


void mergeLogicalFields(String pathToNewLfFile, File baseLfFile, String pathToMergeFile) {

    File newLfFile = new File(pathToNewLfFile)

    // json with  lf
    def newLfJson = checkFileAndParse(newLfFile)
    // json with base lf
    def baseLfJson = checkFileAndParse(baseLfFile)
    // json for merge lf
    def mergeJson = new JsonSlurper().parseText('{}')

    def forReplace
    Map mergeLfMap = new HashMap<>()

    baseLfJson.each {

        mergeLfMap.clear()
        List itemsForAddList = new ArrayList()

        def lfItem = newLfJson.get(it.key)
        if (lfItem != null) {

            def existField
            for (int j = 0; j < lfItem.size; j++) {

                existField = false;
                for (int i = 0; i < it.value.size; i++) {

                    // Find  elements in lf that is not in base-lf
                    if (lfItem[j].fields == it.value[i].fields) {
                        existField = true
                        def tmp = (String) it.value[i].mpName
                        tmp = tmp.replace("[", "").replace("]", "")

                        //if this item is not in collection  remember it
                        if (!lfToReplaceMaps.find { it.value == tmp }?.key) {
                            forReplace = (String) it.value[i].mpName
                            break
                        }
                    }

                }

                if (!existField) {      // add item for merge
                    itemsForAddList.add(lfItem[j])
                    existField = true
                } else {                 // add item to replace in rule and lf
                    tmpString = (String) lfItem[j].mpName
                    if (tmpString != forReplace) {
                        lfToReplaceMaps.put(tmpString.replace("[", "").replace("]", ""), forReplace.replace("[", "").replace("]", ""))
                    }

                }
            }
        }

        // add all items from base-lf  for merge
        for (int j = 0; j < it.value.size; j++) {
            itemsForAddList.add(it.value[j])
            mergeLfMap.put(it.key, itemsForAddList)
        }

        // add to merge json items
        mergeJson << mergeLfMap

    }

    //  Search items from lf  which are not in the base-lf
    def tempMap = new HashMap()
    newLfJson.each {
        currentNewLFElement = it
        def change = true
        baseLfJson.each {
            if (it.key == currentNewLFElement.key) {
                change = false
            }

        }
        if (change) {
            tempMap.put(currentNewLFElement.key, currentNewLFElement.value)
        }
    }
    // add to merge-json items
    mergeJson << tempMap

    JsonBuilder mergeJsonBuilder = new JsonBuilder(mergeJson)
    new File(pathToMergeFile).write(mergeJsonBuilder.toPrettyString())


}

//-----------start script----------------------

// checks  arguments for starts script
if (args.length != countArguments) {
    outInformationScript()
}
// checks exist files
checkExistsInputFiles()

//Retrieval fields for compare from base LF
extractFieldsAndMpNameFromLF(baseLfFile, baseLFMaps)

//Retrieval fields for compare from LF
extractFieldsAndMpNameFromLF(lfFile, lfMaps)

// find collisions - compare fields and mpName
findCollisions(lfMaps, baseLFMaps)

// print all collisions
printList(logicalNameToChangeList, "COLLISIONS DELETING ..... RENAMING FIELDS IN LF AND RULE ")

//rename fields in rule
сollisionsDeleting(ruleFile, outDir.path + newRuleFileName, logicalNameToChangeList)

//rename fields in lf
сollisionsDeleting(lfFile, outDir.path + newLFFileName, logicalNameToChangeList)

//merge lf and base lf
mergeLogicalFields(outDir.path + newLFFileName, baseLfFile, outDir.path + mergeLfFileName)

//print logical fields for replace in rule and lf
printMap(lfToReplaceMaps, "MERGE FILES  --> LOGICAL FIELDS FOR REPLACE IN RULE AND LF:")

//replace fields in rule after  merge
replacementFieldsInFile(outDir.path + newRuleFileName, outDir.path + newRuleFileName, lfToReplaceMaps)

//replace fields in LF after  merge
replacementFieldsInFile(outDir.path + newLFFileName, outDir.path + newLFFileName, lfToReplaceMaps)













