https://codelabs.developers.google.com/codelabs/frauddetection-using-console#5

# EDA

```
SELECT * FROM `bigquery-public-data.ml_datasets.ulb_fraud_detection` LIMIT 5

SELECT count(*) FROM `bigquery-public-data.ml_datasets.ulb_fraud_detection` 
where Class=0;

SELECT count(*) FROM `bigquery-public-data.ml_datasets.ulb_fraud_detection` 
where Class=1;
```

# Create Model

```
CREATE OR REPLACE MODEL advdata.ulb_fraud_detection 
TRANSFORM(
    * EXCEPT(Amount),
    SAFE.LOG(Amount) AS log_amount
)
OPTIONS(
    INPUT_LABEL_COLS=['class'],
    AUTO_CLASS_WEIGHTS = TRUE,
    DATA_SPLIT_METHOD='seq',
    DATA_SPLIT_COL='Time',
    MODEL_TYPE='logistic_reg'
) AS

SELECT 
 *
FROM `bigquery-public-data.ml_datasets.ulb_fraud_detection`
```

# Eval

```
SELECT * FROM ML.EVALUATE(MODEL advdata.ulb_fraud_detection)
```

# Predict

```
SELECT Amount, predicted_class_probs, Class
FROM ML.PREDICT( MODEL advdata.ulb_fraud_detection,
 (SELECT * FROM `bigquery-public-data.ml_datasets.ulb_fraud_detection` WHERE Time = 85285.0)
)
```





# Results

| Row  | Amount | predicted_class_probs.label | predicted_class_probs.prob | Class |      |
| :--- | :----- | :-------------------------- | :------------------------- | :---- | :--- |
| 1    | 9.5    | 1                           | 0.08118692480487301        | 0     |      |
|      |        | 0                           | 0.918813075195127          |       |      |
| 2    | 0.0    | 1                           | 0.9999999993676187         | 1     |      |
|      |        | 0                           | 6.323812584696498E-10      |       |      |
| 3    | 0.0    | 1                           | 0.9999999993676187         | 1     |      |
|      |        | 0                           | 6.323812584696498E-10      |       |      |
| 4    | 252.92 | 1                           | 0.9999999994845519         | 1     |      |
|      |        | 0                           | 5.154481286240298E-10      |       |      |
| 5    | 252.92 | 1                           | 0.9999999994845519         | 1     |      |
|      |        | 0                           | 5.154481286240298E-10      |       |      |





# Getting All Frauds

```
WITH detection as(
SELECT Amount, predicted_class_probs, Class
FROM ML.PREDICT( MODEL advdata.ulb_fraud_detection,
 (SELECT * FROM `bigquery-public-data.ml_datasets.ulb_fraud_detection`)
))
SELECT * FROM detection where class = 1
```



# refs
https://stackoverflow.com/questions/40224457/reading-a-csv-files-using-akka-streams
