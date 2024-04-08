-- table locatienaam
create table locatienaam
(
    id   bigint not null primary key,
    naam text
);

create index locatienaam_naam_idx on locatienaam (naam);

ALTER TABLE locatienaam
    OWNER TO testuser;

ALTER TABLE locatienaam
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME locatienaam_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
