package com.semkagtn.lastfm;


import com.semkagtn.lastfm.vkapi.VkAccessTokens;
import com.semkagtn.lastfm.vkapi.VkApi;
import com.semkagtn.lastfm.vkapi.VkApiConfig;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 20.07.15.
 */
public class Test {

    public static class AnimalDeserializer extends JsonDeserializer<List<Animal>> {

        @Override
        public List<Animal> deserialize(JsonParser jsonParser,
                                        DeserializationContext deserializationContext) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Animal> result = new ArrayList<>();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            for (int i = 0; i < node.size(); i++) {
                JsonNode child = node.get(i);
                Class<? extends Animal> clazz;
                if (child.get("catPoints") != null) {
                    clazz = Cat.class;
                } else {
                    clazz = Dog.class;
                }
                result.add(objectMapper.readValue(child, clazz));
            }
            return result;
        }
    }

    public static class Animals {

        private List<Animal> animals;

        @JsonDeserialize(using = AnimalDeserializer.class)
        public List<Animal> getAnimals() {
            return animals;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static abstract class Animal {

        private String name;

        public String getName() {
            return name;
        }
    }

    public static class Dog extends Animal {

        private int dogPoints;

        public int getDogPoints() {
            return dogPoints;
        }
    }

    public static class Cat extends Animal {

        private int catPoints;

        public int getCatPoints() {
            return catPoints;
        }
    }

    public static void main(String[] args) throws IOException {
        VkApi api = new VkApi(VkApiConfig.newInstance()
                .withToken(VkAccessTokens.getRandom())
                .build());

        Animals animals = new ObjectMapper()
                .readValue("{\"animals\":[{\"catPoints\": 1},{\"dogPoints\": 2}]}", Animals.class);
        System.out.println();
    }
}
