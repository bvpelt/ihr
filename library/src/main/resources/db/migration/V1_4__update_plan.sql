alter table plan
    add besluitnummer text,
    add regelstatus text;


create index plan_dossier_idx on plan (dossierid);
create index plan_besluitnummer_idx on plan (besluitnummer);