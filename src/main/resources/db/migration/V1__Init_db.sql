create sequence hibernate_sequence start 1 increment 1;

create table cart (
    id int8 not null,
    is_active boolean not null,
    consumer_id int8,
    primary key (id)
    );

create table comment (
    id int8 not null,
    text varchar(2048),
    author_id int8,
    product_id int8,
    primary key (id)
    );

create table consumer (
    id int8 not null,
    activation_code varchar(255),
    email varchar(255) not null,
    first_name varchar(255) not null,
    hash_password varchar(255) not null,
    is_activated boolean not null,
    last_name varchar(255) not null,
    phone_number varchar(255),
    primary key (id)
    );

create table consumer_role (
    consumer_id int8 not null,
    roles varchar(255)
    );

create table order_line (
    id int8 not null,
    count int4 not null,
    cart_id int8,
    product_id int8,
    primary key (id)
    );

create table product (
    id int8 not null,
    company varchar(255) not null,
    description varchar(2048) not null,
    img_name varchar(255) not null,
    name varchar(255) not null,
    order_count int4 not null,
    price float8 not null, primary key (id)
    );

alter table cart
    add constraint cart_consumer_fk
    foreign key (consumer_id) references consumer;

alter table comment
    add constraint comment_consumer_fk
    foreign key (author_id) references consumer;

alter table comment
    add constraint comment_product_fk
    foreign key (product_id) references product;

alter table consumer_role
    add constraint consumer_role_fk
    foreign key (consumer_id) references consumer;

alter table order_line
    add constraint order_line_cart_fk
    foreign key (cart_id) references cart;

alter table order_line
    add constraint order_line_product_fk
    foreign key (product_id) references product;