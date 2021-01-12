import sbt._

/** sbt dependencies for all (sub-)projects defined in build.sbt
  *
  */
object Dependencies {
  private val spark: String = "org.apache.spark"
  private val sparkVersion: String = "2.4.0-cdh6.3.3"
  private val hadoop: String = "org.apache.hadoop"
  private val hadoopVersion: String = "3.0.0-cdh6.3.3"
  private val jacksonCore: String = "com.fasterxml.jackson.core"
  private val jacksonVersion: String = "2.8.1"

  /** Build dependencies
    *
    */
  val applicationDependencies: Seq[ModuleID] = Seq() ++
    Seq(spark %% "spark-core" % sparkVersion % "provided") ++
    Seq(spark %% "spark-sql"  % sparkVersion % "provided") ++
    Seq(hadoop % "hadoop-common" % hadoopVersion % "provided") ++
    Seq(hadoop % "hadoop-client" % hadoopVersion % "provided") ++
    Seq(hadoop % "hadoop-yarn-client" % hadoopVersion % "provided") ++
    Seq(hadoop % "hadoop-aws" % "3.0.3") ++
    Seq("com.amazonaws" % "aws-java-sdk-s3" % "1.11.271") ++
    Seq("com.databricks" %% "spark-xml" % "0.10.0") ++
    Seq("io.delta" %% "delta-core" % "0.6.1")

  /** Fixed dependencies for assemblies
    *
    */
  val fixedDependencies: Seq[ModuleID] = Seq() ++
    Seq(jacksonCore % "jackson-core" % jacksonVersion) ++
    Seq(jacksonCore % "jackson-databind" % jacksonVersion) ++
    Seq(jacksonCore % "jackson-annotations" % jacksonVersion) ++
    Seq("com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion)

  /** Test dependencies
    *
    */
  val testingDependencies: Seq[ModuleID] = Seq()
}
