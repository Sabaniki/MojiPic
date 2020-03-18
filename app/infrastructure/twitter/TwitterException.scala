package infrastructure.twitter

case class TwitterException(str: String = null, throwable: Throwable = null) extends RuntimeException(str, throwable)
