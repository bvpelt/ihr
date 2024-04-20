-- table plan_ondergrond
create table plan_ondergrond
(
    id            bigint not null primary key,
    plan_id       bigint,
    ondergrond_id bigint
);

create index plan_ondergrond_plan_idx on plan_ondergrond (plan_id);
create index plan_ondergrond_ondergrond_idx on plan_ondergrond (ondergrond_id);

ALTER TABLE plan_ondergrond
    OWNER TO testuser;

ALTER TABLE plan_ondergrond
    ALTER
        COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME plan_ondergrond_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );
