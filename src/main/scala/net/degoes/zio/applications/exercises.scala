// Copyright(C) 2019 - John A. De Goes. All rights reserved.

package net.degoes.zio
package applications

import scalaz.zio._
import scalaz.zio.blocking.Blocking
import scalaz.zio.console._
import scalaz.zio.duration.Duration
import scalaz.zio.random.Random

import java.io.IOException
import net.degoes.zio.applications.hangman.GuessResult.Incorrect
import net.degoes.zio.applications.hangman.GuessResult.Won
import net.degoes.zio.applications.hangman.GuessResult.Correct
import net.degoes.zio.applications.hangman.GuessResult.Unchanged
import net.degoes.zio.applications.hangman.GuessResult.Lost
import java.util.concurrent.ConcurrentHashMap

object hangman extends App {

  /**
   * Create a hangman game that requires the capability to perform `Console` and
   * `Random` effects.
   */
  lazy val myGame: ZIO[Console with Random, IOException, Unit] =
    ???

  final case class State(name: String, guesses: Set[Char], word: String) {
    final def failures: Int = (guesses -- word.toSet).size

    final def playerLost: Boolean = failures > 10

    final def playerWon: Boolean = (word.toSet -- guesses).isEmpty

    final def addChar(char: Char): State = copy(guesses = guesses + char)
  }

  sealed trait GuessResult
  object GuessResult {
    case object Won       extends GuessResult
    case object Lost      extends GuessResult
    case object Correct   extends GuessResult
    case object Incorrect extends GuessResult
    case object Unchanged extends GuessResult
  }

  def guessResult(oldState: State, newState: State, char: Char): GuessResult =
    if (oldState.guesses.contains(char)) GuessResult.Unchanged
    else if (newState.playerWon) GuessResult.Won
    else if (newState.playerLost) GuessResult.Lost
    else if (oldState.word.contains(char)) GuessResult.Correct
    else GuessResult.Incorrect

  /**
   * Implement the main game loop, which gets choices from the user until
   * the game is won or lost.
   */
  def gameLoop(state0: State): ZIO[Console, IOException, Unit] = ???

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

  /**
   * Implement an effect that gets a single, lower-case character from
   * the user.
   */
  lazy val getChoice: ZIO[Console, IOException, Char] = ???

  /**
   * Implement an effect that prompts the user for their name, and
   * returns it.
   */
  lazy val getName: ZIO[Console, IOException, String] = ???

  /**
   * Implement an effect that chooses a random word from the dictionary.
   */
  lazy val chooseWord: ZIO[Random, Nothing, String] = ???

