import Settings._
import Testing._

// Project definition
lazy val root = (project in file("."))
  .enablePlugins(GitVersioning, GitBranchPrompt)
  .configs(IntegrationTest)
  .settings(
    testSettings,
    startupSettings,
    buildSettings,
    name := "CiCdForScala",
  )
