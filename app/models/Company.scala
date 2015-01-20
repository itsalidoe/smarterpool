package models

import java.lang.reflect.Field

class Company(
               var id: Int = 0,
               var timestamp: Int = 0,
               var companyName: String = "",
               var email: String = "")
  extends Model[Company] {

  def tableName = "Company"
  def instantiate = new Company()
  def getFields: Array[Field] = classOf[Company].getDeclaredFields
}
