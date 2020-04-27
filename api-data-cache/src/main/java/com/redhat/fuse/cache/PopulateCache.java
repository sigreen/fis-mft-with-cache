
package com.redhat.fuse.cache;

import org.apache.camel.builder.RouteBuilder;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.component.infinispan.InfinispanConstants;
import org.apache.camel.component.infinispan.InfinispanOperation;
import org.apache.camel.component.jackson.JacksonDataFormat;


@Component
public class PopulateCache extends RouteBuilder{


    private static final Logger log = LoggerFactory.getLogger(PopulateCache.class);


    public static final int MIN = 150;

  @Override
  public void configure() throws Exception{

    from("timer:tick?fixedRate=true&period=5000")
    .choice()
        .when(simple("{{simulator.run}}"))
            .setBody(method(this, "genRandomIoTData()"))
            .marshal().json()
            .log("${body}")
            .to("kafka:my-topic")
        .otherwise()
            .log("Nothing send ")
    ;

    from("kafka:my-topic")
    .split().jsonpath("$.harvest[*]")
        .choice()
            .when().jsonpath("$[?(@.diameter > 4 )]" )
                .log("Premium ${body}")
                .marshal().json()
                .to("kafka:premium?groupId=sender")
            .when().jsonpath("$[?(@.diameter > 1 )]")
                .log("Standard ${body}")
                .marshal().json()
                .to("kafka:standard?groupId=sender")
            .otherwise()
                .log("Utility ${body}")
                .marshal().json()
                .to("kafka:utility?groupId=sender")
            .end()
    ;

        from("timer:cleanup?repeatCount=1")
        .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.CLEAR)
        .setHeader(InfinispanConstants.KEY).constant("premium")
        .to("infinispan:default")
        ;


        from("kafka:premium?groupId=premium-shipping")
        .streamCaching()
            .unmarshal(new JacksonDataFormat(Map.class))
            .log("Input --> ${body}")
            .setHeader("marshmallow").simple("${body}")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.GET)
            .setHeader(InfinispanConstants.KEY).constant("premium")
            .to("infinispan:default")
            .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.PUT)
            .setHeader(InfinispanConstants.KEY).constant("premium")
            .setHeader(InfinispanConstants.VALUE).method(this, "assignShipment(${body}, ${header.marshmallow})")
            .log("${body}")
            .to("infinispan:default")
        ;
  }

    public Map genRandomIoTData(){
        Random generator = new Random();
        Map iotData = new HashMap<String,Object>();

        Integer[] farms = {101, 302, 787, 645, 555, 460, 892};
        int randomIndex = generator.nextInt(farms.length);
        int batchcnt =  generator.nextInt(87) + MIN;
        long batchtime = System.currentTimeMillis();

        List<Map> harvest = new ArrayList<Map>();

        for (int i = 0; i < batchcnt; i++) {
            harvest.add(genSingleEvent((batchtime*10)+i));
        }

        iotData.put("farmid", farms[randomIndex]);
        iotData.put("batchcnt", batchcnt);
        iotData.put("harvest", harvest);
        iotData.put("batchtime", batchtime);

        return iotData;
    }

    private Map genSingleEvent(long eventid){

        Map harvestEvent = new HashMap<String,Integer>();
        Random generator = new Random();
        harvestEvent.put("diameter", (generator.nextInt(5) + 1));
        harvestEvent.put("weight", ((generator.nextInt(5) + 1.0) + (((int)generator.nextInt(8)+1)*0.1)));
        harvestEvent.put("mmid", eventid);

        return harvestEvent;
    }

}