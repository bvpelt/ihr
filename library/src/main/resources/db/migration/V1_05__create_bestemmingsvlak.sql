-- table bestemmingsvlak
create table bestemmingsvlak
(
    id                    bigint not null primary key,
    planidentificatie     text   not null,
    identificatie         text   not null,
    type                  text,
    naam                  text,
    bestemmingshoofdgroep text,
    artikelnummer         text,
    labelinfo             text,
    md5hash               text
);

create index bestemmingsvlak_planidentificatie_idx on bestemmingsvlak (planidentificatie);

create index bestemmingsvlak_identificatie_idx on bestemmingsvlak (identificatie);

create unique index bestemmingsvlak_planidentificatie_identificatie_idx on bestemmingsvlak (planidentificatie, identificatie);

ALTER TABLE bestemmingsvlak
    OWNER TO testuser;

ALTER TABLE bestemmingsvlak
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME bestemmingsvlak_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
