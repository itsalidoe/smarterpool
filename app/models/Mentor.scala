package models

import java.lang.reflect.Field

class Mentor(
               var id: Int = 0,
               var timestamp: Int = 0,
               var email: String = "",
               var linkedinId: String = "")
  extends Model[Mentor] {

  def tableName = "Mentor"
  def instantiate = new Mentor()
  def getFields: Array[Field] = classOf[Mentor].getDeclaredFields
}
