# ihr
IHR Informatie Huis Ruimte

# Structuurvisie
probleem with NL.IMRO.0262.xxStructuurv2012-SV41

# Swagger
To see swagger specification of the synchronisation, first start synchronisation
- See http://localhost:8080/swagger-ui/index.html

# Java
- Serialisation see https://www.baeldung.com/java-serial-version-uid

# Postgres
- fuzzy search https://www.freecodecamp.org/news/fuzzy-string-matching-with-postgresql/

## Postgis
- see https://postgis.net/documentation/getting_started/
- ubuntu https://trac.osgeo.org/postgis/wiki/UsersWikiPostGIS3UbuntuPGSQLApt

```sql
 SELECT * FROM pg_available_extensions order by name;
name              | default_version | installed_version |                                                       comment                                                       
--------------------------------+-----------------+-------------------+---------------------------------------------------------------------------------------------------------------------
address_standardizer            | 3.4.2           |                   | Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.
 address_standardizer-3         | 3.4.2           |                   | Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.
 address_standardizer_data_us   | 3.4.2           |                   | Address Standardizer US dataset example
 address_standardizer_data_us-3 | 3.4.2           |                   | Address Standardizer US dataset example
 adminpack                      | 2.1             |                   | administrative functions for PostgreSQL
 amcheck                        | 1.3             |                   | functions for verifying relation integrity
 autoinc                        | 1.0             |                   | functions for autoincrementing fields
 bloom                          | 1.0             |                   | bloom access method - signature file based index
 btree_gin                      | 1.3             |                   | support for indexing common datatypes in GIN
 btree_gist                     | 1.7             |                   | support for indexing common datatypes in GiST
 citext                         | 1.6             |                   | data type for case-insensitive character strings
 cube                           | 1.5             |                   | data type for multidimensional cubes
 dblink                         | 1.2             |                   | connect to other PostgreSQL databases from within a database
 dict_int                       | 1.0             |                   | text search dictionary template for integers
 dict_xsyn                      | 1.0             |                   | text search dictionary template for extended synonym processing
 earthdistance                  | 1.1             |                   | calculate great-circle distances on the surface of the Earth
 file_fdw                       | 1.0             |                   | foreign-data wrapper for flat file access
 fuzzystrmatch                  | 1.2             |                   | determine similarities and distance between strings
 hstore                         | 1.8             |                   | data type for storing sets of (key, value) pairs
 insert_username                | 1.0             |                   | functions for tracking who changed a table
 intagg                         | 1.1             |                   | integer aggregator and enumerator (obsolete)
 intarray                       | 1.5             |                   | functions, operators, and index support for 1-D arrays of integers
 isn                            | 1.2             |                   | data types for international product numbering standards
 lo                             | 1.1             |                   | Large Object maintenance
 ltree                          | 1.2             |                   | data type for hierarchical tree-like structures
 moddatetime                    | 1.0             |                   | functions for tracking last modification time
 old_snapshot                   | 1.0             |                   | utilities in support of old_snapshot_threshold
 pageinspect                    | 1.12            |                   | inspect the contents of database pages at a low level
 pg_buffercache                 | 1.4             |                   | examine the shared buffer cache
 pg_freespacemap                | 1.2             |                   | examine the free space map (FSM)
 pg_prewarm                     | 1.2             |                   | prewarm relation data
 pg_stat_statements             | 1.10            |                   | track planning and execution statistics of all SQL statements executed
 pg_surgery                     | 1.0             |                   | extension to perform surgery on a damaged relation
 pg_trgm                        | 1.6             |                   | text similarity measurement and index searching based on trigrams
 pg_visibility                  | 1.2             |                   | examine the visibility map (VM) and page-level visibility info
 pg_walinspect                  | 1.1             |                   | functions to inspect contents of PostgreSQL Write-Ahead Log
 pgcrypto                       | 1.3             |                   | cryptographic functions
 pgrowlocks                     | 1.2             |                   | show row-level locking information
 pgstattuple                    | 1.5             |                   | show tuple-level statistics
 plpgsql                        | 1.0             | 1.0               | PL/pgSQL procedural language
 postgis                        | 3.4.2           | 3.4.2             | PostGIS geometry and geography spatial types and functions
 postgis-3                      | 3.4.2           |                   | PostGIS geometry and geography spatial types and functions
 postgis_raster                 | 3.4.2           |                   | PostGIS raster types and functions
 postgis_raster-3               | 3.4.2           |                   | PostGIS raster types and functions
 postgis_sfcgal                 | 3.4.2           |                   | PostGIS SFCGAL functions
 postgis_sfcgal-3               | 3.4.2           |                   | PostGIS SFCGAL functions
 postgis_tiger_geocoder         | 3.4.2           |                   | PostGIS tiger geocoder and reverse geocoder
 postgis_tiger_geocoder-3       | 3.4.2           |                   | PostGIS tiger geocoder and reverse geocoder
 postgis_topology               | 3.4.2           |                   | PostGIS topology spatial types and functions
 postgis_topology-3             | 3.4.2           |                   | PostGIS topology spatial types and functions
 postgres_fdw                   | 1.1             |                   | foreign-data wrapper for remote PostgreSQL servers
 refint                         | 1.0             |                   | functions for implementing referential integrity (obsolete)
 seg                            | 1.4             |                   | data type for representing line segments or floating-point intervals
 sslinfo                        | 1.2             |                   | information about SSL certificates
 tablefunc                      | 1.0             |                   | functions that manipulate whole tables, including crosstab
 tcn                            | 1.0             |                   | Triggered change notifications
 tsm_system_rows                | 1.0             |                   | TABLESAMPLE method which accepts number of rows as a limit
 tsm_system_time                | 1.0             |                   | TABLESAMPLE method which accepts time in milliseconds as a limit
 unaccent                       | 1.1             |                   | text search dictionary that removes accents
 uuid-ossp                      | 1.1             |                   | generate universally unique identifiers (UUIDs)
 xml2                           | 1.1             |                   | XPath querying and XSLT
(61 rows)


```

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

