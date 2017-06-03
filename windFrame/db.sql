delete from frame_code where code_name = 'degree'
/
insert into FRAME_CODE
  (CODE_NAME, CODE_INFO, CODE_TYPE)
values
  ('degree', '文化程度', 'busi_common')
/
delete from frame_code_map where code_name = 'degree'
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '11', '博士研究生', 1)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '14', '硕士研究生', 2)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '21', '大学本科', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '31', '大学专科', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '41', '中等专科', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '44', '职业高中', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '47', '技工学校', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '61', '普通高中', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '71', '初中', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '81', '小学', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '90', '其他', 3)
/
commit
/