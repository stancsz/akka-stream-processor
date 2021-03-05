create table if not exists CourierTest
(
    courier_id varchar(100) not null
    primary key,
    courier_score FLOAT null,
    app_created_timestamp timestamp null,
    db_created_timestamp timestamp default CURRENT_TIMESTAMP null,
    lat FLOAT null,
    lon FLOAT null
    );

create table if not exists OrderTest
(
    order_id varchar(100) not null
    primary key,
    order_score FLOAT null,
    app_created_timestamp timestamp null,
    db_created_timestamp timestamp default CURRENT_TIMESTAMP null,
    lat FLOAT null,
    lon FLOAT null
    );

