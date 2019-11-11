create table users
(
	id varchar(36) not null
		constraint users_pkey
			primary key,
	username varchar(64),
	email varchar(255) not null
		constraint users_email_key
			unique,
	password varchar(255) not null,
	birthday date,
	gender varchar(16),
	height smallint,
	weight smallint,
	enabled boolean
);

create table roles
(
	id varchar(36) not null
		constraint roles_pk
			primary key,
	role varchar(32) not null
);

create unique index roles_role_uindex
	on roles (role);

create unique index roles_id_uindex
	on roles (id);
	
INSERT INTO public.roles (id, role) VALUES ('8600dac9-a123-4e6e-91b1-14276ffca3f2', 'ROLE_USER');
INSERT INTO public.roles (id, role) VALUES ('ade2c8f3-2565-44e0-b0da-77dea5398802', 'ROLE_ADMIN');
INSERT INTO public.roles (id, role) VALUES ('cbc7c5c7-5da8-4b46-935e-caa5a27f2b33', 'ROLE_MODERATOR');

create table users_roles
(
	role_id varchar(36) not null
		constraint users_roles_roles_id_fk
			references roles
				on delete cascade,
	user_id varchar(36) not null
		constraint users_roles_users_id_fk
			references users
				on delete cascade
);

create table ingredients
(
	id varchar(36) not null
		constraint ingredients_pkey
			primary key,
	name varchar(30)
);

create table receipts
(
	id varchar(36) not null
		constraint table_name_pk
			primary key,
	name varchar(255) not null,
	calories integer,
	proteins real,
	fats real,
	carbohydrates real,
	rating real
);

create unique index table_name_id_uindex
	on receipts (id);

create table receipt_steps
(
	id varchar(36) not null
		constraint receipt_steps_pk
			primary key,
	description varchar(2048),
	picture varchar(36),
	receipt_id varchar(36)
		constraint receipt_steps_receipts_id_fk
			references receipts
				on delete cascade
);

create table ration_categories
(
	id varchar(36) not null
		constraint ration_categories_pk
			primary key,
	name varchar(255),
	owner varchar(36)
		constraint ration_categories_users_id_fk
			references users
				on delete cascade
);

create table ration_items
(
	id varchar(36) not null
		constraint ration_items_pk
			primary key,
	date date,
	category varchar(36)
		constraint ration_items_ration_categories_id_fk
			references ration_categories
				on delete cascade,
	owner varchar(36)
		constraint ration_items_users_id_fk
			references users
				on delete cascade,
	receipt varchar(36)
		constraint ration_items_receipts_id_fk
			references receipts
				on delete cascade
);

create table tag_categories
(
	id varchar(36) not null
		constraint tag_categories_pk
			primary key,
	name varchar(255)
);

create table tags
(
	id varchar(36) not null
		constraint tags_pk
			primary key,
	name varchar(255) not null,
	category varchar(36)
		constraint tags_tag_categories_id_fk
			references tag_categories
				on delete cascade
);

create unique index tags_name_uindex
	on tags (name);

create unique index tag_categories_name_uindex
	on tag_categories (name);

create table tags_receipts
(
	tag_id varchar(36)
		constraint tags_receipts_tags_id_fk
			references tags
				on delete cascade,
	receipt_id varchar(36)
		constraint tags_receipts_receipts_id_fk
			references receipts
				on delete cascade
);

create table ingredients_receipts
(
	ingredient_id varchar(36)
		constraint ingredients_receipts_ingredients_id_fk
			references ingredients
				on delete cascade,
	receipt_id varchar(36)
		constraint ingredients_receipts_receipts_id_fk
			references receipts
				on delete cascade,
	value_type varchar(16),
	value real
);

create table user_filters
(
	id varchar(36) not null
		constraint user_filters_pk
			primary key,
	name varchar(255),
	enabled_from date,
	enabled_until date,
	creator varchar(36)
		constraint user_filters_users_id_fk
			references users
);

create table global_filters
(
	id varchar(36) not null
		constraint global_filters_pk
			primary key,
	name varchar(255),
	enabled_from date,
	enabled_until date
);

create table filters_tags
(
	filter_id varchar(36)
		constraint filters_tags_global_filters_id_fk
			references global_filters
				on delete cascade
		constraint filters_tags_user_filters_id_fk
			references user_filters
				on delete cascade,
	tag_id varchar(36)
		constraint filters_tags_tags_id_fk
			references tags
				on delete cascade
);

create table filter_ingredients
(
	filter_id varchar(36)
		constraint filter_ingredients_global_filters_id_fk
			references global_filters
				on delete cascade
		constraint filter_ingredients_user_filters_id_fk
			references user_filters
				on delete cascade,
	ingredient_id varchar(36)
		constraint filter_ingredients_ingredients_id_fk
			references ingredients
				on delete cascade
);

create table receipt_reviews
(
	id varchar(36) not null
		constraint receipt_reviews_pk
			primary key,
	"user" varchar(36)
		constraint receipt_reviews_users_id_fk
			references users
				on delete set null,
	receipt varchar(36)
		constraint receipt_reviews_receipts_id_fk
			references receipts
				on delete cascade,
	created_on date,
	rating integer,
	review varchar(1024)
);