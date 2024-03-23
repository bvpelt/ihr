--
--  manytomany for gebiedsaanwijzing - artikel
--
create table gebiedsaanduiding_artikel
(
    id                   bigint not null primary key,
    gebiedsaanduiding_id bigint,
    artikel_id           bigint
);

create index gebiedsaanduiding_artikel_gebiedsaanduiding_idx on gebiedsaanduiding_artikel (gebiedsaanduiding_id);
create index gebiedsaanduiding_artikel_artikel_idx on gebiedsaanduiding_artikel (artikel_id);

ALTER TABLE public.gebiedsaanduiding_artikel
    OWNER TO testuser;

ALTER TABLE public.gebiedsaanduiding_artikel
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.gebiedsaanduiding_artikel_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

--
--  manytomany for gebiedsaanwijzing - tekstref
--
create table gebiedsaanduiding_tekstref
(
    id                   bigint not null primary key,
    gebiedsaanduiding_id bigint,
    tekstref_id          bigint
);

create index gebiedsaanduiding_tekstref_gebiedsaanwijzing_idx on gebiedsaanduiding_tekstref (gebiedsaanduiding_id);
create index gebiedsaanduiding_tekstref_tekstref_idx on gebiedsaanduiding_tekstref (tekstref_id);

ALTER TABLE public.gebiedsaanduiding_tekstref
    OWNER TO testuser;

ALTER TABLE public.gebiedsaanduiding_tekstref
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.gebiedsaanduiding_tekstref_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

--
--  manytomany for gebiedsaanwijzing - bestemmingsfunctie
--
create table gebiedsaanduiding_bestemmingsfunctie
(
    id                    bigint not null primary key,
    gebiedsaanduiding_id  bigint,
    bestemmingsfunctie_id bigint
);

create index gebiedsaanduiding_bestemmingsfunctie_gebiedsaanwijzing_idx on gebiedsaanduiding_bestemmingsfunctie (gebiedsaanduiding_id);
create index gebiedsaanduiding_bestemmingsfunctie_tekstref_idx on gebiedsaanduiding_bestemmingsfunctie (bestemmingsfunctie_id);

ALTER TABLE public.gebiedsaanduiding_bestemmingsfunctie
    OWNER TO testuser;

ALTER TABLE public.gebiedsaanduiding_bestemmingsfunctie
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.gebiedsaanduiding_bestemmingsfunctie_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
