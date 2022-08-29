create table cart_product
(
    id         bigint auto_increment
        primary key,
    cart_id    bigint not null,
    product_id bigint not null,
    constraint FK2kdlr8hs2bwl14u8oop49vrxi
        foreign key (product_id) references product (product_id),
    constraint FKlv5x4iresnv4xspvomrwd8ej9
        foreign key (cart_id) references cart (cart_id)
);