package controllers

import java.util.UUID

import infrastructure.twitter.TwitterLoginRequest
import play.api.cache.SyncCacheApi
import play.api.mvc.{AbstractController, ActionBuilder, AnyContent, BodyParser, ControllerComponents, Cookie, Request, Result}
import twitter4j.auth.AccessToken

import scala.concurrent.{ExecutionContext, Future}

abstract class TwitterLoginController(protected val cc: ControllerComponents) extends AbstractController(cc) {
    val cache: SyncCacheApi
    val sessionIdName = "mojipic.sessionId"

    def TwitterLoginAction: ActionBuilder[TwitterLoginRequest, AnyContent] = new ActionBuilder[TwitterLoginRequest, AnyContent] {
        override def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

        override def invokeBlock[A](request: Request[A], block: TwitterLoginRequest[A] => Future[Result]): Future[Result] = {
            val sessionIdOpt = request.cookies.get(sessionIdName).map(_.value)
            val accessToken = sessionIdOpt.flatMap(cache.get[AccessToken])
            val sessionId = sessionIdOpt.getOrElse(UUID.randomUUID().toString)
            val result = block(TwitterLoginRequest(sessionId, accessToken, request))
            implicit val executionContext: ExecutionContext = cc.executionContext
            result.map(_.withCookies(Cookie(sessionIdName, sessionId, Some(30 * 60))))
        }

        override protected def executionContext: ExecutionContext = cc.executionContext
    }
}
