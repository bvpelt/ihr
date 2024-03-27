
delete from artikel;
delete from auditlog;
delete from bestemmingsvlak;
delete from bestemmingsvlak_bestemmingsfunctie;
delete from bestemmingsvlak_tekstref;
delete from gebiedsaanduiding;
delete from gebiedsaanduiding_artikel;
delete from gebiedsaanduiding_bestemmingsfunctie;
delete from gebiedsaanduiding_tekstref;
-- delete from imroload;
delete from locatie;
delete from locatienaam;
delete from overheid;
delete from plan;
delete from plan_locatienaam;
delete from structuurvisiegebied;
delete from structuurvisiegebied_beleid;
delete from structuurvisiegebied_thema;
delete from tekst;
delete from tekstref;

update imroload set loaded = false;