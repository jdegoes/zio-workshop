// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package applications

import scalaz.zio._
import scalaz.zio.blocking.Blocking
import scalaz.zio.console.{Console, putStrLn}
import scalaz.zio.duration.Duration
import scalaz.zio.random.Random

object circuit_breaker extends App {

  /**
   * implement a circuit breaker mechanism
   */
  trait CircuitBreaker {

  }

  object CircuitBreaker {
    sealed trait State
    final case class Closed(maxFailure: Int)                      extends State
    final case class Open(startAt: Duration, openUntil: Duration) extends State
    final case class HalfOpen(retryUntil: Duration)               extends State

  }

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = ???
}

object hangman extends App {

  /**
   * Create a hangman game that requires the capability to perform `Console` and `Random` effects.
   */
  def myGame: ZIO[Console with Random, Nothing, Unit] = ???

  case class State(name: String, guesses: Set[Char], word: String) {
    def failures: Int = (guesses -- word.toSet).size

    def playerLost: Boolean = failures > 10

    def playerWon: Boolean = (word.toSet -- guesses).isEmpty
  }

  def gameLoop(state: State): ZIO[Console, Nothing, State] = ???

  def renderState(state: State): ZIO[Console, Nothing, Unit] = {

    /**
     *
     *  f     n  c  t  o
     *  -  -  -  -  -  -  -
     *
     *  Guesses: a, z, y, x
     *
     */
    val word =
      state.word.toList.map(c => if (state.guesses.contains(c)) s" $c " else "   ").mkString("")

    val line = List.fill(state.word.length)(" - ").mkString("")

    val guesses = " Guesses: " + state.guesses.mkString(", ")

    val text = word + "\n" + line + "\n\n" + guesses + "\n"

    putStrLn(text)
  }

  def getChoice: ZIO[Console, Nothing, Char] = ???

  def getName: ZIO[Console, Nothing, String] = ???

  def chooseWord: ZIO[Random, Nothing, String] = ???

