package com.outr.arango

import com.outr.arango.managed.{VertexCollection, PolymorphicVertexCollection, PolymorphicDocumentOption, PolymorphicType}

import scala.annotation.compileTimeOnly
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.concurrent.ExecutionContext.Implicits.global

@compileTimeOnly("Enable macro paradise to expand compile-time macros")
object Macros {
  def vertex[T <: DocumentOption](c: blackbox.Context)(name: c.Expr[String])(implicit t: c.WeakTypeTag[T]): c.Expr[VertexCollection[T]] = {
    import c.universe._

    val graph = c.prefix.tree
    val collection =
      q"""
         import io.circe.{Decoder, Encoder}
         import io.circe.generic.semiauto._
         import com.outr.arango.rest

         new VertexCollection[$t]($graph, $name) {
           override protected implicit val encoder: Encoder[$t] = deriveEncoder[$t]
           override protected implicit val decoder: Decoder[$t] = deriveDecoder[$t]
           override protected def updateDocument(document: $t, info: rest.CreateInfo): $t = {
             document.copy(_key = Option(info._key), _id = Option(info._id), _rev = Option(info._rev))
           }
         }
       """
    c.Expr[VertexCollection[T]](collection)
  }

  def polymorphicType[T <: PolymorphicDocumentOption, P <: T](c: blackbox.Context)(value: c.Expr[String])(implicit t: c.WeakTypeTag[T], p: c.WeakTypeTag[P]): c.Expr[PolymorphicType[T]] = {
    import c.universe._

    val instance =
      q"""
       import io.circe.{Decoder, Encoder}
       import io.circe.generic.semiauto._

       new PolymorphicType[$p] {
         override def value = $value
         override def encoder = deriveEncoder[$p]
         override def decoder = deriveDecoder[$p]
         override def updateDocument(document: $p, info: rest.CreateInfo) = {
           document.copy(_key = Option(info._key), _id = Option(info._id), _rev = Option(info._rev))
         }
       }.asInstanceOf[PolymorphicType[$t]]
     """
    c.Expr[PolymorphicType[T]](instance)
  }

  def polymorphic2[T <: PolymorphicDocumentOption, P1 <: T, P2 <: T](c: blackbox.Context)
                                                                    (name: c.Expr[String])
                                                                    (implicit t: c.WeakTypeTag[T],
                                                                              p1: c.WeakTypeTag[P1],
                                                                              p2: c.WeakTypeTag[P2]): c.Expr[PolymorphicVertexCollection[T]] = {
    import c.universe._

    val graph = c.prefix.tree

    def tag2Name[C](tag: c.WeakTypeTag[C]): String = {
      var s = tag.tpe.toString
      val index = s.lastIndexOf('.')
      if (index != -1) {
        s = s.substring(index + 1)
      }
      s.charAt(0).toLower + s.substring(1)
    }

    val p1Name = tag2Name(p1)
    val p2Name = tag2Name(p2)
    val p1Type = polymorphicType[T, P1](c)(c.Expr[String](q"$p1Name"))
    val p2Type = polymorphicType[T, P2](c)(c.Expr[String](q"$p2Name"))
    val instance =
      q"""
         new PolymorphicVertexCollection[$t]($graph, $name, List($p1Type, $p2Type))
       """
    c.Expr[PolymorphicVertexCollection[T]](instance)
  }

  def polymorphic3[T <: PolymorphicDocumentOption, P1 <: T, P2 <: T, P3 <: T](c: blackbox.Context)
                                                                             (name: c.Expr[String])
                                                                             (implicit t: c.WeakTypeTag[T],
                                                                              p1: c.WeakTypeTag[P1],
                                                                              p2: c.WeakTypeTag[P2],
                                                                              p3: c.WeakTypeTag[P3]): c.Expr[PolymorphicVertexCollection[T]] = {
    import c.universe._

    val graph = c.prefix.tree

    def tag2Name[C](tag: c.WeakTypeTag[C]): String = {
      var s = tag.tpe.toString
      val index = s.lastIndexOf('.')
      if (index != -1) {
        s = s.substring(index + 1)
      }
      s.charAt(0).toLower + s.substring(1)
    }

    val p1Name = tag2Name(p1)
    val p2Name = tag2Name(p2)
    val p3Name = tag2Name(p3)
    val p1Type = polymorphicType[T, P1](c)(c.Expr[String](q"$p1Name"))
    val p2Type = polymorphicType[T, P2](c)(c.Expr[String](q"$p2Name"))
    val p3Type = polymorphicType[T, P3](c)(c.Expr[String](q"$p3Name"))
    val instance =
      q"""
         new PolymorphicVertexCollection[$t]($graph, $name, List($p1Type, $p2Type, $p3Type))
       """
    c.Expr[PolymorphicVertexCollection[T]](instance)
  }

