-- table tekst
create table tekst
(
    id                 bigint not null primary key,
    planidentificatie  text   not null,
    tekstidentificatie text   not null,
    titel              text,
    inhoud             text,
    volgnummer         int,
    externhref         text,
    externlabel        text
);

create unique index tekst_planidentificatie_idx on tekst (planidentificatie, volgnummer);

create index tekst_tekstidentificatie_idx on tekst (tekstidentificatie);

create unique index tekst_textidentificatie_volgnr_idx on tekst (planidentificatie, tekstidentificatie, volgnummer);

ALTER TABLE public.tekst
    OWNER TO testuser;

ALTER TABLE public.tekst
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.tekst_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
