delete from product_colors;
delete from product_sizes;
delete from product_img_name;
delete from product;

insert into product(id, company, description, is_disabled, name, order_count, price, rating, category_id)
    values (1, 'company', 'description', false, 'product 1', 0, 10.0, 0, 4),
            (2, 'company', 'description', false, 'product 2', 0, 20.0, 0, 3);

insert into product_colors(product_id, colors)
    values (1, 'black'), (1, 'white'), (2, 'Red');

insert into product_img_name(product_id, img_name)
    values (1, 'img1.png'), (2, 'img2.png');

insert into product_sizes(product_id, sizes)
    values (1, 'l'), (1, 'xxl'), (2, 'm'), (2, 'l');