  val Dictionary = List(
    "aaron",
    "abelian",
    "ability",
    "about",
    "abstract",
    "abstract",
    "abstraction",
    "accurately",
    "adamek",
    "add",
    "adjacent",
    "adjoint",
    "adjunction",
    "adjunctions",
    "after",
    "after",
    "again",
    "ahrens",
    "albeit",
    "algebra",
    "algebra",
    "algebraic",
    "all",
    "all",
    "allegories",
    "almost",
    "already",
    "also",
    "american",
    "among",
    "amount",
    "ams",
    "an",
    "an",
    "analysis",
    "analytic",
    "and",
    "and",
    "andre",
    "any",
    "anyone",
    "apart",
    "apologetic",
    "appears",
    "applicability",
    "applications",
    "applications",
    "applied",
    "apply",
    "applying",
    "applying",
    "approach",
    "archetypical",
    "archetypical",
    "are",
    "areas",
    "argument",
    "arising",
    "aristotle",
    "arrowsmorphism",
    "article",
    "arxiv13026946",
    "arxiv13030584",
    "as",
    "as",
    "aspect",
    "assumed",
    "at",
    "attempts",
    "audience",
    "august",
    "awodey",
    "axiom",
    "axiomatic",
    "axiomatized",
    "axioms",
    "back",
    "barr",
    "barry",
    "basic",
    "basic",
    "be",
    "beginners",
    "beginning",
    "behind",
    "being",
    "benedikt",
    "benjamin",
    "best",
    "better",
    "between",
    "bicategories",
    "binary",
    "bodo",
    "book",
    "borceux",
    "both",
    "both",
    "bourbaki",
    "bowdoin",
    "brash",
    "brendan",
    "build",
    "built",
    "but",
    "but",
    "by",
    "called",
    "cambridge",
    "can",
    "cardinal",
    "carlos",
    "carnap",
    "case",
    "cases",
    "categorial",
    "categorical",
    "categorical",
    "categories",
    "categories",
    "categorification",
    "categorize",
    "category",
    "category",
    "cats",
    "catsters",
    "central",
    "certain",
    "changes",
    "charles",
    "cheng",
    "chicago",
    "chiefly",
    "chopin",
    "chris",
    "cite",
    "clash",
    "classes",
    "classical",
    "closed",
    "coend",
    "coin",
    "colimit",
    "colin",
    "collection",
    "collections",
    "comparing",
    "completion",
    "composed",
    "composition",
    "computational",
    "computer",
    "computing",
    "concept",
    "concepts",
    "concepts",
    "conceptual",
    "concrete",
    "confronted",
    "consideration",
    "considers",
    "consistently",
    "construction",
    "constructions",
    "content",
    "contents",
    "context",
    "context",
    "contexts",
    "continues",
    "continuous",
    "contrast",
    "contributed",
    "contributions",
    "cooper",
    "correctness",
    "costas",
    "count",
    "course",
    "cover",
    "covering",
    "current",
    "currently",
    "david",
    "decategorification",
    "deducing",
    "define",
    "defined",
    "defining",
    "definition",
    "definitions",
    "der",
    "derives",
    "described",
    "describing",
    "description",
    "descriptions",
    "detailed",
    "development",
    "dictum",
    "did",
    "different",
    "dimensions",
    "directed",
    "discovered",
    "discovery",
    "discuss",
    "discussed",
    "discussion",
    "discussion",
    "disparage",
    "disservice",
    "do",
    "does",
    "driving",
    "drossos",
    "duality",
    "dvi",
    "each",
    "easy",
    "ed",
    "edges",
    "edit",
    "edition",
    "eilenberg",
    "eilenbergmaclane",
    "elementary",
    "elementary",
    "elements",
    "elementwise",
    "elephant",
    "ellis",
    "else",
    "embedding",
    "embodiment",
    "embryonic",
    "emily",
    "end",
    "enthusiastic",
    "equations",
    "equivalence",
    "equivalences",
    "equivalences",
    "etc",
    "etcs",
    "eugenia",
    "even",
    "eventually",
    "everything",
    "evident",
    "example",
    "examples",
    "examples",
    "except",
    "excused",
    "exist",
    "exists",
    "exposure",
    "expressed",
    "expressiveness",
    "extension",
    "extra",
    "f",
    "fact",
    "fair",
    "families",
    "far",
    "feeds",
    "feeling",
    "finds",
    "finite",
    "first",
    "flourished",
    "focuses",
    "folklore",
    "follows",
    "fong",
    "for",
    "for",
    "force",
    "forced",
    "foremost",
    "form",
    "formalizes",
    "formulated",
    "forthcoming",
    "found",
    "foundation",
    "foundations",
    "foundations",
    "francis",
    "free",
    "freyd",
    "freydmitchell",
    "from",
    "functions",
    "functor",
    "functor",
    "functors",
    "fundamental",
    "further",
    "gabrielulmer",
    "general",
    "general",
    "generalized",
    "generalizes",
    "geometry",
    "geometry",
    "george",
    "geroch",
    "get",
    "gift",
    "give",
    "given",
    "going",
    "goldblatt",
    "grandis",
    "graph",
    "gray",
    "grothendieck",
    "ground",
    "group",
    "groupoid",
    "grp",
    "guide",
    "göttingen",
    "had",
    "handbook",
    "handful",
    "handle",
    "harper",
    "has",
    "have",
    "he",
    "here",
    "here",
    "herrlich",
    "higher",
    "higher",
    "higherdimensional",
    "highlevel",
    "hilberts",
    "his",
    "historical",
    "historically",
    "history",
    "history",
    "holistic",
    "holland",
    "home",
    "homomorphisms",
    "homotopy",
    "homotopy",
    "horizontal",
    "horst",
    "however",
    "i",
    "idea",
    "ideas",
    "ieke",
    "if",
    "if",
    "illustrated",
    "important",
    "in",
    "in",
    "inaccessible",
    "inadmissible",
    "include",
    "includes",
    "including",
    "indeed",
    "indexes",
    "infinite",
    "informal",
    "initial",
    "innocent",
    "instance",
    "instead",
    "instiki",
    "interacting",
    "internal",
    "intersection",
    "into",
    "introduce",
    "introduced",
    "introduces",
    "introducing",
    "introduction",
    "introduction",
    "introductory",
    "intuitions",
    "invitation",
    "is",
    "isbell",
    "isbn",
    "isomorphisms",
    "it",
    "it",
    "its",
    "itself",
    "ive",
    "j",
    "jaap",
    "jacob",
    "jiri",
    "johnstone",
    "joy",
    "jstor",
    "just",
    "kan",
    "kant",
    "kapulkin",
    "kashiwara",
    "kind",
    "kinds",
    "kleins",
    "kmorphisms",
    "ktransfors",
    "kℕ",
    "la",
    "lagatta",
    "lane",
    "language",
    "large",
    "last",
    "later",
    "later",
    "latest",
    "lauda",
    "lawvere",
    "lawveres",
    "lead",
    "leads",
    "least",
    "lectures",
    "led",
    "leinster",
    "lemma",
    "lemmas",
    "level",
    "library",
    "lifting",
    "likewise",
    "limit",
    "limits",
    "link",
    "linked",
    "links",
    "list",
    "literally",
    "logic",
    "logic",
    "logically",
    "logische",
    "long",
    "lurie",
    "mac",
    "maclane",
    "made",
    "major",
    "make",
    "manifest",
    "many",
    "many",
    "mappings",
    "maps",
    "marco",
    "masaki",
    "material",
    "mathct0305049",
    "mathematical",
    "mathematical",
    "mathematician",
    "mathematician",
    "mathematics",
    "mathematics",
    "mathematicsbrit",
    "may",
    "mclarty",
    "mclartythe",
    "means",
    "meet",
    "membership",
    "methods",
    "michael",
    "misleading",
    "mitchell",
    "models",
    "models",
    "moerdijk",
    "monad",
    "monadicity",
    "monographs",
    "monoid",
    "more",
    "morphisms",
    "most",
    "mostly",
    "motivation",
    "motivations",
    "much",
    "much",
    "music",
    "must",
    "myriads",
    "named",
    "natural",
    "natural",
    "naturally",
    "navigation",
    "ncategory",
    "necessary",
    "need",
    "never",
    "new",
    "nlab",
    "no",
    "no",
    "nocturnes",
    "nonconcrete",
    "nonsense",
    "nontechnical",
    "norman",
    "north",
    "northholland",
    "not",
    "notes",
    "notes",
    "nothing",
    "notion",
    "now",
    "npov",
    "number",
    "object",
    "objects",
    "obliged",
    "observation",
    "observing",
    "of",
    "on",
    "one",
    "online",
    "oosten",
    "operads",
    "opposed",
    "or",
    "order",
    "originally",
    "other",
    "other",
    "others",
    "out",
    "outside",
    "outside",
    "over",
    "packing",
    "page",
    "page",
    "pages",
    "paper",
    "paradigm",
    "pareigis",
    "parlance",
    "part",
    "particularly",
    "pdf",
    "pedagogical",
    "people",
    "perfect",
    "perhaps",
    "perpetrated",
    "perspective",
    "peter",
    "phenomenon",
    "phil",
    "philosopher",
    "philosophers",
    "philosophical",
    "philosophy",
    "physics",
    "physics",
    "pierce",
    "pierre",
    "played",
    "pleasure",
    "pointed",
    "poset",
    "possession",
    "power",
    "powered",
    "powerful",
    "pp",
    "preface",
    "prerequisite",
    "present",
    "preserving",
    "presheaf",
    "presheaves",
    "press",
    "prevail",
    "print",
    "probability",
    "problem",
    "proceedings",
    "process",
    "progression",
    "project",
    "proof",
    "property",
    "provide",
    "provides",
    "ps",
    "publicly",
    "published",
    "pure",
    "purloining",
    "purpose",
    "quite",
    "quiver",
    "rails",
    "rather",
    "reader",
    "realizations",
    "reason",
    "recalled",
    "record",
    "references",
    "reflect",
    "reflects",
    "rejected",
    "related",
    "related",
    "relation",
    "relation",
    "relations",
    "representable",
    "reprints",
    "reproduce",
    "resistance",
    "rests",
    "results",
    "reveals",
    "reverse",
    "revised",
    "revisions",
    "revisions",
    "rezk",
    "riehl",
    "robert",
    "role",
    "row",
    "ruby",
    "running",
    "same",
    "samuel",
    "saunders",
    "say",
    "scedrov",
    "schanuel",
    "schapira",
    "school",
    "sci",
    "science",
    "scientists",
    "search",
    "see",
    "see",
    "sense",
    "sep",
    "sequence",
    "serious",
    "set",
    "set",
    "sets",
    "sets",
    "sheaf",
    "sheaves",
    "shortly",
    "show",
    "shulman",
    "similar",
    "simon",
    "simple",
    "simplified",
    "simply",
    "simpson",
    "since",
    "single",
    "site",
    "situations",
    "sketches",
    "skip",
    "small",
    "so",
    "society",
    "some",
    "some",
    "sometimes",
    "sophisticated",
    "sophistication",
    "source",
    "space",
    "speak",
    "special",
    "specific",
    "specifically",
    "speculative",
    "spivak",
    "sprache",
    "stage",
    "standard",
    "statements",
    "steenrod",
    "stephen",
    "steps",
    "steve",
    "still",
    "stop",
    "strecker",
    "structural",
    "structuralism",
    "structure",
    "structures",
    "students",
    "study",
    "studying",
    "subjects",
    "such",
    "suggest",
    "summer",
    "supported",
    "supports",
    "symposium",
    "syntax",
    "tac",
    "taken",
    "talk",
    "tannaka",
    "tautological",
    "technique",
    "tend",
    "tends",
    "term",
    "terminology",
    "ternary",
    "tex",
    "textbook",
    "textbooks",
    "texts",
    "than",
    "that",
    "the",
    "the",
    "their",
    "their",
    "them",
    "themselves",
    "then",
    "theorem",
    "theorems",
    "theorems",
    "theoretic",
    "theoretical",
    "theories",
    "theorist",
    "theory",
    "theory",
    "there",
    "there",
    "these",
    "these",
    "they",
    "thinking",
    "this",
    "this",
    "thought",
    "through",
    "throughout",
    "thus",
    "time",
    "to",
    "tom",
    "tone",
    "too",
    "toolset",
    "top",
    "topics",
    "topoi",
    "topological",
    "topology",
    "topologyhomotopy",
    "topos",
    "topos",
    "toposes",
    "toposes",
    "transactions",
    "transformation",
    "transformations",
    "trinitarianism",
    "trinity",
    "triple",
    "triples",
    "trivial",
    "trivially",
    "true",
    "turns",
    "two",
    "two",
    "type",
    "typically",
    "uncountable",
    "under",
    "under",
    "understood",
    "unification",
    "unify",
    "unions",
    "univalent",
    "universal",
    "universal",
    "universes",
    "university",
    "use",
    "used",
    "useful",
    "using",
    "usual",
    "van",
    "variants",
    "various",
    "vast",
    "vect",
    "versatile",
    "video",
    "videos",
    "viewpoint",
    "views",
    "vol",
    "vol",
    "vs",
    "was",
    "way",
    "we",
    "wealth",
    "web",
    "wells",
    "were",
    "what",
    "when",
    "when",
    "where",
    "which",
    "while",
    "whole",
    "whose",
    "will",
    "willerton",
    "william",
    "willingness",
    "with",
    "witticism",
    "words",
    "working",
    "working",
    "would",
    "writes",
    "xfy",
    "xfygzxgfz",
    "xy",
    "yoneda",
    "york1964",
    "youtube"
  )

