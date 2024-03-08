# ihr

IHR

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

```

# JPA Releations
See https://www.baeldung.com/hibernate-one-to-many 
