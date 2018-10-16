import java.io._
import scala.io.Source
import scala.util.Random

object Functions {
  def helloworld1: Unit = {
    println("Hello, functions world!")
  }

  def splitLine(line: String) = line.split(" ")

  def countOccurrences(target: String)(line: String) = {
    splitLine(line).map(_ == target).foldLeft(0)((acc, n) => if (n) acc+1 else acc)
  }

  def time[R](block: => R): R = {
    time("{}")(block)
  }

  def time[R](name: String)(block: => R): R = {
    val formatter = java.text.NumberFormat.getIntegerInstance
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time for " + name +": " + formatter.format((t1 - t0)) + "ns. Result: "+result)
    result
  }

  def makeFile(size: Integer, name: String): File = {

    val file = new File(name)
    if (file.exists && file.length >= size) return file
    val bw = new BufferedWriter(new FileWriter(file))

    for (
      i <- Stream.continually(Random.nextInt(100)).takeWhile(x => file.length < size)
    ) {
      val r = scala.util.Random
      bw.write(i.toString())
      bw.write("\n")
    }
    bw.close()
    return file
  }

  def makeBigFile() = {
    val size = 1024*1024*100
    makeFile(size, "BigFile.txt")
  }

  def makeSmallFile() = {
    val size = 1024*1024
    makeFile(size, "SmallFile.txt")
  }

  def applyToFile[R](file: File)(block: String=>R)(parallel: Boolean): Iterator[R] = {
    if (parallel) {
      Source.fromFile(file).getLines().grouped(1000).flatMap({ y=>
        val z = if (parallel) y.toIndexedSeq.par else y;
        z.map(x=>block(x))})
    } else {
      Source.fromFile(file).getLines().map(x=>block(x))
    }
  }

  def mapRedFile[R](file: File)(mapper: String=>R)(reducer: (R,R)=>R)(parallel: Boolean): R = {
    if (parallel)
      (applyToFile(file)(mapper)(parallel)).grouped(1000).map(x=>x.toIndexedSeq.par.reduce(reducer)).reduce(reducer)
    else
      (applyToFile(file)(mapper)(parallel)).reduce(reducer)
  }

}
