# ihr
IHR Informatie Huis Ruimte

# Postgres
- fuzzy search https://www.freecodecamp.org/news/fuzzy-string-matching-with-postgresql/

## Import data
In postgresql (psql) connected with the database
```sql
\copy imroload (identificatie) from data-1708678385058.csv with CSV HEADER;
```
## Usefull Queries

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

-- plannen met locatienaam en beleidsmatige overheid 
select p.identificatie, l.naam, o.* from plan_locatienaam pl, plan p, locatienaam l, overheid o where pl.plan_id = p.id and pl.locatienaam_id = l.id and p.beleidsmatigeoverheid_id = o.id order by o.code;

-- last loaded plan
select * from imroload where id = (select max(id) from imroload where loaded = true);

-- (re)load test data
select distinct(identificatie) into bart from imroload;
delete from imroload;
insert into imroload (identificatie) select identificatie from bart;
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

## OneToMany 
- https://www.baeldung.com/hibernate-one-to-many

## ManyToMany
Example https://attacomsian.com/blog/spring-data-jpa-many-to-many-mapping

- https://www.youtube.com/watch?v=jCYonZey5dY
- https://www.baeldung.com/jpa-many-to-many

```sql

select l1_0.plan_id, l1_1.id, l1_1.locatienaam 
from plan_locatienaam l1_0 
    join public.locatienaam l1_1 on l1_1.id = l1_0.locatienaam_id where l1_0.plan_id=?

```

# Bash scripts
## Delete all tables

 library/src/main/resource/db/migration
```bash
pushd library/src/main/resource/db/migration
grep -e '-- table' *.sql | sed -e 's/[^-]*-- table \(.*\)/drop table if exists \1;/'
grep -e '-- table' *.sql | sed -e 's/[^-]*-- table \(.*\)/\1;/' | sort | sed -e 's/^\(.*\);/select count\(*\) from \1;/'
popd
```
