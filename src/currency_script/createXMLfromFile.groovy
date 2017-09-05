package currency_script

import groovy.json.JsonBuilder
import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.transform.Field
import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder

/**
 * Created  3.8.17.
 */


    def writeFile(fileName, closure) {
        def xmlFile = new File(fileName)
        def writer = xmlFile.newWriter()
        def builder = new StreamingMarkupBuilder()
        def Writable writable = builder.bind closure
        writable.writeTo(writer)
    }


        File file = new File("/home/alex/IdeaProjects/ihg/groovy3/src/currency_script/export.csv")
        def closure = {
            data {
                file.eachLine { String line ->
                    Metadata_Fanout {
                    line.splitEachLine(',') { items ->
                        classification(items[0])
                        perm_id(items[1])
                        qcode(items[2])
                        english (items[3])
                        multilanguages(items[4])
                        qcode_genealogy(items[5])
                        english_genealogy(items[6])
                        multilanguage_genealogy(items[7])
                    }
                }
            }
            }
        }
        writeFile("Currency.xml", closure)
