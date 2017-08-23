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