package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import models._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ModelSpec extends Specification {
  
  "Identifier" should {
    
    "Create valid SQL statements" in {

      "Numeric values" in {
        val identifier = new Identifier("id", "=", 1)
        identifier.toString must equalTo("id=1")
      }

      "Non-Numeric values" in {
        val identifier = new Identifier("id", "=", "1")
        identifier.toString must equalTo("id='1'")
      }

      "And connectors" in {
        val identifier = new Identifier("id", "=", "1").and("name", "=", 1)
        identifier.toString must equalTo("id='1' AND (name=1)")

        val compoundIdentifier =  new Identifier("locale", "=", "EN").and(identifier)
        compoundIdentifier.toString must equalTo("locale='EN' AND (id='1' AND (name=1))")
      }

      "Or connectors" in {
        val identifier = new Identifier("id", "=", "1").or("name", "=", 1)
        identifier.toString must equalTo("id='1' OR (name=1)")

        val compoundIdentifier =  new Identifier("locale", "=", "EN").or(identifier)
        compoundIdentifier.toString must equalTo("locale='EN' OR (id='1' OR (name=1))")
      }

    }
  }
}