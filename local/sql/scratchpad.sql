set schema 'petclinic_sp';

--- pet view
select p.id   as id,
       p.name as pet,
       t.name as type,
       g.name as gender,
       f.data as photo,
       f.meta as photo_meta
from petclinic_sp.pet as p
         join petclinic_sp.type t on t.id = p.type_id
         join petclinic_sp.gender as g on g.id = p.gender_id
         left join petclinic_sp.pet_photo pp on p.id = pp.pet_id and pp.label = 'pet_folder_front'
         left join petclinic_sp.photo f on pp.photo_id = f.id
order by t.name, g.name;