# Check relation external plannen

```sql
select * from externplan where vervangtmetplan_id=858;
select * from externplan where tengevolgevanmetplan_id=858;
select * from externplan where muteertmetplan_id=858;
select * from externplan where gebruiktinfouitmetplan_id=858;
select * from externplan where gedeeltelijkeherzieningmetplan_id=858;
select * from externplan where uittewerkinginmetplan_id=858;
select * from externplan where uitgewerktinmetplan_id=858;
select * from externplan where vervangtvanuitplan_id=858;
select * from externplan where tegevolgevanvanuitplan_id=858;
select * from externplan where muteertvanuitplan_id=858;
select * from externplan where gebruiktinforuitvanuitplan_id=858;
select * from externplan where gedeeltelijkeherzieningvanuitplan_id=858;
select * from externplan where uittewerkinginvanuitplan_id=858;
select * from externplan where uitgewerktinvanuitplan_id=858;

select * from externplan where vervangtmetplan_id=858 or
    tengevolgevanmetplan_id=858 or
    muteertmetplan_id=858 or
    gebruiktinfouitmetplan_id=858 or
    gedeeltelijkeherzieningmetplan_id=858 or
    uittewerkinginmetplan_id=858 or
    uitgewerktinmetplan_id=858 or
    vervangtvanuitplan_id=858 or
    tegevolgevanvanuitplan_id=858 or
    muteertvanuitplan_id=858 or
    gebruiktinforuitvanuitplan_id=858 or
    gedeeltelijkeherzieningvanuitplan_id=858 or
    uittewerkinginvanuitplan_id=858 or
    uitgewerktinvanuitplan_id=858;

-- ref: https://stackoverflow.com/questions/4547672/return-multiple-fields-as-a-record-in-postgresql-with-pl-pgsql
create or replace function check_plan_relation_on_id (in bigint) returns record
as $$
    declare ret record;
begin    
    select * from externplan where vervangtmetplan_id=$1 or
    tengevolgevanmetplan_id=$1 or
    muteertmetplan_id=$1 or
    gebruiktinfouitmetplan_id=$1 or
    gedeeltelijkeherzieningmetplan_id=$1 or
    uittewerkinginmetplan_id=$1 or
    uitgewerktinmetplan_id=$1 or
    vervangtvanuitplan_id=$1 or
    tegevolgevanvanuitplan_id=$1 or
    muteertvanuitplan_id=$1 or
    gebruiktinforuitvanuitplan_id=$1 or
    gedeeltelijkeherzieningvanuitplan_id=$1 or
    uittewerkinginvanuitplan_id=$1 or
    uitgewerktinvanuitplan_id=$1  into ret;
return ret;    
end;$$ language plpgsql;
 
-- example: select check_plan_relation_on_id(862);

select id, naam, identificatie, planstatus, planstatusdate, dossier, href, vervangtmetplan_id, tengevolgevanmetplan_id, muteertmetplan_id, 
       gebruiktinfouitmetplan_id, gedeeltelijkeherzieningmetplan_id, uittewerkinginmetplan_id, uitgewerktinmetplan_id, vervangtvanuitplan_id,  
 tegevolgevanvanuitplan_id, muteertvanuitplan_id, gebruiktinforuitvanuitplan_id, gedeeltelijkeherzieningvanuitplan_id, uittewerkinginvanuitplan_id, uitgewerktinvanuitplan_id 
from  check_plan_relation_on_id(862)
    as (id bigint, naam text, identificatie text, planstatus text, planstatusdate text, dossier  text, href text,vervangtmetplan_id  bigint, tengevolgevanmetplan_id  bigint,
    muteertmetplan_id  bigint, gebruiktinfouitmetplan_id bigint, gedeeltelijkeherzieningmetplan_id bigint, uittewerkinginmetplan_id bigint, uitgewerktinmetplan_id bigint,
    vervangtvanuitplan_id bigint, tegevolgevanvanuitplan_id bigint, muteertvanuitplan_id   bigint, gebruiktinforuitvanuitplan_id bigint, gedeeltelijkeherzieningvanuitplan_id bigint,
        uittewerkinginvanuitplan_id  bigint, uitgewerktinvanuitplan_id  bigint);
```