  /**
   *  Eliminate the game's environment by providing it `Console with Random`
   */
  lazy val myGameIO: UIO[Unit] = myGame ?

  /**
   * Implement the `runScenario` method according to its type. Hint: You
   * will have to use `provide` on `TestModule`.
   */
  def runScenario(testData: TestData): IO[IOException, TestData] = ???

  case class TestData(
    output: List[String],
    input: List[String],
    integers: List[Int]
  ) {
    def render: String =
      output.reverse.mkString("\n")
  }
  case class TestModule(ref: Ref[TestData]) extends Random with Console {
    val console = new Console.Service[Any] {
      val getStrLn: IO[IOException, String] =
        ref.modify(data => (data.input.head, data.copy(input = data.input.tail)))
      def putStr(line: String): UIO[Unit] =
        ref.update(data => data.copy(output = line :: data.output)).void
      def putStrLn(line: String): UIO[Unit] = putStr(line + "\n")
    }

    val random = new Random.Service[Any] {
      val nextBoolean: UIO[Boolean]                           = UIO(false)
      def nextBytes(length: Int): UIO[scalaz.zio.Chunk[Byte]] = UIO(Chunk.empty)
      val nextDouble: UIO[Double]                             = UIO(0.0)
      val nextFloat: UIO[Float]                               = UIO(0.0f)
      val nextGaussian: UIO[Double]                           = UIO(0.0)
      val nextInt: UIO[Int]                                   = ref.modify(data => (data.integers.head, data.copy(integers = data.integers.tail)))
      def nextInt(n: Int): UIO[Int]                           = ref.modify(data => (data.integers.head, data.copy(integers = data.integers.tail)))
      val nextLong: UIO[Long]                                 = UIO(0L)
      val nextPrintableChar: UIO[Char]                        = UIO('A')
      def nextString(length: Int): UIO[String]                = UIO("foo")
    }
  }

