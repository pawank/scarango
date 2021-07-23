package com.outr.arango.api.model

import fabric._
import fabric.rw._

case class GeneralGraphVertexReplaceHttpExamplesRc200(error: Boolean,
                                                      code: Option[Int] = None,
                                                      `new`: Option[VertexRepresentation] = None,
                                                      old: Option[VertexRepresentation] = None,
                                                      vertex: Option[VertexRepresentation] = None)

object GeneralGraphVertexReplaceHttpExamplesRc200 {
  implicit val rw: ReaderWriter[GeneralGraphVertexReplaceHttpExamplesRc200] = ccRW
}