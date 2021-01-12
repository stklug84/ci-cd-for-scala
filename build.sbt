import Settings._
import Testing._

// Project definition
lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    testSettings,
    buildSettings,
    name := "CiCdForScala",
  )
