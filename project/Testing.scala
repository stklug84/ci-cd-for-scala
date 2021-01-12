import Dependencies._
import sbt.Keys._
import sbt._

object Testing {
  private val commonTestSettings = Seq(
    /** Test dependencies
      *
      * All testing specific libraries are defined here
      * @note: dependencies must be appended to keep sbt plugins dependencies from plugins
      *
      */
    libraryDependencies ++= testingDependencies,

    /** Test configuration
      *
      * Additional JVM options used only for testing (note that sequence needs to be appended)
      *
      * @param javaOptions in test: Append test-only JVM options
      * @param fork in test: Test task need to be forked into new JVM to activate test-only JVM options
      * @param parallelExecution in test: Run tests sequentially to easily spot failed tests
      */
    Test / fork := true,
    Test / parallelExecution := false,
    Test / logBuffered := false,
    Test / javaOptions ++= Seq(
      "-Xms1G",
      "-Xmx4G",
      "-XX:+UseConcMarkSweepGC",
       "-XX:+CMSClassUnloadingEnabled"
    ),
)
  lazy val testSettings = Defaults.itSettings ++ commonTestSettings
}
