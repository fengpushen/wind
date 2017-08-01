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
---------------------------------------------------------------
insert into frame_config
  (config_name, config_value, memo, config_type)
values
  ('rtmp_url_inside',
   'rtmp://10.137.5.189/myChat',
   '视频流位置(内网)',
   'busi_yyhr')
/
insert into frame_config
  (config_name, config_value, memo, config_type)
values
  ('ip_inside',
   '10.137.5.189:8088',
   '内网访问ip',
   'busi_yyhr')
/
-- Add/modify columns 
alter table BS_C_AREA_VIDEO add video_status varchar2(2) default 0;
-- Add comments to the columns 
comment on column BS_C_AREA_VIDEO.video_status
  is '邀请状态，0：尚未回应邀请；1：已经进入';
/
create or replace view v_C_AREA_VIDEO_last as
select *
  from BS_C_AREA_VIDEO a
 where not exists (select 1
          from BS_C_AREA_VIDEO b
         where a.c_id = b.c_id
           and a.area_code = b.area_code
           and a.in_time < b.in_time)
/