package controllers

import controllers.general.Question
import play.api.libs.json._
import play.api.mvc._
import play.api.templates.Html
import utilities.ContentReader

object General extends MarketingPage {

  def faq = Action {
    implicit val format = Json.format[Question]
    val faq = ContentReader.ReadJson[List[Question]]("conf/content/general/FAQ", List.empty)
    Ok(views.html.base("companies.")(new Html(new StringBuilder))(views.html.general.faq(faq))("faq"))
  }

  def contact = Action {
    val marketing = LargeMarketing("conf/content/general/contact/LargeMarketingFeatures")
    Ok(views.html.base("companies.")(new Html(new StringBuilder))(views.html.general.contact() += marketing)("contact"))
  }
}
