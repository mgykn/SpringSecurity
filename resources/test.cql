CREATE TABLE omsgdb.msg_group_info(
company_id uuid,
group_id uuid,
group_name varchar,
group_desc varchar,
create_date TIMESTAMP,
PRIMARY KEY (group_id)
);

CREATE INDEX sec_inx_cid ON omsgdb.msg_group_info(company_id);
INSERT INTO omsgdb.msg_group_info(group_id, company_id, group_desc, group_name) VALUES (a894285e-fa11-11e8-99f5-913bd53ded60, 4077152c-83f4-48cc-ba6b-542b8644fc5f, 'Halil', 'aLTAR');

CREATE TABLE omsgdb.msg_group_list(
id uuid,
group_id uuid,
msisdn varchar,
name varchar,
surname varchar,
PRIMARY KEY ((group_id),id));

INSERT INTO omsgdb.msg_group_list(group_id, id, msisdn, name, surname) VALUES (a894285e-fa11-11e8-99f5-913bd53ded60, f8d8ace4-994c-4096-bd46-a403804c4090, '905425614935', 'Halil', 'Altar');

