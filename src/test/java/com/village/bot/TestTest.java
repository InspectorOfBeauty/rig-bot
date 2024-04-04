package com.village.bot;

import com.village.bot.dto.UserRequest;
import com.village.bot.entities.UserSession;
import com.village.bot.handlers.RequestHandler;
import com.village.bot.repositories.UserSessionRepository;
import com.village.bot.services.RequestProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

// JUnit Runner - особый класс в JUnit, который занимается поиском тестов в классе, их инициализацией, запуском, слежением за выполнением (таймауты, фиксация результата), сохранением результата выполнения, очисткой ресурсов
// стандартный JUnit Runner расширяемый - т.е. в каждый из описанных выше этапов можно добавить свою логику
// классы Extension - расширения логики Runnera, часто используются для добавления функционала каких-либо библиотек

// в данном случае MockitoExtension неявно инициализирует сессию mockito и сами моки, в конце после теста освобождает ресурсы. Тоже можно сделать явно написав код в методах, помеченных @BeforeEach, @AfterAll

// инициализация моков - это создание такой реализации интерфейса или наследника класса, когда для всех его методов переопределяется поведение самим mockito
@ExtendWith(MockitoExtension.class)
public class TestTest {


    // @Spy - такая инициализация мока, когда для каждого вызываемого метода необходимо в тесте явно описать входные/выходные данные (*для void методой это не обязательно)

    // Mock - делается новый класс и входные/выходные данные методов можно настраивать с помощью Mockito в тесте
    // Spy - когда можно переопределить входные/выходные данные методов, иначе вызывается инстанс, которым было инициализировано поле (например вызовется foo в @Spy RequestHandler handler = foo;)
    @Mock
    RequestHandler handler;
    @Mock
    UserSessionRepository userSessionRepository;


    @Test

    public void test(){
        // === подготовка замоканных входных и выходных данных ===
        UserRequest request = new UserRequest(false, 1, 1, "Hello");

        UserSession userSession = new UserSession();
        userSession.setId(42);

        // when - говорит что метод будет будет с определенными параметрами
        Mockito.when( userSessionRepository.getAuthorizedUserSessionByChatId(1) ).then(
                invocationOnMock -> userSession
        );
        // then - функция, для генерации возвращаемого методом значения

        Mockito.when( handler.handle(request, userSession) ).then(
                invocationOnMock -> true
        );


        // === создание тестируемого класса (юнита) ===
        RequestProcessingService processingService = new RequestProcessingService(Collections.singletonList(handler), userSessionRepository);

        // === старт тестируемого кода ===
        processingService.process(request);
    }
}
