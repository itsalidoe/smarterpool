package controllers

import play.api.mvc._
import controllers.bootstrap.{Features, Marketing, Carousel, Featurette}
import models.Company
import utilities.ContentReader
import play.api.libs.json.Json
import scala.collection.immutable.HashMap

object Companies extends MarketingPage {

  def index = Action {
    val carousel = Carousel("conf/content/companies/CarouselFeatures")
    val marketing = Marketing("conf/content/companies/MarketingFeatures", 2)
    val largeMarketing = LargeMarketing("conf/content/companies/LargeMarketingFeatures")

    Ok(views.html.base("Companies.")(carousel)(views.html.companies.apply() += marketing += largeMarketing += views.html.signup.company_signup.apply())("companies"))
  }

  def signUp = Action { request =>
    val companyName = request.body.asFormUrlEncoded.get("company") mkString ""
    val email = request.body.asFormUrlEncoded.get("email") mkString ""
    var company =  new Company(companyName = companyName, email = email)
    var success = company.write()
    if (success == None) {
      controllers.Status.failure
    } else {
      controllers.Status.success
    }
  }
}