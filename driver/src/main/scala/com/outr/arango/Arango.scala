package com.outr.arango

import com.outr.arango.rest.{AuthenticationRequest, AuthenticationResponse}
import com.typesafe.config.ConfigFactory
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.parser.decode
import io.youi.client.HttpClient
import io.youi.http.{Headers, HttpResponse, Method, StringContent}
import io.youi.net.URL

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Arango(baseURL: URL = Arango.defaultURL) {
  private var disposed: Boolean = false
  private val client = new HttpClient

  protected[arango] def defaultErrorHandler[Request, Response](request: Request): HttpResponse => Response = (response: HttpResponse) => {
    val content = response.content.get.asInstanceOf[StringContent].value
    decode[ArangoError](content) match {
      case Left(_) => throw new RuntimeException(s"Error from server: ${response.status} with content: ${response.content}")
      case Right(error) => throw new ArangoException(error, response.status.message, request)
    }
  }

  protected[arango] def restful[Request, Response](path: String,
                                                   request: Request,
                                                   token: Option[String],
                                                   params: Map[String, String] = Map.empty,
                                                   errorHandler: Option[HttpResponse => Response] = None,
                                                   method: Method = Method.Post)
                                                  (implicit encoder: Encoder[Request], decoder: Decoder[Response]): Future[Response] = {
    val headers = token.map(t => Headers.empty.withHeader(Headers.Request.Authorization(s"bearer $t"))).getOrElse(Headers.empty)
    val url = baseURL.withPath(path).withParams(params)
    client.restful[Request, Response](url, request, headers, errorHandler.getOrElse(defaultErrorHandler(request)), method)
  }

  protected[arango] def call[Response](path: String,
                                       method: Method,
                                       token: Option[String],
                                       params: Map[String, String] = Map.empty,
                                       errorHandler: Option[HttpResponse => Response] = None)
                                      (implicit decoder: Decoder[Response]): Future[Response] = {
    val headers = token.map(t => Headers.empty.withHeader(Headers.Request.Authorization(s"bearer $t"))).getOrElse(Headers.empty)
    val url = baseURL.withPath(path).withParams(params)
    client.call[Response](url, method, headers, errorHandler.getOrElse(defaultErrorHandler(path)))
  }

  def session(credentials: Option[Credentials] = Arango.defaultCredentials): Future[ArangoSession] = credentials match {
    case Some(Credentials(username, password)) => restful[AuthenticationRequest, AuthenticationResponse](
      path = "/_open/auth",
      request = AuthenticationRequest(username, password),
      token = None
    ).map { response =>
      new ArangoSession(this, Option(response.jwt))
    }
    case None => Future.successful(new ArangoSession(this, None))
  }

  def isDisposed: Boolean = disposed

  def dispose(): Unit = {
    client.dispose()
    disposed = true
  }
}

object Arango {
  private val config = ConfigFactory.load(getClass.getClassLoader)

  var defaultDatabase: String = config.getString("Arango.db")
  var defaultURL: URL = URL(config.getString("Arango.url"))
  var defaultAuthentication: Boolean = config.getBoolean("Arango.authentication")
  var defaultUsername: String = config.getString("Arango.username")
  var defaultPassword: String = config.getString("Arango.password")

  def defaultCredentials: Option[Credentials] = if (defaultAuthentication) {
    Some(Credentials(defaultUsername, defaultPassword))
  } else {
    None
  }
}