import java.net.URLEncoder
import java.io.File

import scala.io.Source._

object Main {
  val pattern = """data-context-item-id="(.{11})"""".r
  val destDir = "songs"
  def main(args: Array[String]) {
    for(query <- fromFile("SongList.txt").getLines()) {
      print(s"$query: ")
      pattern.findFirstMatchIn(fromURL(s"https://www.youtube.com/results?search_query=${URLEncoder.encode(query, "UTF-8")}").mkString) match {
        case Some(id) =>
          val vidLink = s"https://www.youtube.com/watch?v=${id.group(1)}"
          val fetchLink = s"http://youtubeinmp3.com/fetch/?video=$vidLink"
          println(s"$vidLink, $fetchLink")
          download(fetchLink, "songs/" + query + ".mp3")
        case None => println(s"$query: No match")
      }
    }
  }

  def download(url: String, file: String): Unit = {
    val input = new File(file)
    HttpRequest.post(url).receive(input)
  }
}