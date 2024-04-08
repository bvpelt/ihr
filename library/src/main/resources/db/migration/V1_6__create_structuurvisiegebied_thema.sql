-- table structuurvisiegebied_thema
create table structuurvisiegebied_thema
(
    id                      bigint not null primary key,
    structuurvisiegebied_id bigint,
    structuurvisiegebiedthema_id bigint
);

create index structuurvisiegebied_thema_structuurvisiegebied_idx on structuurvisiegebied_thema (structuurvisiegebied_id);
create index structuurvisiegebied_thema_structuurvisiegebiedthema_idx on structuurvisiegebied_thema (structuurvisiegebiedthema_id);

ALTER TABLE structuurvisiegebied_thema
    OWNER TO testuser;

ALTER TABLE structuurvisiegebied_thema
ALTER
COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME structuurvisiegebied_thema_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
