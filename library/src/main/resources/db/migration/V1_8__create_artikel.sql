--
--  artikel
--
create table artikel
(
    id                      bigint not null primary key,
    referentie              text,
    structuurvisiegebied_id bigint,
    gebiedsaanduiding_id    bigint
);

create index artikel_gebiedsaanduiding_idx on artikel (gebiedsaanduiding_id);

ALTER TABLE public.artikel
    OWNER TO testuser;

ALTER TABLE public.artikel
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.artikel_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
