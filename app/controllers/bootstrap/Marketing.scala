package controllers.bootstrap

import play.api.templates.Html

class Marketing (val featurettes: List[Featurette], minimum: Int = 4) {

  def Html: Html = {
    val spanClass = "span" + (12/featurettes.count(_ => true).min(minimum)).toString
    views.html.bootstrap.marketing(featurettes)(spanClass)
  }
}
