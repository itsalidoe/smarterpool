package models

import java.lang.reflect.Field

class Student(
               var id: Int = 0,
               var timestamp: Int = 0,
               var fullName: String = "",
               var password: String = "",
               var school: String = "",
               var email: String = "",
               var major: String = "",
               var linkedinId: String = "")
  extends Model[Student] {

  def tableName = "Student"
  def instantiate = new Student()
  def getFields: Array[Field] = classOf[Student].getDeclaredFields
}
