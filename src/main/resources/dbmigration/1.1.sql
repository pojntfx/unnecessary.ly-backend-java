-- apply changes
create table users (
  id                            bigint generated by default as identity not null,
  display_name                  varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id)
);

