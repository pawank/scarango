package com.outr.arango.api.model

import fabric._
import fabric.rw._

case class GeneralGraphGetHttpExamplesRc404(error: Boolean,
                                            code: Option[Int] = None,
                                            errorMessage: Option[String] = None,
                                            errorNum: Option[Int] = None)

object GeneralGraphGetHttpExamplesRc404 {
  implicit val rw: ReaderWriter[GeneralGraphGetHttpExamplesRc404] = ccRW
}