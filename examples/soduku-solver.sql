with x( s, ind ) as
( select sud, instr( sud, ' ' )
  from ( select '53  7    6  195    98    6 8   6   34  8 3  17   2   6 6    28    419  5    8  79' sud from dual )
  union all
  select substr( s, 1, ind - 1 ) || z || substr( s, ind + 1 )
       , instr( s, ' ', ind + 1 )
  from x
     , ( select to_char( rownum ) z
         from dual
         connect by rownum <= 9
       ) z
  where ind > 0
  and not exists ( select null
                   from ( select rownum lp
                          from dual
                          connect by rownum <= 9
                        )
                   where z = substr( s, trunc( ( ind - 1 ) / 9 ) * 9 + lp, 1 )
                   or    z = substr( s, mod( ind - 1, 9 ) - 8 + lp * 9, 1 )
                   or    z = substr( s, mod( trunc( ( ind - 1 ) / 3 ), 3 ) * 3
                                      + trunc( ( ind - 1 ) / 27 ) * 27 + lp
                                      + trunc( ( lp - 1 ) / 3 ) * 6
                                   , 1 )
                 )
)
select s
from x
where ind = 0
/
