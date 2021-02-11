create schema if not exists courier_order_db;
use courier_order_db;


create table if not exists CourierTest
(
    courier_id varchar(100) charset utf8 not null
    primary key,
    courier_score mediumtext null,
    app_created_timestamp timestamp null,
    db_created_timestamp timestamp default CURRENT_TIMESTAMP null,
    lat mediumtext null,
    lon mediumtext null
    );

create table if not exists OrderTest
(
    order_id varchar(100) charset utf8 not null
    primary key,
    order_score mediumtext null,
    app_created_timestamp timestamp null,
    db_created_timestamp timestamp default CURRENT_TIMESTAMP null,
    lat mediumtext null,
    lon mediumtext null
    );

