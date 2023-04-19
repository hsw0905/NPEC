package com.mogak.npec.auth.repository;

import com.mogak.npec.auth.domain.BlackList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlackListRepository extends CrudRepository<BlackList, String> {
    List<BlackList> findAll();

}