  def polymorphic4[T <: PolymorphicDocumentOption, P1 <: T, P2 <: T, P3 <: T, P4 <: T](c: blackbox.Context)
                                                                                      (name: c.Expr[String])
                                                                                      (implicit t: c.WeakTypeTag[T],
                                                                                                p1: c.WeakTypeTag[P1],
                                                                                                p2: c.WeakTypeTag[P2],
                                                                                                p3: c.WeakTypeTag[P3],
                                                                                                p4: c.WeakTypeTag[P4]): c.Expr[PolymorphicVertexCollection[T]] = {
    import c.universe._

    val graph = c.prefix.tree

    def tag2Name[C](tag: c.WeakTypeTag[C]): String = {
      var s = tag.tpe.toString
      val index = s.lastIndexOf('.')
      if (index != -1) {
        s = s.substring(index + 1)
      }
      s.charAt(0).toLower + s.substring(1)
    }

    val p1Name = tag2Name(p1)
    val p2Name = tag2Name(p2)
    val p3Name = tag2Name(p3)
    val p4Name = tag2Name(p4)
    val p1Type = polymorphicType[T, P1](c)(c.Expr[String](q"$p1Name"))
    val p2Type = polymorphicType[T, P2](c)(c.Expr[String](q"$p2Name"))
    val p3Type = polymorphicType[T, P3](c)(c.Expr[String](q"$p3Name"))
    val p4Type = polymorphicType[T, P4](c)(c.Expr[String](q"$p4Name"))
    val instance =
      q"""
         new PolymorphicVertexCollection[$t]($graph, $name, List($p1Type, $p2Type, $p3Type, $p4Type))
       """
    c.Expr[PolymorphicVertexCollection[T]](instance)
  }

  def aql(c: blackbox.Context)(args: c.Expr[Any]*): c.Expr[Query] = {
    import c.universe._

    c.prefix.tree match {
      case Apply(_, List(Apply(_, rawParts))) => {
        val parts = rawParts map { case t @ Literal(Constant(const: String)) => (const, t.pos) }

        val b = new StringBuilder
        parts.zipWithIndex.foreach {
          case ((raw, _), index) => {
            if (index > 0) {
              b.append(s"@arg$index")
            }
            b.append(raw)
          }
        }
        val argsMap = args.zipWithIndex.map {
          case (value, index) => {
            val vt = value.actualType
            val queryArg = if (vt <:< typeOf[String]) {
              c.Expr[QueryArg](q"com.outr.arango.QueryArg.string($value)")
            } else if (vt <:< typeOf[Int]) {
              c.Expr[QueryArg](q"com.outr.arango.QueryArg.int($value)")
            } else {
              c.abort(c.enclosingPosition, s"Unsupported QueryArg: $vt.")
            }
            s"arg${index + 1}" -> queryArg
          }
        }.toMap

        val query = b.toString().trim
        val future = ArangoSession.default.flatMap { session =>
          val result = session.parse(query)
          result.onComplete { _ =>
            session.instance.dispose()
          }
          result
        }

        val result = Await.result(future, 30.seconds)
        if (result.error) {
          c.abort(c.enclosingPosition, s"Error: ${result.errorMessage.get} (${result.errorCode}). Bad syntax for AQL query: $query.")
        }
        c.Expr[Query](q"""com.outr.arango.Query($query, $argsMap)""")
      }
      case _ => c.abort(c.enclosingPosition, "Bad usage of cypher interpolation.")
    }
  }
}