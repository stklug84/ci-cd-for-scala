import Dependencies._
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtGit._
import com.scalapenos.sbt.prompt._
import SbtPrompt.autoImport._

/** Definition of settings for build instances
  *
  * All project related settings are set here
  *
  */
object Settings {
  /** General project related startup configuration
    *
    * Operations are only executed during startup process
    *
    */
  lazy val startupTransition: State => State = {
    s: State => "writeHooks" :: s
  }

  lazy val startupSettings = Seq(
    Global / onLoad := {
      val old = (Global / onLoad).value
      startupTransition compose old
    }
  )

  /** General project related configuration
    *
    * Define all project related properties
    *
    */
  lazy val buildSettings = Seq(
    organization := "org.sklug",

    /** General Scala configuration
      *
      * Define scala/scalac options
      *
      * @param scalaVersion: scala version to be used in the project
      * @param scalacOptions: scala compiler settings
      * @param scalacOptions in doc: scala compiler options for scaladoc creation
      *
      */
    scalaVersion := "2.11.12",
    scalacOptions := Seq(
      "-unchecked",
      "-feature",
      "-deprecation",
      "-encoding", "UTF-8",
      "-Xlint",
      "-Xfatal-warnings",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-adapted-args"
    ),
    doc / scalacOptions := Seq(
      "-diagrams",
      "-groups",
      "-implicits"
    ),

    /** Global configuration (may be set in ~/.sbt/1.0/config.sbt)
      *
      */
    // Build configuration is automatically loaded when build source changes
    //Global / onChangedBuildSource := ReloadOnSourceChanges,

    /** Library dependencies loaded from [[Dependencies]]
      *
      * Prerequisite: {{{ import Dependencies._ }}}
      *
      * All project specific library dependencies are defined in [[Dependencies]] as applicationDependencies
      * @note: applicationDependencies must be appended to keep sbt plugins dependencies from plugins
      *
      */
    libraryDependencies ++= applicationDependencies,

    /** Dependency overrides loaded from [[Dependencies]]
      *
      * Prerequisite: {{{ import Dependencies._ }}}
      *
      * All project specific overrides are defined in [[Dependencies]] as fixedDependencies that overrides
      * third-party dependency resolution while creating assemblies
      *
      */
    dependencyOverrides ++= fixedDependencies,

    /** Repository dependencies loaded from [[Repositories]]
      *
      * Prerequisite: {{{ import Repositories._ }}}
      *
      * All project specific required repository definitions are listed in [[Repositories]] in respositories
      * @note: repositories must be appended to not override global settings in /home/klg1rt/.sbt/repositories
      *
      */
    // resolvers += ("cloudera" at "http://repository.cloudera.com/artifactory/cloudera-repos/")
    //   .withAllowInsecureProtocol(true),

    /** Publish configuration --> should be defined in separate file.scala */
    // Disable publishing the main API jar
    Compile / packageDoc / publishArtifact := false,
    // Disable publishing the main sources jar
    Compile / packageSrc / publishArtifact := false,
    // Publishing path
    publishTo :=
      Some("Artifactory Realm" at "<>"),

    /** sbt-assembly plugin configuration
      *
      * Configuration for creating assemblies/fat/uber jars
      *
      * @param assembly / assemblyOption : Define rules of how to generate assemblies
      * @param assembly / assemblyMergeStrategy: Define rules which library to pick in case of duplicates
      *r
      */
    // Avoid adding scala libraries to assemblies
    assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false),
    // Pick the first encountered by the assembler (this is the default strategy)
    assembly / assemblyMergeStrategy := {
      // Needed for 3rd party data sources such as Delta or Kafka to work properly
      case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" =>
        MergeStrategy.concat
      // Ignore duplicates not used by Java 8
      case "module-info.class" => MergeStrategy.discard
      // Conflicts can be solved by overriding dependencies
      //case PathList("com","sun", xs @ _*) => MergeStrategy.first
      //case PathList("com","fasterxml", xs @ _*) => MergeStrategy.first
      //case PathList("com","amazonaws", xs @ _*) => MergeStrategy.first
      // Conflicting meta-information except for Spark data sources should be discarded
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    // Exclude full jars when building an assembly
    assemblyExcludedJars in assembly := {
      val cp = (fullClasspath in assembly).value
      cp filter {_.data.getName == "aws-java-sdk-bundle-1.11.271.jar"}
      // cp filter {_.data.getName == "httpclient-4.5.3.jar"}
    },

    /** sbt-git plugin configuration
      *
      * Prerequisite: {{{ import com.typesafe.sbt.SbtGit._ }}}
      *
      * @param git.gitTagToVersionNumber: Redefines default version tag detection for tags without
      *
      * Enable plugin in build.sbt per project via {{{enablePlugins(GitVersioning, GitBranchPrompt)}}}
      *
      */
    git.gitTagToVersionNumber := {
      tag: String =>
      if(tag matches "[0-9]+\\..*") Some(tag)
      else None
    },

    /** sbt-prompt configuration
      *
      * Prerequisite: {{{import com.scalapenos.sbt.prompt._
      import SbtPrompt.autoImport._}}}
      *
      * @param promptTheme: Pick predefined theme
      *
      */
    promptTheme := com.scalapenos.sbt.prompt.PromptThemes.ScalapenosTheme,

    /** sbt-scapegoat plugin configuration
      *
      * Set scapegoat version, required for scapegoat to find correct sbt and scala version
      *
      * @param scapegoatVersion: Define scapegoat version to be used by project
      */
    //ThisBuild / scapegoatVersion := "1.3.8"
  )
}