  val Dictionary = List("aaron", "abelian", "ability", "about", "abstract", "abstract", "abstraction", "accurately", "adamek", "add", "adjacent", "adjoint", "adjunction", "adjunctions", "after", "after", "again", "ahrens", "albeit", "algebra", "algebra", "algebraic", "all", "all", "allegories", "almost", "already", "also", "american", "among", "amount", "ams", "an", "an", "analysis", "analytic", "and", "and", "andre", "any", "anyone", "apart", "apologetic", "appears", "applicability", "applications", "applications", "applied", "apply", "applying", "applying", "approach", "archetypical", "archetypical", "are", "areas", "argument", "arising", "aristotle", "arrowsmorphism", "article", "arxiv13026946", "arxiv13030584", "as", "as", "aspect", "assumed", "at", "attempts", "audience", "august", "awodey", "axiom", "axiomatic", "axiomatized", "axioms", "back", "barr", "barry", "basic", "basic", "be", "beginners", "beginning", "behind", "being", "benedikt", "benjamin", "best", "better", "between", "bicategories", "binary", "bodo", "book", "borceux", "both", "both", "bourbaki", "bowdoin", "brash", "brendan", "build", "built", "but", "but", "by", "called", "cambridge", "can", "cardinal", "carlos", "carnap", "case", "cases", "categorial", "categorical", "categorical", "categories", "categories", "categorification", "categorize", "category", "category", "cats", "catsters", "central", "certain", "changes", "charles", "cheng", "chicago", "chiefly", "chopin", "chris", "cite", "clash", "classes", "classical", "closed", "coend", "coin", "colimit", "colin", "collection", "collections", "comparing", "completion", "composed", "composition", "computational", "computer", "computing", "concept", "concepts", "concepts", "conceptual", "concrete", "confronted", "consideration", "considers", "consistently", "construction", "constructions", "content", "contents", "context", "context", "contexts", "continues", "continuous", "contrast", "contributed", "contributions", "cooper", "correctness", "costas", "count", "course", "cover", "covering", "current", "currently", "david", "decategorification", "deducing", "define", "defined", "defining", "definition", "definitions", "der", "derives", "described", "describing", "description", "descriptions", "detailed", "development", "dictum", "did", "different", "dimensions", "directed", "discovered", "discovery", "discuss", "discussed", "discussion", "discussion", "disparage", "disservice", "do", "does", "driving", "drossos", "duality", "dvi", "each", "easy", "ed", "edges", "edit", "edition", "eilenberg", "eilenbergmaclane", "elementary", "elementary", "elements", "elementwise", "elephant", "ellis", "else", "embedding", "embodiment", "embryonic", "emily", "end", "enthusiastic", "equations", "equivalence", "equivalences", "equivalences", "etc", "etcs", "eugenia", "even", "eventually", "everything", "evident", "example", "examples", "examples", "except", "excused", "exist", "exists", "exposure", "expressed", "expressiveness", "extension", "extra", "f", "fact", "fair", "families", "far", "feeds", "feeling", "finds", "finite", "first", "flourished", "focuses", "folklore", "follows", "fong", "for", "for", "force", "forced", "foremost", "form", "formalizes", "formulated", "forthcoming", "found", "foundation", "foundations", "foundations", "francis", "free", "freyd", "freydmitchell", "from", "functions", "functor", "functor", "functors", "fundamental", "further", "gabrielulmer", "general", "general", "generalized", "generalizes", "geometry", "geometry", "george", "geroch", "get", "gift", "give", "given", "going", "goldblatt", "grandis", "graph", "gray", "grothendieck", "ground", "group", "groupoid", "grp", "guide", "göttingen", "had", "handbook", "handful", "handle", "harper", "has", "have", "he", "here", "here", "herrlich", "higher", "higher", "higherdimensional", "highlevel", "hilberts", "his", "historical", "historically", "history", "history", "holistic", "holland", "home", "homomorphisms", "homotopy", "homotopy", "horizontal", "horst", "however", "i", "idea", "ideas", "ieke", "if", "if", "illustrated", "important", "in", "in", "inaccessible", "inadmissible", "include", "includes", "including", "indeed", "indexes", "infinite", "informal", "initial", "innocent", "instance", "instead", "instiki", "interacting", "internal", "intersection", "into", "introduce", "introduced", "introduces", "introducing", "introduction", "introduction", "introductory", "intuitions", "invitation", "is", "isbell", "isbn", "isomorphisms", "it", "it", "its", "itself", "ive", "j", "jaap", "jacob", "jiri", "johnstone", "joy", "jstor", "just", "kan", "kant", "kapulkin", "kashiwara", "kind", "kinds", "kleins", "kmorphisms", "ktransfors", "kℕ", "la", "lagatta", "lane", "language", "large", "last", "later", "later", "latest", "lauda", "lawvere", "lawveres", "lead", "leads", "least", "lectures", "led", "leinster", "lemma", "lemmas", "level", "library", "lifting", "likewise", "limit", "limits", "link", "linked", "links", "list", "literally", "logic", "logic", "logically", "logische", "long", "lurie", "mac", "maclane", "made", "major", "make", "manifest", "many", "many", "mappings", "maps", "marco", "masaki", "material", "mathct0305049", "mathematical", "mathematical", "mathematician", "mathematician", "mathematics", "mathematics", "mathematicsbrit", "may", "mclarty", "mclartythe", "means", "meet", "membership", "methods", "michael", "misleading", "mitchell", "models", "models", "moerdijk", "monad", "monadicity", "monographs", "monoid", "more", "morphisms", "most", "mostly", "motivation", "motivations", "much", "much", "music", "must", "myriads", "named", "natural", "natural", "naturally", "navigation", "ncategory", "necessary", "need", "never", "new", "nlab", "no", "no", "nocturnes", "nonconcrete", "nonsense", "nontechnical", "norman", "north", "northholland", "not", "notes", "notes", "nothing", "notion", "now", "npov", "number", "object", "objects", "obliged", "observation", "observing", "of", "on", "one", "online", "oosten", "operads", "opposed", "or", "order", "originally", "other", "other", "others", "out", "outside", "outside", "over", "packing", "page", "page", "pages", "paper", "paradigm", "pareigis", "parlance", "part", "particularly", "pdf", "pedagogical", "people", "perfect", "perhaps", "perpetrated", "perspective", "peter", "phenomenon", "phil", "philosopher", "philosophers", "philosophical", "philosophy", "physics", "physics", "pierce", "pierre", "played", "pleasure", "pointed", "poset", "possession", "power", "powered", "powerful", "pp", "preface", "prerequisite", "present", "preserving", "presheaf", "presheaves", "press", "prevail", "print", "probability", "problem", "proceedings", "process", "progression", "project", "proof", "property", "provide", "provides", "ps", "publicly", "published", "pure", "purloining", "purpose", "quite", "quiver", "rails", "rather", "reader", "realizations", "reason", "recalled", "record", "references", "reflect", "reflects", "rejected", "related", "related", "relation", "relation", "relations", "representable", "reprints", "reproduce", "resistance", "rests", "results", "reveals", "reverse", "revised", "revisions", "revisions", "rezk", "riehl", "robert", "role", "row", "ruby", "running", "same", "samuel", "saunders", "say", "scedrov", "schanuel", "schapira", "school", "sci", "science", "scientists", "search", "see", "see", "sense", "sep", "sequence", "serious", "set", "set", "sets", "sets", "sheaf", "sheaves", "shortly", "show", "shulman", "similar", "simon", "simple", "simplified", "simply", "simpson", "since", "single", "site", "situations", "sketches", "skip", "small", "so", "society", "some", "some", "sometimes", "sophisticated", "sophistication", "source", "space", "speak", "special", "specific", "specifically", "speculative", "spivak", "sprache", "stage", "standard", "statements", "steenrod", "stephen", "steps", "steve", "still", "stop", "strecker", "structural", "structuralism", "structure", "structures", "students", "study", "studying", "subjects", "such", "suggest", "summer", "supported", "supports", "symposium", "syntax", "tac", "taken", "talk", "tannaka", "tautological", "technique", "tend", "tends", "term", "terminology", "ternary", "tex", "textbook", "textbooks", "texts", "than", "that", "the", "the", "their", "their", "them", "themselves", "then", "theorem", "theorems", "theorems", "theoretic", "theoretical", "theories", "theorist", "theory", "theory", "there", "there", "these", "these", "they", "thinking", "this", "this", "thought", "through", "throughout", "thus", "time", "to", "tom", "tone", "too", "toolset", "top", "topics", "topoi", "topological", "topology", "topologyhomotopy", "topos", "topos", "toposes", "toposes", "transactions", "transformation", "transformations", "trinitarianism", "trinity", "triple", "triples", "trivial", "trivially", "true", "turns", "two", "two", "type", "typically", "uncountable", "under", "under", "understood", "unification", "unify", "unions", "univalent", "universal", "universal", "universes", "university", "use", "used", "useful", "using", "usual", "van", "variants", "various", "vast", "vect", "versatile", "video", "videos", "viewpoint", "views", "vol", "vol", "vs", "was", "way", "we", "wealth", "web", "wells", "were", "what", "when", "when", "where", "which", "while", "whole", "whose", "will", "willerton", "william", "willingness", "with", "witticism", "words", "working", "working", "would", "writes", "xfy", "xfygzxgfz", "xy", "yoneda", "york1964", "youtube")

