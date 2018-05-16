create table author_data
(
	id bigint auto_increment
		primary key,
	date date null,
	email varchar(255) null,
	name varchar(255) null
)
engine=InnoDB
;

create table owner_data
(
	id bigint auto_increment
		primary key,
	login varchar(255) null,
	site_admin bit null,
	constraint UK_2w16cjbxaxset61r6hmqj1vgp
		unique (login)
)
engine=InnoDB
;

create table git_hub_data
(
	id bigint auto_increment
		primary key,
	commits_url varchar(255) null,
	description varchar(255) null,
	error varchar(255) null,
	full_name varchar(255) null,
	stargazer_counts varchar(255) null,
	url varchar(255) null,
	watchers_count int null,
	owner_id bigint null,
	constraint UK_nye2m5h7wup1pao9qf7cpgh90
		unique (full_name),
	constraint FKtkclonmcsrus92u09vuamlr5j
		foreign key (owner_id) references owner_data (id)
)
engine=InnoDB
;

create index FKtkclonmcsrus92u09vuamlr5j
	on git_hub_data (owner_id)
;

create table single_commit
(
	id bigint auto_increment
		primary key,
	message varchar(255) null,
	author_id bigint null,
	commiter_id bigint null,
	constraint FK49fkhegs01i2pb4aupo7u91kv
		foreign key (author_id) references author_data (id),
	constraint FKnhqpow79i89u6ryr4sno2qv4i
		foreign key (commiter_id) references author_data (id)
)
engine=InnoDB
;

create table commit_data
(
	id bigint auto_increment
		primary key,
	url varchar(255) null,
	commit_id bigint null,
	constraint FKt9o90ivuiwuv9a58e70vei57n
		foreign key (commit_id) references single_commit (id)
)
engine=InnoDB
;

create index FKt9o90ivuiwuv9a58e70vei57n
	on commit_data (commit_id)
;

create index FK49fkhegs01i2pb4aupo7u91kv
	on single_commit (author_id)
;

create index FKnhqpow79i89u6ryr4sno2qv4i
	on single_commit (commiter_id)
;