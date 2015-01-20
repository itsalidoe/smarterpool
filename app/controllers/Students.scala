package controllers

import play.api.mvc._
import controllers.bootstrap.{Features, Marketing, Carousel, Featurette}
import models.Student;
import org.mindrot.jbcrypt.BCrypt
import utilities.ContentReader
;

object Students extends MarketingPage {

  def index = Action {
    val carousel = Carousel("conf/content/students/CarouselFeatures")
    val marketing = Marketing("conf/content/students/MarketingFeatures", 2)
    val howItWorksMarketing = Marketing("conf/content/students/HowItWorksFeatures", 3)
    val largeMarketing = LargeMarketing("conf/content/students/LargeMarketingFeatures")
    val universities = ContentReader.ReadJson[List[String]]("conf/content/students/Universities", List.empty)
    val majors = ContentReader.ReadJson[List[String]]("conf/content/students/Majors", List.empty)

    Ok(views.html.base("Students.")(carousel)(marketing += howItWorksMarketing += largeMarketing += views.html.signup.student_signup(universities, majors))("students"))
  }

  def signUp = Action { request =>
    val fullName = request.body.asFormUrlEncoded.get("fullName") mkString ""
    val password = request.body.asFormUrlEncoded.get("password") mkString ""
    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
    val school = request.body.asFormUrlEncoded.get("school") mkString ""
    val email = request.body.asFormUrlEncoded.get("email") mkString ""
    val major = request.body.asFormUrlEncoded.get("major") mkString ""
    var student =  new Student(fullName = fullName, password = hashedPassword, school = school, email = email, major  = major)
    var success = student.write()
    if (success == None) {
      controllers.Status.failure
    } else {
      controllers.Status.success
    }
  }
}
