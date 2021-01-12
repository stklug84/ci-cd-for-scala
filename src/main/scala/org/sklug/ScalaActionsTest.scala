package org.sklug

import org.apache.spark.sql.{DataFrame,SparkSession}
import io.delta.tables._


object ScalaActionsTest {

  def main(args: Array[String]): Unit = {

    // Instantiate Spark session
    val spark: SparkSession = SparkSession
      .builder()
      .appName("AiImagePipeline")
      .config("spark.sql.extensions","io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog","org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .config("spark.databricks.delta.optimizeWrite.enabled","true")
      .config("spark.databricks.delta.autoCompact.enabled","true")
      .config("spark.hadoop.fs.s3a.endpoint","<not-configured>")
      .config("spark.hadoop.fs.s3a.connection.ssl.enabled","true")
      .config("spark.hadoop.fs.s3a.path.style.access","true")
      .getOrCreate()
    import spark.implicits._

    val df: DataFrame = (1 to 10).toSeq.toDF()

    spark.stop()

  }

}
