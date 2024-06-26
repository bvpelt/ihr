-- table figuur
create table figuur
(
    id                bigint not null primary key,
    planidentificatie text   not null,
    identificatie     text   not null,
    naam              text,
    labelinfo         text,
    styleid           text,
    md5hash           text
);

create index figuur_planidentificatie_idx on figuur (planidentificatie);

create index figuur_identificatie_idx on figuur (identificatie);

create unique index figuur_planidentificatie_identificatie_idx on figuur (planidentificatie, identificatie);

ALTER TABLE figuur
    OWNER TO testuser;

ALTER TABLE figuur
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME figuur_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

-- table figuur_artikel
create table figuur_artikel
(
    id         bigint not null primary key,
    figuur_id  bigint,
    artikel_id bigint
);

create index figuur_artikel_maatvoering_idx on figuur_artikel (figuur_id);
create index figuur_artikel_omvang_idx on figuur_artikel (artikel_id);

ALTER TABLE figuur_artikel
    OWNER TO testuser;

ALTER TABLE figuur_artikel
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME figuur_artikel_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

-- table figuur_tekstref
create table figuur_tekstref
(
    id          bigint not null primary key,
    figuur_id   bigint,
    tekstref_id bigint
);

create index figuur_tekstref_maatvoering_idx on figuur_tekstref (figuur_id);
create index figuur_tekstref_omvang_idx on figuur_tekstref (tekstref_id);

ALTER TABLE figuur_tekstref
    OWNER TO testuser;

ALTER TABLE figuur_tekstref
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME figuur_tekstref_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

-- table figuur_illustratie
create table figuur_illustratie
(
    id             bigint not null primary key,
    figuur_id      bigint,
    illustratie_id bigint
);

create index figuur_illustratie_maatvoering_idx on figuur_illustratie (figuur_id);
create index figuur_illustratie_omvang_idx on figuur_illustratie (illustratie_id);

ALTER TABLE figuur_illustratie
    OWNER TO testuser;

ALTER TABLE figuur_illustratie
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME figuur_illustratie_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
