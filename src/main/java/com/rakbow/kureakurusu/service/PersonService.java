package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rakbow
 * @since 2023-11-14 20:47
 */
@Service
@RequiredArgsConstructor
public class PersonService extends ServiceImpl<PersonMapper, Person> {
}
