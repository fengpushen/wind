delete from frame_code where code_name = 'degree'
/
insert into FRAME_CODE
  (CODE_NAME, CODE_INFO, CODE_TYPE)
values
  ('degree', '�Ļ��̶�', 'busi_common')
/
delete from frame_code_map where code_name = 'degree'
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '11', '��ʿ�о���', 1)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '14', '˶ʿ�о���', 2)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '21', '��ѧ����', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '31', '��ѧר��', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '41', '�е�ר��', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '44', 'ְҵ����', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '47', '����ѧУ', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '61', '��ͨ����', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '71', '����', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '81', 'Сѧ', 3)
/
insert into frame_code_map
  (code_name, code_key, code_value, code_value_order)
values
  ('degree', '90', '����', 3)
/
commit
/