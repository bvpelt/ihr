-- table structuurvisiegebied_illustratie
create table structuurvisiegebied_illustratie
(
    id                      bigint not null primary key,
    structuurvisiegebied_id bigint,
    illustratie_id bigint
);

create index structuurvisiegebied_illustratie_structuurvisiegebied_idx on structuurvisiegebied_illustratie (structuurvisiegebied_id);
create index structuurvisiegebied_illustratie_structuurvisiegebiedbeleid_idx on structuurvisiegebied_illustratie (illustratie_id);

ALTER TABLE structuurvisiegebied_illustratie
    OWNER TO testuser;

ALTER TABLE structuurvisiegebied_illustratie
ALTER
COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME structuurvisiegebied_illustratie_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