  val Scenario1: TestData =
    TestData(
      output = Nil,
      input = "John" :: "a" :: "r" :: "o" :: "n" :: Nil,
      integers = 0 :: Nil
    )

  lazy val testScenario1 = runScenario(Scenario1).flatMap(testData => putStrLn(testData.render))

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] =
    myGame.fold(_ => 1, _ => 0)
}

object parallel_web_crawler {
  trait Web {
    def web: Web.Service
  }
  object Web {
    trait Service {
      def getURL(url: URL): IO[Exception, String]
    }
    trait Live extends Web with Blocking {
      val web = new Service {

        /**
         * EXERCISE 1
         *
         * Use the `effectBlocking` combinator to safely import the Scala `Source.fromURL`
         * side-effect into a purely functional ZIO effect, using `refineOrDie` to narrow
         * the `Throwable` error to `Exceptiono`.
         */
        def getURL(url: URL): IO[Exception, String] = {
          // def effectBlocking[A](sideEffect: => A): ZIO[Blocking, Throwable, A]

          def getURLImpl(url: URL): String =
            scala.io.Source.fromURL(url.url)(scala.io.Codec.UTF8).mkString

          ???
        }
      }
    }
  }

  /**
   * EXERCISE 2
   *
   * Using `ZIO.accessM`, delegate to the `Web` module's `getURL` function.
   */
  def getURL(url: URL): ZIO[Web, Exception, String] = ???

