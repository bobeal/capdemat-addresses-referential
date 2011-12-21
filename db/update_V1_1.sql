alter table city drop constraint fk200d8b88194376;
alter table way drop constraint fk152cf88194376;

alter table city add constraint fk_referential_city foreign key (referential_id) references referential (id) match simple on update cascade on delete cascade;
alter table way add constraint fk_referential_way foreign key (referential_id) references referential (id) match simple on update cascade on delete cascade;

alter table importlog add line bigint;

