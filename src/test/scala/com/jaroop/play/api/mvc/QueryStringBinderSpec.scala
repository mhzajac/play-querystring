
import com.jaroop.play.api.mvc._, syntax._
import org.specs2.mutable._
import play.api.mvc.QueryStringBindable
import play.api.libs.functional.syntax._
import scala.util.{ Either, Left, Right }

case class SubFilter(ascending: Boolean, orderBy: String, ids: List[Long])

object SubFilter {

    implicit val binder: QueryStringBinder[SubFilter] = (
        "ascending".bind[Boolean] and
        "orderBy".bind[String] and
        "ids".bind[List[Long]]
    )(SubFilter.apply _)

}

case class TestFilter(filterText: String, startIndex: Int, sub: SubFilter, none: Option[String], some: Option[String])

object TestFilter {

    implicit val binder: QueryStringBinder[TestFilter] = (
        "filterText".bind[String] and
        "startIndex".bind[Int] and
        "sub".bind[SubFilter] and
        "none".bind[Option[String]] and
        "some".bind[Option[String]]
    )(TestFilter.apply _)

}

object QueryStringBinderSpec extends Specification {

    "QueryStringBinder" should {

        "prove to Jonald that this is possible" in {
            val qs = Map(
                "filter.filterText" -> Seq("blah"),
                "filter.startIndex" -> Seq("55"),
                "filter.sub.ascending" -> Seq("1"),
                "filter.sub.orderBy" -> Seq("red"),
                "filter.sub.ids" -> Seq("1", "2", "10"),
                "filter.some" -> Seq("!")
            )

            val sub = SubFilter(true, "red", List(1L, 2L, 10L))
            val filter = TestFilter("blah", 55, sub, None, Some("!"))

            TestFilter.binder.bind("filter", qs) must equalTo(Some(Right(filter)))
        }

    }

}
