import Functions._

object Parallel {
  def main(args: Array[String]): Unit = {
    time("helloworld"){ helloworld1}
    val countScala = countOccurrences("scala")(_)
    time("count"){ countScala("scala is the best language for scalability, says the creator of scala :)")}
    val bigFile = time("makeFile"){ makeBigFile }
    val smallFile = time("makeSmall"){ makeSmallFile }

    val example1 = (file: java.io.File, parallel: Boolean) => mapRedFile(file)(x=> (1, Math.sqrt(Math.pow(1.1, x.toInt))))((x1,x2)=>(x1._1+x2._1, x1._2+x2._2))(parallel)

    time("applyToFile Small\tConcurrent"){
      val (total, sum) = example1(smallFile, false)
      println("Average: " + sum/total)
    }
    time("applyToFile Small\tParallel"){
      val (total, sum) = example1(smallFile, true)
      println("Average: " + sum/total)
    }
    time("applyToFile Big\tConcurrent"){
      val (total, sum) = example1(bigFile, false)
      println("Average: " + sum/total)
    }
    time("applyToFile Big\tParallel"){
      val (total, sum) = example1(bigFile, true)
      println("Average: " + sum/total)
    }
  }
}
