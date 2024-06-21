import org.apache.spark.sql.SparkSession
import java.time.LocalDate

val spark = SparkSession.builder.config("spark.sql.legacy.timeParserPolicy", "LEGACY").getOrCreate()

val gdcp_df = spark.read.format("csv").option("sep", ",").option("inferSchema", "true").option("header", "true").load("/opt/bitnami/spark/dataset/raw/global-daily-commodity-prices-dataset.csv")
val renamed_columns_gdcp_df = gdcp_df.withColumnRenamed("Date", "date").withColumnRenamed("Wheat", "wheat_avg_price_per_pound").withColumnRenamed("Coffe", "coffee_avg_price_per_pound").withColumnRenamed("Corn", "corn_avg_price_per_pound")
val cleaned_gdcp_df = renamed_columns_gdcp_df.na.drop()
val formatted_date_gdcp_df = cleaned_gdcp_df.withColumn("date", to_date($"date", "MM/dd/yyyy"))
val rounded_values_gdcp_df = formatted_date_gdcp_df.withColumn("wheat_avg_price_per_pound", format_number($"wheat_avg_price_per_pound", 4)).withColumn("coffee_avg_price_per_pound", format_number($"coffee_avg_price_per_pound", 4)).withColumn("corn_avg_price_per_pound", format_number($"corn_avg_price_per_pound", 4))
val processed_gdcp_df = rounded_values_gdcp_df

val usc_df = spark.read.format("csv").option("sep", ",").option("inferSchema", "true").option("header", "true").load("/opt/bitnami/spark/dataset/raw/us-significant-cataclysms-dataset.csv")
val dropped_columns_usc_df = usc_df.drop("Total CPI-Adjusted Cost (Millions of Dollars)", "Deaths")
val renamed_columns_usc_df = dropped_columns_usc_df.withColumnRenamed("Name", "cataclysm_description").withColumnRenamed("Disaster", "cataclysm_type").withColumnRenamed("Begin Date", "date_begin").withColumnRenamed("End Date", "date_end")
val cleaned_usc_df = renamed_columns_usc_df.na.drop()
val formatted_date_usc_df = cleaned_usc_df.withColumn("date_begin", to_date($"date_begin", "yyyyMMdd")).withColumn("date_end", to_date($"date_end", "yyyyMMdd"))
val generateDatesFunction = udf((startDate: String, endDate: String) => { val start = LocalDate.parse(startDate); val end = LocalDate.parse(endDate); Iterator.iterate(start)(_.plusDays(1)).takeWhile(!_.isAfter(end)).toList })
val exploded_dates_usc_df = formatted_date_usc_df.withColumn("dates", generateDatesFunction($"date_begin", $"date_end")).withColumn("date", explode($"dates")).drop("dates", "date_begin", "date_end")
val grouped_date_usc_df = exploded_dates_usc_df.groupBy("date").agg(concat_ws(";", sort_array(collect_set(col("cataclysm_type")))).as("cataclysm_types"), concat_ws(";", sort_array(collect_set(col("cataclysm_description")))).as("cataclysm_descriptions"), count("*").as("cataclysm_total_count"))
val processed_usc_df = grouped_date_usc_df

val udc_df = spark.read.format("csv").option("sep", ",").option("inferSchema", "true").option("header", "true").load("/opt/bitnami/spark/dataset/raw/us-daily-crimes-dataset.csv")
val dropped_columns_udc_df = udc_df.drop("agency_name", "division_name", "population_group", "offender_race", "location_name", "victim_types")
val renamed_columns_udc_df = dropped_columns_udc_df.withColumnRenamed("incident_date", "date").withColumnRenamed("state_name", "crime_state_name").withColumnRenamed("offense_name", "crime_type").withColumnRenamed("offender_count", "crime_offender_count").withColumnRenamed("victim_count", "crime_victim_count").withColumnRenamed("bias_desc", "crime_victim_group")
val cleaned_udc_df = renamed_columns_udc_df.na.drop()
val casted_victim_count_udc_df = cleaned_udc_df.withColumn("crime_victim_count", col("crime_victim_count").cast("integer"))
val formatted_date_udc_df = casted_victim_count_udc_df.withColumn("date", to_date($"date", "dd-MMM-yy"))
val grouped_date_udc_df = formatted_date_udc_df.groupBy("date").agg(concat_ws(";", sort_array(collect_set(col("crime_state_name")))).as("crime_state_names"), count("*").as("crime_total_count"), sum("crime_offender_count").as("crime_total_offender_count"), concat_ws(";", sort_array(collect_set(col("crime_type")))).as("crime_types"), sum("crime_victim_count").as("crime_total_victim_count"), concat_ws(";", sort_array(collect_set(col("crime_victim_group")))).as("crime_victim_groups"))
val processed_udc_df = grouped_date_udc_df

val joined_df = processed_gdcp_df.join(processed_usc_df, processed_gdcp_df("date") === processed_usc_df("date"), "left_outer").join(processed_udc_df, processed_gdcp_df("date") === processed_udc_df("date"), "left_outer")
val selected_columns_joined_df = joined_df.select(processed_gdcp_df("date"), col("wheat_avg_price_per_pound"), col("coffee_avg_price_per_pound"), col("corn_avg_price_per_pound"), col("cataclysm_total_count"), col("cataclysm_types"), col("cataclysm_descriptions"), col("crime_total_count"), col("crime_total_offender_count"), col("crime_total_victim_count"), col("crime_state_names"), col("crime_types"), col("crime_victim_groups"))
val filtered_joined_df = selected_columns_joined_df.filter(col("date").geq(lit("1975-01-01")) && col("date").lt(lit("2020-01-01")))
val processed_df = filtered_joined_df

processed_df.coalesce(1).write.format("csv").option("sep", ",").option("header", "true").save("/opt/bitnami/spark/dataset/processed")

spark.stop()
