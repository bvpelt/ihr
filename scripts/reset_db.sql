delete from auditlog;
delete from locatie;
delete from overheid;
delete from plan;
delete from tekst;

update imroload set loaded = false;

ALTER SEQUENCE auditlog_id_seq RESTART WITH 1;
ALTER SEQUENCE locatie_id_seq RESTART WITH 1;
ALTER SEQUENCE overheid_id_seq RESTART WITH 1;
ALTER SEQUENCE plan_id_seq RESTART WITH 1;
ALTER SEQUENCE tekst_id_seq RESTART WITH 1;