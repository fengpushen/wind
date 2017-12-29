drop table bs_hr_label
/
-- Create table
create table bs_hr_label
(
  label_id   varchar2(32) default sys_guid() not null,
  c_id       varchar2(32) not null,
  hr_id      varchar2(32) not null,
  label_code varchar2(2) default '10' not null,
  in_time    date default sysdate not null
)
;
-- Add comments to the table 
comment on table bs_hr_label
  is '企业标注人员表';
-- Add comments to the columns 
comment on column bs_hr_label.label_id
  is '主键';
comment on column bs_hr_label.c_id
  is '企业id';
comment on column bs_hr_label.hr_id
  is '人员id';
comment on column bs_hr_label.label_code
  is '标签值，10：关注';
-- Create/Recreate indexes 
create unique index ux_hr_label on bs_hr_label (c_id, hr_id);
-- Create/Recreate primary, unique and foreign key constraints 
alter table bs_hr_label
  add constraint pk_hr_label primary key (LABEL_ID);
/
delete from frame_code where code_name = 'label_code'
/
insert into frame_code
  (code_name, code_info, code_type)
values
  ('label_code', '企业对人员的标签', 'busi_yyhr')
/
delete from Frame_Code_Map where code_name = 'label_code'
/
insert into Frame_Code_Map
  (code_name, code_key, code_value, code_value_order)
values
  ('label_code', '10', '关注', 1)
/
create or replace view v_code_map_label as
select "CODE_NAME","CODE_KEY","CODE_VALUE","CODE_VALUE_ORDER" from frame_code_map where code_name = 'label_code'
/
create or replace view v_hr_label as 
select a.*, b.CODE_VALUE as label_name
  from bs_hr_label a
 inner join v_code_map_label b
    on a.label_code = b.CODE_KEY
/
----------------------------------------------------------------
create or replace view v_com_area_all_name as
select a.*,
       b.area_name as province_name,
       c.area_name as city_name,
       d.area_name as street_name,
       e.area_name as village_name,
       f.area_name as country_name
  from com_area a
  left outer join com_area b
    on a.province_code = b.area_code
  left outer join com_area c
    on a.city_code = c.area_code
  left outer join com_area d
    on a.street_code = d.area_code
  left outer join com_area e
    on a.village_code = e.area_code
    left outer join com_area f
    on a.country_code = f.area_code
/
delete from frame_code where code_name = 'area_level'
/
insert into frame_code
  (code_name, code_info, code_type)
values
  ('area_level', '地区层级', 'busi_yyhr')
/
delete from Frame_Code_Map where code_name = 'area_level'
/
insert into Frame_Code_Map
  (code_name, code_key, code_value, code_value_order)
values
  ('area_level', '0', '省', 1)
/
insert into Frame_Code_Map
  (code_name, code_key, code_value, code_value_order)
values
  ('area_level', '1', '市州', 2)
/
insert into Frame_Code_Map
  (code_name, code_key, code_value, code_value_order)
values
  ('area_level', '2', '区县', 3)
/
insert into Frame_Code_Map
  (code_name, code_key, code_value, code_value_order)
values
  ('area_level', '3', '乡镇/街道', 4)
/
insert into Frame_Code_Map
  (code_name, code_key, code_value, code_value_order)
values
  ('area_level', '4', '社区/村', 5)
/
update busi_hr set WANT_WORK_AREA_KIND = '20'
 where is_job = '0'
   and is_want_job = '1'
   and WANT_WORK_AREA_KIND is null
/