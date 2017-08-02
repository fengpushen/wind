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
v_busi_hr
-- Create table
create table bs_s_area_phone
(
  area_phone_id varchar2(32) default sys_guid() not null,
  area_code     varchar2(12) not null,
  phone         varchar2(20) not null,
  contractor    varchar2(20) not null
)
;
-- Add comments to the table 
comment on table bs_s_area_phone
  is '行政区联系表';
-- Add comments to the columns 
comment on column bs_s_area_phone.area_phone_id
  is '主键';
comment on column bs_s_area_phone.area_code
  is '地区编码';
comment on column bs_s_area_phone.phone
  is '联系电话';
comment on column bs_s_area_phone.contractor
  is '联系人';
-- Create/Recreate indexes 
create unique index ux_area_phone on bs_s_area_phone (area_code);
-- Create/Recreate primary, unique and foreign key constraints 
alter table bs_s_area_phone
  add constraint pk_area_phone primary key (AREA_PHONE_ID);
/
create or replace view v_code_map_job_lv as
select "CODE_NAME","CODE_KEY","CODE_VALUE","CODE_VALUE_ORDER" from frame_code_map where code_name = 'job_lv';
/