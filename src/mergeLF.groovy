import groovy.json.JsonBuilder
import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.transform.Field

/**
 * Created by alex on 23.5.17.
 */

@Field File lfFile
@Field File ruleFile
@Field File baseLfFile
@Field File outDir;
@Field File newLfFile;


@Field Map<String, String> baseLFMaps = new HashMap<>();
@Field Map<String, String> lfMaps = new HashMap<>();
@Field Map<String, String> newLFMaps = new HashMap<>();


@Field List logicalNameToChangeList = new ArrayList()
@Field List logicalNameToReplaceList = new ArrayList()

@Field String postfixForNewLogicalName = "1n1"
@Field String newLFNameFile = "/newLF.json"
@Field String newRuleFile = "/newRule.json"

@Field Integer countArguments = 4


void outInformationScript() {

    println("Usage: groovy mergeLF.groovy [lf] [rule] [baself] [outdir]")
    println("lf:  input logic field json ")
    println("rule: path to ruledir for lf json ")
    println("base: the logic fields as base, not change json")
    println("outdir: the merged result")
    System.exit(0)

}

void outError(String nameFile) {

    println(nameFile + " -  file/dir  not found ")
    outInformationScript()
    System.exit(0)

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

void extractFieldsAndMpNameFromLF(File fileName, Map<String, String> lfMap) {
    def json

    try {
        json = new JsonSlurper().parseText(fileName.text)
    } catch (JsonException e) {
        println fileName + " - File is not valid"
        throw e
    }

    json.each {

        for (int i = 0; i < it.value.size; i++) {
            lfMap.put((String) it.value[i].mpName, (String) it.value[i].fields)
        }
    }
}

void findSameMpNames() {

    println "  ----------------------------------------WE SEEK SAME LOGICAL FIELDS BUT WITH DIFFERENT VALUE ----------------------------------------"
    println " --------------------------------LF-----------------------------------------------------------------------------------Base LF ----------------------------------------"

    for (Map.Entry<String, String> lfEntry : lfMaps.entrySet()) {
        for (Map.Entry<String, String> baseLFEntry : baseLFMaps.entrySet()) {
            if ((baseLFEntry.getKey() == lfEntry.getKey()) && (lfEntry.getValue() != baseLFEntry.value)) {

                logicalNameToChangeList.add(lfEntry.getKey().replace("[", "").replace("]", ""))


                printf('%-115s', lfEntry.getKey().replace("[", "").replace("]", "") + " " + lfEntry.getValue() + "      ")
                println baseLFEntry.key.replace("[", "").replace("]", "") + " " + baseLFEntry.value

            }

        }
    }

}


void replacementInLFAndSaveFile() {

    println("---------------Replacement with new names in LF file---------------------- ")

    String lfFileText = lfFile.text

    for (String logicalName : logicalNameToChangeList) {

        lfFileText = lfFileText.replaceAll('"' + logicalName + '"', '"' + logicalName + postfixForNewLogicalName + '"')
        println "change " + logicalName + "--->" + logicalName + postfixForNewLogicalName
    }

    def newLFJson = new JsonSlurper().parseText(lfFileText)
    def builder = new JsonBuilder(newLFJson)

    //save  LF file with new logical names
    new File(outDir.path + newLFNameFile).write(builder.toPrettyString())

}

void replacementInRuleAndSaveFile() {

    String ruleText = ruleFile.text

    for (String logicalName : logicalNameToChangeList) {
//        ruleText = ruleText.replaceAll(logicalName, logicalName + postfixForNewLogicalName)
        ruleText = ruleText.replaceAll('"' + logicalName + '"', '"' + logicalName + postfixForNewLogicalName + '"')
        ruleText = ruleText.replaceAll('/' + logicalName + '/', '/' + logicalName + postfixForNewLogicalName + '/')
    }

    def newJSONRule = new JsonSlurper().parseText(ruleText)
    def builderRule = new JsonBuilder(newJSONRule)

    new File(outDir.path + newRuleFile).write(builderRule.toPrettyString())

}

void findSameFieldsInLF(def lfMaps, def baseLFMaps) {

    println "  ----------------------------------------findSameFieldsInLF ----------------------------------------"
    println " --------------------------------LF-----------------------------------------------------------------------------------Base LF ----------------------------------------"
    def i = 1
    Map changes = new HashMap()
    def lfName
    def baseName
    for (Map.Entry<String, String> baseLFEntry : baseLFMaps.entrySet()) {
        for (Map.Entry<String, String> lfEntry : lfMaps.entrySet()) {
            lfName = lfEntry.getKey().replace("[", "").replace("]", "")
            baseName = baseLFEntry.getKey().replace("[", "").replace("]", "")

            if ((baseLFEntry.getKey() != lfEntry.getKey()) && (lfEntry.getValue() == baseLFEntry.value) && (!logicalNameToReplaceList.contains(lfName))) {

                changes.put(lfName, baseName)

                logicalNameToReplaceList.add(lfName)
                print i++ + " "
                printf('%-115s', lfName + " " + lfEntry.getValue() + "      ")
                println baseName + " " + baseLFEntry.value
                break
            }
        }
    }
    println(logicalNameToReplaceList + " " + changes)
    println("changes= " + changes)
}

void mergeLogicalFields() {

    newLfFile = new File(outDir.path + newLFNameFile)
    assert newLfFile.exists(): outError(newLfFile.name)
    def newLfJson = new JsonSlurper().parseText(newLfFile.text)

    def baseLfJson = new JsonSlurper().parseText(baseLfFile.text)

    def tempJson = new JsonSlurper().parseText('{}')


    Map changeMap = new HashMap()

    baseLfJson.each {

        LinkedHashMap tempLinkedHasMap = new HashMap<>()
        List listLazyMap = new ArrayList()



        def lfItem = newLfJson.get(it.key)
        if (lfItem != null){

            def existField
            for (int j = 0; j < lfItem.size; j++) {

                existField = false;
                for (int i = 0; i < it.value.size; i++) {
                    def tmp1 = lfItem[j].fields
                    def tmp2 = it.value[i].fields

                    if (tmp1 == tmp2) {
                        existField = true
                        break
                    }

                }

                if (!existField) {
                    listLazyMap.add(lfItem[j])
                    existField = true
                }
            }
        }



        for (int j = 0; j < it.value.size; j++) {
            listLazyMap.add(it.value[j])
            tempLinkedHasMap.put(it.key, listLazyMap)
        }

            tempJson << tempLinkedHasMap



    }

// Merge
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

    baseLfJson << tempMap

    tempJson << tempMap


    JsonBuilder tempJsonBuilder = new JsonBuilder(tempJson)
    new File(outDir.path + "/temp_lf.json").write(tempJsonBuilder.toPrettyString())

//    println "Add to base LF - " + tempMap
//    JsonBuilder mergeJsonBuilder = new JsonBuilder(baseLfJson)
//    new File(outDir.path + "/merge_lf.json").write(mergeJsonBuilder.toPrettyString())

}

//-----------start script----------------------

if (args.length != countArguments) {
    outInformationScript()
}
checkExistsInputFiles()
extractFieldsAndMpNameFromLF(baseLfFile, baseLFMaps)
extractFieldsAndMpNameFromLF(lfFile, lfMaps)
findSameMpNames()
replacementInLFAndSaveFile()
replacementInRuleAndSaveFile()
mergeLogicalFields()

///!!!!!!!!!!!! CHANGE RETURN!!!!!!!!
//newLfFile = new File(outDir.path + newLFNameFile)
//newLfFile = new File(args[0])
//
//assert newLfFile.exists(): outError(newLfFile.name)
//extractFieldsAndMpNameFromLF(newLfFile, newLFMaps)
//
//findSameFieldsInLF(newLFMaps,baseLFMaps)










