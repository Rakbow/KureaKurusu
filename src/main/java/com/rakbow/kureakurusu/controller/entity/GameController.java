package com.rakbow.kureakurusu.controller.entity;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2023-01-06 15:50
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/db/game")
public class GameController {
    private static final Logger log = LoggerFactory.getLogger(GameController.class);

}
