-- table locatie
create table locatie
(
    id          bigint                    not null primary key,
    md5hash     varchar(40)               not null,
    geometrie   geometry(Geometry, 28992) not null,
    registratie timestamp default now()   not null
);

create unique index locatie_md5hash_idx on locatie (md5hash);

create index locatie_geometry_idx
    on locatie using gist (geometrie);

ALTER TABLE public.locatie
    OWNER TO testuser;

ALTER TABLE public.locatie
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.locatie_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
