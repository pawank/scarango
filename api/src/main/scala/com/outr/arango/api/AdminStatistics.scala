package com.outr.arango.api

import com.outr.arango.api.model._
import io.youi.client.HttpClient
import io.youi.http.HttpMethod
import io.youi.net._
import io.circe.Json
import scala.concurrent.{ExecutionContext, Future}
      
object AdminStatistics {

  def get(client: HttpClient)(implicit ec: ExecutionContext): Future[GetAdminStatisticsRc200] = client
    .method(HttpMethod.Get)
    .path(path"/_admin/statistics", append = true) 
    .call[GetAdminStatisticsRc200]
}