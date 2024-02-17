create table plan
(
    id            bigint      not null primary key,
    identificatie text not null,
    naam          text
);

create index plan_identificatie_idx on plan (identificatie);

create index plan_naam_idx on plan (naam);

ALTER TABLE public.plan
    OWNER TO testuser;

ALTER TABLE public.plan
ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.plan_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );