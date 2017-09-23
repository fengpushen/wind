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
----------------------------------------------------------------
v_busi_hr
/
create or replace view v_code_map_area_kind as
select "CODE_NAME","CODE_KEY","CODE_VALUE","CODE_VALUE_ORDER" from frame_code_map where code_name = 'area_kind'
/
create or replace view v_jobnojob_list as
select h_job_id as id,
       '1' as is_job,
       hr_id,
       job_time,
       job_area,
       job_dw,
       job_gw,
       job_type,
       income,
       sy_month,
       add_months(to_date(job_time, 'yyyy-mm-dd'), nvl(sy_month, 0)) as wd_job_time,
       null as nojob_reason,
       null as nojob_dw,
       in_time,
       opr_id,
       opr_type
  from BS_H_JOB
union all
select h_nojob_id as id,
       '0' as is_job,
       hr_id,
       nojob_time as job_time,
       null as job_area,
       null as job_dw,
       null as job_gw,
       null as job_type,
       null as income,
       null as sy_month,
       null as wd_job_time,
       nojob_reason,
       nojob_dw,
       in_time,
       opr_id,
       opr_type
  from bs_h_nojob;
/
create or replace view v_jobnojob_list_name as
select a."ID",a."IS_JOB",a."HR_ID",a."JOB_TIME",a."JOB_AREA",a."JOB_DW",a."JOB_GW",a."JOB_TYPE",a."INCOME",a."NOJOB_REASON",a."NOJOB_DW",a."IN_TIME",a."OPR_ID",a."OPR_TYPE",
       a.sy_month,
       a.wd_job_time,
       b.area_name as job_area_name,
       c.CODE_VALUE as job_type_name,
       decode(is_job, '1', '入职', '0', '离职') is_job_name,
       to_char(a.in_time, 'yyyy-mm-dd') as in_time_job_str,
       to_char(a.wd_job_time, 'yyyy-mm-dd') as wd_job_time_str
  from v_jobnojob_list a
  left outer join com_area b
    on a.job_area = b.area_code
  left outer join v_code_map_jobtype c
    on c.CODE_KEY = a.job_type;
/
-- Create table
create table bs_c_hire
(
  hire_id   varchar2(32) default sys_guid() not null,
  c_id      varchar2(32) not null,
  hr_id     varchar2(32) not null,
  hire_time varchar2(10) not null,
  in_time   date default sysdate not null,
  sy_month  INTEGER,
  quit_time varchar2(10),
  quit_reason varchar2(100),
  is_wd     varchar2(2) default 0 not null
)
;
-- Add comments to the table 
comment on table bs_c_hire
  is '企业录用人员表';
-- Add comments to the columns 
comment on column bs_c_hire.hire_id
  is '主键';
comment on column bs_c_hire.c_id
  is '企业id';
comment on column bs_c_hire.hr_id
  is '人员id';
comment on column bs_c_hire.hire_time
  is '录用时间';
comment on column bs_c_hire.in_time
  is '记录插入时间';
comment on column bs_c_hire.sy_month
  is '试用期';
comment on column bs_c_hire.quit_time
  is '离职时间';
comment on column bs_c_hire.quit_reason
  is '离职原因';
comment on column bs_c_hire.is_wd
  is '是否稳定就业';
-- Create/Recreate indexes 
create unique index ux_c_hire on bs_c_hire (c_id, hr_id, hire_time);
-- Create/Recreate primary, unique and foreign key constraints 
alter table bs_c_hire
  add constraint pk_c_hire primary key (HIRE_ID);
alter table BS_H_NOJOB modify h_nojob_id default sys_guid();
-- Add/modify columns 
alter table BS_C_HIRE add in_c_name VARCHAR2(40);
-- Add comments to the columns 
comment on column BS_C_HIRE.in_c_name
  is '入职单位';