  final case class CrawlState[+E](visited: Set[URL], errors: List[E]) {
    final def visitAll(urls: Set[URL]): CrawlState[E] = copy(visited = visited ++ urls)

    final def logError[E1 >: E](e: E1): CrawlState[E1] = copy(errors = e :: errors)
  }

  /**
   * EXERCISE 3
   *
   * Implement the `crawl` function using the helpers provided in this object.
   *
   * {{{
   * def getURL(url: URL): ZIO[Blocking, Exception, String]
   * def extractURLs(root: URL, html: String): List[URL]
   * }}}
   */
  def crawl[E](
    seeds: Set[URL],
    router: URL => Set[URL],
    processor: (URL, String) => IO[E, Unit]
  ): ZIO[Web, Nothing, List[E]] = ???

  /**
   * A data structure representing a structured URL, with a smart constructor.
   */
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

    def make(url: String): Option[URL] =
      scala.util.Try(AbsoluteUrl.parse(url)).toOption match {
        case None         => None
        case Some(parsed) => Some(new URL(parsed))
      }
  }

  /**
   * A function that extracts URLs from a given web page.
   */
  def extractURLs(root: URL, html: String): List[URL] = {
    val pattern = "href=[\"\']([^\"\']+)[\"\']".r

    scala.util
      .Try({
        val matches = (for (m <- pattern.findAllMatchIn(html)) yield m.group(1)).toList

        for {
          m   <- matches
          url <- URL.make(m).toList ++ root.relative(m).toList
        } yield url
      })
      .getOrElse(Nil)
  }

  object test {
    val Home          = URL.make("http://scalaz.org").get
    val Index         = URL.make("http://scalaz.org/index.html").get
    val ScaladocIndex = URL.make("http://scalaz.org/scaladoc/index.html").get
    val About         = URL.make("http://scalaz.org/about").get

    val SiteIndex =
      Map(
        Home          -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        Index         -> """<html><body><a href="index.html">Home</a><a href="/scaladoc/index.html">Scaladocs</a></body></html>""",
        ScaladocIndex -> """<html><body><a href="index.html">Home</a><a href="/about">About</a></body></html>""",
        About         -> """<html><body><a href="home.html">Home</a><a href="http://google.com">Google</a></body></html>"""
      )

    val TestWeb = new Web {
      val web = new Web.Service {

        /**
         * EXERCISE 4
         *
         * Use the `SiteIndex` test data to provide an implementation of
         * `getURL` for the `TestWeb` module.
         */
        def getURL(url: URL): IO[Exception, String] =
          ???
      }
    }

    val TestRouter: URL => Set[URL] =
      url => if (url.parsed.apexDomain == Some("scalaz.org")) Set(url) else Set()

    val Processor: (URL, String) => IO[Unit, List[(URL, String)]] =
      (url, html) => IO.succeed(List(url -> html))
  }

  def run(args: List[String]): ZIO[Console, Nothing, Int] =
    (for {
      _ <- putStrLn("Hello World!")
    } yield ()).fold(_ => 1, _ => 0)
}

