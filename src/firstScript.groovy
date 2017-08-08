import epam.Money
import groovy.json.JsonSlurper

/**
 * Created by alex on 23.5.17.
 */


println("Hello world!")

def nameAndAge() {
    [name: 'john', age: 30]
}

Person p = nameAndAge() as Person
println(p.age + ' ' + p.name)

//Person person=new Person(lastName: 'alex',name: 'boroch',age: 150)

def person = new Person(age: 100)
if (person) {
    println 'age = ' + person.age
}
def person1 = new Person(name: 'Alex', age: 12)
def person2 = new Person(name: 'Alex', age: 12)

if (person1 == person2) println('Person is eqauls')
else println('Person is not equals')

if (person1.equals(person2)) println('Person is eqauls')
else println('Person is not equals')

Money money1 = new Money(currency: 'BYR', amount: 10)
Money money2 = new Money(currency: 'USD', amount: 100)

Money resultMoney = money1 + money2
println resultMoney
println money1 + money2
println money1 + 200

URL rateUrl = new URL("http://api.fixer.io/latest?symbols=USD,GBP");
String json = rateUrl.text

def start = System.currentTimeMillis()

for (int i = 0; i < 100000; i++) {

    def rate = new JsonSlurper().parseText(json)
    println rate
// remove objects
    rate.remove("date")
    println rate

    rate.rates.remove("USD")
    println rate

    rate.remove("base")
    println rate
}

println("Groovy(static ): ${System.currentTimeMillis() - start}ms")

//URL rateUrl =new URL("http://api.fixer.io/latest?symbols=USD,GBP");
//
//def rate =new JsonSlurper().parseText(rateUrl.text)
//println rate

//assert rate instanceof Map
//println(rate.rates)
//rate.each {println(it)}

//assert rate.rates instanceof Map
//println rate.rates."USD"
//println rate.rates."GBP"

//assert rate.rates instanceof List
//println(rate.rates)

//assert rate instanceof List
//assert object.myList == [4, 8, 15, 16, 23, 42]