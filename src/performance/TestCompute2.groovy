package performance

/**
 * Created by alex on 30.5.17.
 */

performance.Computer computer=new performance.Computer()
computer.test()

Computer.metaClass.compute = { String str -> new Date() }
