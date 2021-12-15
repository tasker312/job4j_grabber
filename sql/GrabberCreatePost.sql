create table post
(
    id serial
        constraint post_pk
            primary key,
    name varchar(255) not null,
    text text not null,
    link varchar(255) not null,
    created timestamp not null
);

create unique index post_link_uindex
    on post (link);
