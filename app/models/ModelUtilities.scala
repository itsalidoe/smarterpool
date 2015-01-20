package models

import java.lang.reflect.Field

trait ModelUtilities {

  protected def isNumeric(field: Field) = {
    List(
      "double",
      "int"
    ).contains(field.getType.getName)
  }

  protected def convertValue(field: Field, value: Any): String = {
    (if (isNumeric(field)) "%s" else "'%s'").format(value.toString)
  }
}
