package com.jaroop.play.api.mvc

import play.api.libs.functional._
import play.api.mvc.QueryStringBindable
import scala.util.{ Either, Left, Right }

/** Embodies the unbinding part of `QueryStringBindable`. */
trait QueryStringUnbinder[A] {

    /**
     * Unbind a query string parameter.
     *
     * @param key Parameter key
     * @param value Parameter value.
     * @return a query string fragment containing the key and its value. E.g. "foo=42"
     */
    def unbind(key: String, value: A): String

}

object QueryStringUnbinder {

    /** If a `QueryStringBindable[A]` exists, we know how to construct a `QueryStringUnbinder[A]`. */
    implicit def fromQueryStringBindable[A : QueryStringBindable] = new QueryStringUnbinder[A] {
       def unbind(key: String, value: A): String = implicitly[QueryStringBindable[A]].unbind(key, value)
    }

    implicit def applicative = new Applicative[QueryStringUnbinder] {

        def apply[A, B](mf: QueryStringUnbinder[A => B], ma: QueryStringUnbinder[A]): QueryStringUnbinder[B] = ???

        def map[A, B](m: QueryStringUnbinder[A], f: A => B): QueryStringUnbinder[B] = ???

        def pure[A](a: A): QueryStringUnbinder[A] = ???

    }

    implicit def functor: Functor[QueryStringUnbinder] = new Functor[QueryStringUnbinder] {
        def fmap[A, B](m: QueryStringUnbinder[A], f: A => B): QueryStringUnbinder[B] = applicative.map(m, f)
    }

}
