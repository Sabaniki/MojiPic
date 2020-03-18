package infrastructure.twitter

import javax.inject.Inject
import play.api.Configuration
import play.api.cache.SyncCacheApi
import twitter4j.TwitterFactory

import scala.concurrent.duration._
import scala.util.control.NonFatal

class TwitterAuthenticator @Inject()(configuration: Configuration, cache: SyncCacheApi) {
    val CacheKeyPrefixTwitter = "twitterInstance"

    val ConsumerKey: String = configuration.get[String]("mojipic.consumerkey")
    val ConsumerSecret: String = configuration.get[String]("mojipic.consumersecret")

    private[this] def cacheKeyTwitter(sessionId: String): String = CacheKeyPrefixTwitter + sessionId

    /**
     * Twitterの認証を開始する
     *
     * @param sessionId   Twitterの認証をしたセッションID
     * @param callbackUrl コールバックURL
     * @return 投稿者に認証してもらうためのURL
     * @throws TwitterException 　何らかの理由でTwitterの認証を開始できなかった
     */
    def startAuthentication(sessionId: String, callbackUrl: String): String = {
        try {
            val twitter = new TwitterFactory().getInstance()
            twitter.setOAuthConsumer(ConsumerKey, ConsumerSecret)
            val requestToken = twitter.getOAuthRequestToken(callbackUrl)
            cache.set(cacheKeyTwitter(sessionId), twitter, 30.seconds)
            requestToken.getAuthenticationURL
        }
        catch {
            case NonFatal(e) => throw TwitterException(s"Could not get a request token. SessionId: $sessionId", e)
        }
    }
}

