package com.outr.arango.api.model

import io.circe.Json


case class GetAPIAqlfunctionRc400(error: Boolean,
                                  code: Option[Long] = None,
                                  errorMessage: Option[String] = None,
                                  errorNum: Option[Long] = None)