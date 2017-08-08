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
-- Create table
create table bs_position_req_video
(
  req_id  varchar2(32) not null,
  in_time date default sysdate not null,
  is_open varchar2(2) default 1 not null
)
;
-- Add comments to the table 
comment on table bs_position_req_video
  is '职位申请视频表';
-- Add comments to the columns 
comment on column bs_position_req_video.req_id
  is '主键';
comment on column bs_position_req_video.is_open
  is '是否开放';
-- Create/Recreate primary, unique and foreign key constraints 
alter table bs_position_req_video
  add constraint pk_position_req_video primary key (REQ_ID);
/