-- table bouwvlak
create table bouwvlak
(
    id                    bigint not null primary key,
    planidentificatie     text   not null,
    identificatie         text   not null,
    naam                  text,
    styleid               text,
    md5hash               text
);

create index bouwvlak_planidentificatie_idx on bouwvlak (planidentificatie);

create index bouwvlak_identificatie_idx on bouwvlak (identificatie);

create unique index bouwvlak_planidentificatie_identificatie_idx on bouwvlak (planidentificatie, identificatie);

ALTER TABLE bouwvlak
    OWNER TO testuser;

ALTER TABLE bouwvlak
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME bouwvlak_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
