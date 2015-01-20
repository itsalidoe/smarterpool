package controllers

import play.api.mvc._
import controllers.bootstrap.{Features, Marketing, Carousel, Featurette}
import models.Mentor

object Mentors extends MarketingPage {

  def index = Action {
    val carousel = Carousel("conf/content/mentors/CarouselFeatures")
    val marketing = Marketing("conf/content/mentors/MarketingFeatures", 2)
    val largeMarketing = LargeMarketing("conf/content/mentors/LargeMarketingFeatures")

    Ok(views.html.base("Mentors.")(carousel)(marketing += largeMarketing += views.html.signup.mentor_signup.apply())("mentors"))
  }

  def signUp = Action { request =>
    val email = request.body.asFormUrlEncoded.get("email") mkString ""
    var mentor =  new Mentor(email = email)
    var success = mentor.write()
    if (success == None) {
      controllers.Status.failure
    } else {
      controllers.Status.success
    }
  }
}