object circuit_breaker extends App {
  import java.util.concurrent.TimeUnit

  import scalaz.zio.clock._

  /**
   * EXERCISE 1
   *
   * Design an API for the following circuit breaker trait.
   */
  trait CircuitBreaker[E] {
    def apply[R, A](zio: ZIO[R, E, A]): ZIO[R with Clock, E, A]
  }

  object CircuitBreaker {

    /**
     * EXERCISE 2
     *
     * Design an immutable data structure to hold a time-windowed histogram of 
     * failures and successes.
     */
    private final case class HistogramState(timeUnit: TimeUnit, size: Int /* add more state */) {
      def add(millis: Long, b: Boolean): HistogramState = ???

      def failures: Int = ???

      def successes: Int = ???
    }

    /**
     * EXERCISE 3
     *
     * Implement the constructor for `CircuitBreaker`.
     */
    def make[E](rejectedError: E, threshold: Double): UIO[CircuitBreaker[E]] =
      for {
        ref  <- Ref.make(HistogramState(TimeUnit.MINUTES, 2))
        hist = new Histogram(ref)
      } yield
        new CircuitBreaker[E] {
          def apply[R, A](zio: ZIO[R, E, A]): ZIO[R with Clock, E, A] =
            for {
              failures  <- hist.failures
              successes <- hist.successes
              ratio     = failures.toDouble / successes.toDouble
              a <- if (ratio > threshold) ZIO.fail(rejectedError)
                  else
                    clock
                      .currentTime(TimeUnit.MILLISECONDS)
                      .flatMap(millis => zio.tapBoth(_ => hist.add(millis, false), _ => hist.add(millis, true)))
            } yield a
        }

    private class Histogram(ref: Ref[HistogramState]) {
      def add(millis: Long, b: Boolean): UIO[Unit] = ref.update(_.add(millis, b)).unit
      def failures: UIO[Int]                       = ref.get.map(_.failures)
      def successes: UIO[Int]                      = ref.get.map(_.successes)
    }
  }

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = ???
}