# **Analysis of Socio-Economic and Climatic Data in the USA (1975-2020) using Apache Spark and ELK Stack** 🌎

### **How To Setup Access Rights?** 🗝️
```bash
chmod -R 777 ./spark-elk-docker-datasets-visualization
```

### **How To Start?** 🆙
```bash
./run.sh
```

### **How To Query in [Kibana](http://localhost:9400)?** ⏩️
```
http://localhost:9400
... open DevTools in Kibana ...
... query ...
```

### **How To Stop The System?** ⏸️
```bash
./stop.sh
```

---

### **Project Structure** 📁

- `analysis/`: Contains analysis results in the form of image files.
- `elk/`: Contains ELK stack configuration files and scripts.
  - `elasticsearch/`: Elasticsearch configuration files.
  - `kibana/`: Kibana configuration files.
    - `dashboard/`: Contains dashboard initialization scripts.
  - `logstash/`: Logstash configuration files.
    - `pipeline/`: Logstash pipeline configurations.
    - `template/`: Logstash templates.
- `query/`: Contains query files for interacting with the database.
- `spark/`: Contains Apache Spark configuration and dataset preprocessing scripts.
  - `dataset/`: Contains raw dataset files.
  - `server-script/`: Contains server scripts for dataset preprocessing.
- `run.sh`: Script to start the system.
- `stop.sh`: Script to stop the system.
- `README.md`: The main README file for the project.

---

### **Data Sources and Theme Selection** 📚
For this project, I selected datasets from the Kaggle platform, covering various aspects of socio-economic and climatic data in the United States of America for the period from the 1970s to the 2020s. The aim of the study is to analyze the interrelationships between key economic indicators, climatic changes, and their impact on the level of crime.

---

### **Data Selection from Kaggle** 🔄
- **Market Value of Food Products** 💰: The dataset includes data on market prices for essential food products such as wheat, corn, and coffee. These products were chosen due to their fundamental role in the nutrition and economy of the USA.
- **Crime Rates by State** 🔫: The data analyzes crime rates in various states, allowing for the assessment of the impact of economic and climatic factors on social well-being and security.
- **Climatic Catastrophes** 🌪️: Includes information on natural disasters such as hurricanes, droughts, frosts, storms, and floods, to evaluate their impact on the economic and social spheres.

---

### **Research Hypothesis** 🧮
The main hypothesis is that there is a correlation between the market value of key food products and the level of crime. An increase in food prices can stimulate the growth of crime due to increased social tension and economic difficulties. Additionally, it is assumed that climatic catastrophes can significantly affect these parameters, causing substantial changes in the economy and the social structure of society.

---

### **Detailed Description of Kaggle Datasets for the Project** 📊

#### 1. **Market Value of Food Products** 💵
- **Source** 🖥️: Global Daily Commodity Prices Dataset.
- **Data Fields** 📑:
  - `Date`: Recording date.
  - `Wheat`: Price of wheat per pound (in USD).
  - `Coffe`: Price of coffee per pound (in USD).
  - `Corn`: Price of corn per pound (in USD).
- **Description** 📝: This dataset represents a time series reflecting daily changes in market prices for wheat, coffee, and corn. These data can be used to analyze economic trends and their impact on socio-economic conditions.

#### 2. **Crime Rates by State** 🔪
- **Source** 🖥️: US Daily Crimes Dataset.
- **Data Fields** 📑:
  - `incident_date`: Incident date.
  - `agency_name`: Name of the law enforcement agency.
  - `state_name`, `division_name`: State and region.
  - `population_group`: Population group.
  - `offender_count`, `offender_race`: Number and race of offenders.
  - `offense_name`: Name of the crime.
  - `victim_count`: Number of victims.
  - `location_name`: Location of the incident.
  - `bias_desc`: Presumed motivation for the crime.
  - `victim_types`: Types of victims.
- **Description** 📝: This dataset contains detailed information about crimes in various states of the USA, including the nature of the crimes, race of offenders and victims, motivation of the crimes, and locations where they occurred. The data can be used to analyze crime in the context of socio-economic changes.

#### 3. **Climatic Catastrophes** 🌩️
- **Source** 🖥️: US Significant Cataclysms Dataset.
- **Data Fields** 📑:
  - `Name`: Name of the catastrophe.
  - `Disaster`: Type of catastrophe (e.g., hurricane, drought).
  - `Begin Date`, `End Date`: Start and end dates of the event.
  - `Total CPI-Adjusted Cost (Millions of Dollars)`: Total cost of damage, adjusted for inflation.
  - `Deaths`: Number of deaths.
- **Description** 📝: This dataset includes information about significant climatic events in the USA, such as hurricanes, droughts, frosts, storms, and floods. The data contain descriptions of events, their duration, economic losses, and number of victims, allowing for the analysis of the impact of climatic changes on the economy and society.

---

