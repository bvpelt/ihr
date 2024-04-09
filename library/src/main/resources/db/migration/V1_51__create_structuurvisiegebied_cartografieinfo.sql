-- table structuurvisiegebied_cartografieinfo
create table structuurvisiegebied_cartografieinfo
(
    id                      bigint not null primary key,
    structuurvisiegebied_id bigint,
    cartografieinfo_id             bigint
);

create index structuurvisiegebied_cartografieinfo_structuurvisiegebied_idx on structuurvisiegebied_cartografieinfo (structuurvisiegebied_id);
create index structuurvisiegebied_cartografieinfo_cartografieinfo_idx on structuurvisiegebied_cartografieinfo (cartografieinfo_id);

ALTER TABLE structuurvisiegebied_cartografieinfo
    OWNER TO testuser;

ALTER TABLE structuurvisiegebied_cartografieinfo
ALTER
COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME structuurvisiegebied_cartografieinfo_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
