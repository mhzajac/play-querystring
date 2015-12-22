package com.jaroop.play.api.mvc

import play.api.libs.functional._
import play.api.mvc.QueryStringBindable
import scala.util.{ Either, Left, Right }

package object syntax {

    implicit class BindSyntax(path: String) {

        def bind[A : QueryStringBinder]: QueryStringBinder[A] = new QueryStringBinder[A] {
            def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]] =
                implicitly[QueryStringBinder[A]].bind(s"${key}.${path}", params)
        }

    }

    implicit class UnbindSyntax(path: String) {

        def unbind[A : QueryStringUnbinder]: QueryStringUnbinder[A] = new QueryStringUnbinder[A] {
            def unbind(key: String, value: A): String =
                implicitly[QueryStringUnbinder[A]].unbind(s"${key}.${path}", value)
        }

    }

}

object QueryString {

    /** If both a `QueryStringBinder[A]` and `QueryStringUnbinder[A]` exist, we can construct a `QueryStringBindable[A]`.
     *  But for now we need to deal with potential diverging implicits.
     */
    implicit def qsb[A : QueryStringBinder : QueryStringUnbinder]: QueryStringBindable[A] = new QueryStringBindable[A] {

        def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, A]] =
            implicitly[QueryStringBinder[A]].bind(key, params)

        def unbind(key: String, value: A): String =
            implicitly[QueryStringUnbinder[A]].unbind(key, value)

    }

}