### **Data Preparation Process in Apache Spark Using Scala Script** ⚙️
In the process of working with the data, I perform a series of key actions in Apache Spark to clean, transform, and combine three datasets. Here's how I do it:

#### 1. **Processing the Food Price Dataset** 🌽
- **Data Reading** 🔎: I use Spark to read the CSV file with data on prices for wheat, coffee, and corn.
- **Column Renaming** 🧾: For ease of work, I rename the columns according to their content.
- **Missing Data Cleaning** ✂️: I remove records with missing values to increase analysis accuracy.
- **Date Format Transformation** 📅: I standardize the dates to ensure compatibility and correct comparison.
- **Price Value Rounding** 🖋️: I standardize prices to a uniform format with four decimal places.

#### 2. **Crime Dataset Processing** 💣
- **Reading and Preliminary Processing** 🔍: Similarly, I load crime data, removing incomplete records.
- **Data Transformation and Normalization** 🧮: I standardize categorical data to a uniform format, for example, standardizing state names and types of crimes.
- **Filtering and Cleaning** 🪛: I filter out records irrelevant for analysis and remove duplicates.

#### 3. **Processing the Climatic Catastrophes Dataset** 🌊
- **Data Loading and Cleaning** 📃: I read data about climatic events, removing rows with missing information.
- **Date Transformation** 📆: I standardize date formats for consistent analysis over time periods.
- **Numerical Data Formatting** 📌: I round and normalize numerical data, such as economic damage and number of victims, for uniformity and precision.

#### 4. **Integration and Matching of Data** 🔄
- **Combining Prepared Datasets** ➕: To create a comprehensive dataset, I combine the cleaned data using common elements such as dates for their matching.
  
- **Matching Records by Common Keys** 🗝️: To ensure accuracy and consistency of information, I meticulously match records between datasets based on common keys.

- **Date Filtering** 📍: To align with the research goals, I filter the data to include only records from 1975 to 2020.

#### 5. **Description of the Final Dataset** 📂
After thorough preparation and integration of data from three different sources, I have created the final dataset that covers socio-economic and climatic aspects in the USA from 1975 to 2020. Here are the main characteristics of this dataset:
- `date`: The date of the observed data, serving as a key element for data matching and analysis over time.
- `wheat_avg_price_per_pound`, `coffee_avg_price_per_pound`, `corn_avg_price_per_pound`: Average prices per pound for wheat, coffee, and corn in USD. These data allow for the analysis of economic trends in the food market.
- `cataclysm_total_count`: The number of significant climatic events that occurred on a given date.
- `cataclysm_types`: Types of climatic catastrophes, such as hurricanes, droughts, frosts, etc.
- `cataclysm_descriptions`: Detailed descriptions of the climatic events that occurred.
- `crime_total_count`: The number of crimes registered on a given day.
- `crime_total_offender_count`, `crime_total_victim_count`: Provide information on the scale and nature of the crimes.
- `crime_state_names`: The list of states where crimes were registered.
- `crime_types`: Categories of crimes, such as aggression, vandalism, etc.
- `crime_victim_groups`: Groups targeted by the crimes.

---

### **Sources** 📍

- [Docker Documentation](https://docs.docker.com) 🐳: Assisted in containerizing the application, ensuring a consistent and isolated environment for development and deployment.

- [Bitnami Docker Images](https://hub.docker.com/r/bitnami) 📦: Provided reliable and consistent Docker images used in the project, facilitating streamlined setup and maintenance of service containers.

- [Elasticsearch Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html) 🔍: Served as a comprehensive resource for understanding and utilizing Elasticsearch capabilities within the project.

- [Kibana Documentation](https://www.elastic.co/guide/en/kibana/current/index.html) 📊: Offered detailed guidance on deploying and managing Kibana for data visualization and analytics.

- [Logstash Documentation](https://www.elastic.co/guide/en/logstash/current/index.html) 🔄: Provided instructions and best practices for data processing and ingestion using Logstash.

- [Apache Spark Documentation](https://spark.apache.org/docs/latest/) 🛠️: Gave insights into big data processing and analytics, essential for handling large datasets efficiently.

- [Crime Rates Dataset by The Marshall Project on Kaggle](https://www.kaggle.com/datasets/marshallproject/crime-rates) 🚨: Supplied data on crime rates, essential for analyzing trends and correlations in criminal activity.

- [Global Grain and Coffee Price History Dataset on Kaggle](https://www.kaggle.com/datasets/mabdullahsajid/global-grain-and-coffee-price-history-1973-2023/data) ☕: Provided historical data on grain and coffee prices, instrumental for economic trend analysis.

- [Weather Events in US Dataset on Kaggle](https://www.kaggle.com/datasets/edndum/weather-events-in-us-from-1980-t0-202) 🌪️: Offered extensive data on weather events, crucial for studying the impact of climatic conditions on various factors.
