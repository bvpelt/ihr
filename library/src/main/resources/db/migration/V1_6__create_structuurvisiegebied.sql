-- table structuurvisiegebied
create table structuurvisiegebied
(
    id                bigint not null primary key,
    planidentificatie text,
    identificatie     text,
    naam              text,
    md5hash           text,
);

create index structuurvisiegebied_planidentificatie_idx on structuurvisiegebied (planidentificatie);

create index structuurvisiegebied_identificatie_idx on structuurvisiegebied (identificatie);

create unique index structuurvisiegebied_planidentificatie_identificatie_idx on structuurvisiegebied (planidentificatie, identificatie);

ALTER TABLE public.structuurvisiegebied
    OWNER TO testuser;

ALTER TABLE public.structuurvisiegebied
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.structuurvisiegebied_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
