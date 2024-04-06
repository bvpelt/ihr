
-- table thema
create table thema
(
    id                      bigint not null primary key,
    thema                   text
);

create unique index thema_thema_idx on thema (thema);

ALTER TABLE public.thema
    OWNER TO testuser;

ALTER TABLE public.thema
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.thema_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
