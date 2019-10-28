--v_bs_position、v_valid_bs_position

select area_code,
       count(1) as ccount,
       nvl(sum(c.pcount), 0) as pcount,
       nvl(sum(c.p_num), 0) as p_num,
       nvl(sum(d.pcount_valid), 0) as pcount_valid,
       nvl(sum(d.p_num_valid), 0) as p_num_valid
  from BS_COMPANY a
 inner join (select frame_account.account_id, busi_staff.area_code
               from frame_account
              inner join busi_staff
                 on busi_id = staff_id) b
    on a.opr_id = b.account_id
  left outer join (select c_id, count(1) as pcount, sum(p_num) as p_num
                     from BS_POSITION
                    group by c_id) c
    on a.c_id = c.c_id
  left outer join (select c_id,
                          count(1) as pcount_valid,
                          sum(p_num) as p_num_valid
                     from v_valid_bs_position
                    group by c_id) d
    on a.c_id = d.c_id
 group by area_code
