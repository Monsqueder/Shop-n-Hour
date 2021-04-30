create extension if not exists pgcrypto;

update consumer set hash_password = crypt(hash_password, gen_salt('bf', 8));