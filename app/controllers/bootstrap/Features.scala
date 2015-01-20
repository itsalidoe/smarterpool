package controllers.bootstrap

import play.api.templates.Html

class Features (val featurettes: List[Featurette]) {

  def Html: Html = {
    views.html.bootstrap.features(featurettes)
  }
}
