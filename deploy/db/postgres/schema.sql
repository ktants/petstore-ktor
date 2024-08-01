set search_path  = 'petclinic_sp';

drop table if exists vet_speciality cascade;
drop table if exists speciality cascade;
drop table if exists pet_visit cascade;
drop table if exists vet_photo cascade;
drop table if exists vet cascade;
drop table if exists pet_photo cascade;
drop table if exists pet cascade;
drop table if exists pet_gender cascade;
drop table if exists pet_type cascade;
drop table if exists pet_owner cascade;
drop table if exists photo cascade;

create table pet_gender
(
    id   bigint      not null,
    name varchar(60) not null,
    constraint pk_gender primary key (id),
    constraint check_gender check ( name is not null and length(name) > 0 )
);

CREATE TABLE vet
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(30),
    last_name  VARCHAR(30)
);

CREATE INDEX vets_last_name ON vet (last_name);

CREATE TABLE speciality
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(80)
);
CREATE INDEX speciality_name ON speciality (name);

CREATE TABLE vet_speciality
(
    vet_id       INTEGER NOT NULL,
    specialty_id INTEGER NOT NULL
);
alter table vet_speciality
    add constraint pk_vet_speciality primary key (vet_id, specialty_id);
ALTER TABLE vet_speciality
    ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vet (id);
ALTER TABLE vet_speciality
    ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES speciality (id);

CREATE TABLE pet_type
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(80) not null
);
CREATE unique index ux_type_name ON pet_type (name);

CREATE TABLE pet_owner
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(30),
    last_name  VARCHAR(30),
    address    VARCHAR(255),
    city       VARCHAR(80),
    telephone  VARCHAR(20)
);
CREATE INDEX owner_last_name ON pet_owner (last_name);

CREATE TABLE pet
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name       VARCHAR(30),
    birth_date DATE,
    type_id    INTEGER NOT NULL,
    owner_id   INTEGER NOT NULL,
    gender_id  integer
);
ALTER TABLE pet
    ADD CONSTRAINT fk_pet_owners FOREIGN KEY (owner_id) REFERENCES pet_owner (id);
ALTER TABLE pet
    ADD CONSTRAINT fk_pet_types FOREIGN KEY (type_id) REFERENCES pet_type (id);
alter table pet
    add constraint fk_pet_gender foreign key (gender_id) references pet_gender (id);

CREATE INDEX pet_name ON pet (name);

CREATE TABLE pet_visit
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pet_id      INTEGER NOT NULL,
    visit_date  DATE,
    description text
);
ALTER TABLE pet_visit
    ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pet (id);


create table photo
(
    id   bigint generated always as identity
        constraint photo_pk
            primary key,
    data bytea not null,
    meta json
);

comment on table photo is 'Keeps photos of pets and vets.';
comment on column photo.data is 'Actual image';
comment on column photo.meta is 'Some meta data associated with image.';

create table vet_photo
(
    photo_id bigint      not null,
    vet_id   bigint      not null,
    label    varchar(80) not null,
    constraint pk_vet_photo primary key (photo_id,vet_id),
    constraint fk_vet_photo foreign key (photo_id) references photo (id),
    constraint fk_vet foreign key (vet_id) references vet (id)
);

create unique index uix_vet_photo_label on vet_photo (label,vet_id);

create table pet_photo
(
    photo_id bigint      not null,
    pet_id   bigint      not null,
    label    varchar(80) not null,
    constraint pk_pet_photo primary key (photo_id, pet_id),
    constraint fk_pet_photo foreign key (photo_id) references photo (id),
    constraint fk_pet foreign key (pet_id) references pet (id)
);

create unique index uix_pet_photo_label on pet_photo (label, pet_id);

create or replace function bytea_import(p_path text, p_result out bytea)
    language plpgsql as
$$
declare
    l_oid oid;
    r     record;
begin
    p_result := '';
    select lo_import(p_path) into l_oid;
    for r in (select data
              from pg_largeobject
              where loid = l_oid
              order by pageno)
        loop
            p_result = p_result || r.data;
        end loop;
    perform lo_unlink(l_oid);
end;
$$;
