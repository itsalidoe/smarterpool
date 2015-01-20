package utilities

import play.api.Play
import play.api.Play.current
import play.api.libs.json.{Reads, Json}
import com.google.common.io.Files
import com.google.common.base.Charsets

object ContentReader {

  def ReadJson[T](filepath: String, default: T)(implicit fmt: Reads[T] = null): T = {
    Play.getExistingFile(filepath) map { file =>
      val jsonStr = Files.toString(file, Charsets.UTF_8).replaceAllLiterally("\r\n", "").replaceAllLiterally("\n", "")
      Json.fromJson[T](
        Json.parse(jsonStr)
      )(fmt) getOrElse default
    } getOrElse default
  }
}
