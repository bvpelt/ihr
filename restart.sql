drop table if exists auditlog;
drop table if exists imroload;
drop table if exists plan;
drop table if exists tekst;
drop table if exists locatie;
drop table if exists bestemmingsvlak;
drop table if exists beleid;
drop table if exists thema;
drop table if exists artikel;
drop table if exists tekstref;
drop table if exists locatienaam;
drop table if exists overheid;
drop table if exists illustratie;
drop table if exists bestemmingsfunctie;
drop table if exists externplan;
drop table if exists gebiedsaanduiding;
drop table if exists structuurvisiegebied_beleid;
drop table if exists structuurvisiegebied_externplan_tengevolgevan;
drop table if exists structuurvisiegebied_externplan_gebruiktinformatieuit;
drop table if exists structuurvisiegebied_tekstref;
drop table if exists structuurvisiegebied_externplan_uitgewerktin;
drop table if exists structuurvisiegebied;
drop table if exists bestemmingsvlak_bestemmingsfunctie;
drop table if exists bestemmingsvlak_tekstref;
drop table if exists gebiedsaanduiding_artikel;
drop table if exists gebiedsaanduiding_bestemmingsfunctie;
drop table if exists gebiedsaanduiding_tekstref;
drop table if exists plan_locatienaam;
drop table if exists structuurvisiegebied_illustratie;
drop table if exists structuurvisiegebied_locatie;
drop table if exists structuurvisiegebied_thema;
drop table if exists cartografieinfo;
drop table if exists structuurvisiegebied_cartografieinfo;
drop table if exists structuurvisiegebied_externplan_uittewerkenin;
drop table if exists planstatus;
drop table if exists plan_verwijzingnorm;
drop table if exists verwijzingnorm;
drop table if exists kruimelpad;

drop table if exists normadressant;
drop table if exists ondergrond;
drop table if exists plan_normadressant;
drop table if exists plan_ondergrond;


delete from flyway_schema_history;