  /**
   *  Instantiate the polymorphic game to the `IO[Nothing, ?]` type.
   *  by providing `Console`and `Random`
   */
  val myGameIO: UIO[Unit] = myGame ?

  /**
   * Create a test data structure that can contain a buffer of lines (to be
   * read from the console), a log of output (that has been written to the
   * console), and a list of "random" numbers.
   */
  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = ???
}

object parallel_web_crawler {

  final case class URL private (parsed: io.lemonlabs.uri.Url) {
    import io.lemonlabs.uri._

    final def relative(page: String): Option[URL] =
      scala.util
        .Try(parsed.path match {
          case Path(parts) =>
            val whole = parts.dropRight(1) :+ page.dropWhile(_ == '/')

            parsed.withPath(UrlPath(whole))
        })
        .toOption
        .map(new URL(_))

    def url: String = parsed.toString

    override def equals(a: Any): Boolean = a match {
      case that: URL => this.url == that.url
      case _         => false
    }

    override def hashCode: Int = url.hashCode
  }

  object URL {
    import io.lemonlabs.uri._

    def apply(url: String): Option[URL] =
      scala.util.Try(AbsoluteUrl.parse(url)).toOption match {
        case None         => None
        case Some(parsed) => Some(new URL(parsed))
      }
  }

  private val blockingPool = java.util.concurrent.Executors.newCachedThreadPool()

  def getURL(url: URL): ZIO[Blocking, Exception, String] =
    blocking.interruptible(scala.io.Source.fromURL(url.url)(scala.io.Codec.UTF8).mkString).refineOrDie{JustExceptions}


  def extractURLs(root: URL, html: String): List[URL] = {
    val pattern = "href=[\"\']([^\"\']+)[\"\']".r

    scala.util
      .Try({
        val matches = (for (m <- pattern.findAllMatchIn(html)) yield m.group(1)).toList

        for {
          m   <- matches
          url <- URL(m).toList ++ root.relative(m).toList
        } yield url
      })
      .getOrElse(Nil)
  }

  object test {
    val Home          = URL("http://scalaz.org").get
    val Index         = URL("http://scalaz.org/index.html").get
    val ScaladocIndex = URL("http://scalaz.org/scaladoc/index.html").get
    val About         = URL("http://scalaz.org/about").get

    val SiteIndex =
      Map(
        Home          -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        Index         -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        ScaladocIndex -> """<html><body><a href="index.html">Home</a><a href="/about">About</a></body></html>""",
        About         -> """<html><body><a href="home.html">Home</a><a href="http://google.com">Google</a></body></html>"""
      )

    val getURL: URL => IO[Exception, String] =
      (url: URL) =>
        SiteIndex
          .get(url)
          .fold[IO[Exception, String]](IO.fail(new Exception("Could not connect to: " + url)))(IO.effectTotal(_))

    val ScalazRouter: URL => Set[URL] =
      url => if (url.parsed.apexDomain == Some("scalaz.org")) Set(url) else Set()

    val Processor: (URL, String) => IO[Unit, List[(URL, String)]] =
      (url, html) => IO.succeed(List(url -> html))
  }

  def run(args: List[String]): ZIO[Console, Nothing, Int] =
    (for {
      _ <- putStrLn("Hello World!")
    } yield ()).fold(_ => 1, _ => 0)
}
