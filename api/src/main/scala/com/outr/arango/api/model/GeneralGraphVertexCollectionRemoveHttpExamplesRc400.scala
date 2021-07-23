package com.outr.arango.api.model

import fabric._
import fabric.rw._

case class GeneralGraphVertexCollectionRemoveHttpExamplesRc400(error: Boolean,
                                                               code: Option[Int] = None,
                                                               errorMessage: Option[String] = None,
                                                               errorNum: Option[Int] = None)

object GeneralGraphVertexCollectionRemoveHttpExamplesRc400 {
  implicit val rw: ReaderWriter[GeneralGraphVertexCollectionRemoveHttpExamplesRc400] = ccRW
}