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


void mergeLogicalFields(String pathToNewLfFile, File baseLfFile, String pathToMergeFile) {

    File newLfFile = new File(pathToNewLfFile)

    // json with  lf
    def newLfJson = checkFileAndParse(newLfFile)
    // json with base lf
    def baseLfJson = checkFileAndParse(baseLfFile)
    // json for merge lf
    def mergeJson = new JsonSlurper().parseText('{}')

    def lfCurrentFields
    def lfCurrentMpName
    def lfBaseCurrentFields
    def lfBaseCurrentMpName
    def lfCurrentRootItem
    Boolean isExistField

    Map mergeLfMap = new HashMap<>()

    baseLfJson.each {

        List itemsForMergeList = new ArrayList()
        mergeLfMap.clear()

        //current root item from lf
        lfCurrentRootItem = newLfJson.get(it.key)

        if (lfCurrentRootItem != null) {

            for (int j = 0; j < lfCurrentRootItem.size; j++) {

                lfCurrentFields = lfCurrentRootItem[j].fields
                lfCurrentMpName = lfCurrentRootItem[j].mpName

                isExistField = false

                for (int i = 0; i < it.value.size; i++) {
                    lfBaseCurrentFields = it.value[i].fields
                    lfBaseCurrentMpName = it.value[i].mpName

                    //if such an element exists in base-lf, then we skip it
                    if (lfCurrentFields == lfBaseCurrentFields && lfCurrentMpName == lfBaseCurrentMpName) {
                        isExistField = true
                        break
                    }
                }

                //  add items in the list from lf  which are not in the base-lf
                if (!isExistField) {
                    itemsForMergeList.add(lfCurrentRootItem[j])
//                    println(lfCurrentRootItem[j])
                }
            }
        }

        //  add all  items current from root-element  base-lf  for merge
        for (int j = 0; j < it.value.size; j++) {
            itemsForMergeList.add(it.value[j])
            mergeLfMap.put(it.key, itemsForMergeList)
        }

        // add to merge-json items
        mergeJson << mergeLfMap

    }

    //  search roots items from lf  which are not in the base-lf
    def mapToAddToJson = new HashMap()
    Boolean toAdd
    newLfJson.each {
        currentNewLFElement = it
        toAdd = true
        baseLfJson.each {
            if (it.key == currentNewLFElement.key) {
                toAdd = false
            }

        }
        if (toAdd) {
            mapToAddToJson.put(currentNewLFElement.key, currentNewLFElement.value)
        }
    }

    // add to merge-json  roots items from lf  which are not in the base-lf
    mergeJson << mapToAddToJson

    //save merge-json to file
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

















