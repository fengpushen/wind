insert into com_area
  (area_code,
   area_name,
   pcode,
   province_code,
   city_code,
   country_code,
   street_code,
   village_code,
   area_level)
  select org_code,
         org_name,
         org_up_code,
         s_code,
         sz_code,
         qx_code,
         jd_code,
         sq_code,
         org_level
    from hn_org
   where length(org_code) = 12
/