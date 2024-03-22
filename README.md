# ihr

IHR

# Import data
In postgresql (psql) connected with the database
```sql
\copy imroload (identificatie) from data-1708678385058.csv with CSV HEADER;
```
# Usefull Queries

```sql

-- maximum fieldlength
select max(nl) from (select id, length(naam) as nl from plan);

-- identificatie of plan which occurs > 1
select aantal, identificatie from (select count(*) as aantal, identificatie from plan group by identificatie) where aantal > 1;

-- dossier with 3 plans > 1
select aantal, dossierid from (select count(*) as aantal, dossierid from plan group by dossierid) where aantal > 1;

-- plannen group by dossier
select * from plan 
         where dossierid in (
            select dossierid from (
                select count(*) as aantal, dossierid from plan group by dossierid
            ) 
            where aantal > 1
        ) 
order by identificatie;

-- plannen with teksten
select p.identificatie, t.planidentificatie from plan p left join tekst t on (p.identificatie=t.planidentificatie) where t.planidentificatie is not null;

-- plannen without teksten
select p.identificatie, t.planidentificatie from plan p left join tekst t on (p.identificatie=t.planidentificatie) where t.planidentificatie is null;


select distinct(identificatie) into bart from imroload;
delete from imroload;
insert into imroload (identificatie) select identificatie from bart;


drop table artikel;
drop table auditlog;
drop table bestemmingsvlak;
drop table bestemmingsvlak_bestemmingsfunctie;
drop table bestemmingsvlak_tekstref;
drop table gebiedsaanduiding;
drop table imroload;
drop table locatie;
drop table plan;
drop table structuurvisiegebied;
drop table structuurvisiegebiedbeleid;
drop table structuurvisiegebiedthema;
drop table tekst;
drop table tekstref;

delete from flyway_schema_history;
```

# Check
```bash
curl -X 'GET' \
  'https://ruimte.omgevingswet.overheid.nl/ruimtelijke-plannen/api/opvragen/v4/plannen?page=1&pageSize=10' \
  -H "X-Api-Key: l7f833bd256c04444da04b0fe9ad3def88" \
  -H 'accept: application/hal+json' \
  -H 'Accept-Crs: epsg:28992'
  ```
# JPA Releations
See 
- https://www.baeldung.com/hibernate-one-to-many
- https://www.baeldung.com/jpa-many-to-many



```sql

select l1_0.plan_id, l1_1.id, l1_1.locatienaam 
from plan_locatienaam l1_0 
    join public.locatienaam l1_1 on l1_1.id = l1_0.locatienaam_id where l1_0.plan_id=?

```