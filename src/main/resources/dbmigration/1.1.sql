-- apply changes
create table moderators_users (
  community_id                  bigint not null,
  users_id                      bigint not null,
  constraint pk_moderators_users primary key (community_id,users_id)
);

create table members_users (
  community_id                  bigint not null,
  users_id                      bigint not null,
  constraint pk_members_users primary key (community_id,users_id)
);

create index ix_moderators_users_community on moderators_users (community_id);
alter table moderators_users add constraint fk_moderators_users_community foreign key (community_id) references community (id) on delete restrict on update restrict;

create index ix_moderators_users_users on moderators_users (users_id);
alter table moderators_users add constraint fk_moderators_users_users foreign key (users_id) references users (id) on delete restrict on update restrict;

create index ix_members_users_community on members_users (community_id);
alter table members_users add constraint fk_members_users_community foreign key (community_id) references community (id) on delete restrict on update restrict;

create index ix_members_users_users on members_users (users_id);
alter table members_users add constraint fk_members_users_users foreign key (users_id) references users (id) on delete restrict on update restrict;

