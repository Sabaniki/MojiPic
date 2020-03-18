package infrastructure.twitter

import play.api.mvc.{Request, WrappedRequest}
import twitter4j.auth.AccessToken

case class TwitterLoginRequest[A](
    sessionId: String,
    accessToken: Option[AccessToken],
    request: Request[A]
) extends WrappedRequest[A](request)
