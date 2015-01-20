package models

import java.lang.reflect.Field
import anorm._
import java.util.HashMap
import play.api.db.DB
import play.api.Play.current

abstract class Model[T]() extends ModelUtilities {

  def getFields: Array[Field]

  def instantiate: T

  def tableName: String

  def fields: String = this.getFields.map(field => field.getName()) mkString (",")

  def getField(key: String): Field = {
    getFields.filter(field => field.getName == key)(0)
  }

  def read(where: String, equality: String, value: Any, extra: String = ""): List[T] = {
    read(new Identifier(where, equality, value, extra))
  }

  def read(id: Identifier): List[T] = {
    val query = DB.withConnection( implicit c => {
      SQL("SELECT %s FROM %s WHERE %s".format(fields, tableName, id.toString))()
    })
    query.toList.map { row =>
      val newT = instantiate
      row.asMap map { column =>
        newT.getClass.getMethods.find(_.getName == column._1 + "_$eq").get.invoke(newT, column._2.asInstanceOf[AnyRef])
      }
      newT
    }
  }

  def delete(id: Identifier): Int = {
    DB.withConnection( implicit c => {
      SQL("DELETE FROM %s WHERE %s".format(tableName, getIdentification)).executeUpdate()
    })
  }

  def write(): Option[Long] = {
    DB.withConnection( implicit c => {
      SQL("INSERT INTO %s(%s) VALUES(%s)".format(tableName, nonEmptyFieldNames, nonEmptyFieldValues)).executeInsert[Option[Long]]()
    })
  }

  def update(value: String, key: Any): Int = {
    var hashmap = new HashMap[String, Any]()
    hashmap.put(value, key)
    update(hashmap)
  }

  def update(values: HashMap[String, Any]): Int = {
    val newValues = (values.keySet().toArray() map { key =>
      "%s=%s".format(key, convertValue(getField(key.toString), values.get(key)))
    }).mkString(",")

    DB.withConnection( implicit c => {
      SQL("UPDATE %s SET %s WHERE %s".format(tableName, newValues, getIdentification)).executeUpdate()
    })
  }

  def nonEmptyFieldNames: String = {
   nonEmptyFields.map { _.getName } mkString(",")
  }

  def nonEmptyFieldValues: String = {
    nonEmptyFields.map (field => {
      convertValue(field, field.get(this))
    }) mkString(",")
  }

  def getIdentification(): String = {
    this.nonEmptyFields.map (field => {
      "%s=%s".format(field.getName, convertValue(field, field.get(this)))
    }) mkString(" AND ")
  }

  private def nonEmptyFields: Array[Field] = {
    this.getFields.filter(field => {
      field.setAccessible(true)
      !Array(0, "").contains(field.get(this))
    })
  }
}

case class Identifier(where: String, equality: String, value: Any, extra: String = "") {

  val strVal = (value match {
      case number: java.lang.Number => "%s%s%s%s"
      case _ => "%s%s'%s'%s"
  }).format(where, equality, value, extra);

  override def toString = strVal;

  def and(where: String, equality: String, value: Any, extra: String = ""): Identifier = {
    and(new Identifier(where, equality, value, extra))
  }

  def or(where: String, equality: String, value: Any, extra: String = ""): Identifier = {
    or(new Identifier(where, equality, value, extra))
  }

  def and(id: Identifier): Identifier = {
    new Identifier(where, equality, value, " AND (%s)".format(id.toString))
  }

  def or(id: Identifier): Identifier = {
    new Identifier(where, equality, value, " OR (%s)".format(id.toString))
  }
}
