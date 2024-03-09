package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.service.DiscService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2022-12-15 20:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/disc")
public class DiscController {

    //region ------inject------
    private final DiscService srv;
    private static final Logger log = LoggerFactory.getLogger(DiscController.class);
    //endregion

}
