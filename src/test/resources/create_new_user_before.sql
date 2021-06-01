delete from consumer_role;
delete from consumer;
create extension if not exists pgcrypto;

insert into consumer(id, email, first_name, last_name, hash_password, is_activated)
    values (1, 'example@gmail.com', 'John', 'Snow', crypt('1234', gen_salt('bf', 8)), true);

insert into consumer_role(consumer_id, roles)
    values (1, 'USER');

