-- table figuur

ALTER TABLE imroload
    add planloaded boolean default false,
    add tekstentried boolean default false,
    add bestemmingsvlakkentried boolean default false,
    add structuurvisiegebiedtried boolean default false,
    add bouwvlakkentried boolean default false,
    add functieaanduidingtried boolean default false,
    add bouwaanduidingtried boolean default false,
    add lettertekenaanduidingtried boolean default false,
    add maatvoeringtried boolean default false,
    add figuurtried boolean default false;

create index imroload_identificatie_tekstentried_idx on imroload (identificatie, tekstentried);
create index imroload_identificatie_bestemmingsvlakkentried_idx on imroload (identificatie, bestemmingsvlakkentried);
create index imroload_identificatie_structuurvisiegebiedtried_idx on imroload (identificatie, structuurvisiegebiedtried);
create index imroload_identificatie_bouwvlakkentried_idx on imroload (identificatie, bouwvlakkentried);
create index imroload_identificatie_functieaanduidingtried_idx on imroload (identificatie, functieaanduidingtried);
create index imroload_identificatie_bouwaanduidingtried_idx on imroload (identificatie, bouwaanduidingtried);
create index imroload_identificatie_lettertekenaanduidingtried_idx on imroload (identificatie, lettertekenaanduidingtried);
create index imroload_identificatie_maatvoeringtried_idx on imroload (identificatie, maatvoeringtried);
create index imroload_identificatie_figuurtried_idx on imroload (identificatie, figuurtried);
