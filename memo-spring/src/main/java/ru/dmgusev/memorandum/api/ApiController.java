package ru.dmgusev.memorandum.api;

import com.google.gson.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {

    public static final Gson gson = new GsonBuilder().create();

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String api() {
        MyData data = new MyData("a", 1);
        String json = gson.toJson(data);
        return json;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class MyData {
        public String a;
        public int b;
    }
}