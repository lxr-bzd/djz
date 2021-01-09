package com.park.api.service;

import com.park.api.ServiceManage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameRefService {

    public static final String TYPE_1 = "group_turn";

    public void createRef(String type,Integer id, List<Integer> ids){

        for (int i = 0; i < ids.size(); i++) {
            ServiceManage.jdbcTemplate.update("INSERT INTO `game_ref` ( `type`, `m_id`, `ref_id`) VALUES ( ?, ?, ?)",
                    type,id,ids.get(i));
        }

    }

}
