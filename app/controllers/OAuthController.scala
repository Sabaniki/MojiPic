package controllers

import infrastructure.twitter.{TwitterAuthenticator, TwitterException}
import javax.inject.Inject
import play.api.Configuration
import play.api.cache.SyncCacheApi
import play.api.mvc.ControllerComponents

class OAuthController @Inject()(
    configuration: Configuration,
    cc: ControllerComponents,
    twitterAuthenticator: TwitterAuthenticator,
    val cache: SyncCacheApi
) extends TwitterLoginController(cc) {
    val documentRootUrl: String = configuration.get[String]("mojipic.documentrooturl")

    def login = super.TwitterLoginAction { request =>
        try {
            val callbackUrl = documentRootUrl + routes.OAuthController.oauthCallback(None).url
            val authenticationUrl = twitterAuthenticator.startAuthentication(request.sessionId, callbackUrl)
            Redirect(authenticationUrl)
        }
        catch {
            case e: TwitterException => BadRequest(e.getMessage)
        }
    }

    def logout() = play.mvc.Results.TODO

    def oauthCallback(oauth_verifier: Option[String]) = play.mvc.Results.TODO
}
