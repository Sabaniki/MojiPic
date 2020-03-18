package controllers

import infrastructure.twitter.TwitterAuthenticator
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
    try {
    }
}
