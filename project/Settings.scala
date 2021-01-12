import Dependencies._
import sbt.Keys._
import sbt._

/** Definition of settings for build instances
  *
  * All project related settings are set here
  *
  */
object Settings {

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
    resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases",

    /** Publish configuration --> should be defined in separate file.scala */
    // Disable publishing the main API jar
    Compile / packageDoc / publishArtifact := false,
    // Disable publishing the main sources jar
    Compile / packageSrc / publishArtifact := false,
    // Publishing path
    publishTo :=
      Some("Artifactory Realm" at "<>"),
  )
}
