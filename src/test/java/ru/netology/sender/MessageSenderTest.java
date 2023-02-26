package ru.netology.sender;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageSenderTest {
    GeoService geoServiceImpl;
    LocalizationService localizationServiceImpl;

    @BeforeEach
    public void init() {
        geoServiceImpl = new GeoServiceImpl();
        localizationServiceImpl = new LocalizationServiceImpl();
        System.out.println("Начало теста");
    }

    @AfterEach
    public void finishedEach() {
        geoServiceImpl = null;
        localizationServiceImpl = null;
        System.out.println("\n" + "Окончание теста");
    }

    @BeforeAll
    static void setUpApp() {
        System.out.println("Запускаюсь до выполнения всех тестов...");
    }
    @AfterAll
    static void tearDownAll() {
        System.out.println("Запускаюсь после выполнения всех тестов...");
    }

    @Test
    public void LocationByCoordinatesTest() throws RuntimeException{

        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            geoServiceImpl.byCoordinates(28.4, 105.1);
        });

    }

    @Test
    public void messageSenderRussianScenarioTest() {
        Map<String, String> map = new HashMap<>();
        map.put("x-real-ip", "172.0.32.11");

        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.0.32.11")).thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        String result = messageSender.send(map);

        assertEquals("Добро пожаловать", result);

    }

    @Test
    public void messageSenderEnglishScenarioTest() {
        Map<String, String> map = new HashMap<>();
        map.put("x-real-ip", "96.44.183.149");

        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("96.44.183.149")).thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        String res = messageSender.send(map);

        assertEquals("Welcome", res);

    }
}