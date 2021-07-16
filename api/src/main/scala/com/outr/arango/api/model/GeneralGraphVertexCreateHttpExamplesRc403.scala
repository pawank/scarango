package com.outr.arango.api.model

import fabric._
import fabric.rw._

case class GeneralGraphVertexCreateHttpExamplesRc403(error: Boolean,
                                                     code: Option[Int] = None,
                                                     errorMessage: Option[String] = None,
                                                     errorNum: Option[Int] = None)

object GeneralGraphVertexCreateHttpExamplesRc403 {
  implicit val rw: ReaderWriter[GeneralGraphVertexCreateHttpExamplesRc403] = ccRW
}