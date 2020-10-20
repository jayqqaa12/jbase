package com.jayqqaa12.jbase.cache.notify.kafka;

import com.jayqqaa12.jbase.cache.notify.Command;
import com.jayqqaa12.jbase.cache.notify.Notify;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaNotify implements Notify {


  @Override
  public void send(Command command) {




    log.debug("kafka send notify   key={}@{}",command.getRegion(),command.getKeys());
  }

  @Override
  public void receive(Command command) {
    log.debug("kafka receive notify   key={}@{}",command.getRegion(),command.getKeys());

    

  }
}
