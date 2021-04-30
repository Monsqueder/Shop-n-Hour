insert into consumer (id, email, first_name, last_name, hash_password, is_activated)
    values (1, 'admin', 'ad', 'min', '123', true);

insert into consumer_role (consumer_id, roles)
    values (1, 'USER'), (1, 'ADMIN');