package com.jaroop.play.api.mvc

import play.api.libs.functional._
import play.api.mvc.QueryStringBindable
import scala.util.{ Either, Left, Right }

/** Embodies only the binding part of a `QueryStringBindable`. */
trait QueryStringBinder[A] {

    /**
     * Bind a query string parameter.
     *
     * @param key Parameter key
     * @param params QueryString data
     * @return `None` if the parameter was not present in the query string data. Otherwise, returns `Some` of either
     * `Right` of the parameter value, or `Left` of an error message if the binding failed.
     */
    def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]]

}

object QueryStringBinder {

    /** If a `QueryStringBindable[A]` exists, we know how to construct a `QueryStringBinder[A]`. */
    implicit def fromQueryStringBindable[A : QueryStringBindable] = new QueryStringBinder[A] {
        def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]] =
            implicitly[QueryStringBindable[A]].bind(key, params)
    }

    implicit def applicative = new Applicative[QueryStringBinder] {

        def apply[A, B](mf: QueryStringBinder[A => B], ma: QueryStringBinder[A]): QueryStringBinder[B] = new QueryStringBinder[B] {
            def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, B]] = {
                for(ef <- mf.bind(key, params); ea <- ma.bind(key, params)) yield {
                    for {
                        f <- ef.right
                        a <- ea.right
                    } yield f(a)
                }                
            }
        }

        def map[A, B](m: QueryStringBinder[A], f: A => B): QueryStringBinder[B] = new QueryStringBinder[B] {
            def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, B]] =
                m.bind(key, params).map(_.right.map(f))
        }

        def pure[A](a: A): QueryStringBinder[A] = new QueryStringBinder[A] {
            def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]] = Some(Right(a))
        }

    }

    implicit def functor = new Functor[QueryStringBinder] {
        def fmap[A, B](m: QueryStringBinder[A], f: A => B): QueryStringBinder[B] = applicative.map(m, f)
    }

}
