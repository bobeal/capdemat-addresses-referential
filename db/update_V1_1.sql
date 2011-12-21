alter table city drop constraint fk200d8b88194376;
alter table way drop constraint fk152cf88194376;
alter table import drop constraint fk82c65fc588194376;
alter table accesscontrol drop constraint fk5f375f3988194376;
alter table importlog drop constraint fk6b03f93f320ca4bb;
alter table accesscontrol drop constraint fk5f375f3955fd001e;
alter table expirationnotification drop constraint fk9e96455ad12fe56;

alter table city add constraint fk_referential_city foreign key (referential_id) references referential (id) match simple on update cascade on delete cascade;
alter table way add constraint fk_referential_way foreign key (referential_id) references referential (id) match simple on update cascade on delete cascade;
alter table import add constraint fk_referential_import foreign key (referential_id) references referential (id) match simple on update cascade on delete cascade;
alter table accesscontrol add constraint fk_referential_accesscontrol foreign key (referential_id) references referential (id) match simple on update cascade on delete cascade;
alter table importlog add constraint fk_referential_importlog foreign key (importentity_id) references import (id) match simple on update cascade on delete cascade;
alter table accesscontrol add constraint fk_customer_accesscontrol foreign key (customer_id) references customer (id) match simple on update cascade on delete cascade;
alter table expirationnotification add constraint fk_accesscontrol_expirationnotification foreign key (accesscontrol_id) references accesscontrol (id) match simple on update cascade on delete cascade;

alter table importlog add line bigint;

